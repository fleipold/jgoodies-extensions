package ws.leipold.felix.jgoodies.misc;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;


/** A value model that holds a true value when the subject holds a non-null reference and false otherwise.*/
public class IsNotNullConverter extends AbstractConverter {
    public IsNotNullConverter(ValueModel subject) {
        super(subject);
    }

    public Object convertFromSubject(Object subjectValue) {
        return subjectValue!=null;
    }

    public void setValue(Object newValue) {
    }
}
