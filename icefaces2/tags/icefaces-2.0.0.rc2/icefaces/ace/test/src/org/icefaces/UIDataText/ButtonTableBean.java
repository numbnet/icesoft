package showcase.test.src.org.icefaces.UIDataText;

import org.icefaces.component.pushbutton.PushButton;
import org.icefaces.component.linkbutton.LinkButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.component.UIComponent;


/**
 * Label service for handling dynamic labels
 * @since 1.5
 */
@ManagedBean (name="labelMasterBean")
@ViewScoped
public class ButtonTableBean implements Serializable {

    private String Labels[] = {
            "On the one hand...",
            "But, on the other hand..." };

    List linkLabelData = new ArrayList();
    List buttonLabelData = new ArrayList();


    public ButtonTableBean(){
        for (int idx = 0; idx < 4; idx ++ ) {
            linkLabelData.add( new LabelHolder( Labels[0] ) );
            buttonLabelData.add( new LabelHolder( Labels[0] ) );
        }
    }

    public void alternateButtonLabel(ActionEvent e) {

        UIComponent uic = e.getComponent();
        PushButton pb = (PushButton) uic;
        String oldLabel = pb.getLabel();
        String newLabel = findNewLabel(oldLabel);
        pb.setLabel( newLabel );
        System.out.println("------ Button: " + pb.getClientId() + " Label Changed to: " + newLabel + " -------");
    }

    public void alternateLinkLabel(ActionEvent e) {

        UIComponent uic = e.getComponent();
        LinkButton cl = (LinkButton) uic;
        String oldLabel = (String) cl.getValue();
        String newLabel = findNewLabel(oldLabel);
        cl.setValue( newLabel );
        System.out.println("------ Link: " + cl.getClientId() + " Label Changed to: " + newLabel + " -------");
    }


    private String findNewLabel( String oldLabel ) {
        String defLabel = "oops fall through case!";
        for (int idx = 0; idx < Labels.length; idx ++ ) {
            if (!Labels[idx].equals(oldLabel) ) {
                return Labels[idx];
            }
        } 
        return defLabel;
    }
    
    public List getLinkLabelData() {
        return linkLabelData;
    }

    public List getButtonLabelData() {
           return buttonLabelData;
    }
}