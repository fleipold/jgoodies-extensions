package ws.leipold.felix.jgoodies.polyadapter;

import com.jgoodies.binding.beans.Model;

/**
 * Simple TestBean
 * User: Felix Leipold
 * Date: 07.10.2005
 * Time: 22:06:38
 */
public class MockC extends Model {
    private String c;

    public String getC() {
        return c;
    }

    public void setC(String c) {
        String old=this.c;
        this.c = c;
    }

}
