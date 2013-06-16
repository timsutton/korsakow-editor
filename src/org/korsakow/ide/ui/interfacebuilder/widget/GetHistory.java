package org.korsakow.ide.ui.interfacebuilder.widget;

import javax.swing.JButton;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.WidgetPersistAction;
import org.korsakow.ide.resources.widget.WidgetPersistCondition;

public class GetHistory extends WidgetModel
{
	private static class GetHistoryWidgetComponent extends WidgetComponent
	{
		private JButton button;
		public GetHistoryWidgetComponent(WidgetModel owner)
		{
			super(owner);
		}
		protected void initUI()
		{
			super.initUI();
			add(button = new JButton(LanguageBundle.getString("widget.gethistory.label")));
			button.setEnabled(false);
			setSize(getPreferredSize());
		}
	}
	protected WidgetComponent createComponent()
	{
		return new GetHistoryWidgetComponent(this);
	}
	public GetHistory()
	{
		super(WidgetType.GetHistory);
		persistCondition = WidgetPersistCondition.MatchType;
		persistAction = WidgetPersistAction.Replace;
	}
}
