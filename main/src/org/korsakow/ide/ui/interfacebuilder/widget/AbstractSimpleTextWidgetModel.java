package org.korsakow.ide.ui.interfacebuilder.widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;

import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.property.AbstractProperty;
import org.korsakow.ide.resources.property.IntegerProperty;
import org.korsakow.ide.resources.property.StringProperty;
import org.korsakow.ide.resources.widget.DefaultTableWidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.FontStyle;
import org.korsakow.ide.resources.widget.FontWeight;
import org.korsakow.ide.resources.widget.TextDecoration;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.propertyhandler.ColorPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontFamilyPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontSizePropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontStylePropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontWeightPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.TextDecorationPropertyHandler;
import org.korsakow.services.util.ColorFactory;

public abstract class AbstractSimpleTextWidgetModel extends WidgetModel
{

	protected static class AbstractSimpleTextWidgetComponent extends WidgetComponent
	{
		protected JLabel label;
		public AbstractSimpleTextWidgetComponent(WidgetModel owner)
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
			label = new JLabel();
			label.setForeground(Color.white);
			add(label);
			final Dimension size = getPreferredSize();
			size.width += 5; // dunno why, i guess the label needs some time to correctly calculate its size but we're lazy
			setSize(size);
		}
		public void setFontColor(Color color) {
			label.setForeground(color);
		}
		public void setFontFamily(String family) {
			label.setFont(new Font(family, label.getFont().getStyle(), label.getFont().getSize()));
		}
		public void setFontStyle(FontStyle style) {
			int s = label.getFont().getStyle() & Font.BOLD;
			switch (style) {
			case Normal:
				s |= Font.PLAIN;
				break;
			case Italic:
				s |= Font.ITALIC;
				break;
			default:
				throw new IllegalArgumentException("no such fontstyle: " + style);
			}
			label.setFont(new Font(label.getFont().getFamily(), s, label.getFont().getSize()));
		}
		public void setFontWeight(FontWeight weight) {
			int s = label.getFont().getStyle() & Font.ITALIC;
			switch (weight) {
			case Normal:
				s |= Font.PLAIN;
				break;
			case Bold:
				s |= Font.BOLD;
				break;
			default:
				throw new IllegalArgumentException("no such fontweight: " + weight);
			}
			label.setFont(new Font(label.getFont().getFamily(), s, label.getFont().getSize()));
		}
		public void setFontSize(int size) {
			label.setFont(label.getFont().deriveFont((float)size));
		}
		public void setTextDecoration(int weight) {
			label.setFont(label.getFont().deriveFont(weight));
		}
	}
	protected static class AbstractSimpleTextWidgetEditor extends DefaultTableWidgetPropertiesEditor
	{
		public AbstractSimpleTextWidgetEditor(AbstractSimpleTextWidgetModel model)
		{
			super(model);
			addPropertyHandler("fontColor", new ColorPropertyHandler());
			addPropertyHandler("fontSize", new FontSizePropertyHandler());
			addPropertyHandler("fontFamily", new FontFamilyPropertyHandler());
			addPropertyHandler("fontWeight", new FontWeightPropertyHandler());
			addPropertyHandler("fontStyle", new FontStylePropertyHandler());
			addPropertyHandler("textDecoration", new TextDecorationPropertyHandler());
		}
	}

	protected String fontColor = ColorFactory.formatCSS(Color.white);
	protected String fontFamily = "Courier";
	protected Integer fontSize = 10;
	protected FontWeight fontWeight = FontWeight.Normal;
	protected FontStyle fontStyle = FontStyle.Normal;
	protected TextDecoration textDecoration = TextDecoration.None;

	public AbstractSimpleTextWidgetModel(WidgetType widgetType)
	{
		super(widgetType);
		addProperty(new StringProperty("fontColor") {
			@Override
			public String getValue() { return fontColor; }
			@Override
			public void setValue(String value) { setFontColor(value); }
		});
		addProperty(new StringProperty("fontFamily") {
			@Override
			public String getValue() { return fontFamily; }
			@Override
			public void setValue(String value) { setFontFamily(value); }
		});
		addProperty(new IntegerProperty("fontSize") {
			@Override
			public Integer getValue() { return fontSize; }
			@Override
			public void setValue(Integer value) { setFontSize(value);  }
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
	public AbstractSimpleTextWidgetComponent getComponent() {
		return (AbstractSimpleTextWidgetComponent)super.getComponent();
	}
	@Override
	public AbstractSimpleTextWidgetEditor createWidgetEditor() {
		return new AbstractSimpleTextWidgetEditor(this);
	}
	
	public void setFontColor(String color)
	{
		String oldValue = fontColor;
		fontColor = color;
		firePropertyChange("fontColor", oldValue, color);
		if (color != null)
			getComponent().setFontColor(ColorFactory.createRGB(color));
	}
	public void setFontFamily(String family)
	{
		fontFamily = family;
		if (family != null)
			getComponent().setFontFamily(family);
	}

	public String getFontFamily()
	{
		return fontFamily;
	}

	public void setFontSize(Integer size)
	{
		fontSize = size;
		if (size != null)
			getComponent().setFontSize(size);
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
		if (weight != null)
			getComponent().setFontWeight(weight);
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
		if (fontStyle != null)
			getComponent().setFontStyle(fontStyle);
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