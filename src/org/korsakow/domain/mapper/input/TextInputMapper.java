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
import org.korsakow.domain.MediaSource;
import org.korsakow.domain.Text;
import org.korsakow.domain.TextFactory;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.proxy.KeywordCollectionProxy;
import org.korsakow.domain.proxy.TextProxy;
import org.korsakow.services.finder.TextFinder;

public class TextInputMapper {
	
	public static Text map(long id) throws MapperException {
		try {
			return IdentityMap.get(id, Text.class);
		} catch (ObjectRemovedException e) {
			throw new DomainObjectNotFoundException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		try {
			ResultSet rs = null;
			Text a = null;
			rs = TextFinder.find(id);
			if(!rs.next()) throw new DomainObjectNotFoundException("That object doesn't exist: " + id);
			a = getText(rs);
			rs.close();
			return a;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	public static List<? extends IText> findAll() throws MapperException {
		List<IText> list = new ArrayList<IText>();
		try {
			ResultSet rs = null;
			rs = TextFinder.findAll();
			while (rs.next()) {
				list.add(new TextProxy(rs.getLong("id")));
			}
			rs.close();
			return list;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	private static Text getText(ResultSet rs) throws SQLException, MapperException {
		long id = rs.getLong("id");
		
		try {
			return IdentityMap.get(id, Text.class);
		} catch (ObjectRemovedException e) {
			throw new MapperException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		
		Text sound = TextFactory.createClean(
				id, 
				rs.getLong("version"),
				rs.getString("name"),
				new KeywordCollectionProxy(id),
				MediaSource.getById(rs.getString("source")),
				rs.getString("value"));
		return sound;
	}
}
