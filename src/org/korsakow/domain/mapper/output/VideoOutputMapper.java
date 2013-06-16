package org.korsakow.domain.mapper.output;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.korsakow.domain.Video;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.mapper.exception.LostUpdateException;
import org.korsakow.services.tdg.KeywordTDG;
import org.korsakow.services.tdg.VideoTDG;

public class VideoOutputMapper implements GenericOutputMapper<Long, Video>{

	public void delete(Video a) throws MapperException {
		try{
			if (0 == VideoTDG.delete(a.getId(), a.getVersion()))
				throw new MapperException(String.format("Record not found: id=%d, version=%d", a.getId(), a.getVersion()));
			KeywordTDG.deleteByObject(a.getId());
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}

	public void insert(Video a) throws MapperException {
		try {
			if(a.getId()==0){
				VideoTDG.insert(a.getVersion(), a.getName(), a.getFilename(), a.getSubtitles());
			} else {
				VideoTDG.insert(a.getId(), a.getVersion(), a.getName(), a.getFilename(), a.getSubtitles());
			}
			for (IKeyword keyword : a.getKeywords())
				KeywordOutputMapper.insert(a.getId(), keyword);
		} catch (XPathException e) {
			throw new MapperException(e);
		}
	}

	public void update(Video a) throws MapperException {
		try{
			if(VideoTDG.update(a.getId(), a.getVersion(), a.getName(), a.getFilename(), a.getSubtitles()) == 0) {
				throw new LostUpdateException("Your version is out of date. No records were altered.");
			}
			for (IKeyword keyword : a.getKeywords())
				keyword.getValue(); // force proxies;
			KeywordTDG.deleteByObject(a.getId());
			for (IKeyword keyword : a.getKeywords())
				KeywordOutputMapper.insert(a.getId(), keyword);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
}
