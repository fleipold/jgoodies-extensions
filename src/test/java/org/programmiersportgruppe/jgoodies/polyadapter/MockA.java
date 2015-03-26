package org.programmiersportgruppe.jgoodies.polyadapter;

import com.jgoodies.binding.beans.Model;

/**
 * Simple TestBean
 * User: Felix Leipold
 * Date: 07.10.2005
 * Time: 22:06:38
 */
public class MockA extends Model {
    private String a;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        String old=this.a;
        this.a = a;
        firePropertyChange("a",old,a);
    }

}
