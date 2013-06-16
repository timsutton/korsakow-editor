/**
 * 
 */
package org.korsakow.ide.ui.controller.eventeditor;

import java.util.Collections;

import org.korsakow.ide.ui.resources.EventEditor;

public class NoArgRuleConfigurer implements EditorConfigurer
{
	public static void Configure(EventEditor editor)
	{
		editor.setThenArgVisible(false);
		editor.setThenArgChoices(Collections.EMPTY_LIST, false, null); // a desired side effect is that getThen() will return null
	}
	public void configure(EventEditor editor)
	{
		Configure(editor);
	}
}