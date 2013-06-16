package org.korsakow.ide.ui.interfacebuilder.widget;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;

import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.util.UIResourceManager;

public class FullscreenButton extends WidgetModel
{
	private static class FullscreenButtonWidgetComponent extends WidgetComponent
	{
		public FullscreenButtonWidgetComponent(WidgetModel owner)
		{
			super(owner);
		}
		@Override
		protected void initUI()
		{
			super.initUI();
			setOpaque(false);
			setBackground(null);
			setLayout(new BorderLayout());
			final ImageIcon icon = (ImageIcon)UIResourceManager.getIcon(UIResourceManager.ICON_CONTROL_FULLSCREEN);
			add(new ImageLabel(icon));
			setSize(icon.getIconWidth(), icon.getIconHeight());
		}
	}

	public FullscreenButton()
	{
		super(WidgetType.FullscreenButton);
	}
	@Override
	protected WidgetComponent createComponent()
	{
		return new FullscreenButtonWidgetComponent(this);
	}
}
