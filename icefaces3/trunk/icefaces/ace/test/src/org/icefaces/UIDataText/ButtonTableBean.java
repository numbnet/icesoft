/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package showcase.test.src.org.icefaces.UIDataText;

import org.icefaces.ace.component.pushbutton.PushButton;
import org.icefaces.ace.component.linkbutton.LinkButton;

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

    private String red_box = "red_box";
    private String blue_box = "blue_box"; 

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

     public void alternateButtonStyleClass(ActionEvent e) {

        UIComponent uic = e.getComponent();
        PushButton pb = (PushButton) uic;
        String oldStyle = pb.getStyleClass();
         if (red_box.equals(oldStyle)) {
             pb.setStyleClass(blue_box);
             System.out.println("------ Button: " + pb.getClientId() + " New style to: blueBox  -------");
         } else {
             pb.setStyleClass(red_box);
             System.out.println("------ Button: " + pb.getClientId() + " New style to: redBox  -------");
         }
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