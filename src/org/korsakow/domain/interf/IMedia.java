package org.korsakow.domain.interf;

import java.io.FileNotFoundException;

import org.korsakow.domain.MediaSource;


public interface IMedia extends IResource
{
	MediaSource getSource();
	void setFilename(String filename);
	String getFilename();
	/**
	 * This method is somewhat badly named, it should be called: getWorkingFilename or getExistingFilename or getResolvedFilename
	 * 
	 * if the file denoted by getFilename() exists, it is returned, otherwise the following modifications are attempted,
	 * the first one which exists is returned
	 * 
	 * @return if no variation on getFilename() exists, the return value is undefined TODO: perhaps throw an exception
	 */
	String getAbsoluteFilename() throws FileNotFoundException;
}
