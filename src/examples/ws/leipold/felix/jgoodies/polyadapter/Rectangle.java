package ws.leipold.felix.jgoodies.polyadapter;

import com.jgoodies.binding.beans.Model;

/**
 * @author Felix Leipold
 * Date: 09.10.2005
 * Time: 22:07:58
 */
public class Rectangle extends Model {
    public static final String PROPERTYNAME_HEIGHT ="height";
    public static final String PROPERTYNAME_WIDTH="width";

    int height;
    int width;

    public Rectangle(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        int old=this.height;
        this.height = height;
        firePropertyChange(PROPERTYNAME_HEIGHT,old,height);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        int old=this.width;
        this.width = width;
        firePropertyChange(PROPERTYNAME_WIDTH,old,width);
    }

    public String toString() {
        return "Rectangle("+width+","+height+")";
    }
}
