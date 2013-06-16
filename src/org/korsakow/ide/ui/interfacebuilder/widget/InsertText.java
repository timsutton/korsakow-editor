package org.korsakow.ide.ui.interfacebuilder.widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.korsakow.ide.lang.LanguageBundle;
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
import org.korsakow.ide.resources.widget.WidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.propertyhandler.ColorPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontFamilyPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontSizePropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontStylePropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontWeightPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.TextDecorationPropertyHandler;
import org.korsakow.services.util.ColorFactory;

public class InsertText extends WidgetModel
{
	private static class InsertTextWidgetComponent extends WidgetComponent
	{
		private JLabel label;
		public InsertTextWidgetComponent(WidgetModel owner)
		{
			super(owner);
		}
		@Override
		protected void initUI()
		{
			super.initUI();
			setLayout(new BorderLayout());
			setSize(80, 80);
			setOpaque(false); // required for alpha background
			setBackground(new Color(0.8f, 0.8f, 1.0f, 0.5f));
			
			add(label = new JLabel(LanguageBundle.getString("widget.inserttext.label")), BorderLayout.CENTER);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			//label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		}
		@Override
		public void paintComponent(final Graphics g)
		{
			// work around for alpha background
			super.paintComponent(g);
			g.setColor( getBackground() );
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	    @Override
		public boolean getMaintainsAspectByDefaultWhenResized()
	    {
	    	return false;
	    }
	}
	private class InsertTextWidgetEditor extends DefaultTableWidgetPropertiesEditor
	{
		public InsertTextWidgetEditor(WidgetModel widget)
		{
			super(widget);
			addPropertyHandler("fontColor", new ColorPropertyHandler());
			addPropertyHandler("fontSize", new FontSizePropertyHandler());
			addPropertyHandler("fontFamily", new FontFamilyPropertyHandler());
			addPropertyHandler("fontWeight", new FontWeightPropertyHandler());
			addPropertyHandler("fontStyle", new FontStylePropertyHandler());
			addPropertyHandler("textDecoration", new TextDecorationPropertyHandler());
		}
	}
	@Override
	protected WidgetComponent createComponent()
	{
		return new InsertTextWidgetComponent(this);
	}
	@Override
	protected WidgetPropertiesEditor createWidgetEditor()
	{
		return new InsertTextWidgetEditor(this);
	}
	private String fontColor = ColorFactory.formatCSS(Color.white);
	private String fontFamily = "Courier";
	private Integer fontSize = 10;
	protected FontWeight fontWeight = FontWeight.Normal;
	protected FontStyle fontStyle = FontStyle.Normal;
	protected TextDecoration textDecoration = TextDecoration.None;
	public InsertText()
	{
		super(WidgetType.InsertText);
		addProperty(new StringProperty("fontColor") {
			@Override
			public String getValue() { return fontColor; }
			@Override
			public void setValue(String value) { fontColor = value; }
		});
		addProperty(new StringProperty("fontFamily") {
			@Override
			public String getValue() { return fontFamily; }
			@Override
			public void setValue(String value) { fontFamily = value; }
		});
		addProperty(new IntegerProperty("fontSize") {
			@Override
			public Integer getValue() { return fontSize; }
			@Override
			public void setValue(Integer value) { fontSize = value;  }
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
