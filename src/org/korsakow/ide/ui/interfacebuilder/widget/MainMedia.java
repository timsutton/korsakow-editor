package org.korsakow.ide.ui.interfacebuilder.widget;

import javax.swing.JLabel;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.property.AbstractProperty;
import org.korsakow.ide.resources.widget.ScalingPolicy;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.WidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.editors.MainMediaWidgetEditor;

public class MainMedia extends WidgetModel
{
	private static class MainMediaWidgetComponent extends WidgetComponent
	{
		public MainMediaWidgetComponent(WidgetModel owner)
		{
			super(owner);
		}
		@Override
		protected void initUI()
		{
			super.initUI();
			setSize(720, 405);
			add(new JLabel(LanguageBundle.getString("widget.mainmedia.label")));
		}
	}
	@Override
	protected WidgetComponent createComponent()
	{
		return new MainMediaWidgetComponent(this);
	}
	@Override
	protected WidgetPropertiesEditor createWidgetEditor()
	{
		return new MainMediaWidgetEditor(this);
	}
	protected ScalingPolicy scalingPolicy = ScalingPolicy.ScaleDownMaintainAspectRatio;
	public MainMedia()
	{
		super(WidgetType.MainMedia);
		addProperty(new AbstractProperty("scalingPolicy") {
			@Override
			public Object getValue() { return getScalingPolicy().getId(); }
			@Override
			public void setValue(Object value) {
				if (value instanceof ScalingPolicy == false)
					value = ScalingPolicy.forId(value.toString());
				setScalingPolicy((ScalingPolicy)value);
			}
		});
	}
	public ScalingPolicy getScalingPolicy()
	{
		return scalingPolicy;
	}
	public void setScalingPolicy(ScalingPolicy scalingPolicy)
	{
		ScalingPolicy oldValue = scalingPolicy;
		this.scalingPolicy = scalingPolicy;
		firePropertyChange("scalingPolicy", oldValue, scalingPolicy);
	}
}
