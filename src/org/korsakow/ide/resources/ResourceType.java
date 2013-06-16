/**
 * 
 */
package org.korsakow.ide.resources;

import java.util.Hashtable;

import javax.swing.Icon;

import org.korsakow.domain.interf.IResource;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.util.UIResourceManager;

public enum ResourceType
{
	SNU("org.korsakow.resource.Snu", LanguageBundle.getString("resource.snu.label"), UIResourceManager.ICON_SNU),
	VIDEO("org.korsakow.resource.Video", LanguageBundle.getString("resource.video.label"), UIResourceManager.ICON_VIDEO),
	SOUND("org.korsakow.resource.Sound", LanguageBundle.getString("resource.sound.label"), UIResourceManager.ICON_SOUND),
	PROJECT("org.korsakow.resource.Project", LanguageBundle.getString("resource.project.label"), UIResourceManager.ICON_PROJECT),
	WIDGET("org.korsakow.resource.Widget", LanguageBundle.getString("resource.widget.label"), UIResourceManager.ICON_WIDGET),
	INTERFACE("org.korsakow.resource.Interface", LanguageBundle.getString("resource.interface.label"), UIResourceManager.ICON_INTERFACE),
	RULE("org.korsakow.resource.Rule", LanguageBundle.getString("resource.rule.label"), UIResourceManager.ICON_RULE),
	PREDICATE("org.korsakow.resource.Predicate", LanguageBundle.getString("resource.predicate.label"), UIResourceManager.ICON_RULE),
	TRIGGER("org.korsakow.resource.Trigger", LanguageBundle.getString("resource.trigger.label"), UIResourceManager.ICON_RULE),
	IMAGE("org.korsakow.resource.Image", LanguageBundle.getString("resource.image.label"), UIResourceManager.ICON_IMAGE),
	TEXT("org.korsakow.resource.Text", LanguageBundle.getString("resource.text.label"), UIResourceManager.ICON_TEXT);
	
	
	private static Hashtable<String, ResourceType> byId = new Hashtable<String, ResourceType>();
	public static ResourceType forId(String id)
	{
		if (byId.get(id)==null) {
			for (ResourceType type : ResourceType.values())
				if (type.getTypeId().equals(id)) {
					byId.put(id, type);
					break;
				}
		}
		if (byId.get(id)==null)
			throw new IllegalArgumentException(id);
		return byId.get(id);
	}
	
	private String display;
	private String typeId;
	private String iconName;
	ResourceType(String typeId, String display, String iconName)
	{
		this.typeId = typeId;
		this.display = display;
		this.iconName = iconName;
	}
	public String getTypeId()
	{
		return typeId;
	}
	public Icon getIcon()
	{
		return UIResourceManager.getIcon(iconName);
	}
	public String getDisplayString() {
		return display;
	}
	public boolean isMedia() {
		switch (this) {
		case IMAGE:
		case VIDEO:
		case SOUND:
		case TEXT:
			return true;
		}
		return false;
	}
	public boolean isInstance( IResource obj ) {
		if ( obj == null )
			return false;
		return forId( obj.getType() ) == this;
	}
}