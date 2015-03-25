package ws.leipold.felix.jgoodies.tree;

import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * User: felix
 * Date: 31.12.2004
 * Time: 13:46:21
 */
public class SelectionInTree implements TreeSelectionListener, TreeModel {
    ValueModel selectionHolder;
    TreeModel model;
    TreeSelectionModel selectionModel;
    private modelListener selectionModelListener;
    HashMap node2parent;

    public ValueModel getSelectionHolder() {
        return selectionHolder;
    }

    public SelectionInTree(TreeModel model){
        this(model,new ValueHolder());
    }
    public SelectionInTree(TreeModel model,ValueModel selection) {
        this.selectionHolder =selection;
        this.model = model;
        this.selectionModel = new DefaultTreeSelectionModel();

        node2parent=new HashMap();
        fillHash(model.getRoot());
        selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        selectionModelListener = new modelListener();
        selection.addValueChangeListener(selectionModelListener);
    }

    private void fillHash(Object base) {
        for (int i=0;i<model.getChildCount(base);i++){
            Object child = model.getChild(base, i);
            node2parent.put(child,base);
            fillHash(child);
        }

    }

    public void setSelectionFromValueHolder() {
        final Object currentCategory = selectionHolder.getValue();
        if (currentCategory!=null){
          setSelectionPath(currentCategory);
        }
    }

    public void dispose(){
        selectionHolder.removeValueChangeListener(selectionModelListener);
    }

    private void setSelectionPath(Object currentCategory) {
        if (currentCategory==null){
            //todo: remove current selection
            return;
        }
        List pathList = new LinkedList();
        pathList.add(currentCategory);
        while (node2parent.get(currentCategory) != null) {
            currentCategory = node2parent.get(currentCategory);
            pathList.add(currentCategory);
        }
        Object[] pathArray = new Object[pathList.size()];
        int index = pathList.size();
        for (Iterator iterator = pathList.iterator(); iterator.hasNext();) {
            index--;
            pathArray[index] = iterator.next();
        }
        TreePath path = new TreePath(pathArray);
        selectionModel.setSelectionPaths(new TreePath[]{path});
    }

    public TreeSelectionModel getSelectionModel() {
        return selectionModel;
    }

    public void valueChanged(TreeSelectionEvent e) {
        selectionHolder.removeValueChangeListener(selectionModelListener);
        final TreePath newLeadSelectionPath = e.getNewLeadSelectionPath();
        if (newLeadSelectionPath!=null){
          selectionHolder.setValue(newLeadSelectionPath.getLastPathComponent());


        }
        else {
            selectionHolder.setValue(null);

        }
        selectionHolder.addValueChangeListener(selectionModelListener);

    }


    public Object getRoot() {
        return model.getRoot();
    }

    public int getChildCount(Object parent) {
        return model.getChildCount(parent);
    }

    public boolean isLeaf(Object node) {
        return model.isLeaf(node);
    }

    public void addTreeModelListener(TreeModelListener l) {
        model.addTreeModelListener(l);
    }

    public void removeTreeModelListener(TreeModelListener l) {
        model.removeTreeModelListener(l);
    }

    public Object getChild(Object parent, int index) {
        return model.getChild(parent, index);
    }

    public int getIndexOfChild(Object parent, Object child) {
        return model.getIndexOfChild(parent, child);
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        model.valueForPathChanged(path, newValue);
    }

    private class modelListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {

                Object currentSelection =  evt.getNewValue();

                setSelectionPath(currentSelection);
            System.out.println("Selection Value Model changed. Source: " + evt.getSource());
            System.out.println("Oldvalue: " + evt.getOldValue() + "  NewValue: " + evt.getNewValue());
        }
    }
}
