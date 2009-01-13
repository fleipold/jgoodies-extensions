package ws.leipold.felix.jgoodies.misc;

import junit.framework.TestCase;
import ws.leipold.felix.jgoodies.polyadapter.MockA;
import com.jgoodies.binding.list.ArrayListModel;

import javax.swing.event.ListDataEvent;

/**
 * Created by IntelliJ IDEA.
 * User: fleipold
 * Date: Jun 10, 2008
 * Time: 2:09:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class BeanObservingObservableListModelTest extends TestCase {
    boolean flag = false;
    private MockA a;
    private ArrayListModel<MockA> wrappee;

    public void testChangingPropertyCallsListenerOnExisitngEelement(){
        wrappee.add(a);
        BeanObservingObservableListModel<MockA> listModel = new BeanObservingObservableListModel<MockA>(wrappee);
        listModel.addListDataListener(new ListDataListener());
        a.setA("Felix");
        assertTrue(flag);

    }


    public void testChangingPropertyCallsListenerOnAddedElement(){
        wrappee.add(a);
        BeanObservingObservableListModel<MockA> listModel = new BeanObservingObservableListModel<MockA>(wrappee);
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
