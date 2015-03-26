package org.programmiersportgruppe.jgoodies.listadapter;


import com.jgoodies.common.collect.ObservableList;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.EventListener;

/**
 * User: Felix Leipold
 * Date: 20.10.2005
 *
 * Adapts a ListModel holding the and an ObservableList to Swings ListSelectionModel.
 */

public class MultiListSelectionAdapter implements ListSelectionModel {
    private ListModel list;
    ObservableList selectionListModel;
    DefaultListSelectionModel dlsm;
    public SwingSelectionListener swingSelectionListener;
    public BindingListener bindingListener;

    /**@param list from which the user may choose
     * @param selection the model for the selection*/
    public MultiListSelectionAdapter(ListModel list, ObservableList selection) {
        this.list = list;
        this.selectionListModel = selection;
        swingSelectionListener = new SwingSelectionListener();
        dlsm=new DefaultListSelectionModel();
        dlsm.setSelectionMode(MULTIPLE_INTERVAL_SELECTION);
        dlsm.addListSelectionListener(swingSelectionListener);
        bindingListener = new BindingListener();
        selectionListModel.addListDataListener(bindingListener);
    }

    /** @param multiSelection the model for the selection*/
    public MultiListSelectionAdapter(MultiSelectionInList multiSelection){
        this(multiSelection.getList(),multiSelection.getSelection());
    }

    // Delegating to DefaultListSelectionModel instance

    public int getAnchorSelectionIndex() {
        return dlsm.getAnchorSelectionIndex();
    }

    public int getLeadSelectionIndex() {
        return dlsm.getLeadSelectionIndex();
    }

    public int getMaxSelectionIndex() {
        return dlsm.getMaxSelectionIndex();
    }

    public int getMinSelectionIndex() {
        return dlsm.getMinSelectionIndex();
    }

    public int getSelectionMode() {
        return dlsm.getSelectionMode();
    }

    public void clearSelection() {
        dlsm.clearSelection();
    }

    public boolean getValueIsAdjusting() {
        return dlsm.getValueIsAdjusting();
    }

    public boolean isLeadAnchorNotificationEnabled() {
        return dlsm.isLeadAnchorNotificationEnabled();
    }

    public boolean isSelectionEmpty() {
        return dlsm.isSelectionEmpty();
    }

    public void setAnchorSelectionIndex(int anchorIndex) {
        dlsm.setAnchorSelectionIndex(anchorIndex);
    }

    public void setLeadSelectionIndex(int leadIndex) {
        dlsm.setLeadSelectionIndex(leadIndex);
    }

    public void setSelectionMode(int selectionMode) {
        dlsm.setSelectionMode(selectionMode);
    }

    public boolean isSelectedIndex(int index) {
        return dlsm.isSelectedIndex(index);
    }

    public void addSelectionInterval(int index0, int index1) {
        dlsm.addSelectionInterval(index0, index1);
    }

    public void removeIndexInterval(int index0, int index1) {
        dlsm.removeIndexInterval(index0, index1);
    }

    public void removeSelectionInterval(int index0, int index1) {
        dlsm.removeSelectionInterval(index0, index1);
    }

    public void setSelectionInterval(int index0, int index1) {
        dlsm.setSelectionInterval(index0, index1);
    }

    public void insertIndexInterval(int index, int length, boolean before) {
        dlsm.insertIndexInterval(index, length, before);
    }

    public void setLeadAnchorNotificationEnabled(boolean flag) {
        dlsm.setLeadAnchorNotificationEnabled(flag);
    }

    public void setValueIsAdjusting(boolean isAdjusting) {
        dlsm.setValueIsAdjusting(isAdjusting);
    }

    public ListSelectionListener[] getListSelectionListeners() {
        return dlsm.getListSelectionListeners();
    }

    public void addListSelectionListener(ListSelectionListener l) {
        dlsm.addListSelectionListener(l);
    }

    public void removeListSelectionListener(ListSelectionListener l) {
        dlsm.removeListSelectionListener(l);
    }

    public EventListener[] getListeners(Class listenerType) {
        return dlsm.getListeners(listenerType);
    }

    private class SwingSelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            selectionListModel.removeListDataListener(bindingListener);
            selectionListModel.clear();
            for (int i=0;i< MultiListSelectionAdapter.this.list.getSize();i++){
                if (dlsm.isSelectedIndex(i)) selectionListModel.add(MultiListSelectionAdapter.this.list.getElementAt(i));

            }

            selectionListModel.addListDataListener(bindingListener);
        }
    }

    private class BindingListener implements ListDataListener {
        public void contentsChanged(ListDataEvent e) {
        }

        public void intervalAdded(ListDataEvent e) {
            dlsm.removeListSelectionListener(swingSelectionListener);
            dlsm.addSelectionInterval(e.getIndex0(),e.getIndex1());
            dlsm.addListSelectionListener(swingSelectionListener);

        }


        public void intervalRemoved(ListDataEvent e) {
            dlsm.removeListSelectionListener(swingSelectionListener);
            dlsm.removeSelectionInterval(e.getIndex0(),e.getIndex1());
            dlsm.addListSelectionListener(swingSelectionListener);
        }
    }
}
