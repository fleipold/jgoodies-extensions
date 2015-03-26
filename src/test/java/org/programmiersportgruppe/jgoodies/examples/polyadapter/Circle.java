package org.programmiersportgruppe.jgoodies.examples.polyadapter;
import com.jgoodies.binding.beans.Model;

/**
 * @author Felix Leipold
 * Date: 09.10.2005
 * Time: 22:07:50
 */
public class Circle extends Model {

    public static final String PROPERTYNAME_RADIUS = "radius";

    int radius=0;

    public Circle(int radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        int old= this.radius;
        this.radius = radius;
        firePropertyChange(PROPERTYNAME_RADIUS,old,radius);
    }

    public String toString() {
        return "Circle("+radius+")";
    }


}
