package org.korsakow.ide.ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.event.EventListenerList;

public class MyButtonGroup extends ButtonGroup implements ActionListener
{
	private final EventListenerList listeners = new EventListenerList();
    @Override
	public void add(AbstractButton b) {
    	super.add(b);
    	b.addActionListener(this);
    }
    @Override
	public void remove(AbstractButton b) {
    	b.removeActionListener(this);
    }

    public void addActionListener(ActionListener listener) {
    	listeners.add(ActionListener.class, listener);
    }
    public void actionPerformed(ActionEvent e) {
    	for (ActionListener listener : listeners.getListeners(ActionListener.class))
    		listener.actionPerformed(e);
    }
}
