package ws.leipold.felix.jgoodies.listadapter;

import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import java.beans.PropertyChangeListener;

/**
 * Adapts a ListModel to a ValueModel
 * If the list has exactly one item the valueModel is set to this item. Otherwise the
 * ValueModel is set to null.
 *
 * The intented use is with multiselections, that should for operations that
 * work only on single objects be adapted
 * to single selections.  
 *
 */
public class MultiSelectionSingleSelection implements ValueModel {
    ListModel multiSelection;
    ValueHolder singleSelection;

    public MultiSelectionSingleSelection(ListModel multiSelection) {
        this.multiSelection = multiSelection;
        this.singleSelection = new ValueHolder();

        final ListDataListener listener = new ListDataListener();
        multiSelection.addListDataListener(listener);
        listener.intervalAdded(new ListDataEvent(this,0,0,0));
    }


    public Object getValue() {
        return singleSelection.getValue();
    }

    public void setValue(Object newValue) {
        singleSelection.setValue(newValue);
    }

    public void addValueChangeListener(PropertyChangeListener l) {
        singleSelection.addValueChangeListener(l);
    }

    public void removeValueChangeListener(PropertyChangeListener l) {
        singleSelection.removeValueChangeListener(l);
    }


    private class ListDataListener implements javax.swing.event.ListDataListener {
        public void contentsChanged(ListDataEvent e) {
            //todo think harder ;-)
        }

        public void intervalAdded(ListDataEvent e) {
            if (multiSelection.getSize()==1){
                singleSelection.setValue(multiSelection.getElementAt(0));
            }
            else singleSelection.setValue(null);
        }

        public void intervalRemoved(ListDataEvent e) {
            if (multiSelection.getSize()==1){
                singleSelection.setValue(multiSelection.getElementAt(0));
            }
            else singleSelection.setValue(null);
        }
    }
}
