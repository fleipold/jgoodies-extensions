package ws.leipold.felix.jgoodies.listadapter;

import junit.framework.TestCase;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.ObservableList;
import com.jgoodies.binding.value.ValueHolder;

import javax.swing.*;
import java.util.Arrays;

/**
 * User: Felix Leipold
 * Date: 26.10.2005
 */
public class MultiSelectionTest extends TestCase {
    private ObservableList list;
    public final String A = "A";


    //this test seems pointless
    public void testMultiSelection() {
        MultiSelectionInList multiSelection=new MultiSelectionInList(list);
        ListModel selectedElements=multiSelection.getSelection();

        assertEquals(0,selectedElements.getSize());

        final ArrayListModel newValue = new ArrayListModel();
        newValue.add(A);
        multiSelection.getSelection().addAll(newValue);

        assertEquals(1,selectedElements.getSize());
    }

    protected void setUp() throws Exception {
        super.setUp();

        list= new ArrayListModel(Arrays.asList(new String[]{A, "B", "C"}));

    }


}
