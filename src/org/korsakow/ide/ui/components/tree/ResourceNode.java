package org.korsakow.ide.ui.components.tree;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.mapper.input.ResourceInputMapper;
import org.korsakow.ide.resources.ResourceType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ResourceNode extends KNode
{
	public static Set<IResource> getResources(Collection<ResourceNode> nodes) throws MapperException {
		Set<IResource> resources = new LinkedHashSet<IResource>();
		for (ResourceNode node : nodes)
			resources.add(ResourceInputMapper.map(node.getResourceId()));
		return resources;
	}
	public static ResourceNode create(IResource r) {
		switch (ResourceType.forId(r.getType())) {
		case SNU:
			ISnu snu = (ISnu)r;
			return new ResourceNode(
						snu.getName(), ResourceType.forId(snu.getType()), ResourceType.forId(snu.getMainMedia().getType()), snu.getId(),
						true, false, snu.getBackgroundSound() != null,
						snu.getPreviewMedia() != null, snu.getStarter(), snu.getEnder(), snu.getLives(),
						snu.getInterface().getName(), snu.getMainMedia().getFilename()
					);
		case VIDEO:
		case IMAGE:
		case SOUND:
			IMedia media = (IMedia)r;
			return new ResourceNode(
						media.getName(), ResourceType.forId(media.getType()), ResourceType.forId(media.getType()), media.getId(),
						false, false, false,
						false, false, false, null,
						null, media.getFilename()
					);
		case INTERFACE:
			IInterface interf = (IInterface)r;
			return new ResourceNode(
					interf.getName(), ResourceType.forId(interf.getType()), null, interf.getId(),
					false, interf.getClickSound()!=null, false,
					false, false, false, null,
					null, null
				);
		case PROJECT:
			IProject proj = (IProject)r;
			return new ResourceNode(
					proj.getName(), ResourceType.forId(proj.getType()), null, proj.getId(),
					false, proj.getClickSound()!=null, proj.getBackgroundSound()!=null,
					false, false, false, null,
					null, null
				);
		case RULE:
			IRule rule = (IRule)r;
			return new ResourceNode(
					rule.getName(), ResourceType.forId(rule.getType()), null, rule.getId(),
					false, false, false,
					false, false, false, null,
					null, null
				);
		case WIDGET:
			IWidget widget = (IWidget)r;
			return new ResourceNode(
					widget.getName(), ResourceType.forId(widget.getType()), null, widget.getId(),
					false, false, false,
					false, false, false, null,
					null, null
				);
		}
		throw new IllegalArgumentException("Unsupported resource type: " + r.getType());
	}
	public static ResourceNode create(IInterface interf) {
		return new ResourceNode(
				interf.getName(), ResourceType.forId(interf.getType()), null, interf.getId(),
				false, false, false,
				false, false, false, null,
				null, null
			);
	}
	
	private final ResourceType type;
	private final ResourceType mediaType;
	private final Long resourceId;
	private boolean isSnufied;
	private boolean clickSound;
	private boolean bgSound;
	private boolean preview;
	private boolean startSnu;
	private boolean endSnu;
	private Long lives;
	private String interfaceName;
	private String filename;
	
	private ResourceNode(String name, ResourceType type, ResourceType mediaType, Long resourceId,
			boolean isSnufied, boolean clickSound, boolean bgSound,
			boolean preview, boolean startSnu, boolean endSnu, Long lives,
			String interfaceName, String filename)
	{
		super(name);
		this.type = type;
		this.mediaType = mediaType;
		this.resourceId = resourceId;
		this.isSnufied = isSnufied;
		this.clickSound = clickSound;
		this.bgSound = bgSound;
		this.preview = preview;
		this.startSnu = startSnu;
		this.endSnu = endSnu;
		this.lives = lives;
		this.interfaceName = interfaceName;
		this.filename = filename;
	}
	public ResourceType getResourceType()
	{
		return type;
	}
	public Long getResourceId()
	{
		return resourceId;
	}
	@Override
	public Element toDomElement(Document doc)
	{
		Element e = doc.createElement("Resource");
		e.setAttribute("class", getResourceType().getTypeId());
		e.setAttribute("id", ""+resourceId);
		e.setAttribute("name", getName());
		for (KNode child : this) {
			e.appendChild(child.toDomElement(doc));
		}
		return e;
	}
	
	public ResourceNode copy() {
		return new ResourceNode(getName(), type, mediaType, resourceId,
				isSnufied, clickSound, bgSound,
				preview, startSnu, endSnu, lives,
				interfaceName, filename);
	}
	
	public ResourceType getType()
	{
		return type;
	}
	public ResourceType getMediaType()
	{
		return mediaType;
	}
	public boolean isSnufied()
	{
		return isSnufied;
	}
	public boolean getClickSound()
	{
		return clickSound;
	}
	public boolean getBgSound()
	{
		return bgSound;
	}
	public boolean getPreview()
	{
		return preview;
	}
	public boolean isStartSnu()
	{
		return startSnu;
	}
	public boolean isEndSnu()
	{
		return endSnu;
	}
	public Long getLives()
	{
		return lives;
	}
	public String getInterfaceName()
	{
		return interfaceName;
	}
	public String getFilename()
	{
		return filename;
	}
	public void setSnufied(boolean isSnufied)
	{
		this.isSnufied = isSnufied;
	}
	public void setClickSound(boolean clickSound)
	{
		this.clickSound = clickSound;
	}
	public void setBgSound(boolean bgSound)
	{
		this.bgSound = bgSound;
	}
	public void setPreview(boolean preview)
	{
		this.preview = preview;
	}
	public void setStartSnu(boolean startSnu)
	{
		this.startSnu = startSnu;
	}
	public void setEndSnu(boolean endSnu)
	{
		this.endSnu = endSnu;
	}
	public void setLives(Long lives)
	{
		this.lives = lives;
	}
	public void setInterfaceName(String interfaceName)
	{
		this.interfaceName = interfaceName;
	}
	public void setFilename(String filename)
	{
		this.filename = filename;
	}
	
}
