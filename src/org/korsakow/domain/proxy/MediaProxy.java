package org.korsakow.domain.proxy;

import java.io.FileNotFoundException;

import org.korsakow.domain.Media;
import org.korsakow.domain.MediaSource;
import org.korsakow.domain.interf.IMedia;

public abstract class MediaProxy<DO extends Media> extends ResourceProxy<DO> implements IMedia {

	public MediaProxy(long id)
	{
		super(id);
	}
	
	public void setSource(MediaSource source)
	{
		getInnerObject().setSource(source);
	}
	public MediaSource getSource()
	{
		return getInnerObject().getSource();
	}

	public String getFilename() {
		return getInnerObject().getFilename();
	}


	public void setFilename(String filename) {
		getInnerObject().setFilename(filename);
		
	}
	
	public String getAbsoluteFilename() throws FileNotFoundException {
		return getInnerObject().getAbsoluteFilename();
	}
}
