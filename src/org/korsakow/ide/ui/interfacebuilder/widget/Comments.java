package org.korsakow.ide.ui.interfacebuilder.widget;

import java.awt.Color;

import javax.swing.JLabel;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.property.IntegerProperty;
import org.korsakow.ide.resources.property.StringProperty;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.WidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.editors.CommentsWidgetEditor;
import org.korsakow.services.util.ColorFactory;

public class Comments extends WidgetModel
{
	private static class CommentsWidgetComponent extends WidgetComponent
	{
		public CommentsWidgetComponent(WidgetModel owner)
		{
			super(owner);
		}
		@Override
		protected void initUI()
		{
			super.initUI();
			add(new JLabel(LanguageBundle.getString("widget.comments.label")));
			setSize(320, 240);
		}
	}
	private String fontColor = ColorFactory.formatCSS(Color.white);
	private Integer fontSize = 10;
	private String backgroundColor = ColorFactory.formatCSS(Color.black);
	private Integer perPage = 3;
	public Comments()
	{
		super(WidgetType.Comments);
		addProperty(new StringProperty("fontColor") {
			@Override
			public String getValue() { return fontColor; }
			@Override
			public void setValue(String value) { fontColor = value; }
		});
		addProperty(new StringProperty("backgroundColor") {
			@Override
			public String getValue() { return backgroundColor; }
			@Override
			public void setValue(String value) { backgroundColor = value; }
		});
		addProperty(new IntegerProperty("fontSize") {
			@Override
			public Integer getValue() { return fontSize; }
			@Override
			public void setValue(Integer value) { fontSize = value; }
		});
		addProperty(new IntegerProperty("perPage") {
			@Override
			public Integer getValue() { return perPage; }
			@Override
			public void setValue(Integer value) { perPage = value; }
		});
	}
	@Override
	protected WidgetComponent createComponent()
	{
		return new CommentsWidgetComponent(this);
	}
	@Override
	protected WidgetPropertiesEditor createWidgetEditor()
	{
		return new CommentsWidgetEditor(this);
	}
}
