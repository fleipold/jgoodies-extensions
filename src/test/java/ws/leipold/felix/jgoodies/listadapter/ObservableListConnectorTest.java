package ws.leipold.felix.jgoodies.listadapter;

import com.jgoodies.common.collect.ArrayListModel;
import com.jgoodies.common.collect.ObservableList;
import junit.framework.TestCase;

/**
 * User: felix
 * Date: 18.11.2005
 * Time: 10:24:26
 */
public class ObservableListConnectorTest extends TestCase {
    private ObservableList list1;
    private ObservableList list2;
    private String element;
    private String anotherElement;
    private ObservableListConnector olc;

    public void testEmtpyLists(){
        connect();
        assertTrue(olc.isInSync());
        list1.add(element);
        assertEquals(1,list2.size());
        assertEquals(element,list2.get(0));
        assertTrue(olc.isInSync());
    }
    public void testAlternatingUpdate(){

    }

    public void testDifferentListsUpdate1(){
        list2.add(element);
        connect();
        assertFalse(olc.isInSync());
        olc.updateList1();
        assertEquals(1,list1.size());
        assertTrue(olc.isInSync());
    }

    public void testDifferentListsUpdate2(){
        list2.add(element);
        connect();
        assertFalse(olc.isInSync());
        olc.updateList2();
        assertEquals(0,list1.size());
        assertEquals(0,list2.size());
        assertTrue(olc.isInSync());
    }
    public void testConnectNull(){
            list2=null;
            list1.add(element);
            list1.add(anotherElement);
            try {
                connect();
                fail();
            }
            catch (NullPointerException e){
                assertEquals("List2 must not be null.",e.getMessage());

            }
        }

    public void testDifferentListsImplicitUpdate2(){
        list1.add(element);
        connect();
        assertFalse(olc.isInSync());
        assertEquals(0,list2.size());
        list1.add(anotherElement);
        assertEquals(2,list2.size());
        assertEquals(2,list1.size());
    }

    public void testChangedElement(){
        connect();
        list1.add(element);
        assertEquals(list1,list2);
        assertEquals(1,list1.size());
        list1.set(0,anotherElement);
        assertEquals(list1,list2);
        assertEquals(anotherElement,list2.get(0));
    }


    public void testRemoveElementInducesUpdate(){
        list1.add(element);
        list1.add(anotherElement);
        connect();
        list1.remove(element);
        assertTrue(olc.isInSync());
        assertEquals(list1,list2);
    }




    // Testfixture specific ***************************************************
    private void connect() {
        olc = new ObservableListConnector(list1,list2);
    }
    protected void setUp() throws Exception {
        super.setUp();
        list1 = new ArrayListModel();
        list2 = new ArrayListModel();
        element = "hello";
        anotherElement = "goodbye";
    }
}
