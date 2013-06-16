package org.korsakow.ide.ui.interfacebuilder.widget;



import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.property.AbstractProperty;
import org.korsakow.ide.resources.property.IntegerProperty;
import org.korsakow.ide.resources.property.StringProperty;
import org.korsakow.ide.resources.widget.FontStyle;
import org.korsakow.ide.resources.widget.FontWeight;
import org.korsakow.ide.resources.widget.HorizontalTextAlignment;
import org.korsakow.ide.resources.widget.PreviewTextEffect;
import org.korsakow.ide.resources.widget.PreviewTextMode;
import org.korsakow.ide.resources.widget.ScalingPolicy;
import org.korsakow.ide.resources.widget.TextDecoration;
import org.korsakow.ide.resources.widget.VerticalTextAlignment;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.WidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.editors.AbstractLinkWidgetEditor;
import org.korsakow.services.util.ColorFactory;

public class AbstractLink extends WidgetModel
{
	protected static class AbstractLinkWidgetComponent extends WidgetComponent
	{
		protected JLabel label;
		public AbstractLinkWidgetComponent(WidgetModel owner)
		{
			super(owner);
		}
		@Override
		protected void initUI()
		{
			super.initUI();
			setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
			setSize(240, 135);
			add(label = new JLabel());
			label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		}
	}
	@Override
	protected WidgetComponent createComponent()
	{
		return new AbstractLinkWidgetComponent(this);
	}
	protected String fontColor = ColorFactory.formatCSS(Color.white);
	protected String fontFamily = "Courier";
	protected Integer fontSize = 10;
	protected FontWeight fontWeight = FontWeight.Normal;
	protected FontStyle fontStyle = FontStyle.Normal;
	protected TextDecoration textDecoration = TextDecoration.None;
	protected HorizontalTextAlignment horizontalTextAlignment = HorizontalTextAlignment.Left;
	protected VerticalTextAlignment verticalTextAlignment = VerticalTextAlignment.Top;
	protected PreviewTextMode previewTextMode = PreviewTextMode.ALWAYS;
	protected PreviewTextEffect previewTextEffect = PreviewTextEffect.NONE;
	protected ScalingPolicy scalingPolicy = ScalingPolicy.MaintainAspectRatio;
	public AbstractLink(WidgetType widgetType)
	{
		super(widgetType);
		addProperty(new StringProperty("fontColor") {
			@Override
			public String getValue() { return getFontColor(); }
			@Override
			public void setValue(String value) { setFontColor(value); }
		});
		addProperty(new StringProperty("fontFamily") {
			@Override
			public String getValue() { return getFontFamily(); }
			@Override
			public void setValue(String value) { setFontFamily(value); }
		});
		addProperty(new IntegerProperty("fontSize") {
			@Override
			public Integer getValue() { return getFontSize(); }
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
		addProperty(new AbstractProperty("horizontalTextAlignment") {
			@Override
			public Object getValue() { return getHorizontalTextAlignment().getId(); }
			@Override
			public void setValue(Object value) {
				if (value instanceof HorizontalTextAlignment == false)
					value = HorizontalTextAlignment.forId(value.toString());
				setHorizontalTextAlignment((HorizontalTextAlignment)value);
			}
		});
		addProperty(new AbstractProperty("verticalTextAlignment") {
			@Override
			public Object getValue() { return getVerticalTextAlignment().getId(); }
			@Override
			public void setValue(Object value) {
				if (value instanceof VerticalTextAlignment == false)
					value = VerticalTextAlignment.forId(value.toString());
				setVerticalTextAlignment((VerticalTextAlignment)value);
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
		addProperty(new AbstractProperty("previewTextMode") {
			@Override
			public Object getValue() { return getPreviewTextMode().getId(); }
			@Override
			public void setValue(Object value) {
				if (value instanceof PreviewTextMode == false)
					value = PreviewTextMode.forId(value.toString());
				setPreviewTextMode((PreviewTextMode)value);
			}
		});
		addProperty(new AbstractProperty("previewTextEffect") {
			@Override
			public Object getValue() { return getPreviewTextEffect().getId(); }
			@Override
			public void setValue(Object value) {
				if (value instanceof PreviewTextEffect == false)
					value = PreviewTextEffect.forId(value.toString());
				setPreviewTextEffect((PreviewTextEffect)value);
			}
		});
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
	@Override
	protected WidgetPropertiesEditor createWidgetEditor()
	{
		return new AbstractLinkWidgetEditor(this);
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
		String oldValue = fontFamily;
		fontFamily = family;
		firePropertyChange("fontFamily", oldValue, family);
	}
	public String getFontFamily()
	{
		return fontFamily;
	}
	public void setFontSize(Integer size)
	{
		Integer oldValue = fontSize;
		fontSize = size;
		firePropertyChange("fontSize", oldValue, size);
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
	public void setHorizontalTextAlignment(HorizontalTextAlignment horizontalTextAlignment)
	{
		HorizontalTextAlignment oldValue = this.horizontalTextAlignment;
		this.horizontalTextAlignment = horizontalTextAlignment;
		firePropertyChange("horizontalTextAlignment", oldValue, horizontalTextAlignment);
	}
	public HorizontalTextAlignment getHorizontalTextAlignment()
	{
		return horizontalTextAlignment;
	}
	public void setVerticalTextAlignment(VerticalTextAlignment verticalTextAlignment)
	{
		VerticalTextAlignment oldValue = this.verticalTextAlignment;
		this.verticalTextAlignment = verticalTextAlignment;
		firePropertyChange("verticalTextAlignment", oldValue, verticalTextAlignment);
	}
	public VerticalTextAlignment getVerticalTextAlignment()
	{
		return verticalTextAlignment;
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
	public void setPreviewTextEffect(PreviewTextEffect effect)
	{
		PreviewTextEffect oldValue = previewTextEffect;
		previewTextEffect = effect;
		firePropertyChange("previewTextEffect", oldValue, effect);
	}
	public PreviewTextEffect getPreviewTextEffect()
	{
		return previewTextEffect;
	}
	public void setPreviewTextMode(PreviewTextMode mode)
	{
		PreviewTextMode oldValue = previewTextMode;
		previewTextMode = mode;
		firePropertyChange("previewTextMode", oldValue, mode);
	}
	public PreviewTextMode getPreviewTextMode()
	{
		return previewTextMode;
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
