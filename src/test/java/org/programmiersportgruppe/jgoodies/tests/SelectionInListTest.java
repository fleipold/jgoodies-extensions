package org.programmiersportgruppe.jgoodies.tests;

import com.jgoodies.common.collect.ArrayListModel;
import com.jgoodies.common.collect.ObservableList;
import com.jgoodies.binding.list.SelectionInList;
import junit.framework.TestCase;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

/**
 * User: Felix Leipold
 * Date: 21.10.2005
 * Time: 12:42:25
 *
 * This was only to help me understand the original SelectionInList
 */
public class SelectionInListTest extends TestCase {
    private boolean flag=false;
    public String thirdElement;
    public String firstElement;
    public ObservableList list;

    public void testListChangePropagatesToSelection(){
        final String thirdElement = "C";
        ObservableList list=new ArrayListModel(Arrays.asList(new String[]{"A","B",thirdElement}));
        SelectionInList testSelection=new SelectionInList((ListModel)list);
        testSelection.setSelection(thirdElement);
        assertEquals(thirdElement,testSelection.getValue());
        list.remove(2);
        assertNull(testSelection.getValue());
    }

    public void testListChangePropagatesToSelectionEventOnLastElement(){
        final String thirdElement = "C";
        ObservableList list=new ArrayListModel(Arrays.asList(new String[]{"A","B",thirdElement}));
        SelectionInList testSelection=new SelectionInList((ListModel)list);
        testSelection.setValue(thirdElement);
        assertEquals(thirdElement,testSelection.getValue());
        testSelection.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                flag=true;
            }
        });
        list.remove(testSelection.getValue());
        assertTrue(flag);
        assertNull(testSelection.getValue());

    }

     public void testListChangePropagatesToSelectionEventOnFirstElement(){
         SelectionInList testSelection=new SelectionInList((ListModel)list);
        testSelection.setValue(firstElement);
        assertEquals(firstElement,testSelection.getValue());
        testSelection.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                flag=true;
            }
        });
        list.remove(testSelection.getValue());
        assertNull(testSelection.getValue());
        assertTrue(flag);
    }

    public void testSelectionBehaviouOnChangedList(){
        SelectionInList selection = new SelectionInList((ListModel) list);
        selection.setValue(firstElement);
        list.remove(thirdElement);
        assertEquals(firstElement,selection.getValue());
    }

    public void testSelectionBehaviouOnElementRemovedFromList(){
        SelectionInList selection = new SelectionInList((ListModel) list);
        selection.setValue(firstElement);
        list.remove(firstElement);
        assertNull(selection.getValue());
    }


    protected void setUp() throws Exception {
    super.setUp();
        thirdElement = "C";
        firstElement = "A";
        list = new ArrayListModel(Arrays.asList(new String[]{firstElement,"B",thirdElement}));
    }
}
