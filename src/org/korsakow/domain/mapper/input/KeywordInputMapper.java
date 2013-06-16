package org.korsakow.domain.mapper.input;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.TreeSet;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Keyword;
import org.korsakow.domain.KeywordFactory;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.finder.KeywordFinder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class KeywordInputMapper {
	
	public static Collection<IKeyword> findByObjectTypeRecursive(String object_type) throws MapperException {
		Collection<IKeyword> keywords = new TreeSet<IKeyword>();
		try {
			ResultSet rs = null;
			rs = KeywordFinder.findByObjectTypeRecursive(object_type);
			while (rs.next()) {
				keywords.add(KeywordFactory.createClean(rs.getString("value"), rs.getFloat("weight")));
			}
			rs.close();
			return keywords;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	public static Collection<IKeyword> findByObjectRecursive(long parentId) throws MapperException {
		Collection<IKeyword> keywords = new TreeSet<IKeyword>();
		try {
			ResultSet rs = null;
			rs = KeywordFinder.findByObjectRecursive(parentId);
			while (rs.next()) {
				keywords.add(KeywordFactory.createClean(rs.getString("value"), rs.getFloat("weight")));
			}
			rs.close();
			return keywords;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	public static Collection<IKeyword> findByObjectType(String object_type) throws MapperException {
		Collection<IKeyword> keywords = new TreeSet<IKeyword>();
		try {
			ResultSet rs = null;
			rs = KeywordFinder.findByObjectType(object_type);
			while (rs.next()) {
				keywords.add(KeywordFactory.createClean(rs.getString("value"), rs.getFloat("weight")));
			}
			rs.close();
			return keywords;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	public static Collection<IKeyword> findByObject(long object_id) throws MapperException {
		
		Collection<IKeyword> keywords = new TreeSet<IKeyword>();
		try {
			ResultSet rs = null;
			rs = KeywordFinder.findByObject(object_id);
			while (rs.next()) {
				keywords.add(KeywordFactory.createClean(rs.getString("value"), rs.getFloat("weight")));
			}
			rs.close();
			return keywords;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	
	public static Collection<IKeyword> findAll() throws MapperException 
	{
		Collection<IKeyword> keywords = new TreeSet<IKeyword>();
		try {
			NodeList nl = KeywordFinder.findAll();
			int length = nl.getLength();
			for (int i = 0; i < length; ++i) {
				Element elm = (Element)nl.item(i);
				String value = DomUtil.getString(elm, "value");
				Float weight = DomUtil.getFloat(elm, "weight");
				keywords.add(KeywordFactory.createClean(value, weight));
			}
			return keywords;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	
	private static Keyword getKeyword(ResultSet rs) throws MapperException, SQLException {
		Keyword keyword = KeywordFactory.createClean(
				rs.getString("value"),
				rs.getFloat("weight")
		);
		return keyword;
	}
}
