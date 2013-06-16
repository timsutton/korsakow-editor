package org.korsakow.ide.ui.interfacebuilder.widget;

import java.awt.Color;

import javax.swing.JLabel;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.property.BooleanProperty;
import org.korsakow.ide.resources.property.IntegerProperty;
import org.korsakow.ide.resources.property.StringProperty;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.WidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.editors.ScrubberWidgetEditor;
import org.korsakow.services.util.ColorFactory;

public class Scrubber extends WidgetModel
{
	private static class ScrubberWidgetComponent extends WidgetComponent
	{
		public ScrubberWidgetComponent(WidgetModel owner)
		{
			super(owner);
		}
		@Override
		protected void initUI()
		{
			super.initUI();
			add(new JLabel(LanguageBundle.getString("widget.scrubber.label")));
			setSize(720, 15);
		}
	}
	private String foregroundColor = ColorFactory.formatCSS(Color.white);
	private String backgroundColor = ColorFactory.formatCSS(Color.black);
	private String loadingColor    = ColorFactory.formatCSS(new Color(221, 221, 221));
	private Integer barWidth = 5;
	private Integer barHeight = 5;
	private boolean interactive = false;
	private boolean loading = true;
	public Scrubber()
	{
		super(WidgetType.Scrubber);
		addProperty(new StringProperty("foregroundColor") {
			@Override
			public String getValue() { return foregroundColor; }
			@Override
			public void setValue(String value) { foregroundColor = value; }
		});
		addProperty(new StringProperty("backgroundColor") {
			@Override
			public String getValue() { return backgroundColor; }
			@Override
			public void setValue(String value) { backgroundColor = value; }
		});
		addProperty(new StringProperty("loadingColor") {
			@Override
			public String getValue() { return loadingColor; }
			@Override
			public void setValue(String value) { loadingColor = value==null?"#dddddd":value; }
		});
		addProperty(new IntegerProperty("barWidth") {
			@Override
			public Integer getValue() { return barWidth; }
			@Override
			public void setValue(Integer value) { barWidth = value; }
		});
		addProperty(new IntegerProperty("barHeight") {
			@Override
			public Integer getValue() { return barHeight; }
			@Override
			public void setValue(Integer value) { barHeight = value; }
		});
		addProperty(new BooleanProperty("interactive") {
			@Override
			public Boolean getValue() { return interactive; }
			@Override
			public void setValue(Boolean value) { interactive = value==null?false:value; }
		});
		addProperty(new BooleanProperty("loading") {
			@Override
			public Boolean getValue() { return loading; }
			@Override
			public void setValue(Boolean value) { loading = value==null?true:value; }
		});
	}
	@Override
	protected WidgetComponent createComponent()
	{
		return new ScrubberWidgetComponent(this);
	}
	@Override
	protected WidgetPropertiesEditor createWidgetEditor()
	{
		return new ScrubberWidgetEditor(this);
	}
}
