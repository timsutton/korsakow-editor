/**
 * 
 */
package org.korsakow.domain.interchange.ddg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SoundDDG extends AbstractMediaDDG
{
	public SoundDDG(Document document) {
		super(document);
	}
	public Element create() {
		return create("Sound");
	}
	public Element createList() {
		return create("sounds");
	}
}