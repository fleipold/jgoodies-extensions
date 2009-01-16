package ws.leipold.felix.jgoodies.listadapter;

import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.ObservableList;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import junit.framework.TestCase;

import javax.swing.event.ListDataEvent;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Felix Leipold
 * Date: 20.10.2005
 * Time: 13:54:49
 */
public class ObservableListHolderTest extends TestCase {
    public final static String[] a = new String[]{"X", "Y", "Z"};
    public ArrayListModel originalList;
    public ObservableListHolder listHolder;
    public ValueModel holder;

    boolean flag = false;
    private ObservableList newList;
    private ArrayListModel newList2;

    public void testReadPropagation() {
        assertEquals("Y", listHolder.get(1));
        assertEquals("Z", listHolder.get(2));
        assertEquals(3, listHolder.size());
    }

    public void testWritePropagation() {
        listHolder.add("1");
        assertEquals("1", listHolder.get(3));
        assertEquals(4, listHolder.size());
    }

    public void testEventPropagationOriginalToHolder() {
        listHolder.addListDataListener(new ListDataListener());
        originalList.add("1");
        assertTrue(flag);
        assertEquals("1", listHolder.get(3));

    }

    public void testEventPropagationFromOriginalToHolder() {
        listHolder.addListDataListener(new ListDataListener());
        originalList.add("1");
        assertTrue(flag);
        assertEquals("1", listHolder.get(3));

    }


    public void testEventPropagationClear() {
        originalList.addListDataListener(new ListDataListener());
        originalList.clear();
        assertTrue(flag);

    }

    public void testEventPropagationFromHolderToOriginal() {
        originalList.addListDataListener(new ListDataListener());
        listHolder.add("1");
        assertTrue(flag);
        assertEquals("1", listHolder.get(3));
    }

    public void testNewListPropagationFromHolderToOriginal() {
        assertEquals("X", listHolder.get(0));
        listHolder.addListDataListener(new ListDataListener());
        holder.setValue(newList);
        assertEquals("A", listHolder.get(0));
        assertTrue(flag);
    }


    public void testNewListEventing() {
        assertEquals("X", listHolder.get(0));
        final StringBuffer buffer = new StringBuffer();
        listHolder.addListDataListener(new javax.swing.event.ListDataListener() {
            public void contentsChanged(ListDataEvent e) {

            }

            public void intervalAdded(ListDataEvent e) {
                buffer.append("A(");
                buffer.append(e.getIndex0());
                buffer.append(",");
                buffer.append(e.getIndex1());
                buffer.append(")");


            }

            public void intervalRemoved(ListDataEvent e) {
                buffer.append("R(");
                buffer.append(e.getIndex0());
                buffer.append(",");
                buffer.append(e.getIndex1());
                buffer.append(")");


            }
        });
        holder.setValue(newList2);
        assertEquals("A", listHolder.get(0));
        assertEquals("R(0,2)A(0,3)", buffer.toString());

    }

    public void testNewListPropagationFromHolderToOriginalAndChange() {
        assertEquals("X", listHolder.get(0));
        listHolder.addListDataListener(new ListDataListener());

        holder.setValue(newList);
        assertEquals("A", listHolder.get(0));
        assertTrue(flag);
        flag = false;
        newList.add("H");
        assertTrue(flag);

    }

    public void testHolderPointingToNull() {
        holder.setValue(null);
        assertEquals(0, listHolder.size());
    }

    public void testEventPropagationHolderChange() {
        listHolder.addListDataListener(new ListDataListener());
        holder.setValue(null);
        assertTrue(flag);
    }


    public void testValueHolderThingy() {


    }

    protected void setUp() throws Exception {
        super.setUp();
        originalList = new ArrayListModel(Arrays.asList(a));
        newList = new ArrayListModel(Arrays.asList(new String[]{"A", "B", "C"}));
        newList2 = new ArrayListModel(Arrays.asList(new String[]{"A", "B", "C", "D"}));
        holder = new ValueHolder(originalList);
        listHolder = new ObservableListHolder(holder);
    }

    private class ListDataListener implements javax.swing.event.ListDataListener {
        public void contentsChanged(ListDataEvent e) {
            flag = true;
        }

        public void intervalAdded(ListDataEvent e) {
            flag = true;
        }

        public void intervalRemoved(ListDataEvent e) {
            flag = true;
        }
    }
}
