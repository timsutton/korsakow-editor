/**
 * 
 */
package org.korsakow.domain.interchange.ddg;

import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SnuDDG extends AbstractResourceDDG
{
	public SnuDDG(Document document) {
		super(document);
	}

	public Element create() {
		return create("Snu");
	}
	public Element createList() {
		return create("snus");
	}
	public void append(Node parent, Long id, String name, 
			long mainMediaId,
			float rating, Long lives, Long maxLinks,
			boolean looping, boolean starter, boolean ender,
			Long backgroundSoundId, float backgroundSoundVolume,
			Long interfaceId,
			Long previewMediaId, String previewText, String insertText)
	{
		append(parent, id, name);
		DomUtil.appendNumberNode(getDocument(), parent, "mainMediaId", mainMediaId);
		DomUtil.appendNumberNode(getDocument(), parent, "rating", rating);
		DomUtil.appendNumberNode(getDocument(), parent, "lives", lives, "NaN");
		DomUtil.appendNumberNode(getDocument(), parent, "maxLinks", maxLinks, "NaN");
		DomUtil.appendBooleanNode(getDocument(), parent, "looping", looping);
		DomUtil.appendBooleanNode(getDocument(), parent, "starter", starter);
		DomUtil.appendBooleanNode(getDocument(), parent, "ender", ender);
    	DomUtil.appendNumberNode(getDocument(), parent, "backgroundSoundId", backgroundSoundId);
        if (backgroundSoundId != null) {
        	DomUtil.appendNumberNode(getDocument(), parent, "backgroundSoundVolume", backgroundSoundVolume);
        }
        DomUtil.appendNumberNode(getDocument(), parent, "interfaceId", interfaceId);
        DomUtil.appendNumberNode(getDocument(), parent, "previewMediaId", interfaceId);
        DomUtil.appendTextNode(getDocument(), parent, "previewText", previewText);
        DomUtil.appendTextNode(getDocument(), parent, "insertText", insertText);
	}
}