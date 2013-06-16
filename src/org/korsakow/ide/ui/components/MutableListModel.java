package org.korsakow.ide.ui.components;

import javax.swing.ListModel;

public interface MutableListModel extends ListModel {
	  /**
	   * Sets the value at the specified index.  
	   * @param index the requested index
	   */
	  void setElementAt(Object value, int index);

}
