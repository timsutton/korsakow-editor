package org.korsakow.ide.ui.components.event;

import javax.swing.event.ChangeEvent;

public class VetoableChangeEvent extends ChangeEvent {
	private boolean isVetoed = false;
	public VetoableChangeEvent(Object source) {
		super(source);
	}
	public void setVetoed(boolean vetoed)
	{
		isVetoed = vetoed;
	}
	public boolean isVetoted()
	{
		return isVetoed;
	}
}
