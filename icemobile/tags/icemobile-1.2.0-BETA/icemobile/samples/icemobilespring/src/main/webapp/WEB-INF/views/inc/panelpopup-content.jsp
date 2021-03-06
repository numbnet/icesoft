<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<div class="panelContent">

	<c:if test="${viewSize eq 'large'}">
		<h3>Panel Popup</h3>
	</c:if>

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow>
			The panelPopup is a container tag that renders a popup panel that 
			hovers on top of a web page. Click either the "Popup" or "Auto-center Popup" buttons
			to display a popup.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<form:form id="panelform" method="POST" cssClass="form">
		<mobi:fieldsetGroup id="groupOne">
			<mobi:fieldsetRow styleClass="mobi-center">
				<input type="button" value="Popup"
					onclick="ice.mobi.panelPopup.openClient('popup');"
					class="mobi-button mobi-button-important" />
				<input type="button" value="Auto-center Popup"
					onclick="ice.mobi.panelPopup.openClient('autoCenterPopup');"
					class="mobi-button mobi-button-important" />
			</mobi:fieldsetRow>
		</mobi:fieldsetGroup>

		<mobi:panelPopup id="popup" width="260"
			headerText="Group of popup items" autoCenter="false"
			style="top:100px;${viewSize eq 'large' ? 'left:40%' : 'left:50px'}">
			<mobi:outputList inset="false" id="itemList">
				<mobi:outputListItem group="true">
                Panel Popup Links (select one)
            </mobi:outputListItem>
				<mobi:outputListItem>
					<a href="#">System Update</a>
				</mobi:outputListItem>
				<mobi:outputListItem>
					<a href="#">Status</a>
				</mobi:outputListItem>
				<mobi:outputListItem>
					<a href="#">Battery use</a>
				</mobi:outputListItem>
				<mobi:outputListItem>
					<a href="#">Device Information</a>
				</mobi:outputListItem>
				<mobi:outputListItem>
					<a onclick="ice.mobi.panelPopup.closeClient('popup');"> This
						will be a dismissive link </a>
				</mobi:outputListItem>
			</mobi:outputList>
			<div
				style="text-align: right; background-color: rgba(255, 255, 255, 0.5)">
				<input type="button" value="Close"
					class="mobi-button mobi-button-important"
					onclick="ice.mobi.panelPopup.closeClient('popup');" />
			</div>
		</mobi:panelPopup>


		<mobi:panelPopup id="autoCenterPopup" width="260"
			headerText="Group of popup items" autoCenter="true">
			<mobi:outputList inset="false" id="itemList">
				<mobi:outputListItem group="true">
                    Panel Popup Links (select one)
                </mobi:outputListItem>
				<mobi:outputListItem>
					<a href="#">System Update</a>
				</mobi:outputListItem>
				<mobi:outputListItem>
					<a href="#">Status</a>
				</mobi:outputListItem>
				<mobi:outputListItem>
					<a href="#">Battery use</a>
				</mobi:outputListItem>
				<mobi:outputListItem>
					<a href="#">Device Information</a>
				</mobi:outputListItem>
				<mobi:outputListItem>
					<a onclick="ice.mobi.panelPopup.closeClient('autoCenterPopup');">
						This will be a dismissive link </a>
				</mobi:outputListItem>
			</mobi:outputList>
			<div
				style="text-align: right; background-color: rgba(255, 255, 255, 0.5)">
				<input type="button" value="Close"
					class="mobi-button mobi-button-important"
					onclick="ice.mobi.panelPopup.closeClient('autoCenterPopup');" />
			</div>
		</mobi:panelPopup>

		<mobi:fieldsetGroup>
			<mobi:fieldsetRow group="true">
				Description
			</mobi:fieldsetRow>
			<mobi:fieldsetRow>
				The panelPopup is a container tag that renders a
				modalpopup panel that hovers on top of a web page. The child
				elements of the tag are inserted into the popup panel.
			</mobi:fieldsetRow>
		</mobi:fieldsetGroup>

	</form:form>

</div>
