/**
 * 
 */
package org.korsakow.domain.interchange.ddg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TextDDG extends AbstractMediaDDG
{
	public TextDDG(Document document) {
		super(document);
	}
	public Element create() {
		return create("Text");
	}
	public Element createList() {
		return create("texts");
	}
}