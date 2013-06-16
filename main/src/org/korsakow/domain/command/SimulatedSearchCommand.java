package org.korsakow.domain.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.ide.rules.RuleType;

public class SimulatedSearchCommand extends AbstractCommand{


	public SimulatedSearchCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
//			System.out.println("===================================================================================================");
			long id = request.getLong("id");
			ISnu snu = SnuInputMapper.map(id);
			
			Map<ISnu, Float> scores = new HashMap<ISnu, Float>();
			
			// currently our "simulated search" is not very sophisticated
			// we only consider keyword lookups, no fancy stuff like exclusions and random and things
			
			for (IRule rule : snu.getRules())
			{
				if (RuleType.Search != RuleType.forId(rule.getRuleType()))
					continue;
				List<IRule> searchRules = rule.getRules();
				for (IRule searchRule : searchRules)
				{
					Collection<IKeyword> keywords = searchRule.getKeywords();
					switch (RuleType.forId(searchRule.getRuleType()))
					{
					case KeywordLookup:
						for (IKeyword keyword : keywords)
						{
							Collection<ISnu> found;
							found = SnuInputMapper.findByInKeyword(keyword.getValue());
//							for (ISnu s : found)
//							{
//								System.out.println("1\t"+keyword.getValue()+"\t" + s.getId()+":"+s.hashCode() + "\t" + s.getName());
//							}
							for (ISnu s : found) {
								if (!scores.containsKey(s))
									scores.put(s, 0f);
								scores.put(s, scores.get(s)+1*s.getRating());
//								System.out.println("\t"+keyword.getValue()+"\t" + s.getId()+":"+s.hashCode() + "\t" + s.getName() + "\t" + scores.get(s));
							}
							
							found = SnuInputMapper.findByName(keyword.getValue());
							for (ISnu s : found) {
								if (!scores.containsKey(s))
									scores.put(s, 0f);
								scores.put(s, scores.get(s)+1*s.getRating());
							}
						}
						break;
					}
				}
			}
			
			List<ISnu> results = new ArrayList<ISnu>(scores.keySet());
			Collections.sort(results, new ScoreComparator(scores));
//			for (ISnu s : results)
//			{
//				System.out.println("\t" + s.getId() + "\t" + scores.get(s));
//			}
			response.set("results", results);
			
		} catch (MapperException e) {
			throw new CommandException(e);
		}
	}

	private static class ScoreComparator implements Comparator<ISnu>
	{
		private final Map<ISnu, Float> scores;
		public ScoreComparator(Map<ISnu, Float> scores)
		{
			this.scores = scores;
		}
		public int compare(ISnu o1, ISnu o2) {
			return scores.get(o2).compareTo(scores.get(o1));
		}
		
	}
}
