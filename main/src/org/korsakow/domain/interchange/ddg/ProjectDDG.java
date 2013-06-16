/**
 * 
 */
package org.korsakow.domain.interchange.ddg;

import org.korsakow.ide.Build;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ProjectDDG extends AbstractResourceDDG
{
	public ProjectDDG(Document document) {
		super(document);
	}

	@Override
	public Element create() {
		return create("Project");
	}
	@Override
	public Element createList() {
		return create("projects");
	}
	public void append(Element parent, Long id, String name,
			String versionMajor, String versionMinor,
			long movieWidth, long movieHeight,
			Long backgroundSoundId, float backgroundSoundVolume,
			Long clickSoundId, float clickSoundVolume,
			Long splashScreenMediaId,
			boolean randomLinkMode, boolean keepLinksOnEmptySearch,
			Long maxLinks
	)
	{
		append(parent, id, name);
        parent.setAttribute("versionMajor", Build.getVersion());
        parent.setAttribute("versionMinor", ""+Build.getRelease());
        
    	DomUtil.appendNumberNode(getDocument(), parent, "movieWidth", movieWidth);
    	DomUtil.appendNumberNode(getDocument(), parent, "movieHeight", movieHeight);
        if (backgroundSoundId!=null) {
        	DomUtil.appendNumberNode(getDocument(), parent, "backgroundSoundId", backgroundSoundId);
        	DomUtil.appendNumberNode(getDocument(), parent, "backgroundSoundVolume", backgroundSoundVolume);
        }
        if (clickSoundId!=null) {
        	DomUtil.appendNumberNode(getDocument(), parent, "clickSoundId", clickSoundId);
        	DomUtil.appendNumberNode(getDocument(), parent, "clickSoundVolume", clickSoundVolume);
        }
        if (splashScreenMediaId != null) {
        	DomUtil.appendTextNode(getDocument(), parent, "splashScreenMediaId", splashScreenMediaId);
        }
        DomUtil.appendBooleanNode(getDocument(), parent, "randomLinkMode", randomLinkMode);
        DomUtil.appendBooleanNode(getDocument(), parent, "keepLinksOnEmptySearch", keepLinksOnEmptySearch);
        DomUtil.appendNumberNode(getDocument(), parent, "maxLinks", maxLinks, "NaN");

	}
}