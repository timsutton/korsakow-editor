package org.korsakow.services.conversion;

import java.util.List;

public interface IConversionModule
{

	List<String> getWarnings();
	void convert() throws ConversionException;

}