package org.programmiersportgruppe.jgoodies.polyadapter;

import com.jgoodies.binding.beans.Model;

/**
 * Simple TestBean 
 * User: Felix Leipold
 * Date: 07.10.2005
 * Time: 22:06:38
 */
public class MockB extends Model {
    private String b;

    public String getB() {
        return b;
    }

    public void setB(String b) {
        String old=this.b;
        this.b = b;
        firePropertyChange("b",old,b);
    }

}
