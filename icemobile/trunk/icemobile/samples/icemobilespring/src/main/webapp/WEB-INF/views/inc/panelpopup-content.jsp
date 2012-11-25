<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<div class="panelContent">
    <form:form id="panelform" method="POST" cssClass="form">
        <mobi:fieldsetGroup id="groupOne">
            <mobi:fieldsetRow styleClass="mobi-center">
                <input type="button" value="Open Popup" onclick="ice.mobi.panelPopup.openClient('popup');" 
                     class="mobi-button mobi-button-important"/>
                <input type="button" value="Open Auto-center Popup" onclick="ice.mobi.panelPopup.openClient('autoCenterPopup');" 
                     class="mobi-button mobi-button-important"/>    
         </mobi:fieldsetRow>
      </mobi:fieldsetGroup>
      
      <mobi:panelPopup id="popup"
             width = "260"
             headerText="Group of popup items"
             autoCenter="false">
        <mobi:outputList inset="false" id="itemList">
            <mobi:outputListItem group="true">
                Panel Popup Links (select one)
            </mobi:outputListItem>
            <mobi:outputListItem>
                System Update
            </mobi:outputListItem>
            <mobi:outputListItem>
                Status
            </mobi:outputListItem>
            <mobi:outputListItem>
                Battery use
            </mobi:outputListItem>
            <mobi:outputListItem>
                Device Information
            </mobi:outputListItem>
            <mobi:outputListItem>
                <a onclick="ice.mobi.panelPopup.closeClient('popup');">
                   This will be a dismissive link </a>
            </mobi:outputListItem>
        </mobi:outputList>
        <div class="text-align:right">
            <input type="button" value="Close via markup" class="mobi-button mobi-button-important"
                onclick="ice.mobi.panelPopup.closeClient('popup');"/>
        </div>
      </mobi:panelPopup>
      
      
      <mobi:panelPopup id="autoCenterPopup"
                     width = "260"
                     headerText="Group of popup items"
                     autoCenter="true">
            <mobi:outputList inset="false" id="itemList">
                <mobi:outputListItem group="true">
                    Panel Popup Links (select one)
                </mobi:outputListItem>
                <mobi:outputListItem>
                    System Update
                </mobi:outputListItem>
                <mobi:outputListItem>
                    Status
                </mobi:outputListItem>
                <mobi:outputListItem>
                    Battery use
                </mobi:outputListItem>
                <mobi:outputListItem>
                    Device Information
                </mobi:outputListItem>
                <mobi:outputListItem>
                    autoCenterPopup
                    <a onclick="ice.mobi.panelPopup.closeClient('autoCenterPopup');">
                       This will be a dismissive link </a>
                </mobi:outputListItem>
            </mobi:outputList>
            <div class="text-align:right">
                <input type="button" value="Close via markup" class="mobi-button mobi-button-important"
                    onclick="ice.mobi.panelPopup.closeClient('autoCenterPopup');"/>
            </div>
        </mobi:panelPopup>
    </form:form>
</div>
