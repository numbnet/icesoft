/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.demo.messages;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

@ManagedBean(name = "facesMessagesBean")
@SessionScoped
public class FacesMessagesBean implements Serializable {
    private String pg_reg_txt;
    private String pg_fpg_txt;
    private String pg_ff_txt;

    private List list_reg;
    private List list_fpg;
    private List list_ff;


    public FacesMessagesBean() {
        list_reg = buildEmptyEntryList(3);
        list_fpg = buildEmptyEntryList(3);
        list_ff = buildEmptyEntryList(3);
    }

    private static List buildEmptyEntryList(int size) {
        List list = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            list.add(new Entry());
        }
        return list;
    }

    public String getPg_reg_txt() { return pg_reg_txt; }
    public void setPg_reg_txt(String txt) { pg_reg_txt = txt; }

    public String getPg_fpg_txt() { return pg_fpg_txt; }
    public void setPg_fpg_txt(String txt) { pg_fpg_txt = txt; }

    public String getPg_ff_txt() { return pg_ff_txt; }
    public void setPg_ff_txt(String txt) { pg_ff_txt = txt; }

    public List getList_reg() { return list_reg; }
    public List getList_fpg() { return list_fpg; }
    public List getList_ff() { return list_ff; }


    public void hi(ActionEvent e) {
        System.out.println("HI");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("HI"));
    }

    public void bye(ActionEvent e) {
        System.out.println("BYE");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("BYE"));
    }

    public void test(ActionEvent e) {
        System.out.println("TEST");
    }

    public static class Entry {
        private String val;
        public String getVal() { return val; }
        public void setVal(String v) { val = v; }
    }
}
