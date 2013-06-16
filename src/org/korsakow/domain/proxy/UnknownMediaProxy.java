package org.korsakow.domain.proxy;

import java.io.FileNotFoundException;
import java.util.Collection;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.korsakow.domain.Media;
import org.korsakow.domain.MediaSource;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.mapper.input.ImageInputMapper;
import org.korsakow.domain.mapper.input.PatternInputMapper;
import org.korsakow.domain.mapper.input.SoundInputMapper;
import org.korsakow.domain.mapper.input.TextInputMapper;
import org.korsakow.domain.mapper.input.VideoInputMapper;
import org.korsakow.ide.DataRegistry;
import org.w3c.dom.Element;

/**
 * Doesn't subclass MediaProxy due to a bug in SOENEA which check for a certain class hiererarchy
 * @author d
 *
 */
public class UnknownMediaProxy extends KDomainObjectProxy<Media> implements IMedia {

	public UnknownMediaProxy(long id)
	{
		super(id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<Media> getInnerClass()
	{
		return (Class<Media>) getMedia().getClass();
	}
	
	/**
	 * Users cannot otherwise currently determine which type of media is being proxied...
	 * @return
	 */
	public Media getMedia()
	{
		return getInnerObject();
	}
	@Override
	protected Media getFromMapper(Long id) throws MapperException {
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
		if (element == null)
			throw new DomainObjectNotFoundException("Object does not exist: " + id);
		String tagName = element.getTagName();
		if ("Video".equals(tagName))
			return VideoInputMapper.map(id);
		if ("Sound".equals(tagName))
			return SoundInputMapper.map(id);
		if ("Image".equals(tagName))
			return ImageInputMapper.map(id);
		if ("Text".equals(tagName))
			return TextInputMapper.map(id);
		if ("Pattern".equals(tagName))
			return PatternInputMapper.map(id);
		throw new DomainObjectNotFoundException("unknown media type for: " + id);
	}

	public String getName() {
		return getInnerObject().getName();
	}

	public void setName(String name) {
		getInnerObject().setName(name);
	}

	public Collection<IKeyword> getKeywords()
	{
		return getInnerObject().getKeywords();
	}
	public void setKeywords(Collection<IKeyword> keywords)
	{
		getInnerObject().setKeywords(keywords);
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
	public String getType()
	{
		return getInnerObject().getType();
	}
}
