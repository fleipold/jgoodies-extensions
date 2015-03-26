package org.programmiersportgruppe.jgoodies.actions;

import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import junit.framework.TestCase;

import javax.swing.*;
import javax.swing.Action;
import java.awt.event.ActionEvent;



public class ActionReflectorTest extends TestCase {
    Action myAction;
    AdvancedAction myAdvancedAction;

    private final AbstractAction SPECIAL_ACTION = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {

        }
    };
    Action specialAction = SPECIAL_ACTION;
    boolean flag = false;
    ValueHolder enabled = new ValueHolder(true);

    public void myAction(){
        flag = true;

    }

    public void myAdvancedAction(){
        
    }

    public ValueModel canMyAction(){
        return enabled;

    }

    public void testPopulatesActionFieldAndCallHandler(){
        ActionReflector.reflect(this);
        assertNotNull(myAction);
        myAction.actionPerformed(new ActionEvent(this, 0,""));
        assertTrue(flag);
    }

    public void testDontPopulatePresuppliedAction(){
        ActionReflector.reflect(this);
        assertEquals(SPECIAL_ACTION, specialAction);
    }


    public void testGetsEnableFromModel(){
        ActionReflector.reflect(this);
        assertTrue(myAction.isEnabled());
        enabled.setValue(false);
        assertFalse(myAction.isEnabled());
    }

   public void testPopulatesActionForActionSubclass(){
        ActionReflector.reflect(this);
        assertNotNull(myAdvancedAction);
    }


}