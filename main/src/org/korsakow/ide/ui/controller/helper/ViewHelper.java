package org.korsakow.ide.ui.controller.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.UpdateInterfaceCommand;
import org.korsakow.domain.command.UpdateSnuCommand;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.IRule;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.util.FileUtil;


public class ViewHelper
{

	private static final class AlphabeticalResourceSorter<R extends IResource> implements Comparator<R>
	{
		private final boolean ignoreCase;

		private AlphabeticalResourceSorter(String nameMatch, List<R> similar, boolean ignoreCase)
		{
			this.ignoreCase = ignoreCase;
		}

		public int compare(R o1, R o2) {
			if (o1.getName() == null) {
				if (o2.getName() == null)
					return 0;
				return 1;
			} else {
				if (o2.getName() == null)
					return -1;
				if (ignoreCase)
					return o1.getName().compareToIgnoreCase(o2.getName());
				else
					return o1.getName().compareTo(o2.getName());
			}
		}
	}
	/**
	 * Sorts the list of resources alphabetically by name
	 * @param similarName may be null resources with similar names are put at the top of the list
	 */
	public static <R extends IResource> List<R> sort(Collection<R> resources, Class<R> clazz, String similarName)
	{
		final String nameMatch = similarName==null?null:FileUtil.getFilenameWithoutExtension(similarName).toLowerCase().trim();
		
		final List<R> similar = new ArrayList<R>();
		List<R> sorted = new ArrayList<R>(resources);

		final Comparator<R> comparator = new AlphabeticalResourceSorter<R>(nameMatch, similar, true);
		Collections.sort(sorted, new Comparator<R>() {
			public int compare(R o1, R o2) {
				if (nameMatch != null) {
					if (o1.getName().toLowerCase().trim().startsWith(nameMatch))
						similar.add(o1);
					if (o2.getName().toLowerCase().trim().startsWith(nameMatch))
						similar.add(o2);
				}
				return comparator.compare(o1, o2);
			}
		});

		Collections.sort(similar, comparator);
		Collections.reverse(similar);
		for (R r : similar) {
			sorted.remove(r);
			sorted.add(0, r);
		}
		return sorted;
	}

	public static void addRulesToRequest(Request request, List<IRule> rules)
	{
		addRulesToRequest(request, rules, "");
	}
	private static void addRulesToRequest(Request request, List<IRule> rules, String base)
	{
		request.set(UpdateSnuCommand.RULE_COUNT + base, rules.size());
		int index = 0;
		for (IRule rule : rules)
		{
			String nextBase = base + "_" + index;
			request.set(UpdateSnuCommand.RULE_NAME+nextBase, rule.getName());
			request.set(UpdateSnuCommand.RULE_TYPE+nextBase, rule.getRuleType());
			request.set(UpdateSnuCommand.RULE_KEYWORDS+nextBase, rule.getKeywords());
			request.set(UpdateSnuCommand.RULE_TRIGGER_TIME+nextBase, rule.getTriggerTime());
			request.set(UpdateSnuCommand.RULE_PROPERTY_IDS+nextBase, new ArrayList<String>(rule.getDynamicPropertyIds()));
			List<Object> propertyValues = new ArrayList<Object>();
			for (String propertyId : rule.getDynamicPropertyIds())
				propertyValues.add(rule.getDynamicProperty(propertyId));
			request.set(UpdateSnuCommand.RULE_PROPERTY_VALUES+nextBase, propertyValues);
			
			if (rule.getRules() != null)
				addRulesToRequest(request, rule.getRules(), nextBase);
			++index;
		}
	}
	
	public static void addWidgetsToRequest(int offsetX, int offsetY, Request request, List<WidgetModel> widgets)
	{
		addWidgetsToRequest(offsetX, offsetY, request, widgets, "");
	}
	private static void addWidgetsToRequest(int offsetX, int offsetY, Request request, List<WidgetModel> widgets, String base)
	{
		request.set(UpdateInterfaceCommand.WIDGET_COUNT + base, widgets.size());
		int index = 0;
		for (WidgetModel widget : widgets)
		{
			String nextBase = base + "_" + index;
			request.set(UpdateInterfaceCommand.WIDGET_NAME+nextBase, widget.getName());
			request.set(UpdateInterfaceCommand.WIDGET_TYPE+nextBase, widget.getWidgetType().getId());
			request.set(UpdateInterfaceCommand.WIDGET_KEYWORDS+nextBase, widget.getKeywords());
			request.set(UpdateInterfaceCommand.WIDGET_PERSIST_CONDITION+nextBase, widget.getPersistCondition().getId());
			request.set(UpdateInterfaceCommand.WIDGET_PERSIST_ACTION+nextBase, widget.getPersistAction().getId());
			request.set(UpdateInterfaceCommand.WIDGET_X+nextBase, widget.getX() + offsetX);
			request.set(UpdateInterfaceCommand.WIDGET_Y+nextBase, widget.getY() + offsetY);
			request.set(UpdateInterfaceCommand.WIDGET_WIDTH+nextBase, widget.getWidth());
			request.set(UpdateInterfaceCommand.WIDGET_HEIGHT+nextBase, widget.getHeight());
			request.set(UpdateInterfaceCommand.WIDGET_PROPERTY_IDS+nextBase, new ArrayList<String>(widget.getDynamicPropertyIds()));
			List<Object> propertyValues = new ArrayList<Object>();
			for (String propertyId : widget.getDynamicPropertyIds())
				propertyValues.add(widget.getDynamicProperty(propertyId));
			request.set(UpdateInterfaceCommand.WIDGET_PROPERTY_VALUES+nextBase, propertyValues);
			
			++index;
		}
	}
}
