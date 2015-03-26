package org.programmiersportgruppe.jgoodies.examples.listadapter;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.common.collect.ObservableList;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.FormLayout;
import org.programmiersportgruppe.jgoodies.listadapter.MultiListSelectionAdapter;
import org.programmiersportgruppe.jgoodies.listadapter.MultiSelectionInList;
import org.programmiersportgruppe.jgoodies.listadapter.MultiSelectionSingleSelection;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Felix Leipold
 * Date: 20.10.2005
 * Time: 10:58:19
 */
public class MultiSelectionExample {
    private MultiSelectionInList multiSelection;
    private JList primaryList;
    private JList secondaryList;
    private ObservableList selectionHolder;
    private SelectionInList comboSelection;
    public JComboBox combo;


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }

        JFrame frame = new JFrame();
        frame.setTitle("Binding Tutorial :: Multi Selection Example");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JComponent panel = new MultiSelectionExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        //TutorialUtils.locateOnScreenCenter(frame);
        frame.setVisible(true);
    }

    private JComponent buildPanel() {
        initModel();
        initComponents();
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("fill:100dlu,4dlu,fill:100dlu", "fill:100dlu"));
        builder.append(new JScrollPane(primaryList));
        builder.append(new JScrollPane(secondaryList));
        builder.append(combo,3);
        builder.append(BasicComponentFactory.createTextField(new MultiSelectionSingleSelection(selectionHolder)),3);

        builder.border(Borders.DIALOG);
        return builder.getPanel();
    }

    private void initComponents() {
        primaryList = new JList();
        primaryList.setModel(multiSelection.getList());
        primaryList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        primaryList.setSelectionModel(new MultiListSelectionAdapter(multiSelection));
        secondaryList = new JList();
        secondaryList.setModel(selectionHolder);
        combo = new JComboBox();
        combo.setModel(new ComboBoxAdapter(comboSelection));

    }

    private void initModel() {

        multiSelection = new MultiSelectionInList(new String[]{"Chris", "Felix", "Hannah", "Eva", "X", "Y"});
        selectionHolder = multiSelection.getSelection();
        comboSelection=new SelectionInList((ListModel) selectionHolder);
        
    }

}
