package ws.leipold.felix.jgoodies.tree;

import junit.framework.TestCase;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * Created by IntelliJ IDEA.
 * User: fleipold
 * Date: 10-Dec-2006
 * Time: 22:01:27
 * To change this template use File | Settings | File Templates.
 */
public class SelectionInTreeTest extends TestCase {
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode root,a,b,c,d;
    private SelectionInTree selectionInTree;

    protected void setUp() throws Exception {
        super.setUp();
        root=new DefaultMutableTreeNode();
        a=createChild(root,"a");
        b= createChild(root, "b");
        c= createChild(b, "c");
        d= createChild(b, "d");
        this.treeModel=new DefaultTreeModel(root);
        this.selectionInTree=new SelectionInTree(treeModel);
    }

    private DefaultMutableTreeNode createChild(DefaultMutableTreeNode parent, String s) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(s);
//        childNode.setParent(parent);
        parent.add(childNode);
        return childNode;
    }

    public void testSelectionViaValueModelYieldsSelectionPath(){
        selectionInTree.getSelectionHolder().setValue(d);
        Object[] path = selectionInTree.getSelectionModel().getSelectionPath().getPath();
        assertEquals(root,path[0]);
        assertEquals(b, path[1]);
        assertEquals(d,path[2]);
       }

    public void testSelectionViaTreeSelectionModelYieldsValueModel(){

        TreePath treePath = new TreePath(new Object[]{root, b, c});
        selectionInTree.valueChanged(new TreeSelectionEvent(this,treePath,true,null,treePath));

        assertEquals(selectionInTree.getSelectionHolder().getValue(),c);
       }

    public static void main(String[] args) throws Exception {
        SelectionInTreeTest test=new SelectionInTreeTest();
        test.staticTest();
    }

    private void staticTest() throws Exception {
        setUp();
        JFrame frame=new JFrame();
        JTree tree = new JTree(this.treeModel);
        tree.setSelectionModel(selectionInTree.getSelectionModel());
        frame.getContentPane().add(tree);
        frame.pack();
        frame.setVisible(true);

    }
}
