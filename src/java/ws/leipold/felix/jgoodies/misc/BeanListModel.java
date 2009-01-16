package ws.leipold.felix.jgoodies.misc;

import com.jgoodies.binding.list.ObservableList;
import com.jgoodies.binding.beans.BeanUtils;

import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import java.util.*;
import static java.util.Arrays.asList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import ws.leipold.felix.jgoodies.listadapter.ObservationListWrapper;

/** A list model that listens to its elements and fires property chaged events in case they fire a property change */

public class BeanListModel<T> implements ObservableList<T> {


    final ObservableList<T> wrappee;
    private BeanListModel<T>.ListElementBeanPropertyChangeListener listener;
    private BeanListModel<T>.WrappedListListener listListener;
    final Set<T> trackedInstances;
    private List<ListDataListener> clientListeners = new ArrayList<ListDataListener>();

    public BeanListModel(T... elements){
        this(asList(elements));
    }

    public BeanListModel(List<T> list) {
        this(new ObservationListWrapper<T>(list));
    }

    public BeanListModel(){
        this(new ArrayList<T>());
    }


    public BeanListModel(ObservableList<T> wrappee) {
        this.wrappee = wrappee;

        trackedInstances = new HashSet<T>();
        listListener = new WrappedListListener();
        listener = new ListElementBeanPropertyChangeListener();

        for (T t : wrappee) {
            BeanUtils.addPropertyChangeListener(t, listener);
            trackedInstances.add(t);
        }

        wrappee.addListDataListener(listListener);
    }

    private void fireBeanChangedEvent(Object source) {
        int index = wrappee.indexOf(source);
        for (ListDataListener ldl : clientListeners){
            ldl.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index, index+1 ));
        }
    }


    public int size() {
        return wrappee.size();
    }

    public boolean isEmpty() {
        return wrappee.isEmpty();
    }

    public boolean contains(Object o) {
        return wrappee.contains(o);
    }

    public Iterator<T> iterator() {
        return wrappee.iterator();
    }

    public Object[] toArray() {
        return wrappee.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return wrappee.toArray(a);
    }

    public boolean add(T o) {
        return wrappee.add(o);
    }

    public boolean remove(Object o) {
        return wrappee.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return wrappee.containsAll(c);
    }

    public boolean addAll(Collection<? extends T> c) {
        return wrappee.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return wrappee.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
        return wrappee.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return wrappee.retainAll(c);
    }

    public void clear() {
        wrappee.clear();
    }

    public boolean equals(Object o) {
        return wrappee.equals(o);
    }

    public int hashCode() {
        return wrappee.hashCode();
    }

    public T get(int index) {
        return wrappee.get(index);
    }

    public T set(int index, T element) {
        return wrappee.set(index, element);
    }

    public void add(int index, T element) {
        wrappee.add(index, element);
    }

    public T remove(int index) {
        return wrappee.remove(index);
    }

    public int indexOf(Object o) {
        return wrappee.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return wrappee.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return wrappee.listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return wrappee.listIterator(index);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return wrappee.subList(fromIndex, toIndex);
    }

    public int getSize() {
        return wrappee.getSize();
    }

    public Object getElementAt(int index) {
        return wrappee.getElementAt(index);
    }

    public void addListDataListener(ListDataListener l) {
        clientListeners.add(l);
        wrappee.addListDataListener(l);
    }

    public void removeListDataListener(ListDataListener l) {
        clientListeners.remove(l);
        wrappee.removeListDataListener(l);
    }

    private class ListElementBeanPropertyChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
          fireBeanChangedEvent(evt.getSource());
        }
    }

    private class WrappedListListener implements ListDataListener {

        public void intervalAdded(ListDataEvent e) {
            for (int index = e.getIndex0(); index < e.getIndex1() + 1; index++){
                BeanUtils.addPropertyChangeListener(wrappee.get(index), listener);
                trackedInstances.add(wrappee.get(index));

            }

        }

        public void intervalRemoved(ListDataEvent e) {
            Set<T> toBeReleased = new HashSet<T>(trackedInstances);
            for (T t : wrappee) {
                toBeReleased.remove(t);
            }

            for (T t : toBeReleased) {
                BeanUtils.removePropertyChangeListener(t, listener);
                trackedInstances.remove(t);
            }

        }

        public void contentsChanged(ListDataEvent e) {
        }
    }
}
