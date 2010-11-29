package showcase.test.src.org.icefaces.button;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;


@ManagedBean (name="menubutton")
@ViewScoped
public class MenuButtonBean implements Serializable {
	

	private String menuItem1 = "no";
	private String menuItem2 = "no";
	private String menuItem3 = "no";

	
    public String menuItem1Action() {
        this.menuItem1 = "one";
        this.menuItem2 = "no";
        this.menuItem3 = "no";
        return this.menuItem1;
    }
    public String menuItem2Action() {
        System.out.println("MB: MenuItem2()  ");
        this.menuItem1 = "no";
        this.menuItem2 = "two";
        this.menuItem3 = "no";
        return this.menuItem2;
    }
    public void menuItem3ActionListener(ActionEvent e) {
        System.out.println("MB: MenuItem3() +e= "+e);
        this.menuItem1 = "no";
        this.menuItem2 = "no";
        this.menuItem3 = "three";
    }
	public String getMenuItem1() {
		return menuItem1;
	}
	public void setMenuItem1(String menuItem1) {
		this.menuItem1 = menuItem1;
	}
	public String getMenuItem2() {
		return menuItem2;
	}
	public void setMenuItem2(String menuItem2) {
		this.menuItem2 = menuItem2;
	}
	public String getMenuItem3() {
		return menuItem3;
	}
	public void setMenuItem3(String menuItem3) {
		this.menuItem3 = menuItem3;
	}  
}
