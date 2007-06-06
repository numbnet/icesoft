package com.icesoft.eb;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.RequestParameter;
import org.jboss.seam.framework.EntityQuery;
import javax.faces.model.SelectItem;
import java.util.List;
import java.util.Arrays;
import javax.faces.event.ActionEvent;
import com.icesoft.faces.component.accordion.PanelAccordion;

@Name("auctionitemList")
public class AuctionitemList extends EntityQuery {
	private String[] selectedFields;
	private boolean openStatus = true;
	private static final String[] RESTRICTIONS = {
			"lower(auctionitem.currency) like concat(lower(#{auctionitemList.auctionitem.currency}),'%')",
			"lower(auctionitem.description) like concat(lower(#{auctionitemList.auctionitem.description}),'%')",
			"lower(auctionitem.imageFile) like concat(lower(#{auctionitemList.auctionitem.imageFile}),'%')",
			"lower(auctionitem.location) like concat(lower(#{auctionitemList.auctionitem.location}),'%')",
			"lower(auctionitem.seller) like concat(lower(#{auctionitemList.auctionitem.seller}),'%')",
			"lower(auctionitem.site) like concat(lower(#{auctionitemList.auctionitem.site}),'%')",
			"lower(auctionitem.title) like concat(lower(#{auctionitemList.auctionitem.title}),'%')",};

	/* list of string fields for search */
	private static final SelectItem[] FIELDS = new SelectItem[]{
			new SelectItem("currency"), new SelectItem("description"),
			new SelectItem("imageFile"), new SelectItem("location"),
			new SelectItem("seller"), new SelectItem("site"),
			new SelectItem("title"),};

	private Auctionitem auctionitem = new Auctionitem();

	@Override
	public String getEjbql() {
		return "select auctionitem from Auctionitem auctionitem";
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

	public Auctionitem getAuctionitem() {
		return auctionitem;
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

	public boolean getCurrencySelect() {
		if (selectedFields != null) {
			for (int i = 0; i < selectedFields.length; i++) {
				if (selectedFields[i].equalsIgnoreCase("currency"))
					return true;
			}
		}
		return false;
	}
	public boolean getDescriptionSelect() {
		if (selectedFields != null) {
			for (int i = 0; i < selectedFields.length; i++) {
				if (selectedFields[i].equalsIgnoreCase("description"))
					return true;
			}
		}
		return false;
	}
	public boolean getImageFileSelect() {
		if (selectedFields != null) {
			for (int i = 0; i < selectedFields.length; i++) {
				if (selectedFields[i].equalsIgnoreCase("imageFile"))
					return true;
			}
		}
		return false;
	}
	public boolean getLocationSelect() {
		if (selectedFields != null) {
			for (int i = 0; i < selectedFields.length; i++) {
				if (selectedFields[i].equalsIgnoreCase("location"))
					return true;
			}
		}
		return false;
	}
	public boolean getSellerSelect() {
		if (selectedFields != null) {
			for (int i = 0; i < selectedFields.length; i++) {
				if (selectedFields[i].equalsIgnoreCase("seller"))
					return true;
			}
		}
		return false;
	}
	public boolean getSiteSelect() {
		if (selectedFields != null) {
			for (int i = 0; i < selectedFields.length; i++) {
				if (selectedFields[i].equalsIgnoreCase("site"))
					return true;
			}
		}
		return false;
	}
	public boolean getTitleSelect() {
		if (selectedFields != null) {
			for (int i = 0; i < selectedFields.length; i++) {
				if (selectedFields[i].equalsIgnoreCase("title"))
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
