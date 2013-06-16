/**
 * 
 */
package org.korsakow.domain.k3.code;

import java.util.ArrayList;
import java.util.List;

import org.korsakow.domain.Keyword;
import org.korsakow.domain.Rule;
import org.korsakow.domain.RuleFactory;
import org.korsakow.domain.interf.IRule;
import org.korsakow.ide.rules.RuleType;

/**
 * K3 source code parser. With helper methods to generate K5 Rules.
 * @author d
 *
 */
public class K3RuleParser
{
	List<K3Lexeme> k3Tokens;
	public boolean isValidKeyword(String keyword)
	{
		// see K5RuleParser about why we currently don't validate keywords.
		// also K3 seems to allow just about anything so we'd have to handle this with care.
		return true;
//		return keyword.matches("[^-<>+]+");
	}
	public void validateKeyword(String keyword) throws RuleParserException
	{
		if (!isValidKeyword(keyword))
			throw new RuleParserException("invalid keyword: \"" + keyword + "\"");
	}
	public List<K3Lexeme> tokenize(String code) throws RuleParserException
	{
		List<K3Lexeme> tokens = new ArrayList<K3Lexeme>();
		String[] parts = code.split(" ");
		for (String part : parts) {
			part = part.trim();
			if (part.length() == 0)
				continue;
			
			char op = part.charAt(part.length()-1);
			String keyword = part.substring(0, part.length()-1);
			K3Lexeme lexeme;
			if (part.equals(K3Symbol.CLEAR_PREVIOUS_LINKS)) {
				lexeme = new K3Lexeme(K3OpType.CLEAR_PREVIOUS_LINKS, null, K3Symbol.CLEAR_PREVIOUS_LINKS);
			} else
			if (part.equals(K3Symbol.KEEP_PREVIOUS_LINKS)) {
				lexeme = new K3Lexeme(K3OpType.KEEP_PREVIOUS_LINKS, null, K3Symbol.KEEP_PREVIOUS_LINKS);
			} else {
				switch (op)
				{
				case K3Symbol.INBOUND_KEYWORD:
//					if (!K3Symbol.ENDFILM_KEYWORD.equals(keyword))
//						throw new RuleParserException("cannot use '"+keyword+"' in this context");
					validateKeyword(keyword);
					lexeme = new K3Lexeme(K3OpType.INBOUND_KEYWORD, op, keyword);
					break;
				case K3Symbol.EXCLUSION_KEYWORD:
					if (K3Symbol.RANDOM_KEYWORD.equals(keyword))
						throw new RuleParserException("cannot use '"+keyword+"' in this context");
					validateKeyword(keyword);
					lexeme = new K3Lexeme(K3OpType.KEYWORD_EXCLUSION, op, keyword);
					break;
				case K3Symbol.LOOKUP_KEYWORD:
					validateKeyword(keyword);
					lexeme = new K3Lexeme(K3OpType.KEYWORD_LOOKUP, op, keyword);
					break;
				case K3Symbol.REQUIRED_KEYWORD:
					if (K3Symbol.RANDOM_KEYWORD.equals(keyword))
						throw new RuleParserException("cannot use '"+keyword+"' in this context");
					validateKeyword(keyword);
					lexeme = new K3Lexeme(K3OpType.KEYWORD_REQUIRED, op, keyword);
					break;
				default:
					if (isValidKeyword(keyword + op)) {
						lexeme = new K3Lexeme(K3OpType.INBOUND_AND_LOOKUP_KEYWORD, keyword + op);
					} else
						throw new RuleParserException("invalid op: " + op);
				}
			}
			tokens.add(lexeme);
		}
		return tokens;
	}
	public List<IRule> createRules(List<K3Lexeme> lexemes) throws RuleParserException
	{
		List<IRule> rules = new ArrayList<IRule>();
		List<String> outboundKeywords = new ArrayList<String>();
		List<String> requireKeywords = new ArrayList<String>();
		List<String> excludeKeywords = new ArrayList<String>();
		boolean clearScores = false;
		boolean setendfilm = false;
		for (K3Lexeme lexeme : lexemes)
		{
			switch(lexeme.getOpType())
			{
			case INBOUND_AND_LOOKUP_KEYWORD:
				// in this special case we have to handle INBOUND and LOOKUP
				// just copy and paste those two into this one
				if (K3Symbol.ENDFILM_KEYWORD.equals(lexeme.getToken()))
					setendfilm = true;
				outboundKeywords.add(lexeme.getToken());
				break;
			case KEYWORD_LOOKUP:
				outboundKeywords.add(lexeme.getToken());
				break;
			case INBOUND_KEYWORD:
				// this special case asside, inbound keywords are lost in translation
				// since k5 doesnt really handle inbound keywords as rules
				// so inbound are handled in a completely separate way
				if (K3Symbol.ENDFILM_KEYWORD.equals(lexeme.getToken()))
					setendfilm = true;
				break;
			case KEYWORD_REQUIRED:
				requireKeywords.add(lexeme.getToken());
				break;
			case KEYWORD_EXCLUSION:
				excludeKeywords.add(lexeme.getToken());
				break;
			case CLEAR_PREVIOUS_LINKS:
					clearScores = true;
				break;
			case KEEP_PREVIOUS_LINKS:
					clearScores = false;
				break;
			default:
				throw new RuleParserException("k3 rule not yet parsable: " + lexeme.getSymbol());
			}
		}
		// the order in which we generate most of the rules is important!
		// generally: Lookup, Require, Exclude
		
		if (clearScores) {
			Rule rule = RuleFactory.createNew(RuleType.ClearScores.getId());
			rules.add(rule);
		}
		
		if (setendfilm) { // this rule is order-independant
			Rule rule = RuleFactory.createNew(RuleType.SetEndfilm.getId());
			rules.add(rule);
		}
		
		{ // scope local vars for clarity
			List<String> regularKeywords = new ArrayList<String>();
			List<String> randomKeywords = new ArrayList<String>();
			List<String> endfilmKeywords = new ArrayList<String>();
			for (String keyword : outboundKeywords) {
				if (K3Symbol.RANDOM_KEYWORD.equals(keyword)) {
					randomKeywords.add(keyword);
				} else if (K3Symbol.ENDFILM_KEYWORD.equals(keyword)) {
					endfilmKeywords.add(keyword);
				} else {
					regularKeywords.add(keyword);
				}
			}
			// the order of random/endfilm vs regular is not important
			for (String random : randomKeywords) {
				Rule rule = RuleFactory.createNew(RuleType.RandomLookup.getId());
				rules.add(rule);
			}
			for (String endfilm : endfilmKeywords) {
				Rule rule = RuleFactory.createNew(RuleType.EndfilmLookup.getId());
				rules.add(rule);
			}
			if (!regularKeywords.isEmpty()) {
				Rule rule = RuleFactory.createNew(RuleType.KeywordLookup.getId());
				rule.setKeywords(Keyword.fromStrings(regularKeywords));
				rules.add(rule);
			}
		}
		
		{ // scope local vars for clarity
			// requirekeywords must be after all other keyword lookup rules except exclusion
			List<String> regularRequires = new ArrayList<String>();
			List<String> endfilmRequires = new ArrayList<String>();
			for (String keyword : requireKeywords) {
				if (K3Symbol.ENDFILM_KEYWORD.equals(keyword)) {
					endfilmRequires.add(keyword);
				} else {
					regularRequires.add(keyword);
				}
			}
			// the order of endfilm vs regular is not important
			for (String endfilm : endfilmRequires) {
				Rule rule = RuleFactory.createNew(RuleType.RequireEndfilm.getId());
				rules.add(rule);
			}
			if (!regularRequires.isEmpty()) {
				Rule rule = RuleFactory.createNew(RuleType.RequireKeywords.getId());
				rule.setKeywords(Keyword.fromStrings(regularRequires));
				rules.add(rule);
			}
		}
		
		{ // scope local vars for clarity
			// exclude keywords must be after all other keyword lookup rules
			List<String> regularExcludes = new ArrayList<String>();
			List<String> endfilmExcludes = new ArrayList<String>();
			for (String keyword : excludeKeywords) {
				if (K3Symbol.ENDFILM_KEYWORD.equals(keyword)) {
					endfilmExcludes.add(keyword);
				} else {
					regularExcludes.add(keyword);
				}
			}
			// the order of endfilm vs regular is not important
			for (String endfilm : endfilmExcludes) {
				Rule rule = RuleFactory.createNew(RuleType.ExcludeEndfilm.getId());
				rules.add(rule);
			}
			if (!regularExcludes.isEmpty()) {
				Rule rule = RuleFactory.createNew(RuleType.ExcludeKeywords.getId());
				rule.setKeywords(Keyword.fromStrings(regularExcludes));
				rules.add(rule);
			}
		}
		
		return rules;
	}
	/**
	 * K3 and K5 rules are many-to-many. That is any one K3 rule might correspond to many K5 rules
	 * and vice versa.
	 * 
	 * @param code
	 * @param triggerTime if null no time is set
	 * @return
	 * @throws RuleParserException
	 */
	public List<IRule> parse(String code, Long triggerTime) throws RuleParserException
	{
		k3Tokens = tokenize(code);
		List<IRule> rules = createRules(k3Tokens);
		if (triggerTime != null) {
			for (IRule rule : rules)
				rule.setTriggerTime(triggerTime);
		}
		return rules;
	}
	public List<IRule> parse(String code) throws RuleParserException
	{
		return parse(code, null);
	}
}