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

import org.icefaces.ace.component.checkboxbutton.CheckboxButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.component.UIComponent;


@ManagedBean (name="checkDataBean")
@SessionScoped
public class CheckDataBean implements Serializable {

    private String STYLES[] = {
            "text-decoration: overline;",
            "text-decoration: underline;",
    };

    List styleData = new ArrayList();
    List styleData2 = new ArrayList();
    List styleData3 = new ArrayList();

    public CheckDataBean(){
        for (int idx = 0; idx < 4; idx ++ ) {
            styleData.add( new org.icefaces.UIDataText.StyleHolder( STYLES[0] ) );
            styleData2.add( new org.icefaces.UIDataText.StyleHolder( STYLES[0] ) );
            styleData3.add( new org.icefaces.UIDataText.StyleHolder( STYLES[0] ) );
        }
    }

    public void alternateCheckStyle(ValueChangeEvent e) {
        UIComponent uic = e.getComponent();
        CheckboxButton cl = (CheckboxButton) uic;
        String oldStyle = cl.getStyle();
        String newStyle = findNewStyle(oldStyle);
        cl.setStyle( newStyle );
        System.out.println("------ Checkbox: " + cl.getClientId() + " Style Changed to: " + newStyle + " -------");
    }

    private String findNewStyle( String oldStyle ) {
        String defStyle = "oops fall through case!";
        for (int idx = 0; idx < STYLES.length; idx ++ ) {
            if (!STYLES[idx].equals(oldStyle) ) {
                return STYLES[idx];
            }
        }
        return defStyle;
    }


    public List getStyleHolderData() {
        return styleData;
    }

    public List getStyleHolderData2() {
        return styleData2;
    }

    public List getStyleHolderData3() {
        return styleData3;
    }

}