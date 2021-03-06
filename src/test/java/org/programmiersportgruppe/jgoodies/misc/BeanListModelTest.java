package org.programmiersportgruppe.jgoodies.misc;

import com.jgoodies.common.collect.ArrayListModel;

import junit.framework.TestCase;
import org.programmiersportgruppe.jgoodies.polyadapter.MockA;

import javax.swing.event.ListDataEvent;

/**
 * Created by IntelliJ IDEA.
 * User: fleipold
 * Date: Jun 10, 2008
 * Time: 2:09:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class BeanListModelTest extends TestCase {
    boolean flag = false;
    private MockA a;
    private ArrayListModel<MockA> wrappee;

    public void testChangingPropertyCallsListenerOnExisitngEelement(){
        wrappee.add(a);
        BeanListModel<MockA> listModel = new BeanListModel<MockA>(wrappee);
        listModel.addListDataListener(new ListDataListener());
        a.setA("Felix");
        assertTrue(flag);

    }


    public void testChangingPropertyCallsListenerOnAddedElement(){
        wrappee.add(a);
        BeanListModel<MockA> listModel = new BeanListModel<MockA>(wrappee);
        MockA b = new MockA();
        listModel.addListDataListener(new ListDataListener());
        listModel.add(b);
        assertFalse(flag);
        b.setA("Felix");
        assertTrue(flag);

    }

    protected void setUp() throws Exception {
        super.setUp();
        a = new MockA();
        wrappee = new ArrayListModel();
    }


    private class ListDataListener implements javax.swing.event.ListDataListener {
        public void intervalAdded(ListDataEvent e) {
        }

        public void intervalRemoved(ListDataEvent e) {
        }

        public void contentsChanged(ListDataEvent e) {
            flag = true;
        }
    }
}
