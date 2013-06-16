package org.korsakow.ide.ui.interfacebuilder.widget;

import javax.swing.JLabel;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;

public class SnuAutoMultiLink extends AbstractLink
{
	private static class SnuAutoMultiLinkWidgetComponent extends AbstractLinkWidgetComponent
	{
		public SnuAutoMultiLinkWidgetComponent(WidgetModel owner)
		{
			super(owner);
		}
		@Override
		protected void initUI()
		{
			super.initUI();
			setSize(120, 80);
			add(new JLabel(LanguageBundle.getString("widget.snuautomultilink.label")));
		}
	}
	@Override
	protected WidgetComponent createComponent()
	{
		return new SnuAutoMultiLinkWidgetComponent(this);
	}
	public SnuAutoMultiLink()
	{
		super(WidgetType.SnuAutoMultiLink);
		removeProperty("scalingPolicy");
	}
}
