package ws.leipold.felix.jgoodies.polyadapter;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.binding.beans.PropertyAdapter;


import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;


/**
 * @author Felix Leipold
 * Date: 09.10.2005
 * Time: 21:54:49
 */
public class FigureExample {
    private SelectionInList figureSelection;
    private CardLayout detailPanelLayout;
    private JPanel detailPanel;
    private PresentationModel circleModel;
    private PresentationModel rectangleModel;
    private ValueModel activeChannel;


    public FigureExample() {
        initModel();
    }

    void initModel() {
        figureSelection = new SelectionInList(new Object[]{new Circle(10), new Rectangle(20, 10), new Circle(3)});
        TypeCaseModel typeCase = new TypeCaseModel(figureSelection.getSelectionHolder());

        activeChannel= new PropertyAdapter(typeCase,"activeChannelID",true);
        circleModel = new PresentationModel(typeCase.getTypeChannel(Circle.class, "circle"));
        rectangleModel = new PresentationModel(typeCase.getTypeChannel(Rectangle.class, "rectangle"));
    }

    JPanel buildPanel() {
        FormLayout layout = new FormLayout("fill:150dlu", "fill:100dlu,4dlu,p");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.add(buildList(), cc.xy(1, 1));
        builder.add(buildDetailPanel(), cc.xy(1, 3));
        builder.setBorder(Borders.DIALOG_BORDER);
        return builder.getPanel();
    }

    private JPanel buildDetailPanel() {
        detailPanel = new JPanel();
        detailPanelLayout = new CardLayout();
        detailPanel.setLayout(detailPanelLayout);

        detailPanel.add(buildCirclePanel(), "circle");
        detailPanel.add(buildRectanglePanel(), "rectangle");
        activeChannel.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                detailPanelLayout.show(detailPanel,(String) activeChannel.getValue());
            }
        });
        detailPanelLayout.show(detailPanel,(String) activeChannel.getValue());  
        return detailPanel;
    }

    private JPanel buildRectanglePanel() {
        FormLayout layout = new FormLayout("right:max(p;24dlu),4dlu,fill:100dlu:grow");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.append("Width", BasicComponentFactory.createIntegerField(rectangleModel.getModel(Rectangle.PROPERTYNAME_WIDTH)));
        builder.append("Height", BasicComponentFactory.createIntegerField(rectangleModel.getModel(Rectangle.PROPERTYNAME_HEIGHT)));
        return builder.getPanel();
    }

    private JPanel buildCirclePanel() {
        FormLayout layout = new FormLayout("right:max(p;24dlu),4dlu,fill:100dlu:grow");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.append("Radius", BasicComponentFactory.createIntegerField(circleModel.getModel(Circle.PROPERTYNAME_RADIUS)));
        return builder.getPanel();
    }

    private JComponent buildList() {
        return BasicComponentFactory.createList(figureSelection);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }

        JFrame frame = new JFrame();
        frame.setTitle("Binding Tutorial :: Figure Example");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JComponent panel = new FigureExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        //TutorialUtils.locateOnScreenCenter(frame);
        frame.setVisible(true);
    }
}

