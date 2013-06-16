package org.korsakow.domain.mapper.input;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;
import org.dsrg.soenea.uow.ObjectRemovedException;
import org.korsakow.domain.Video;
import org.korsakow.domain.VideoFactory;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.proxy.KeywordCollectionProxy;
import org.korsakow.domain.proxy.VideoProxy;
import org.korsakow.services.finder.VideoFinder;

public class VideoInputMapper {
	
	public static Video map(long id) throws MapperException {
		try {
			return IdentityMap.get(id, Video.class);
		} catch (ObjectRemovedException e) {
			throw new DomainObjectNotFoundException(e);
		} catch (DomainObjectNotFoundException e) {
			// ok we'll create it
		}
		try {
			ResultSet rs = null;
			Video a = null;
			rs = VideoFinder.find(id);
			if(!rs.next()) throw new DomainObjectNotFoundException("That object doesn't exist: " + id);
			a = getVideo(rs);
			rs.close();
			return a;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}

	public static List<? extends IVideo> findAll() throws MapperException {
		List<IVideo> list = new ArrayList<IVideo>();
		try {
			ResultSet rs = null;
			rs = VideoFinder.findAll();
			while (rs.next()) {
				list.add(new VideoProxy(rs.getLong("id")));
			}
			rs.close();
			return list;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	private static Video getVideo(ResultSet rs) throws SQLException, MapperException {
		long id = rs.getLong("id");
		
		try {
			return IdentityMap.get(id, Video.class);
		} catch (ObjectRemovedException e) {
			throw new MapperException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		
		Video video = VideoFactory.createClean(
				id, 
				rs.getLong("version"),
				rs.getString("name"),
				new KeywordCollectionProxy(id),
				rs.getString("filename"),
				rs.getString("subtitles"));		
		return video;
	}
}
