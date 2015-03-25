package ws.leipold.felix.jgoodies.listadapter;


import com.jgoodies.common.collect.ArrayListModel;
import com.jgoodies.common.collect.ObservableList;

import javax.swing.*;
import java.util.Arrays;

/**
 *@author Felix Leipold
 * Date: 20.10.2005
 *
 * This class represents a multi selection in has a ListModel representing the set of possible
 * and an ObservableList representing the current selection.
 * <strong>Note:</strong> Unlike SingleSelectionInList this class does not enforce constraints
 * on the selection. That means you can add items to the selection that are not in the list.
 *
 * But we should definitly think about it. Firstly we could add a listener to the selection which removes
 * invalid elements that are added. The disadvantage of this solution lies in the fact that there
 * is phase in which we are invalid.
 * Secondly we might consider wrapping the ObservableList to prevent illegal selections. So only while reducing the
 * possible we might run into problems. (What circumstances?)
 */

public class MultiSelectionInList {
    ObservableList selection;
    ListModel list;

  public MultiSelectionInList(ListModel list) {
        this.list = list;
        selection =new ArrayListModel();
    }

    public MultiSelectionInList(Object[] list) {
        this(new ArrayListModel(Arrays.asList(list)));
    }

    public ObservableList getSelection() {
        return selection;
    }

    public ListModel getList() {
        return list;
    }

  




}
