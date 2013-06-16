package org.korsakow.domain.interchange.ddg;


import org.w3c.dom.Document;

public class DomDataGateway
{
	private Document document;
	public DomDataGateway(Document document)
	{
		this.document = document;
	}
	public Document getDocument()
	{
		return document;
	}

}

