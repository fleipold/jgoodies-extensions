package ws.leipold.felix.jgoodies.misc;

import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.ValueModel;

/**todo: Add support for PropertyChangeListener on "value"-property see ValueHolder
 * Created by IntelliJ IDEA.
 * User: felix
 * Date: 05.01.2005
 * Time: 10:27:57
 * To change this template use File | Settings | File Templates.
 */
public class ObservationAdapter extends AbstractValueModel implements ValueModel {

   ValueModel wrappedModel;

    public ObservationAdapter(ValueModel wrappedModel) {
        this.wrappedModel = wrappedModel;
    }

   public Object getValue() {
       return wrappedModel.getValue();
    }

    public void setValue(Object newValue) {
        Object oldValue=wrappedModel.getValue();
        wrappedModel.setValue(newValue);
        fireValueChange(oldValue,newValue);   
    }

}
