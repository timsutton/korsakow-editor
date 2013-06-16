/**
 * 
 */
package org.korsakow.ide.ui.controller.eventeditor;

import java.util.Collection;

import org.korsakow.ide.ui.resources.EventEditor;

public class RuleConfigurer implements EditorConfigurer
{
	private ArgConfig config;
	public RuleConfigurer(ArgConfig config)
	{
		this.config = config;
	}
	public void configure(EventEditor editor)
	{
		Collection<?> choices = config.getChoices();
		editor.setThenArgVisible(!choices.isEmpty());
		if (!choices.isEmpty()) {
			editor.setThenArgLabel(config.getLabel());
			editor.setThenArgChoices(choices, config.isEditable(), config.getRenderer(editor));
		}
	}
}