package ws.leipold.felix.jgoodies.polyadapter;

import junit.framework.TestCase;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import ws.leipold.felix.jgoodies.polyadapter.TypeCaseModel;

/**
 * TestCase for the TypeCaseModel
 *
 * User: Felix Leipold
 * Date: 07.10.2005
 * Time: 22:02:33
 */
public class TypeCaseTest extends TestCase {

    private MockA a;
    private MockB b;
    private MockC c;
    private ValueHolder polyBean;

    protected void setUp() throws Exception {
        a = new MockA();
        a.setA("a_value");
        b = new MockB();
        b.setB("b_value");
        c = new MockC();
        c.setC("c_value");

        polyBean = new ValueHolder();
    }

    public void testChangeOnSelected() {
        polyBean.setValue(a);
        TypeCaseModel typeCase = new TypeCaseModel(polyBean);
        ValueModel aChannel = typeCase.getTypeChannel(MockA.class, null, "a");
        ValueModel bChannel = typeCase.getTypeChannel(MockB.class, null, "b");
        assertEquals(a, aChannel.getValue());
        assertEquals("a", typeCase.getActiveChannelID());
        polyBean.setValue(b);
        assertEquals(b, bChannel.getValue());
        assertEquals("b", typeCase.getActiveChannelID());
    }

    public void testChangeOnDeSelected() {
        polyBean.setValue(a);
        TypeCaseModel typeCase = new TypeCaseModel(polyBean);
        MockA nullObject = new MockA();
        ValueModel aChannel = typeCase.getTypeChannel(MockA.class, nullObject, "a");
        ValueModel bChannel = typeCase.getTypeChannel(MockB.class, null, "b");
        assertEquals(a, aChannel.getValue());
        assertEquals("a", typeCase.getActiveChannelID());
        polyBean.setValue(b);
        assertEquals(nullObject, aChannel.getValue());
        assertEquals("b", typeCase.getActiveChannelID());
    }

    public void testEventingOnTypeID() {
        polyBean.setValue(a);
        TypeCaseModel typeCase = new TypeCaseModel(polyBean);
        MockA nullObject = new MockA();
        ValueModel aChannel = typeCase.getTypeChannel(MockA.class, nullObject, "a");
        ValueModel bChannel = typeCase.getTypeChannel(MockB.class, null, "b");
        assertEquals(a, aChannel.getValue());
        assertEquals("a", typeCase.getActiveChannelID());
        final boolean[] eventFired = new boolean[]{false};
        typeCase.addPropertyChangeListener("activeChannelID", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                eventFired[0] = true;
            }
        });

        polyBean.setValue(b);
        assertTrue(eventFired[0]);
    }

    public void testEventingOnModelLoosingChannel() {
        polyBean.setValue(a);
        TypeCaseModel typeCase = new TypeCaseModel(polyBean);
        MockA nullObject = new MockA();
        ValueModel aChannel = typeCase.getTypeChannel(MockA.class, nullObject, "a");
        ValueModel bChannel = typeCase.getTypeChannel(MockB.class, null, "b");
        assertEquals(a, aChannel.getValue());
        assertEquals("a", typeCase.getActiveChannelID());
        final boolean[] eventFired = new boolean[]{false};
        aChannel.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                eventFired[0] = true;
            }
        });
        polyBean.setValue(b);
        assertTrue(eventFired[0]);
    }

    public void testEventingOnModelGainingChannel() {
        polyBean.setValue(a);
        TypeCaseModel typeCase = new TypeCaseModel(polyBean);
        MockA nullObject = new MockA();
        ValueModel aChannel = typeCase.getTypeChannel(MockA.class, nullObject, "a");
        ValueModel bChannel = typeCase.getTypeChannel(MockB.class, null, "b");
        final boolean[] eventFired = new boolean[]{false};
        bChannel.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                eventFired[0] = true;
            }
        });
        polyBean.setValue(b);
        assertTrue(eventFired[0]);
    }

    public void testEventingOnConstantChannel() {
        polyBean.setValue(a);
        TypeCaseModel typeCase = new TypeCaseModel(polyBean);
        MockA nullObject = new MockA();
        ValueModel aChannel = typeCase.getTypeChannel(MockA.class, nullObject, "a");
        ValueModel bChannel = typeCase.getTypeChannel(MockB.class, null, "b");
        final boolean[] eventFired = new boolean[]{false};
        aChannel.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                eventFired[0] = true;
            }
        });
        polyBean.setValue(new MockA());
        assertTrue(eventFired[0]);
    }



    public void testEventingOnUnchangedModel() {
        polyBean.setValue(a);
        TypeCaseModel typeCase = new TypeCaseModel(polyBean);
        MockA nullObject = new MockA();
        ValueModel aChannel = typeCase.getTypeChannel(MockA.class, nullObject, "a");
        ValueModel bChannel = typeCase.getTypeChannel(MockB.class, null, "b");
        ValueModel cChannel = typeCase.getTypeChannel(MockC.class, null, "c");
        final boolean[] eventFired = new boolean[]{false};
        cChannel.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                eventFired[0] = true;
            }
        });
        polyBean.setValue(b);
        assertFalse(eventFired[0]);
    }

    public void testEventingOnUnchangedModelUsingNullObject() {
        polyBean.setValue(a);
        TypeCaseModel typeCase = new TypeCaseModel(polyBean);
        MockA nullObject = new MockA();
        ValueModel aChannel = typeCase.getTypeChannel(MockA.class, nullObject, "a");
        ValueModel bChannel = typeCase.getTypeChannel(MockB.class, null, "b");
        MockC nullObjectC = new MockC();
        nullObjectC.setC("NULL C");
        ValueModel cChannel = typeCase.getTypeChannel(MockC.class, nullObjectC, "c");
        final boolean[] eventFired = new boolean[]{false};
        cChannel.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                eventFired[0] = true;
            }
        });
        polyBean.setValue(b);
        assertFalse(eventFired[0]);
    }

    public void testInitialNullValue() {
        polyBean.setValue(null);
        TypeCaseModel typeCase = new TypeCaseModel(polyBean);
        ValueModel aChannel = typeCase.getTypeChannel(MockA.class, null, "a");
        ValueModel bChannel = typeCase.getTypeChannel(MockB.class, null, "b");
        polyBean.setValue(b);
        assertEquals(b, bChannel.getValue());
        assertEquals("b", typeCase.getActiveChannelID());
    }

    public void testActiveModels() {
        TypeCaseModel typeCase = new TypeCaseModel(polyBean);
        polyBean.setValue(a);
        MockA nullObject = new MockA();
        ValueModel aChannel = typeCase.getTypeChannel(MockA.class, nullObject, "a");
        ValueModel bChannel = typeCase.getTypeChannel(MockB.class, null, "b");
        ValueModel aActive = typeCase.getActivityModel(aChannel);
        ValueModel bActive = typeCase.getActivityModel(bChannel);

        assertEquals( Boolean.TRUE,aActive.getValue());
        assertEquals(Boolean.FALSE, bActive.getValue());

        polyBean.setValue(b);
        assertEquals(Boolean.FALSE, aActive.getValue());
        assertEquals(Boolean.TRUE, bActive.getValue());
    }




}
