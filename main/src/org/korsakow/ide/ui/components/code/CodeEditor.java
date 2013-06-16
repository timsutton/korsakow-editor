/**
 * 
 */
package org.korsakow.ide.ui.components.code;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

import javax.swing.JTextField;

import org.korsakow.ide.code.RuleParserException;
import org.korsakow.ide.code.k5.K5Lexeme;
import org.korsakow.ide.code.k5.K5RuleParser2;
import org.korsakow.ide.code.k5.K5Symbol;

public class CodeEditor extends JTextField
{
	private K5RuleParser2 parser;
	public CodeEditor()
	{
		parser = new K5RuleParser2();
		addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent event) {
				update();
			}
		});
	}
	public void update()
	{
		String text = getText();
		try {
			List<K5Lexeme> tokens = parser.tokenize(text);
			StringBuilder sb = new StringBuilder();
			for (K5Lexeme lexeme : tokens)
			{
				sb.append(lexeme.getToken())
					.append(K5Symbol.DEFAULT_STATEMENT_SEPARATOR);
			}
			if (sb.length() > 0)
				sb.deleteCharAt(1); // trailing whitespace
			setText(sb.toString());
			setForeground(Color.black);
		} catch (RuleParserException e) {
			setForeground(Color.red);
		}
	}
}