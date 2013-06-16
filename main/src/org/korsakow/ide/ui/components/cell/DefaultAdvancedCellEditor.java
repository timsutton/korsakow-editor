/**
 * 
 */
package org.korsakow.ide.ui.components.cell;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;

import org.korsakow.ide.ui.components.event.VetoableChangeEvent;

public class DefaultAdvancedCellEditor extends DefaultCellEditor
{
	public DefaultAdvancedCellEditor()
	{
		super(new JTextField());
	}
	protected boolean tryStopCellEditing()
	{
		return fireEditingWillStop();
	}
	protected boolean doStopCellEditing()
	{
		return super.stopCellEditing();
	}
    @Override
	public boolean stopCellEditing()
    {
    	if (!fireEditingWillStop())
    		return false;
    	return doStopCellEditing();
    }
    public void setEditorValue(Object value)
    {
    	delegate.setValue(value);
    }
    protected boolean fireEditingWillStop()
    {
    	// Guaranteed to return a non-null array
    	Object[] listeners = listenerList.getListenerList();
    	// Process the listeners last to first, notifying
    	// those that are interested in this event
    	VetoableChangeEvent changeEvent = null;
    	for (int i = listeners.length-2; i>=0; i-=2) {
    	    if ((listeners[i]==CellEditorListener.class &&
    	    	listeners[i+1] instanceof AdvancedCellEditorListener)) {
    		// Lazily create the event:
    		if (changeEvent == null)
    		    changeEvent = new VetoableChangeEvent(this);
    		((AdvancedCellEditorListener)listeners[i+1]).editingWillStop(changeEvent);
    	    }	       
    	}
    	return changeEvent==null || !changeEvent.isVetoted();
    }
}