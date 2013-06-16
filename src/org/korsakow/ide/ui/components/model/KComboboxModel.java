package org.korsakow.ide.ui.components.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

public class KComboboxModel extends AbstractListModel implements MutableComboBoxModel, List<Object> {
    protected List<Object> objects;
    protected Object selectedObject;

    /**
     * Constructs an empty DefaultComboBoxModel object.
     */
    public KComboboxModel() {
        objects = new ArrayList<Object>();
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * an array of objects.
     *
     * @param items  an array of Object objects
     */
    public KComboboxModel(final Object items[], Object selected) {
        objects = new ArrayList<Object>(items.length);

        int i,c;
        for ( i=0,c=items.length;i<c;i++ )
            objects.add(items[i]);

        selectedObject = selected;
    }
    public KComboboxModel(final Object items[]) {
    	this(items, items.length>0?items[0]:null);
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * a vector.
     *
     * @param v  a Collection object ...
     */
    public KComboboxModel(Collection<?> v, Object selected) {
        objects = new ArrayList<Object>(v);

        selectedObject = selected;
    }
    
    public KComboboxModel(Collection<?> v) {
    	this(v, v.isEmpty()?null:v.iterator().next());
    }

    // implements javax.swing.ComboBoxModel
    /**
     * Set the value of the selected item. The selected item may be null.
     * <p>
     * @param anObject The combo box value or null for no selection.
     */
    public void setSelectedItem(Object anObject) {
        if ((selectedObject != null && !selectedObject.equals( anObject )) ||
	    selectedObject == null && anObject != null) {
	    selectedObject = anObject;
	    // notice how we dont notify
        }
    }
    
    // implements javax.swing.ComboBoxModel
    public Object getSelectedItem() {
        return selectedObject;
    }

    // implements javax.swing.ListModel
    public int getSize() {
        return objects.size();
    }

    // implements javax.swing.ListModel
    public Object getElementAt(int index) {
        if ( index >= 0 && index < objects.size() )
            return objects.get(index);
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
        return objects.indexOf(anObject);
    }

    // implements javax.swing.MutableComboBoxModel
    public void addElement(Object anObject) {
        objects.add(anObject);
        fireIntervalAdded(this,objects.size()-1, objects.size()-1);
        if ( objects.size() == 1 && selectedObject == null && anObject != null ) {
            setSelectedItem( anObject );
        }
    }

    // implements javax.swing.MutableComboBoxModel
    public void insertElementAt(Object anObject,int index) {
        objects.add(index, anObject);
        fireIntervalAdded(this, index, index);
    }

    // implements javax.swing.MutableComboBoxModel
    public void removeElementAt(int index) {
        if ( getElementAt( index ) == selectedObject ) {
            if ( index == 0 ) {
                setSelectedItem( getSize() == 1 ? null : getElementAt( index + 1 ) );
            }
            else {
                setSelectedItem( getElementAt( index - 1 ) );
            }
        }

        objects.remove(index);

        fireIntervalRemoved(this, index, index);
    }

    // implements javax.swing.MutableComboBoxModel
    public void removeElement(Object anObject) {
        int index = objects.indexOf(anObject);
        if ( index != -1 ) {
            removeElementAt(index);
        }
    }

    /**
     * Empties the list.
     */
    public void removeAllElements() {
        if ( objects.size() > 0 ) {
            int firstIndex = 0;
            int lastIndex = objects.size() - 1;
            objects.clear();
	    selectedObject = null;
            fireIntervalRemoved(this, firstIndex, lastIndex);
        } else {
	    selectedObject = null;
	}
    }

	public boolean add(Object o) {
		addElement(o);
		return true;
	}

	public void add(int index, Object element) {
		insertElementAt(element, index);
	}

	public boolean addAll(Collection<? extends Object> c) {
		int index0 = objects.size();
		boolean ret = objects.addAll(c);
		int index1 = objects.size()-1;
		fireIntervalAdded(this, index0, index1);
		return ret;
	}

	public boolean addAll(int index, Collection<? extends Object> c) {
		int index0 = index;
		boolean ret = objects.addAll(c);
		int index1 = index+c.size()-1;
		fireIntervalAdded(this, index0, index1);
		return ret;
	}

	public void clear() {
		removeAllElements();
	}

	public boolean contains(Object o) {
		return objects.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return objects.containsAll(c);
	}

	public Object get(int index) {
		return getElementAt(index);
	}

	public int indexOf(Object o) {
		return objects.indexOf(o);
	}

	public boolean isEmpty() {
		return objects.isEmpty();
	}

	public Iterator<Object> iterator() {
		return objects.iterator();
	}

	public int lastIndexOf(Object o) {
		return objects.lastIndexOf(o);
	}

	public ListIterator<Object> listIterator() {
		return objects.listIterator();
	}

	public ListIterator<Object> listIterator(int index) {
		return objects.listIterator(index);
	}

	public boolean remove(Object o) {
		boolean ret = objects.contains(o);
		removeElement(o);
		return ret;
	}

	public Object remove(int index) {
		Object element = objects.get(index);
		removeElementAt(index);
		return element;
	}

	public boolean removeAll(Collection<?> c) {
		int index0 = 0;
		int index1 = size();
		boolean ret = objects.removeAll(c);
		fireContentsChanged(this, index0, index1);
		return ret;
	}

	public boolean retainAll(Collection<?> c) {
		int index0 = 0;
		int index1 = size();
		boolean ret = objects.retainAll(c);
		fireContentsChanged(this, index0, index1);
		return ret;
	}

	public Object set(int index, Object element) {
		Object ret = objects.set(index, element);
		fireContentsChanged(this, index, index);
		return ret;
	}

	public int size() {
		return objects.size();
	}

	public List<Object> subList(int fromIndex, int toIndex) {
		return objects.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return objects.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return objects.toArray(a);
	}
}
