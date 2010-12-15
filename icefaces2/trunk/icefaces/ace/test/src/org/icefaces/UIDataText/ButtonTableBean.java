package showcase.test.src.org.icefaces.UIDataText;

import org.icefaces.component.pushbutton.PushButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.component.UIComponent;


/**
 * The ColumnsBean object generates data for the ice:columns example.
 *
 * @since 1.5
 */
@ManagedBean (name="buttonTableBean")
@ViewScoped
public class ButtonTableBean implements Serializable {

    private String Labels[] = {
            "First Label",
            "Second Label",
            "Third Label",
            "Fourth Label",
            "Fifth Label",
            "Sixth Label"
    };

    List labelData = new ArrayList();
    List labelData2 = new ArrayList();
    List labelData3 = new ArrayList();

    private int labelIndex;
    private int labelIndex2;
    private int labelIndex3;

    public ButtonTableBean(){
        for (int idx = 0; idx < Labels.length; idx ++ ) {
            labelData.add( new LabelHolder( Labels[idx] ) );
            labelData2.add( new LabelHolder( Labels[idx] ) );
            labelData3.add( new LabelHolder( Labels[idx] ) );
        }
    }

    public void incrementLabelIndex(ActionEvent e) {

        UIComponent uic = e.getComponent();
        PushButton cl = (PushButton) uic;
        String oldStyle = cl.getLabel();

        String newLabel = findNewLabel(oldStyle, labelIndex);
        cl.setLabel( newLabel );
        System.out.println("------ Label DATA CHANGED -------");
        dump(labelData);
    }

    private String findNewLabel( String oldLabel, int LabelIndex) {
        String newLabel;
        int index = LabelIndex++ % labelData.size();
        int idx = 0;
        while ((newLabel = labelData.get( index ).toString()).equals(
                oldLabel)) {
            index = labelIndex++ % labelData.size();
            idx++;
            if (idx > 5) {
                return oldLabel;
            }
        }
        return newLabel;
    }
//    public void incrementLabelIndex2(ActionEvent e) {
//
//        UIComponent uic = e.getComponent();
//        PushButton cl = (PushButton) uic;
//        int index = labelIndex2++ % LabelData2.size();
//        String newLabel = labelData2.get( index ).toString();
//        cl.setLabel( newLabel );
//        System.out.println("------ Label DATA 2-------");
//        dump(labelData2);
//    }
//
//    public void incrementLabelIndex3(ActionEvent e) {
//
//        UIComponent uic = e.getComponent();
//        PushButton cl = (PushButton) uic;
//        int index = labelIndex3++ % LabelData3.size();
//        String newLabel = labelData3.get( index ).toString();
//        cl.setLabel( newLabel );
//        System.out.println("------ Label DATA 3  -------");
//        dump(LabelData3);
//    }

    public List getLabelHolderData() {
        return labelData;
    }

    public List getLabelHolderData2() {
        return labelData2;
    }

    public List getLabelHolderData3() {
        return labelData3;
    }

    public void dump(List al) {
        for (int idx = 0; idx < al.size(); idx ++  ) {
            System.out.println("   " + al.get(idx) + " idx: " + idx);
        }
    }
}