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
            "text-decoration: underline;",
    };

    List styleData = new ArrayList();
    List styleData2 = new ArrayList();
    List styleData3 = new ArrayList();

    public ColumnBean(){
        for (int idx = 0; idx < STYLES.length; idx ++ ) {
            System.out.println("Adding new style: " + STYLES[idx]);
            styleData.add( new StyleHolder( STYLES[idx] ) );
            styleData2.add( new StyleHolder( STYLES[idx] ) );
            styleData3.add( new StyleHolder( STYLES[idx] ) );
        }
    }

    public void alternateLinkStyle(ActionEvent e) {
        UIComponent uic = e.getComponent();
        LinkButton cl = (LinkButton) uic;
        String oldStyle = cl.getStyle();
        String newStyle = findNewStyle(oldStyle);
        cl.setStyle( newStyle );
        System.out.println("------ Link: " + cl.getClientId() + " Style Changed to: " + newStyle + " -------");
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

