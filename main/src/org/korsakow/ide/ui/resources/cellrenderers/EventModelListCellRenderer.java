/**
 * 
 */
package org.korsakow.ide.ui.resources.cellrenderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.model.EventModel;
import org.korsakow.ide.ui.model.PredicateModel;
import org.korsakow.ide.ui.model.RuleModel;
import org.korsakow.services.plugin.predicate.PredicateTypeInfoFactory;
import org.korsakow.services.plugin.rule.RuleTypeInfoFactory;

public class EventModelListCellRenderer implements ListCellRenderer
{
	private JTextArea renderer = new JTextArea();
	public Component getListCellRendererComponent(
		       JList list,              // the list
		       Object value,            // value to display
		       int index,               // cell index
		       boolean isSelected,      // is the cell selected
		       boolean cellHasFocus)    // does the cell have focus
	{
		adjustRendererText(value);
		adjustRendererStyles(list, index, isSelected, cellHasFocus);
		adjustSize(list); // must be called after text is set
		return renderer;
	}
	
	private void adjustRendererText(Object value)
	{
		EventModel model = (EventModel)value; 
		String triggerString = model.getTrigger().getType().getDisplayName();
		String predicateString = renderPredicate(model.getPredicate());
		String ruleString = renderRule(model.getRule());
		String display = LanguageBundle.getString("eventview.renderer.format", triggerString, predicateString, ruleString);
		renderer.setText(display);
	}
	private void adjustRendererStyles(JList list, int index, boolean isSelected, boolean cellHasFocus)
	{
		if (isSelected || cellHasFocus) {
			renderer.setBackground(list.getSelectionBackground());
			renderer.setForeground(list.getSelectionForeground());
		} else {
			renderer.setBackground((index%2==0)?list.getBackground():(Color)list.getClientProperty("List.altBackground"));
			renderer.setForeground(list.getForeground());
		}
		renderer.setWrapStyleWord(true);
		renderer.setLineWrap(true);
	}
	/**
	 */
	private void adjustSize(JList list)
	{
		renderer.setSize(new Dimension(list.getWidth(), 0));
	}
	
	private static String renderRule(RuleModel ruleModel)
	{
		String display = RuleTypeInfoFactory.getFactory().getTypeInfo(ruleModel.getType().getId()).getFormattedDisplayString(ruleModel.getPropertyValues().toArray());
		return display;
	}
	private static String renderPredicate(PredicateModel predicateModel)
	{
		List<String> flatList = new ArrayList<String>();
		renderLeaves(flatList, predicateModel);
		StringBuilder sb = new StringBuilder();
		for (String s : flatList) {
			sb.append(s).append(' ');
		}
		return sb.toString();
	}
	private static void renderLeaves(List<String> flatList, PredicateModel predicateModel)
	{
		if (predicateModel.getPredicates().isEmpty()) {
			String predicateString = PredicateTypeInfoFactory.getFactory().getTypeInfo(predicateModel.getType().getId()).getFormattedDisplayString(predicateModel.getPropertyValues().toArray());
			flatList.add(predicateString);
		} else {
			String predicateString = PredicateTypeInfoFactory.getFactory().getTypeInfo(predicateModel.getType().getId()).getFormattedDisplayString(predicateModel.getPropertyValues().toArray());
			int i = -1;
			for (PredicateModel model : predicateModel.getPredicates()) {
				renderLeaves(flatList, model);
				if (++i%2==0) // insert the parent(glue) between each child
					flatList.add(predicateString);
			}
		}
	}
	private static int calculateHeight(String text, int maxWidth, JTextArea textArea, JList list)
	{
		FontMetrics metrics = textArea.getFontMetrics(textArea.getFont());
//		int avgCharWidth = (int)(metrics.getStringBounds(text, textArea.getGraphics()).getWidth()/text.length());
		int maxTextWidth = text.length()*metrics.getMaxAdvance();
		int rows = maxTextWidth / maxWidth;
		return (int)(rows * metrics.getLineMetrics(text, textArea.getGraphics()).getHeight());
	}
}
