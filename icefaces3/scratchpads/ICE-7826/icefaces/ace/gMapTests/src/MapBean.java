/**
 * Created with IntelliJ IDEA.
 * User: BrennanMcKinney
 * Date: 07/08/12
 * Time: 1:10 PM
 * To change this template use File | Settings | File Templates.
 */
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.validator.ValidatorException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ManagedBean (name="mapBean")
@SessionScoped
public class MapBean implements Serializable {

    private boolean checked = false;
    private String label = "checkbox";
    private String tabindex="10";
    private boolean rendered;
    private boolean immediate;
    private boolean disabled;
    private String style="background-color:lightgreen";
    private String styleClass="checkbttnStyle";
    private String value2="";
    private String labelPosition;
    private int sclassCount=0;
    private String latitude = "0";
    private String longitude = "0";

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }



    private SelectItem[] POSITIONS = new SelectItem[]{
            new SelectItem("on"),
            new SelectItem("left"),
    };


    public SelectItem[] getPositions() {
        return this.POSITIONS;
    }
    public void setPositions(SelectItem[] setPositions) {
        this.POSITIONS = setPositions;
    }
    public MapBean() { }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabelPosition() {
        // System.out.println("labelPosition is: "+labelPosition);
        return labelPosition;
    }

    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }


    public String getTabindex() {
        return tabindex;
    }

    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }
    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }
    public boolean isImmediate() {
        System.out.println("******");
        System.out.println(FacesContext.getCurrentInstance().getCurrentPhaseId());
        System.out.println("******");
        return immediate;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getStyle() {
        System.out.println("style: "+style);
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public void init() {
        style="background-color:lightgreen";
        styleClass="checkbttnStyle";
    }

    public void next(ActionEvent e) {
        init();
        switch (sclassCount++) {
            case 0: style = "background-color:red";
                System.out.println("style: "+style);
                System.out.println("styleClass: "+styleClass);
                break;
            case 1: styleClass = "MyStyleClasss";
                System.out.println("style: "+style);
                System.out.println("styleClass: "+styleClass);
                sclassCount=0;
                break;
        }
    }

}
