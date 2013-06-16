package org.korsakow.domain.mapper.output;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.korsakow.domain.Pattern;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.mapper.exception.LostUpdateException;
import org.korsakow.services.tdg.KeywordTDG;
import org.korsakow.services.tdg.PatternTDG;
import org.korsakow.services.tdg.PropertyTDG;

public class PatternOutputMapper implements GenericOutputMapper<Long, Pattern>{

	public void delete(Pattern a) throws MapperException {
		try{
			if (0 == PatternTDG.delete(a.getId(), a.getVersion()))
				throw new MapperException(String.format("Record not found: id=%d, version=%d", a.getId(), a.getVersion()));
			KeywordTDG.deleteByObject(a.getId());
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}

	public void insert(Pattern a) throws MapperException {
		try {
			if(a.getId()==0){
				PatternTDG.insert(a.getVersion(), a.getPatternType());
			} else {
				PatternTDG.insert(a.getId(), a.getVersion(), a.getPatternType());
			}
			for (IKeyword keyword : a.getKeywords())
				KeywordOutputMapper.insert(a.getId(), keyword);
			for (String id : a.getDynamicPropertyIds())
				PropertyTDG.insert(a.getId(), id, a.getDynamicProperty(id));
		} catch (XPathException e) {
			throw new MapperException(e);
		}
	}

	public void update(Pattern a) throws MapperException {
		try{
			if(PatternTDG.update(a.getId(), a.getVersion(), a.getPatternType()) == 0) {
				throw new LostUpdateException("Your version is out of date. No records were altered.");
			}
			for (IKeyword keyword : a.getKeywords())
				keyword.getValue(); // force proxies;
			KeywordTDG.deleteByObject(a.getId());
			for (IKeyword keyword : a.getKeywords())
				KeywordOutputMapper.insert(a.getId(), keyword);
			for (String id : a.getDynamicPropertyIds())
				PropertyTDG.insert(a.getId(), id, a.getDynamicProperty(id));
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
}
