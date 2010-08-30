package org.icefaces.UIDataText;

import org.icefaces.component.commandlink.CommandLink;
import org.icefaces.UIDataText.StyleHolder;

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
@ManagedBean (name="columnBean")
@SessionScoped
public class ColumnBean implements Serializable {

	private String label = "ActionListener";

    private String myStyle;

    private String STYLES[] = {
            "text-decoration: overline;",
            "text-decoration: line-through;",
            "text-decoration: underline;",
            "text-decoration: blink;",
            "text-decoration: none;"
     };

    List styleData = new ArrayList();
    private int styleIndex;

    public ColumnBean(){
        for (int idx = 0; idx < STYLES.length; idx ++ ) {
            System.out.println("Adding new style: " + STYLES[idx]);
            styleData.add( new StyleHolder( STYLES[idx] ) );
        }
   }
    
    public void incrementStyleIndex(ActionEvent e) {

        UIComponent uic = e.getComponent();
        CommandLink cl = (CommandLink) uic;
        //cl.setStyle("black_line");
        int index = styleIndex++ % styleData.size();
        String newStyle = styleData.get( index ).toString();

        System.out.println("--> Setting style to: " + newStyle + " at " + index);
        cl.setStyle( newStyle );
    }


    public String getMyStyle() {
        return myStyle;
    }

    public void setMyStyle(String style ) {
        myStyle = style;
    }

    public String getStyleClass() {
        return styleData.get(0).toString();
    }

    public List getStyleHolderData() {
        return styleData;
    }
}

