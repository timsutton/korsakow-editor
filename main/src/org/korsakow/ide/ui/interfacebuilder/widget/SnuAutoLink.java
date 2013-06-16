package org.korsakow.ide.ui.interfacebuilder.widget;


import javax.swing.JLabel;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.property.IntegerProperty;
import org.korsakow.ide.resources.widget.ScalingPolicy;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.WidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.editors.SnuAutoLinkWidgetEditor;

public class SnuAutoLink extends AbstractLink
{
	private static class SnuAutoLinkWidgetComponent extends AbstractLinkWidgetComponent
	{
		private JLabel indexLabel;
		public SnuAutoLinkWidgetComponent(WidgetModel owner)
		{
			super(owner);
		}
		@Override
		protected void initUI()
		{
			super.initUI();
			label.setText(LanguageBundle.getString("widget.snuautolink.label"));
			indexLabel = new JLabel();
			add(indexLabel);
		}
		public void setIndex(int index)
		{
			indexLabel.setText("#"+(index+1));
		}
	}
	@Override
	protected WidgetComponent createComponent()
	{
		return new SnuAutoLinkWidgetComponent(this);
	}
	@Override
	protected WidgetPropertiesEditor createWidgetEditor()
	{
		return new SnuAutoLinkWidgetEditor(this);
	}
	private int index;
	public SnuAutoLink()
	{
		super(WidgetType.SnuAutoLink);
		scalingPolicy = ScalingPolicy.MaintainAspectRatio;
		addProperty(new IntegerProperty("index") {
			@Override
			public Integer getValue() { return getIndex(); }
			@Override
			public void setValue(Integer value) {
				setIndex(Integer.parseInt(value.toString()));
			}
		});
	}
	
	public int getIndex()
	{
		return index;
	}
	public void setIndex(int index)
	{
		int oldIndex = this.index;
		this.index = index;
		((SnuAutoLinkWidgetComponent)getComponent()).setIndex(index);
		firePropertyChange("index", oldIndex, index);
	}
}
