package ws.leipold.felix.jgoodies.listadapter;

import com.jgoodies.binding.list.ObservableList;
import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;

import java.util.List;

public class ListAdapters {
    public static <T> ObservableList<T> observe(List<T> list){
        return new ObservationListWrapper<T>(list);
    }


    public static <T> ObservableList<T> observe(ValueModel listHolder){
        return new ObservableListHolder<T>(new AbstractConverter(listHolder){
            public Object convertFromSubject(Object o) {
                if (o instanceof ObservableList)
                    return (o);
                return observe((List)o);
            }

            public void setValue(Object o) {}
        });
    }
}
