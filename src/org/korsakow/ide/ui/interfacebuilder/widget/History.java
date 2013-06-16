package org.korsakow.ide.ui.interfacebuilder.widget;

import java.awt.Color;
import java.awt.Graphics;

import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.WidgetPersistAction;
import org.korsakow.ide.resources.widget.WidgetPersistCondition;

public class History extends WidgetModel
{
	private static class HistoryWidgetComponent extends WidgetComponent
	{
		public HistoryWidgetComponent(WidgetModel owner)
		{
			super(owner);
		}
		protected void initUI()
		{
			super.initUI();
			setSize(400, 8);
		}
		public void paintComponent(Graphics g)
		{
			g.setColor(Color.black);
			g.fillRect(0, 0, getWidth(), getHeight()/2);
			g.setColor(new Color(0.5f, 0.5f, 0.5f));
			g.fillRect(0, getHeight()/2, getWidth(), getHeight()/2);
		}
	}
	protected WidgetComponent createComponent()
	{
		return new HistoryWidgetComponent(this);
	}
	public History()
	{
		super(WidgetType.History);
		persistCondition = WidgetPersistCondition.MatchType;
		persistAction = WidgetPersistAction.Replace;
	}
}
