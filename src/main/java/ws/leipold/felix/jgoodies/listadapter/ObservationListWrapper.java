package ws.leipold.felix.jgoodies.listadapter;

import com.jgoodies.common.collect.ObservableList;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.*;

/**
 * Implements the Observable list and delegates the calls to
 * collection that is determined by a callback.
 * This is necessary for persistency frameworks that change collection
 * implementations e.g. hibernate
 */
public class ObservationListWrapper<T> implements ObservableList<T> {
    final private List<T> wrappedList;
    private List subjectListListeners=new ArrayList(0);

    public ObservationListWrapper(List<T> wrappedList) {
        this.wrappedList = wrappedList;

    }



    private List<T> getList() {
        if (wrappedList!= null)
            return wrappedList;
        return Collections.emptyList();
    }

    public int hashCode() {
        return getList().hashCode();
    }

    public int size() {
        return getList().size();
    }

    public void clear() {
        if (isEmpty())
            return;
        int oldLastIndex = size() - 1;
        getList().clear();
        fireIntervalRemoved(0, oldLastIndex);
        fireSubjectListChanged();
    }

    public boolean isEmpty() {
        return getList().isEmpty();
    }

    public Object[] toArray() {
        return getList().toArray();
    }

    public T get(int index) {
        return getList().get(index);
    }

    public T remove(int index) {
        final T o = getList().remove(index);
        fireIntervalRemoved(index, index);
        fireSubjectListChanged();
        return o;

    }

    public void add(int index, T element) {
        getList().add(index, element);
        fireIntervalAdded(index, index);
        fireSubjectListChanged();
    }

    public int indexOf(Object o) {
        return getList().indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return getList().lastIndexOf(o);
    }

    public boolean add(T o) {
        int newIndex = getList().size();
        final boolean b = getList().add(o);
        fireIntervalAdded(newIndex, newIndex);
        fireSubjectListChanged();
        return b;
    }

    public boolean contains(Object o) {
        return getList().contains(o);
    }

    public boolean equals(Object o) {
        return getList().equals(o);
    }

    public boolean remove(Object o) {
        int index = getList().indexOf(o);
        final boolean b = getList().remove(o);
        fireIntervalRemoved(index, index);
        fireSubjectListChanged();
        return b;
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        final boolean b = getList().addAll(index, c);
        fireIntervalAdded(index, index + c.size() - 1);
        fireSubjectListChanged();
        return b;
    }


    public boolean addAll(Collection<? extends T> c) {
        final int firstIndex = getList().size();
        final boolean b = getList().addAll(c);
        fireIntervalAdded(firstIndex, firstIndex + c.size() - 1);
        fireSubjectListChanged();
        return b;
    }

    public boolean containsAll(Collection c) {
        return getList().containsAll(c);
    }

    public boolean removeAll(Collection c) {
        boolean changed = false;
        for (Iterator iterator = c.iterator(); iterator.hasNext();) {
            Object o = (Object) iterator.next();
            if (this.remove(o)) changed = true;
        }
        fireSubjectListChanged();
        return changed;
    }

    public boolean retainAll(Collection c) {
        boolean modified = false;
        Iterator e = iterator();
        while (e.hasNext()) {
            if (!c.contains(e.next())) {
                e.remove();
                modified = true;
            }
        }
        //todo: bug eventing!
        fireSubjectListChanged();
        return modified;
    }

    public Iterator iterator() {
        return getList().iterator();
    }

    public List subList(int fromIndex, int toIndex) {
        return getList().subList(fromIndex, toIndex);
    }

    public ListIterator listIterator() {
        return getList().listIterator();
    }

    public ListIterator listIterator(int index) {
        return getList().listIterator(index);
    }

    public T set(int index, T element) {
        final T o = getList().set(index, element);
        fireContentsChanged(index, index);
        fireSubjectListChanged();
        return o;
    }

    public Object[] toArray(Object a[]) {
        return getList().toArray(a);
    }


    public void addSubjectListListener(SubjectListListener listener){
        subjectListListeners.add(listener);
    }

    public void removeSubjectListListener(SubjectListListener listener){
        subjectListListeners.remove(listener);
    }

    private void fireSubjectListChanged(){
        for (Iterator iterator = subjectListListeners.iterator(); iterator.hasNext();) {
            SubjectListListener subjectListChangeListener = (SubjectListListener) iterator.next();
            subjectListChangeListener.listChanged();
        }
    }
    // Contained List Change Notification


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
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at <code>index</code>
     */
    public Object getElementAt(int index) {
        return get(index);
    }


    /**
     * Returns the length of the list or 0 if there's no list.
     *
     * @return the length of the list or 0 if there's no list
     */
    public int getSize() {
        return size();
    }





// Explicit Change Notification *******************************************

    /**
     * Notifies all registered <code>ListDataListeners</code> that the element
     * at the specified index has changed. Useful if there's a content change
     * without any structural change.<p>
     * <p/>
     * This method must be called <em>after</em> the element of the list changes.
     *
     * @param index the index of the element that has changed
     * @see javax.swing.event.EventListenerList
     */
    public void fireContentsChanged(int index) {
        fireContentsChanged(index, index);
    }


    // ListModel Helper Code **************************************************

    /**
     * Returns an array of all the list data listeners
     * registered on this <code>ArrayListModel</code>.
     *
     * @return all of this model's <code>ListDataListener</code>s,
     *         or an empty array if no list data listeners
     *         are currently registered
     * @see #addListDataListener(javax.swing.event.ListDataListener)
     * @see #removeListDataListener(javax.swing.event.ListDataListener)
     */
    public ListDataListener[] getListDataListeners() {
        return (ListDataListener[]) listenerList.getListeners(ListDataListener.class);
    }


    /**
     * This method must be called <em>after</em> one or more elements
     * of the list change.  The changed elements
     * are specified by the closed interval index0, index1 -- the end points
     * are included.  Note that index0 need not be less than or equal to index1.
     *
     * @param index0 one end of the new interval
     * @param index1 the other end of the new interval
     * @see javax.swing.event.EventListenerList
     */
    private void fireContentsChanged(int index0, int index1) {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(this,
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
     * @see javax.swing.event.EventListenerList
     */
    private void fireIntervalAdded(int index0, int index1) {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index0, index1);
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
     * @see javax.swing.event.EventListenerList
     */
    private void fireIntervalRemoved(int index0, int index1) {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index0, index1);
                }
                ((ListDataListener) listeners[i + 1]).intervalRemoved(e);
            }
        }
    }


}