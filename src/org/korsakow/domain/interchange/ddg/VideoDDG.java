/**
 * 
 */
package org.korsakow.domain.interchange.ddg;

import org.korsakow.domain.MediaSource;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VideoDDG extends AbstractMediaDDG
{

	public VideoDDG(Document document) {
		super(document);
	}
	public Element create()
	{
		return create("Video");
	}
	public Element createList()
	{
		return create("videos");
	}
	public void append(Node parent, Long id, String name, MediaSource mediaSource, String contentorfilename, String subtitles)
	{
		super.append(parent, id, name, mediaSource, contentorfilename);
		DomUtil.appendTextNode(getDocument(), parent, "subtitles", subtitles);
	}
}