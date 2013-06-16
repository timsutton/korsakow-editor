package org.korsakow.ide.ui.interfacebuilder.widget;

import java.awt.Dimension;

import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;

public class PlayTime extends AbstractSimpleTextWidgetModel
{
	private static class PlayTimeWidgetComponent extends AbstractSimpleTextWidgetComponent
	{
		public PlayTimeWidgetComponent(WidgetModel owner)
		{
			super(owner);
		}
		@Override
		protected void initUI()
		{
			super.initUI();
			label.setText("00:00");
			final Dimension size = getPreferredSize();
			size.width += 5; // dunno why, i guess the label needs some time to correctly calculate its size but we're lazy
			setSize(size);
		}
	}

	public PlayTime()
	{
		super(WidgetType.PlayTime);
	}
	@Override
	protected WidgetComponent createComponent()
	{
		return new PlayTimeWidgetComponent(this);
	}
}
