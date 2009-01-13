package ws.leipold.felix.jgoodies.listadapter;

import junit.framework.TestCase;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import com.jgoodies.binding.value.ValueHolder;

import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

/**
  * User: Felix Leipold
 * Date: 24.10.2005
  */
public class ObservationListWrapperTest extends TestCase {
    public List wrappedList;
    public String firstElement;
    public String secondElement;
    private String thirdElement;
    private boolean flag=false;

    public void testEventingOnRemove(){
        ObservationListWrapper converter=new ObservationListWrapper(wrappedList);
        converter.addListDataListener(new ListDataListener() {
            public void contentsChanged(ListDataEvent e) {
                fail("no content added");
            }
            public void intervalAdded(ListDataEvent e) {
                fail("nothing added");
            }
            public void intervalRemoved(ListDataEvent e) {
               flag=true;
            }
        });

        converter.remove(firstElement);
        assertTrue(flag);
    }

    public void testEventingOnRemoveIndex0(){
        ObservationListWrapper converter=new ObservationListWrapper(wrappedList);
        converter.addListDataListener(new ListDataListener() {
            public void contentsChanged(ListDataEvent e) {
                fail("no content added");
            }
            public void intervalAdded(ListDataEvent e) {
                fail("nothing added");
            }
            public void intervalRemoved(ListDataEvent e) {
               assertEquals(0,e.getIndex0());
               assertEquals(0,e.getIndex1());
                flag=true;
            }
        });

        converter.remove(0);
        assertTrue(flag);
    }

    public void testEventingOnRemoveIndex1(){
        ObservationListWrapper converter=new ObservationListWrapper(wrappedList);
        converter.addListDataListener(new ListDataListener() {
            public void contentsChanged(ListDataEvent e) {
                fail("no content added");
            }
            public void intervalAdded(ListDataEvent e) {
                fail("nothing added");
            }
            public void intervalRemoved(ListDataEvent e) {
               assertEquals(1,e.getIndex0());
               assertEquals(1,e.getIndex1());
                flag=true;
            }
        });

        converter.remove(1);
        assertTrue(flag);
    }

       public void testEventingOnRemoveElementOnIndex1(){
        ObservationListWrapper converter=new ObservationListWrapper((wrappedList));
        converter.addListDataListener(new ListDataListener() {
            public void contentsChanged(ListDataEvent e) {
                fail("no content added");
            }
            public void intervalAdded(ListDataEvent e) {
                fail("nothing added");
            }
            public void intervalRemoved(ListDataEvent e) {
               assertEquals(1,e.getIndex0());
               assertEquals(1,e.getIndex1());
                flag=true;
            }
        });

        converter.remove(secondElement);
        assertTrue(flag);
    }


    public void testEventingAdd(){
        ObservationListWrapper converter=new ObservationListWrapper(wrappedList);
        converter.addListDataListener(new ListDataListener() {
            public void contentsChanged(ListDataEvent e) {
                fail("no content added");
            }
            public void intervalAdded(ListDataEvent e) {
                      flag=true;
            }
            public void intervalRemoved(ListDataEvent e) {
                fail("nothing removed");
            }
        });

        converter.add("d");
        assertTrue(flag);
    }

    protected void setUp() throws Exception {
        super.setUp();
        firstElement = "a";
        secondElement = "b";
        thirdElement = "c";
        wrappedList = new ArrayList(Arrays.asList(new String []{firstElement, secondElement, thirdElement}));
    }


}