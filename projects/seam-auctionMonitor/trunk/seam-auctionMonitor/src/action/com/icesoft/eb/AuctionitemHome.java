package com.icesoft.eb;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import javax.faces.event.ActionEvent;
import com.icesoft.faces.component.accordion.PanelAccordion;

@Name("auctionitemHome")
public class AuctionitemHome extends EntityHome<Auctionitem> {
	private boolean openStatus = true;

	public void setAuctionitemItemId(Long id) {
		setId(id);
	}

	public Long getAuctionitemItemId() {
		return (Long) getId();
	}

	@Override
	protected Auctionitem createInstance() {
		Auctionitem auctionitem = new Auctionitem();
		return auctionitem;
	}

	public void wire() {
	}

	public boolean isWired() {
		return true;
	}

	public Auctionitem getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
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
