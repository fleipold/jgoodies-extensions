package ws.leipold.felix.jgoodies.listadapter;


import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.collect.ObservableList;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class ObservableListHolder<T> implements ObservableList<T> {

/**
 * An adapter for a {@link com.jgoodies.binding.value.ValueHolder} that holds
 * instances of {@link com.jgoodies.common.collect.ObservableList} and that is a
 * <code>ObservableList</code> itself.<p>
 *
 * <strong>Note:</strong> It'a a hack and there are some issues as can be seen by the debug code
 * in getListModel(). It seems to work anyway...
 *
 * @author Felix Leipold
 */



    /**
     * Holds the change handler that fires changes is the underlying
     * list model changes.
     */
    private final ListDataChangeHandler listDataChangeHandler;


    /**
     * Holds the list model value.
     */

    private ObservableList<T> currentListModel;

    // Instance Creation ******************************************************

    /**
     * Constructs a <code>ListModelHolderTest</code> on the given
     * <code>ListModel</code>.
     *
     * @param subject the initial list model
     */
    public ObservableListHolder(ValueModel subject) {
        subject.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                setListModel((ObservableList) evt.getNewValue());
            }
        });
        listDataChangeHandler = new ListDataChangeHandler();
        setListModel((ObservableList) subject.getValue());
    }

    // ValueModel Implementation **********************************************


    /**
     * Sets a new list model  and notifies any registered value listeners
     * if it has changed.
     *
     * @param newListModel the <code>ListModel</code> to be set
     */
    private void setListModel(ObservableList newListModel) {
        ListModel oldListModel = currentListModel;
        currentListModel = newListModel;


        if (oldListModel != null) {
            oldListModel.removeListDataListener(listDataChangeHandler);
        }

        if (newListModel != null) {
            newListModel.addListDataListener(listDataChangeHandler);
        }
        if (oldListModel != null) {
            if (oldListModel.getSize() > 0) {
                fireIntervalRemoved(0, oldListModel.getSize() - 1, this);
            }
        }
        if (newListModel != null) {
            if (newListModel.getSize() > 0) {
                fireIntervalAdded(0, newListModel.getSize() - 1, this);
            }
        }
    }






    // ListModel Field ********************************************************

    protected EventListenerList listenerList = new EventListenerList();

    // ListModel Implementation ***********************************************

    /**
     * Adds a listener to the list that's notified each time a change
     * to the data model occurs.
     *
     * @param l the <code>ListDataListener</code> to be added
     */
    public void addListDataListener(ListDataListener l) {
        listenerList.add(ListDataListener.class, l);
    }


    /**
     * Removes a listener from the list that's notified each time a
     * change to the data model occurs.
     *
     * @param l the <code>ListDataListener</code> to be removed
     */
    public void removeListDataListener(ListDataListener l) {
        listenerList.remove(ListDataListener.class, l);
    }


    /**
     * Returns the value at the specified index, or <code>null</code>
     * if the current value is <code>null</code>
     *
     * @param index the requested index
     * @return the value at <code>index</code> or <code>null</code>
     *         if the current value is <code>null</code>
     */
    public Object getElementAt(int index) {
        return getListModel() == null
                ? null
                : getListModel().getElementAt(index);
    }


    /**
     * Returns the length of the list or 0 if there's no list.
     *
     * @return the length of the list or 0 if there's no list
     */
    public int getSize() {
        return getListModel() == null
                ? 0
                : getListModel().getSize();
    }


    /**
     * Returns an array of all the list data listeners
     * registered on this <code>ArrayListModel</code>.
     *
     * @return all of this model's <code>ListDataListener</code>s,
     *         or an empty array if no list data listeners
     *         are currently registered
     * @see #addListDataListener(ListDataListener)
     * @see #removeListDataListener(ListDataListener)
     */
    public ListDataListener[] getListDataListeners() {
        return (ListDataListener[]) listenerList.getListeners(
                ListDataListener.class);
    }

    // ListModel Helper Code **************************************************

    /**
     * This method must be called <em>after</em> one or more elements
     * of the list change.  The changed elements
     * are specified by the closed interval index0, index1 -- the end points
     * are included.  Note that index0 need not be less than or equal to index1.
     *
     * @param index0 one end of the new interval
     * @param index1 the other end of the new interval
     * @param source
     * @see EventListenerList
     */
    private void fireContentsChanged(int index0, int index1, Object source) {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(source,
                            ListDataEvent.CONTENTS_CHANGED, index0, index1);
                }
                ((ListDataListener) listeners[i + 1]).contentsChanged(e);
            }
        }
    }


    /**
     * This method must be called <em>after</em> one or more elements
     * are added to the model.  The new elements
     * are specified by a closed interval index0, index1 -- the end points
     * are included.  Note that index0 need not be less than or equal to index1.
     *
     * @param index0 one end of the new interval
     * @param index1 the other end of the new interval
     * @param source
     * @see EventListenerList
     */
    private void fireIntervalAdded(int index0, int index1, Object source) {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(source, ListDataEvent.INTERVAL_ADDED, index0, index1);
                }
                ((ListDataListener) listeners[i + 1]).intervalAdded(e);
            }
        }
    }


    /**
     * This method must be called <em>after</em>  one or more elements
     * are removed from the model.
     * <code>index0</code> and <code>index1</code> are the end points
     * of the interval that's been removed.  Note that <code>index0</code>
     * need not be less than or equal to <code>index1</code>.
     *
     * @param index0 one end of the removed interval,
     *               including <code>index0</code>
     * @param index1 the other end of the removed interval,
     *               including <code>index1</code>
     * @param source
     * @see EventListenerList
     */
    private void fireIntervalRemoved(int index0, int index1, Object source) {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(source, ListDataEvent.INTERVAL_REMOVED, index0, index1);
                }
                ((ListDataListener) listeners[i + 1]).intervalRemoved(e);
            }
        }
    }

    // Methods from java.util.List ********************************************


    public int size() {
        if (getListModel() == null) {
            return 0;
        }
        return getListModel().size();
    }

    public void clear() {
        if (getListModel() == null) {
            return;
        }
        getListModel().clear();
    }

    public boolean isEmpty() {
        return getListModel().isEmpty();
    }

    public Object[] toArray() {
        return getListModel().toArray();
    }

    public T get(int index) {
        return getListModel().get(index);
    }

    public T remove(int index) {
        return getListModel().remove(index);
    }

    public void add(int index, T element) {
        getListModel().add(index, element);
    }

    public int indexOf(Object o) {
        return getListModel().indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return getListModel().lastIndexOf(o);
    }

    public boolean add(T o) {
        return getListModel().add(o);
    }

    public boolean contains(Object o) {
        if (getListModel() == null) {
            return false;
        }
        return getListModel().contains(o);
    }


    public boolean remove(Object o) {
        return getListModel().remove(o);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return getListModel().addAll(index, c);
    }

    public boolean addAll(Collection c) {
        return getListModel().addAll(c);
    }

    public boolean containsAll(Collection c) {
        return getListModel().containsAll(c);
    }

    public boolean removeAll(Collection c) {
        return getListModel().removeAll(c);
    }

    public boolean retainAll(Collection c) {
        return getListModel().retainAll(c);
    }

    public Iterator<T> iterator() {
        return getListModel().iterator();
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return getListModel().subList(fromIndex, toIndex);
    }

    public ListIterator<T> listIterator() {
        return getListModel().listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return getListModel().listIterator(index);
    }

    public T set(int index, T element) {
        return getListModel().set(index, element);
    }

    public Object[] toArray(Object[] a) {
        return getListModel().toArray(a);
    }

    private ObservableList<T> getListModel() {
        return currentListModel;
    }

    // Helper Class ***********************************************************


    /*
     * Listens to subject changes and fires a contents change event.
     */
    private class ListDataChangeHandler implements ListDataListener {

        /**
         * Sent after the indices in the index0, index1
         * interval have been inserted in the data model.
         * The new interval includes both index0 and index1.
         *
         * @param evt a <code>ListDataEvent</code> encapsulating the
         *            event information
         */
        public void intervalAdded(ListDataEvent evt) {
            fireIntervalAdded(evt.getIndex0(), evt.getIndex1(), evt.getSource());
        }


        /**
         * Sent after the indices in the index0, index1 interval
         * have been removed from the data model.  The interval
         * includes both index0 and index1.
         *
         * @param evt a <code>ListDataEvent</code> encapsulating the
         *            event information
         */
        public void intervalRemoved(ListDataEvent evt) {
            fireIntervalRemoved(evt.getIndex0(), evt.getIndex1(), evt.getSource());
        }


        /**
         * Sent when the contents of the list has changed in a way
         * that's too complex to characterize with the previous
         * methods. For example, this is sent when an item has been
         * replaced. Index0 and index1 bracket the change.
         *
         * @param evt a <code>ListDataEvent</code> encapsulating the
         *            event information
         */
        public void contentsChanged(ListDataEvent evt) {
            fireContentsChanged(evt.getIndex0(), evt.getIndex1(), evt.getSource());
        }

    }


}
