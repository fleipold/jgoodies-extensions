package ws.leipold.felix.jgoodies.misc;

import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

import java.beans.PropertyChangeListener;


public class BeanHolder implements ValueModel {
    final ValueHolder holder = new ValueHolder();

    public BeanHolder() {
        holder.setIdentityCheckEnabled(true);
    }

    public Object getValue() {
        return holder.getValue();
    }

    public void setValue(Object o) {
        holder.setValue(o);
    }

    public void addValueChangeListener(PropertyChangeListener propertyChangeListener) {
        holder.addValueChangeListener(propertyChangeListener);
    }

    public void removeValueChangeListener(PropertyChangeListener propertyChangeListener) {
        holder.removeValueChangeListener(propertyChangeListener);
    }
}
