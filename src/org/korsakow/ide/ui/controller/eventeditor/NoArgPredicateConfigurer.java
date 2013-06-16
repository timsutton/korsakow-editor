/**
 * 
 */
package org.korsakow.ide.ui.controller.eventeditor;

import java.util.Collections;

import org.korsakow.ide.ui.resources.EventEditor;

public class NoArgPredicateConfigurer implements EditorConfigurer
{
	public static void Configure(EventEditor editor)
	{
		editor.setIfArgVisible(false);
		editor.setIfArgChoices(Collections.EMPTY_LIST, false, null); // a desired side effect is that getIf() will return null
	}
	public void configure(EventEditor editor)
	{
		Configure(editor);
	}
}