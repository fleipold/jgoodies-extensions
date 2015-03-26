package org.programmiersportgruppe.jgoodies.trigger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class DelayedTrigger extends Trigger {

    /**
     * The Timer used to perform the delayed commit.
     */
    private final Timer timer;



    // Instance Creation ******************************************************

    /**
     * Constructs a DelayedValueModel for the given subject ValueModel
     * and the specified Timer delay in milliseconds.
     *
     * @param subject   the underlying (or wrapped) ValueModel
     * @param delay     the milliseconds to wait before a change
     *     shall be committed
     */
    public DelayedTrigger(Trigger subject, int delay) {

        this.timer = new Timer(delay, new ValueCommitListener());
        timer.setRepeats(false);

        subject.addTiggerListener(new TriggerListener() {
            public void handleTrigger() {
                  timer.start();
            }
        });

    }


    // Event Handling *****************************************************


    private class ValueCommitListener implements ActionListener {

        /**
         * An ActionEvent has been fired by the Timer after its delay.
         * Commits the pending value and stops the timer.
         */
        public void actionPerformed(ActionEvent e) {
            timer.stop();
            fire();

        }
    }

}