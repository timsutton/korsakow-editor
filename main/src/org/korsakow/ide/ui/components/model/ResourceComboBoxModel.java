package org.korsakow.ide.ui.components.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

public class ResourceComboBoxModel extends AbstractListModel implements MutableComboBoxModel
{
	public static final Object NULL_SELECTION_ITEM = "NULL_SELECTION_ITEM";
	protected List<Object> data = new ArrayList<Object>();
	protected Object selectedItem = null;
	
	protected boolean allowNullSelection = false;
	
	public ResourceComboBoxModel(boolean allowNullSelection)
	{
		this(new ArrayList<Object>(), false);
	}
	public ResourceComboBoxModel(Collection<?> items)
	{
		this(items, false);
	}
	public ResourceComboBoxModel(Collection<?> items, boolean allowNullSelection)
	{

		setAllowNullSelection(allowNullSelection);
		if (allowNullSelection) {
			data.add(NULL_SELECTION_ITEM);
		}
		data.addAll(items);
		
		if (allowNullSelection) {
			selectedItem = NULL_SELECTION_ITEM;
		}
	}
	public void setAllowNullSelection(boolean allow)
	{
		allowNullSelection = allow;
	}
	public boolean isAllowNullSelection()
	{
		return allowNullSelection;
	}

    // implements javax.swing.ComboBoxModel
    /**
     * Set the value of the selected item. The selected item may be null.
     * <p>
     * @param anObject The combo box value or null for no selection.
     */
    public void setSelectedItem(Object anObject) {
        if ((selectedItem != null && !selectedItem.equals( anObject )) ||
        		selectedItem == null && anObject != null)
        {
        	// for convenience we perform this conversion
        	if (anObject == null && allowNullSelection)
        		anObject = NULL_SELECTION_ITEM;
		    selectedItem = anObject;
		    fireContentsChanged(this, -1, -1);
        }
    }

    // implements javax.swing.ComboBoxModel
    public Object getSelectedItem() {
    	// as a convenience we do this conversion
    	if (allowNullSelection && selectedItem == NULL_SELECTION_ITEM)
    		return null;
        return selectedItem;
    }

    // implements javax.swing.ListModel
    public int getSize() {
        return data.size();
    }

    // implements javax.swing.ListModel
    public Object getElementAt(int index) {
        if ( index >= 0 && index < data.size() )
            return data.get(index);
        else
            return null;
    }

    /**
     * Returns the index-position of the specified object in the list.
     *
     * @param anObject  
     * @return an int representing the index position, where 0 is 
     *         the first position
     */
    public int getIndexOf(Object anObject) {
        return data.indexOf(anObject);
    }

    // implements javax.swing.MutableComboBoxModel
    public void addElement(Object anObject) {
        data.add(anObject);
        fireIntervalAdded(this,data.size()-1, data.size()-1);
        if ( data.size() == 1 && selectedItem == null && anObject != null )
        {
            setSelectedItem( anObject );
        }
        data.remove(NULL_SELECTION_ITEM);
		if (allowNullSelection) {
			data.add(NULL_SELECTION_ITEM);
		}
    }

    // implements javax.swing.MutableComboBoxModel
    public void insertElementAt(Object anObject,int index) {
        data.add(index, anObject);
        fireIntervalAdded(this, index, index);
        data.remove(NULL_SELECTION_ITEM);
		if (allowNullSelection) {
			data.add(NULL_SELECTION_ITEM);
		}
    }

    // implements javax.swing.MutableComboBoxModel
    public void removeElementAt(int index) {
    	if (getElementAt(index) == NULL_SELECTION_ITEM)
    		throw new IllegalArgumentException();
        if ( getElementAt( index ) == selectedItem ) {
            if ( index == 0 ) {
                setSelectedItem( getSize() == 1 ? null : getElementAt( index + 1 ) );
            }
            else {
                setSelectedItem( getElementAt( index - 1 ) );
            }
        }

        data.remove(index);

        fireIntervalRemoved(this, index, index);
    }

    // implements javax.swing.MutableComboBoxModel
    public void removeElement(Object anObject) {
    	if (anObject == NULL_SELECTION_ITEM)
    		throw new IllegalArgumentException();
        int index = data.indexOf(anObject);
        if ( index != -1 ) {
            removeElementAt(index);
        }
    }

    /**
     * Empties the list.
     */
    public void removeAllElements() {
        if ( data.size() > 0 ) {
            int firstIndex = 0;
            int lastIndex = data.size() - 1;
            data.clear();
            selectedItem = null;
            fireIntervalRemoved(this, firstIndex, lastIndex);
        } else {
        	selectedItem = null;
        }
		if (allowNullSelection) {
			data.add(NULL_SELECTION_ITEM);
		}
    }
}
