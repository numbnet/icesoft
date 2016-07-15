/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icefaces.mobile.layout;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class ContentStackMenu2 implements Serializable{
    
    private String selectedPane = "menu";

    public String getSelectedPane() {
        return selectedPane;
    }

    public void setSelectedPane(String selectedPane) {
        this.selectedPane = selectedPane;
    }
    
    public void goToMenu(ActionEvent evt){
        System.out.println("goToMenu()");
        this.selectedPane = "menu";
    }
    public void goToPage1(ActionEvent evt){
        System.out.println("go to page 1");
        this.selectedPane = "page1" ;
    }
    public void goToPage2(ActionEvent ae){
        System.out.println("go to page 2");
        this.selectedPane = "page2" ;
    }

}
