package org.korsakow.services.conversion;


import java.awt.Color;
import java.util.UUID;

import javax.xml.xpath.XPathException;

import org.korsakow.domain.interchange.ddg.DynamicPropertiesDDG;
import org.korsakow.domain.interf.ISnu.BackgroundSoundMode;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.widget.FontStyle;
import org.korsakow.ide.resources.widget.FontWeight;
import org.korsakow.ide.resources.widget.PlayMode;
import org.korsakow.ide.resources.widget.ScalingPolicy;
import org.korsakow.ide.resources.widget.TextDecoration;
import org.korsakow.services.util.ColorFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ConvertUpTo22_4 extends ConversionModule
{
	public ConvertUpTo22_4(Document document)
	{
		super(document);
	}

	@Override
	public void convert() throws ConversionException
	{
		try {
			document.getDocumentElement().setAttribute("versionMajor", "5.0.5");
			document.getDocumentElement().setAttribute("versionMinor", "22.4");
			
			for (Node node : helper.xpathAsList("//Snu")) {
				if (helper.getBoolean((Element)node, "backgroundSoundLooping") == null)
					helper.setBoolean((Element)node, "backgroundSoundLooping", true);
				if (helper.getString((Element)node, "backgroundSoundMode") == null)
					helper.setString((Element)node, "backgroundSoundMode", BackgroundSoundMode.KEEP.getId());
			}
			for (Node node : helper.xpathAsList("//Project")) {
				if (helper.getBoolean((Element)node, "backgroundSoundLooping") == null)
					helper.setBoolean((Element)node, "backgroundSoundLooping", true);
				if (helper.getString((Element)node, "uuid") == null)
					helper.setString((Element)node, "uuid", UUID.randomUUID().toString());
				helper.setLong((Element)node, "defaultInterface", helper.xpathAsLong("//Interface[1]/id"));
			}
			
			// interchange format
			for (Node node : helper.xpathAsList("//Widget")) {
				if (helper.getString((Element)node, "widgetType") == null) {
					helper.setString((Element)node, "widgetType", helper.getString((Element)node, "type"));
					helper.setString((Element)node, "type", null);
				}
			}
			
			helper.removeNodes("//Widget//fontBackgroundColor");
			for (Node node : helper.xpathAsList("//Widget[widgetType=?]", WidgetType.MediaArea.getId())) {
				if (helper.getString((Element)node, "playMode") == null)
					helper.setString((Element)node, "playMode", PlayMode.Always.getId())
						.setAttribute(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE, DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE_TRUE);
				if (helper.getString((Element)node, "looping") == null)
					helper.setBoolean((Element)node, "looping", Boolean.FALSE)
						.setAttribute(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE, DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE_TRUE);
			}
			for (Node node : helper.xpathAsList("//Widget[widgetType=?]", WidgetType.MainMedia.getId())) {
				if (helper.getString((Element)node, "scalingPolicy") == null)
					helper.setString((Element)node, "scalingPolicy", ScalingPolicy.ScaleDownMaintainAspectRatio.getId())
						.setAttribute(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE, DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE_TRUE);
			}
			for (Node node : helper.xpathAsList("//Widget[widgetType=? or widgetType=?]",
					WidgetType.SnuAutoLink.getId(),
					WidgetType.SnuFixedLink.getId()
					)) {
				if (helper.getString((Element)node, "scalingPolicy") == null)
					helper.setString((Element)node, "scalingPolicy", ScalingPolicy.MaintainAspectRatio.getId())
						.setAttribute(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE, DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE_TRUE);
			}
			boolean addedMediaControlsWarning = false;
			for (Node node : helper.xpathAsList("//Widget[widgetType=?]", WidgetType.MediaArea.getId())) {
				if (!addedMediaControlsWarning) {
					addedMediaControlsWarning = true;
					addWarning("The MediaControls widget has been replaced by individual widgets for each control");
				}
				node.getParentNode().removeChild(node);
			}
			for (Node node : helper.xpathAsList("//Widget[widgetType=? or widgetType=? or widgetType=? or widgetType=? or widgetType=?]", 
					WidgetType.SnuAutoLink.getId(),
					WidgetType.SnuAutoMultiLink.getId(),
					WidgetType.SnuFixedLink.getId(),
					WidgetType.InsertText.getId(),
					WidgetType.Subtitles.getId()
					)) {
				if (helper.getString((Element)node, "fontWeight") == null)
					helper.setString((Element)node, "fontWeight", FontWeight.Normal.getId())
						.setAttribute(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE, DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE_TRUE);
				if (helper.getString((Element)node, "fontStyle") == null)
					helper.setString((Element)node, "fontStyle", FontStyle.Normal.getId())
						.setAttribute(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE, DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE_TRUE);
				if (helper.getString((Element)node, "textDecoration") == null)
					helper.setString((Element)node, "textDecoration", TextDecoration.None.getId())
						.setAttribute(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE, DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE_TRUE);
			}
			for (Node node : helper.xpathAsList("//Interface")) {
				if (helper.getString((Element)node, "backgroundColor") == null)
					helper.setString((Element)node, "backgroundColor", ColorFactory.toString(Color.black));
			}
		} catch (XPathException e) {
			throw new ConversionException(e);
		} catch (NumberFormatException e) {
			throw new ConversionException(e);
		}
	}
	
}
