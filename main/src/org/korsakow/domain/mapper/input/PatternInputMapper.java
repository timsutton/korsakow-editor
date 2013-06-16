package org.korsakow.domain.mapper.input;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;
import org.dsrg.soenea.uow.ObjectRemovedException;
import org.korsakow.domain.Pattern;
import org.korsakow.domain.PatternFactory;
import org.korsakow.domain.interf.IPattern;
import org.korsakow.domain.proxy.KeywordCollectionProxy;
import org.korsakow.domain.proxy.PatternProxy;
import org.korsakow.services.finder.PatternFinder;

public class PatternInputMapper {
	
	public static Pattern map(long id) throws MapperException {
		try {
			return IdentityMap.get(id, Pattern.class);
		} catch (ObjectRemovedException e) {
			throw new DomainObjectNotFoundException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		try {
			ResultSet rs = null;
			Pattern a = null;
			rs = PatternFinder.find(id);
			if(!rs.next()) throw new DomainObjectNotFoundException("That object doesn't exist: " + id);
			a = getPattern(rs);
			rs.close();
			return a;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}

	public static List<? extends IPattern> findAll() throws MapperException {
		List<IPattern> list = new ArrayList<IPattern>();
		try {
			ResultSet rs = null;
			rs = PatternFinder.findAll();
			while (rs.next()) {
				list.add(new PatternProxy(rs.getLong("id")));
			}
			rs.close();
			return list;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	private static Pattern getPattern(ResultSet rs) throws MapperException, SQLException {
		long id = rs.getLong("id");
		
		try {
			return IdentityMap.get(id, Pattern.class);
		} catch (ObjectRemovedException e) {
			throw new MapperException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		
		Pattern pattern = PatternFactory.createClean(
				id, 
				rs.getLong("version"),
				rs.getString("name"),
				rs.getString("patternType"),
				new KeywordCollectionProxy(id),
				rs.getString("filename"));		
		Map<String, String> properties = PropertyInputMapper.map(pattern.getId());
		for (String propId : properties.keySet())
			pattern.setDynamicProperty(propId, properties.get(propId));
		return pattern;
	}
}
