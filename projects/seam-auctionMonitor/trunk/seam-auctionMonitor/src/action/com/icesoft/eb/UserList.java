package com.icesoft.eb;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.RequestParameter;
import org.jboss.seam.framework.EntityQuery;
import javax.faces.model.SelectItem;
import java.util.List;
import java.util.Arrays;
import javax.faces.event.ActionEvent;
import com.icesoft.faces.component.accordion.PanelAccordion;

@Name("userList")
public class UserList extends EntityQuery {
	private String[] selectedFields;
	private boolean openStatus = true;
	private static final String[] RESTRICTIONS = {
			"lower(user.username) like concat(lower(#{userList.user.username}),'%')",
			"lower(user.password) like concat(lower(#{userList.user.password}),'%')",
			"lower(user.name) like concat(lower(#{userList.user.name}),'%')",
			"lower(user.role) like concat(lower(#{userList.user.role}),'%')",};

	/* list of string fields for search */
	private static final SelectItem[] FIELDS = new SelectItem[]{
			new SelectItem("username"), new SelectItem("password"),
			new SelectItem("name"), new SelectItem("role"),};

	private User user = new User();

	@Override
	public String getEjbql() {
		return "select bidder from User bidder";
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

	public User getUser() {
		return user;
	}

	@Override
	public List<String> getRestrictions() {
		return Arrays.asList(RESTRICTIONS);
	}
	public void setSelectedFields(String[] selectedFields) {
		this.selectedFields = selectedFields;
	}

	public String[] getSelectedFields() {
		return this.selectedFields;
	}

	public SelectItem[] getFieldsList() {
		return FIELDS;
	}

	public boolean getUsernameSelect() {
		if (selectedFields != null) {
			for (int i = 0; i < selectedFields.length; i++) {
				if (selectedFields[i].equalsIgnoreCase("username"))
					return true;
			}
		}
		return false;
	}
	public boolean getPasswordSelect() {
		if (selectedFields != null) {
			for (int i = 0; i < selectedFields.length; i++) {
				if (selectedFields[i].equalsIgnoreCase("password"))
					return true;
			}
		}
		return false;
	}
	public boolean getNameSelect() {
		if (selectedFields != null) {
			for (int i = 0; i < selectedFields.length; i++) {
				if (selectedFields[i].equalsIgnoreCase("name"))
					return true;
			}
		}
		return false;
	}
	public boolean getRoleSelect() {
		if (selectedFields != null) {
			for (int i = 0; i < selectedFields.length; i++) {
				if (selectedFields[i].equalsIgnoreCase("role"))
					return true;
			}
		}
		return false;
	}

	public void toggle(ActionEvent event) {
		PanelAccordion component1 = (PanelAccordion) event.getSource();
		openStatus = component1.getExpanded().booleanValue();
	}

	public void setOpenStatus(boolean input) {
		this.openStatus = input;
	}

	public boolean getOpenStatus() {
		return openStatus;
	}

}
