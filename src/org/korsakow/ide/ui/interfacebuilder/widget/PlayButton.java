package org.korsakow.ide.ui.interfacebuilder.widget;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;

import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.util.UIResourceManager;

public class PlayButton extends WidgetModel
{
	private static class PlayButtonWidgetComponent extends WidgetComponent
	{
		public PlayButtonWidgetComponent(WidgetModel owner)
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
			final ImageIcon icon = (ImageIcon)UIResourceManager.getIcon(UIResourceManager.ICON_CONTROL_PLAY_WIDGET);
			add(new ImageLabel(icon));
			setSize(icon.getIconWidth(), icon.getIconHeight());
		}
	}

	public PlayButton()
	{
		super(WidgetType.PlayButton);
	}
	@Override
	protected WidgetComponent createComponent()
	{
		return new PlayButtonWidgetComponent(this);
	}
}
