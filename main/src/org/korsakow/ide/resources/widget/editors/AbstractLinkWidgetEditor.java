package org.korsakow.ide.resources.widget.editors;



import org.korsakow.ide.resources.widget.DefaultTableWidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.propertyhandler.ColorPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontFamilyPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontSizePropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontStylePropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontWeightPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.HorizontalTextAlignmentPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.PreviewTextEffectPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.PreviewTextModePropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.ScalingPolicyPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.TextDecorationPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.VerticalTextAlignmentPropertyHandler;
import org.korsakow.ide.ui.interfacebuilder.widget.AbstractLink;

public class AbstractLinkWidgetEditor extends DefaultTableWidgetPropertiesEditor
{
	public AbstractLinkWidgetEditor(AbstractLink widget)
	{
		super(widget);
		addPropertyHandler("fontColor", new ColorPropertyHandler());
		addPropertyHandler("fontSize", new FontSizePropertyHandler());
		addPropertyHandler("fontFamily", new FontFamilyPropertyHandler());
		addPropertyHandler("fontWeight", new FontWeightPropertyHandler());
		addPropertyHandler("fontStyle", new FontStylePropertyHandler());
		addPropertyHandler("horizontalTextAlignment", new HorizontalTextAlignmentPropertyHandler());
		addPropertyHandler("verticalTextAlignment", new VerticalTextAlignmentPropertyHandler());
		addPropertyHandler("textDecoration", new TextDecorationPropertyHandler());
		addPropertyHandler("previewTextMode", new PreviewTextModePropertyHandler());
		addPropertyHandler("previewTextEffect", new PreviewTextEffectPropertyHandler());
		addPropertyHandler("scalingPolicy", new ScalingPolicyPropertyHandler());
	}
}