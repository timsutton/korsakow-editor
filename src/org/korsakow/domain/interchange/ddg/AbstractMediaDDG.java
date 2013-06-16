/**
 * 
 */
package org.korsakow.domain.interchange.ddg;

import org.korsakow.domain.MediaSource;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class AbstractMediaDDG extends AbstractResourceDDG
{

	public AbstractMediaDDG(Document document) {
		super(document);
	}
	protected void appendFile(Node parent, Long id, String name, String filename)
	{
		append(parent, id, name, MediaSource.FILE, filename);
	}
	protected void appendInline(Node parent, Long id, String name, String content)
	{
		append(parent, id, name, MediaSource.INLINE, content);
	}
	public void append(Node parent, Long id, String name, MediaSource mediaSource, String contentorfilename)
	{
		append(parent, id, name);
		switch (mediaSource)
		{
		case FILE:
			DomUtil.appendTextNode(getDocument(), parent, "filename", contentorfilename);
			break;
		case INLINE:
			DomUtil.appendTextNode(getDocument(), parent, "content", contentorfilename);
			break;
		}
	}
}