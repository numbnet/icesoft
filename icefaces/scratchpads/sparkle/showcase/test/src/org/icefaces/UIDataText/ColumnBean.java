package org.icefaces.UIDataText;

import org.icefaces.component.linkbutton.LinkButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.component.UIComponent;


/**
 * The ColumnsBean object generates data for the ice:columns example.
 *
 * @since 1.5
 */
@ManagedBean (name="styleMasterBean")
@SessionScoped
public class ColumnBean implements Serializable {

    private String STYLES[] = {
            "text-decoration: overline;",
            "text-decoration: line-through;",
            "text-decoration: underline;",
            "text-decoration: blink;",
            "text-decoration: none;"
     };

    List styleData = new ArrayList();
    List styleData2 = new ArrayList();
    List styleData3 = new ArrayList();
    
    private int styleIndex;
    private int styleIndex2;
    private int styleIndex3;

    public ColumnBean(){
        for (int idx = 0; idx < STYLES.length; idx ++ ) {
            System.out.println("Adding new style: " + STYLES[idx]);
            styleData.add( new StyleHolder( STYLES[idx] ) );
            styleData2.add( new StyleHolder( STYLES[idx] ) );
            styleData3.add( new StyleHolder( STYLES[idx] ) );
        }
   }
    
    public void incrementStyleIndex(ActionEvent e) {

        UIComponent uic = e.getComponent();
        LinkButton cl = (LinkButton) uic;
        int index = styleIndex++ % styleData.size();
        String newStyle = styleData.get( index ).toString();
        cl.setStyle( newStyle );
        System.out.println("------ STYLE DATA -------");
        dump(styleData);
    }

    public void incrementStyleIndex2(ActionEvent e) {

        UIComponent uic = e.getComponent();
        LinkButton cl = (LinkButton) uic;
        int index = styleIndex2++ % styleData2.size();
        String newStyle = styleData2.get( index ).toString();
        cl.setStyle( newStyle );
        System.out.println("------ STYLE DATA 2-------");
        dump(styleData2); 
    }

    public void incrementStyleIndex3(ActionEvent e) {

        UIComponent uic = e.getComponent();
        LinkButton cl = (LinkButton) uic;
        int index = styleIndex3++ % styleData3.size();
        String newStyle = styleData3.get( index ).toString();
        cl.setStyle( newStyle );
         System.out.println("------ STYLE DATA 3  -------");
        dump(styleData3); 
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

    public void dump(List al) {
        for (int idx = 0; idx < al.size(); idx ++  ) {
            System.out.println("   " + al.get(idx) + " idx: " + idx);  
        }
    }
}

