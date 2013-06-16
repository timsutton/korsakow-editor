package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import org.dsrg.soenea.uow.UoW;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.w3c.dom.Document;

public abstract class AbstractAction implements ActionListener, UndoableEdit {
	private boolean alive = true;
	private Document state;
	private long version;
	
	public void actionPerformed(ActionEvent e) {
		try {
			if (isUndoable()) {
				saveState();
				Application.getInstance().getUndoManager().addEdit(this);
			} else
				Application.getInstance().getUndoManager().discardAllEdits();
			
			Application.getInstance().beginBusyOperation();
			
			UoW.newCurrent(); // otherwise we might be working with stale data
			
			performAction();
		} finally  {
			Application.getInstance().endBusyOperation();
		}
	}
	
	public abstract void performAction();

	private void restoreState()
	{
		DataRegistry.setDocument(state);
//		DataRegistry.setDocument(state);
	}
	private void saveState()
	{
		state = (Document) DataRegistry.getDomSession().getDocument().cloneNode(true);
		version = DataRegistry.getDomSession().getVersion();
	}
	
	public boolean isUndoable()
	{
		return false;
	}
	
	public boolean isAlive()
	{
		return alive;
	}

	@Override // UndoableEdit
	public boolean addEdit(UndoableEdit anEdit)
	{
		return false;
	}

	@Override // UndoableEdit
	public boolean canRedo()
	{
		return isUndoable() && isAlive() && state != null && version==DataRegistry.getDomSession().getVersion()+1;
	}

	@Override // UndoableEdit
	public boolean canUndo()
	{
		return isUndoable() && isAlive() && state != null && version==DataRegistry.getDomSession().getVersion()-1;
	}

	@Override // UndoableEdit
	public void die()
	{
		alive = false;
	}

	@Override // UndoableEdit
	public String getPresentationName()
	{
		return "";
	}

	@Override // UndoableEdit
	public String getRedoPresentationName()
	{
		return "";
	}

	@Override // UndoableEdit
	public String getUndoPresentationName()
	{
		return "";
	}

	@Override // UndoableEdit
	public boolean isSignificant()
	{
		return true;
	}

	@Override // UndoableEdit
	public void redo() throws CannotRedoException
	{
		if (!canRedo())
			throw new CannotRedoException();
		restoreState();
	}

	@Override // UndoableEdit
	public boolean replaceEdit(UndoableEdit anEdit)
	{
		return false;
	}

	@Override // UndoableEdit
	public void undo() throws CannotUndoException
	{
		if (!canUndo())
			throw new CannotUndoException();
		restoreState();
	}
}
