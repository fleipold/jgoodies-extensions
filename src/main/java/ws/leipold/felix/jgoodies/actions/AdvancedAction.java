package ws.leipold.felix.jgoodies.actions;

import ws.leipold.felix.jgoodies.trigger.Trigger;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * An action that uses a Trigger rather to notify it's clients rather than you having to override action performed.
 *
 */
public class AdvancedAction extends AbstractAction {
    private Trigger actionTrigger = new Trigger();

    public AdvancedAction(String name, Icon icon) {
        super(name, icon);
    }

    public AdvancedAction(String name) {
        super(name);
    }

    protected boolean isArmed(){
        return true;
    }

    public final void actionPerformed(ActionEvent e) {
       if (isArmed())
        actionTrigger.fire();
    }

    public final Trigger getActionTrigger() {
        return actionTrigger;
    }
}