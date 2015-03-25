package ws.leipold.felix.jgoodies.cascading;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ObservableList;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import junit.framework.TestCase;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Felix Leipold
 * Date: 21.10.2005
 */
public class CascadingPresentationModelTest extends TestCase {
    private boolean flag;


    public static class TestBean extends Model {
        List children;
        public static final String THIRD_ELEMENT = "C";

        public TestBean(){
            this(new ArrayList(Arrays.asList(new String[]{"A","B",THIRD_ELEMENT})));
        }

        public TestBean(List children) {
            this.children = children;
        }

        public List getChildren() {
            return children;
        }

        public void setChildren(List children) {
            List old=this.children;
            this.children = children;
            firePropertyChange("children",old,children);
        }

    }

    public void testRealCascading(){
        TestBean outerBean = new TestBean(new ArrayList());
        TestBean innerBean = new TestBean();
        outerBean.getChildren().add(innerBean);

        CascadingPresentationModel outerModel=new CascadingPresentationModel(new ValueHolder(outerBean, true));
        ObservableList outerList = outerModel.getListModel("children");
        SelectionInList outerSelection = new SelectionInList((ListModel) outerList);
        CascadingPresentationModel innerModel=new CascadingPresentationModel(outerSelection,outerModel,outerList);
        ObservableList innerList = innerModel.getListModel("children");
        SelectionInList innerSelection = new SelectionInList((ListModel) innerList);
        outerSelection.setSelection(innerBean);
        assertEquals(3, innerList.size());
        innerSelection.setSelection(TestBean.THIRD_ELEMENT);
         innerSelection.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                flag=true;
                System.out.println("Hi");
            }
        });

        innerList.remove(TestBean.THIRD_ELEMENT);
        assertTrue(flag);
        assertNull(innerSelection.getValue());
    }

    public void testListWrapperInSelection(){
        CascadingPresentationModel cpm = new CascadingPresentationModel(new ValueHolder(new TestBean(), true));
        ObservableList childList = cpm.getListModel("children");
        assertEquals(3,childList.getSize());
        Object thirdElement = childList.get(2);
        SelectionInList selection=new SelectionInList((ListModel) childList);
        selection.setValue(thirdElement);
        assertEquals(thirdElement,selection.getValue());
        flag=false;
        selection.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                flag=true;
            }
        });
        childList.remove(thirdElement);
        assertTrue(flag);
        assertNull(selection.getValue());
    }

}
