package org.korsakow.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;

import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.ide.DataRegistry;

public abstract class Media extends Resource implements IMedia
{
	MediaSource source = MediaSource.FILE;
	private String filename;
	public Media(long id, long version)
	{
		super(id, version);
	}
	/**
	 * Initializes the media with the source as a file
	 * @param id
	 * @param version
	 * @param name
	 * @param keywords
	 * @param filename
	 */
	public Media(long id, long version, String name, Collection<IKeyword> keywords, String filename)
	{
		super(id, version, name, keywords);
		setFilename(filename);
		setSource(MediaSource.FILE);
	}
	public Media(long id, long version, String name, Collection<IKeyword> keywords, MediaSource source)
	{
		super(id, version, name, keywords);
		setSource(source);
	}
	public MediaSource getSource()
	{
		return source;
	}
	public void setSource(MediaSource source)
	{
		this.source = source;
	}
	public String getFilename()
	{
		return filename;
	}
	public void setFilename(String filename)
	{
		this.filename = filename;
	}
	public String getAbsoluteFilename() throws FileNotFoundException {
		return getAbsoluteFilename(getFilename());
	}
	public static String getAbsoluteFilename(String path) throws FileNotFoundException {
		File file = new File(path);
		if(file.exists()) return file.getAbsolutePath();
		file = new File(DataRegistry.getFile().getParent()+File.separatorChar+path);
		if (file.exists())
			return file.getAbsolutePath();
		throw new FileNotFoundException(path);
	}
}
