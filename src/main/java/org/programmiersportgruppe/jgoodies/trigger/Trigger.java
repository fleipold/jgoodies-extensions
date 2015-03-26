package org.programmiersportgruppe.jgoodies.trigger;

import java.util.HashSet;
import java.util.Set;


public class Trigger {

    final transient Set<TriggerListener> listeners = new HashSet<TriggerListener>(1);

    public void addTiggerListener(TriggerListener listener){
        listeners.add(listener);
    }

    public void removeTriggerListener(TriggerListener listener){
        if (!listeners.contains(listener))
            throw new IllegalArgumentException("Trying to remove listener that has not been added in the first place ;-).");
        listeners.remove(listener);
    }



    public void fire() {
        for (TriggerListener triggerListener : listeners) {
            triggerListener.handleTrigger();
        }

    }
}