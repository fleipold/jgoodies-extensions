package org.programmiersportgruppe.swingutils;
/**
 *
 *
 * User: felix
 *
 * Date: 19.07.2005
 */
public interface ActionStatus {

    public boolean isCanceled();
    /** Appends a message*/
    public void appendMessage(String message);
    /** @param progress value between 0-100 */
    public void complete(double progress);
}
