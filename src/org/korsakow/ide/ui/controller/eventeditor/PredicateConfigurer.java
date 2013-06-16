/**
 * 
 */
package org.korsakow.ide.ui.controller.eventeditor;

import java.util.Collection;

import org.korsakow.ide.ui.resources.EventEditor;

public class PredicateConfigurer implements EditorConfigurer
{
	private ArgConfig config;
	public PredicateConfigurer(ArgConfig config)
	{
		this.config = config;
	}
	public void configure(EventEditor editor)
	{
		Collection<?> choices = config.getChoices();
		editor.setIfArgVisible(!choices.isEmpty());
		if (!choices.isEmpty()) {
			editor.setIfArgLabel(config.getLabel());
			editor.setIfArgChoices(choices, config.isEditable(), config.getRenderer(editor));
		}
	}
}