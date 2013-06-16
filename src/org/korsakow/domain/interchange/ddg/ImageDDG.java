/**
 * 
 */
package org.korsakow.domain.interchange.ddg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ImageDDG extends AbstractMediaDDG
{
	public ImageDDG(Document document) {
		super(document);
	}
	public Element create() {
		return create("Image");
	}
	public Element createList() {
		return create("images");
	}
}