/**
 * 
 */
package org.korsakow.ide.ui.controller.eventeditor;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ListCellRenderer;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.mapper.input.KeywordInputMapper;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.resources.EventEditor;

public class KeywordsConfig implements ArgConfig
{
	public String getLabel() {
		return LanguageBundle.getString("predicate.arg.keyword.label");
	}
	public boolean isEditable() {
		return false;
	}
	public Collection<?> getChoices() {
		try {
			return KeywordInputMapper.findAll();
		} catch (MapperException e) {
			return new ArrayList< IKeyword >();
		}
	}
	public ListCellRenderer getRenderer(EventEditor editor) {
		return new KeywordListCellRenderer();
	}
}