/**
 * 
 */
package org.korsakow.ide.ui.controller.eventeditor;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ListCellRenderer;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.resources.EventEditor;

public class SnusConfig implements ArgConfig
{
	public String getLabel() {
		return LanguageBundle.getString("predicate.arg.snu.label");
	}
	public boolean isEditable() {
		return false;
	}
	public Collection<?> getChoices() {
		try {
			return SnuInputMapper.findAll();
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog( e );
			return new ArrayList< ISnu >();
		}
	}
	public ListCellRenderer getRenderer(EventEditor editor) {
		return new SnuListCellRenderer();
	}
}