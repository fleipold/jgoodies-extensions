package ws.leipold.felix.jgoodies.cascading;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.ObservableList;
import com.jgoodies.binding.value.ValueModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import ws.leipold.felix.jgoodies.listadapter.*;
import static ws.leipold.felix.jgoodies.listadapter.ListAdapters.observe;

import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

/**
 * Created with IntelliJ IDEA.
 * User: Felix Leipold
 * Date: 10.10.2005
 * Time: 12:27:40
 */
public class    CascadingPresentationModel<T> extends PresentationModel<T> {
    private List changeChildren;
    private boolean changing = false;
    ChildListListener childListListener;
    private ObservableList parentList;

    public CascadingPresentationModel(ValueModel beanChannel) {
        this(beanChannel, null, null);
    }

    public CascadingPresentationModel(ValueModel beanChannel, CascadingPresentationModel parentModel) {
        this(beanChannel, parentModel, null);
    }


    public CascadingPresentationModel(ValueModel beanChannel, CascadingPresentationModel parentModel, ObservableList<T> parentListModel) {
        super(beanChannel);
        if (parentModel != null) {
            parentModel.bindChildModelChangeTracking(this);
        }
        if (parentListModel != null) {
            addParentListModel(parentListModel);
        }
        childListListener = new ChildListListener();
        addPropertyChangeListener(PROPERTYNAME_BEFORE_BEAN, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                changing = true;
            }
        });
        addPropertyChangeListener(PROPERTYNAME_AFTER_BEAN, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                changing = false;
            }
        });

    }

    public CascadingPresentationModel(ValueModel beanChannel, ObservableList parentListModel) {
        this(beanChannel, null, parentListModel);

    }


    private List getChangeChildren() {
        if (changeChildren == null) {
            changeChildren = new ArrayList(4);
        }
        return changeChildren;
    }

    private void bindChildModelChangeTracking(final PresentationModel childModel) {
        getChangeChildren().add(childModel);
        childModel.addPropertyChangeListener("changed", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getNewValue().equals(Boolean.TRUE)) {
                    if (!changing) {
                        setChanged(true);
                    }
                }
            }
        });
        childModel.addBeanPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {

            }
        });
    }

    public ObservableList getObservableListModel(String propertyName) {
        ObservableListHolder listHolder = new ObservableListHolder(this.getModel(propertyName));
        listHolder.addListDataListener(childListListener);
        return listHolder;
    }


    public ObservableList getListModel(String propertyName) {
        return observe (this.getModel(propertyName));

    }

    public void addParentListModel(final ObservableList parentList) {
        this.parentList = parentList;
        this.addBeanPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {

                fireParentList();

            }
        });
    }

    private void fireParentList() {
        if (parentList == null)
            return;
        Object bean = this.getBean();
        int index = parentList.indexOf(bean);
        parentList.set(index, bean);

    }

    public void resetChanged() {
        super.resetChanged();
        if (changeChildren != null) {
            for (Iterator iterator = changeChildren.iterator(); iterator.hasNext();) {
                PresentationModel pm = (PresentationModel) iterator.next();
                pm.resetChanged();
            }
        }
    }

    class ChildListListener implements ListDataListener {
        public void contentsChanged(ListDataEvent e) {
            update();
        }

        private void update() {
            if (!changing) {
                setChanged(true);
                fireParentList();
            }

        }

        public void intervalAdded(ListDataEvent e) {
            update();
        }

        public void intervalRemoved(ListDataEvent e) {
            update();
        }
    }


}
