package org.programmiersportgruppe.jgoodies.listadapter;

import com.jgoodies.common.collect.ObservableList;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;

/**
 * @author Felix Leipold
 * Connects two ObservableLists in the same way that a PropertyConnector connects two properties.
 *
 * It registers ListDataListeners with both ObservableLists and propagates the changes to the respective other list.
 *
 * If the lists are not equal the connector is in async state. The two lists will remain different until either one of
 * them changes or the updateListX() is called explicity.
 * Then the changed list is copied to the other one. Afterwards only changes are propagated from one list to another.
 *

 */
public class ObservableListConnector {
    private ObservableList list1;
    private ObservableList list2;

    private SyncingListDataListener list1Listener;
    private SyncingListDataListener list2Listener;

    private boolean inSync=false;
    
    /** @return the sync state. */
    public boolean isInSync() {
        return inSync;
    }

    /**
     * Creates a new ObservableListConnector two sync list1 and list2. If they are not equal they will remain different
     * until one of the lists changes or an update Method is called.
     *
     * @param list1
     * @param list2
     */
    public ObservableListConnector(final ObservableList list1, final ObservableList list2) {
        if (list1==null){
            throw new NullPointerException("List1 must not be null.");
        }
        if (list2==null){
            throw new NullPointerException("List2 must not be null.");
        }
        this.list1 = list1;
        this.list2 = list2;
        list1Listener = new SyncingListDataListener(list1,list2);
        list2Listener = new SyncingListDataListener(list2,list1);
        list1Listener.setOtherListener(list2Listener);
        list2Listener.setOtherListener(list1Listener);
        list1.addListDataListener(list1Listener);
        list2.addListDataListener(list2Listener);
        inSync=list1.equals(list2);
    }

    public void release(){
        list1.removeListDataListener(list1Listener);
        list2.removeListDataListener(list2Listener);
    }


    /** Forces an update of list1 with the contents of list2. This has no effect if the two lists are already in sync.*/
    public void updateList1(){
        list2Listener.updateOtherList();
    }

    /** Forces an update of list2 with the contents of list2. This has no effect if the two lists are already in sync.*/
    public void updateList2(){
        list1Listener.updateOtherList();
    }

    private class SyncingListDataListener implements ListDataListener {

        ListDataListener otherListener;
        ObservableList otherList;
        ObservableList myList;

        public SyncingListDataListener(final ObservableList myList, final ObservableList otherList) {
            this.myList = myList;
            this.otherList = otherList;
        }

        public void setOtherListener(final ListDataListener otherListener) {
            this.otherListener = otherListener;
        }

        private void checkSyncAndRunWithoutListener(Runnable r){
            if (!inSync)
            {
                updateOtherList();
                return;
            }

            otherList.removeListDataListener(otherListener);
            r.run();
            otherList.addListDataListener(otherListener);
        }

        public void updateOtherList() {
            otherList.removeListDataListener(otherListener);
            otherList.clear();
            otherList.addAll(myList);
            otherList.addListDataListener(otherListener);
            inSync=true;
        }


        public void contentsChanged(final ListDataEvent e) {
            checkSyncAndRunWithoutListener(new Runnable() {
                public void run() {
                    otherList.set(e.getIndex0(),myList.get(e.getIndex0()));
                }
            });
        }

        public void intervalAdded(final ListDataEvent e) {
               checkSyncAndRunWithoutListener(new Runnable() {
                public void run() {
                    otherList.addAll(myList.subList(e.getIndex0(),e.getIndex1()+1));
                }
            });
        }

        public void intervalRemoved(final ListDataEvent e) {
               checkSyncAndRunWithoutListener(new Runnable() {
                public void run() {
                    otherList.removeAll(new ArrayList(otherList.subList(e.getIndex0(),e.getIndex1()+1)));
                }
            });
        }
    }
}
