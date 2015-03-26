package org.programmiersportgruppe.swingutils;


/**
 * User: Felix Leipold
 * Date: 22.09.2005
 */
public class NullActionStatus implements ActionStatus {
    public boolean isCanceled() {
        return false;
    }

    public void appendMessage(String message) {
    }

    public void complete(double progress) {
    }
}