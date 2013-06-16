package org.korsakow.ide.ui.constraints;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Hashtable;

import javax.swing.JTextField;

public abstract class TextFieldConstraint implements FocusListener, ActionListener
{
	protected Hashtable<Component, Object> valueOnFocus = new Hashtable<Component, Object>();
	public void focusGained(FocusEvent event) {
		JTextField textField = (JTextField)event.getSource();
		if (!textField.isEnabled()) // dont validate disabled: when other ppl listen for focus lost to do stuff, our validation could interfere. specifically infopanel adds a validator and an update notifier which results in a text field's text being updated after its been disabled and set to a special value
			return;
		String text = textField.getText();
		valueOnFocus.put(textField, text); // dont enforce constraints on old value;
	}
	public void focusLost(FocusEvent event) {
		JTextField textField = (JTextField)event.getSource();
		if (!textField.isEnabled())
			return;
		validate(textField);
	}
	public void actionPerformed(ActionEvent event) {
		JTextField textField = (JTextField)event.getSource();
		if (!textField.isEnabled())
			return;
		validate(textField);
	}
	protected abstract void validate(JTextField textField);

	/**
	 * The constraint may listen on any number of events, so we hide the implementation from the caller in this method.
	 * This is the intended way to use the class.
	 * @param textField
	 */
	public void addAsListenerTo(JTextField textField)
	{
		textField.addFocusListener(this);
		textField.addActionListener(this);
	}
}
