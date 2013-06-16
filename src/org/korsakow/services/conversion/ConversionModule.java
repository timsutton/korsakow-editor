package org.korsakow.services.conversion;

import java.util.ArrayList;
import java.util.List;

import org.korsakow.ide.DomHelper;
import org.w3c.dom.Document;

public abstract class ConversionModule implements IConversionModule
{

	protected final Document document;
	protected final DomHelper helper;

	protected List<String> warnings = new ArrayList<String>();
	
	public ConversionModule(Document document)
	{
		this.document = document;
		helper = new DomHelper(document);
	}

	protected void addWarning(String warning) {
		if (!warnings.contains(warning))
			warnings.add(warning);
	}
	public List<String> getWarnings() {
		return warnings;
	}
	public abstract void convert() throws ConversionException;

}