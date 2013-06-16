package org.korsakow.ide.ui.interfacebuilder.widget;

import java.awt.Color;

import javax.swing.JLabel;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.property.AbstractProperty;
import org.korsakow.ide.resources.property.IntegerProperty;
import org.korsakow.ide.resources.property.StringProperty;
import org.korsakow.ide.resources.widget.FontStyle;
import org.korsakow.ide.resources.widget.FontWeight;
import org.korsakow.ide.resources.widget.TextDecoration;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.WidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.editors.SubtitlesWidgetEditor;
import org.korsakow.services.util.ColorFactory;

public class Subtitles extends WidgetModel
{
	private static class SubtitlesWidgetComponent extends WidgetComponent
	{
		public SubtitlesWidgetComponent(WidgetModel owner)
		{
			super(owner);
		}
		@Override
		protected void initUI()
		{
			super.initUI();
			add(new JLabel(LanguageBundle.getString("widget.subtitles.label")));
			setSize(720, 20);
		}
	    @Override
		public boolean getMaintainsAspectByDefaultWhenResized()
	    {
	    	return false;
	    }
	}
	private String fontColor = ColorFactory.formatCSS(Color.white);
	private String fontFamily = "Courier";
	private Integer fontSize = 10;
	protected FontWeight fontWeight = FontWeight.Normal;
	protected FontStyle fontStyle = FontStyle.Normal;
	protected TextDecoration textDecoration = TextDecoration.None;
	public Subtitles()
	{
		super(WidgetType.Subtitles);
		addProperty(new StringProperty("fontColor") {
			@Override
			public String getValue() { return fontColor; }
			@Override
			public void setValue(String value) { fontColor = value; }
		});
		addProperty(new StringProperty("fontFamily") {
			@Override
			public String getValue() {
				return fontFamily;
			}
			@Override
			public void setValue(String value) {
				fontFamily = value;
			}
		});
		addProperty(new IntegerProperty("fontSize") {
			@Override
			public Integer getValue() {
				return fontSize;
			}
			@Override
			public void setValue(Integer value) {
				fontSize = value;
			}
		});
		addProperty(new AbstractProperty("fontWeight") {
			@Override
			public Object getValue() { return getFontWeight().getId(); }
			@Override
			public void setValue(Object value) {
				if (value instanceof FontWeight == false)
					value = FontWeight.forId(value.toString());
				setFontWeight((FontWeight)value);
			}
		});
		addProperty(new AbstractProperty("fontStyle") {
			@Override
			public Object getValue() { return getFontStyle().getId(); }
			@Override
			public void setValue(Object value) {
				if (value instanceof FontStyle == false)
					value = FontStyle.forId(value.toString());
				setFontStyle((FontStyle)value);
			}
		});
		addProperty(new AbstractProperty("textDecoration") {
			@Override
			public Object getValue() { return getTextDecoration().getId(); }
			@Override
			public void setValue(Object value) {
				if (value instanceof TextDecoration == false)
					value = TextDecoration.forId(value.toString());
				setTextDecoration((TextDecoration)value);
			}
		});
	}
	@Override
	protected WidgetComponent createComponent()
	{
		return new SubtitlesWidgetComponent(this);
	}
	@Override
	protected WidgetPropertiesEditor createWidgetEditor()
	{
		return new SubtitlesWidgetEditor(this);
	}
	public void setFontColor(String color)
	{
		String oldValue = fontColor;
		fontColor = color;
		firePropertyChange("fontColor", oldValue, color);
	}
	public String getFontColor()
	{
		return fontColor;
	}
	public void setFontFamily(String family)
	{
		fontFamily = family;
	}
	public String getFontFamily()
	{
		return fontFamily;
	}
	public void setFontSize(Integer size)
	{
		fontSize = size;
	}
	public Integer getFontSize()
	{
		return fontSize;
	}
	public void setFontWeight(FontWeight weight)
	{
		FontWeight oldValue = fontWeight;
		fontWeight = weight;
		firePropertyChange("fontWeight", oldValue, weight);
	}
	public FontWeight getFontWeight()
	{
		return fontWeight;
	}
	public void setFontStyle(FontStyle fontStyle)
	{
		FontStyle oldValue = this.fontStyle;
		this.fontStyle = fontStyle;
		firePropertyChange("fontStyle", oldValue, fontStyle);
	}
	public FontStyle getFontStyle()
	{
		return fontStyle;
	}
	public void setTextDecoration(TextDecoration textDecoration)
	{
		TextDecoration oldValue = this.textDecoration;
		this.textDecoration = textDecoration;
		firePropertyChange("textDecoration", oldValue, textDecoration);
	}
	public TextDecoration getTextDecoration()
	{
		return textDecoration;
	}
}
