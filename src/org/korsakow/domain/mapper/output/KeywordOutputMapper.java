package org.korsakow.domain.mapper.output;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Keyword;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.mapper.exception.LostUpdateException;
import org.korsakow.services.tdg.KeywordTDG;

public class KeywordOutputMapper
{
	public void delete(long object_id, Keyword a) throws MapperException {
		try{
			if (0 == KeywordTDG.delete(object_id, a.getValue()))
				throw new MapperException(String.format("Record not found: value=%d", a.getValue()));

		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}

	public static void insert(long object_id, IKeyword a) throws MapperException {
		try {
			KeywordTDG.insert(object_id, a.getValue(), a.getWeight());
		} catch (XPathException e) {
			throw new MapperException(e);
		}
	}

	public void update(long object_id, Keyword a) throws MapperException {
		try{
			if(KeywordTDG.update(object_id, a.getValue(), a.getWeight()) == 0) {
				throw new LostUpdateException("Your version is out of date. No records were altered.");
			}
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
}
