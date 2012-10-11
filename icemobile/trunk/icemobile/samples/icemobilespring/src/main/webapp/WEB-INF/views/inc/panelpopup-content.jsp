<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<div class="panelContent">

    <form:form id="panelform" method="POST"  modelAttribute="panelPopupBean" cssClass="cleanform">
      <input type="button" value="Open Popup" onclick="ice.mobi.panelpopup.open('panelConfirmation');" />

        <mobi:panelPopup id="panelConfirmation"
                             style="width:300px"
                             title="Group of popup items"
                             autocenter="true"
                             visible="${panelPopupBean.visible}">

                <mobi:outputList inset="false" id="itemList" >
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
                        <a onclick="ice.mobi.panelpopup.close('panelConfirmation');">
                           This will be a dismissive link </a>
                    </mobi:outputListItem>
                </mobi:outputList>
                <input type="button" value="Close via markup"
                onclick="ice.mobi.panelpopup.close('panelConfirmation');" />
            </mobi:panelPopup>

    </form:form>


    <h4> PanelPopup example with auto generated close button </h4>

    <form:form id="panelTwoform" method="POST"  modelAttribute="panelPopupBean" cssClass="cleanform">
      <input type="button" value="Open Popup (with client dismiss)" onclick="ice.mobi.panelpopup.open('panelClient');" />

        <mobi:panelPopup id="panelClient"
                            name="propertyTwo"
                             style="width:300px"
                             title="Group of popup items"
                             autocenter="true"
                             autoCloseButton="true"
                             closeButtonLabel="Close"
                             visible="false">

                <mobi:outputList inset="false" id="seconditemList" >
                    <mobi:outputListItem group="true">
                        Panel Popup Links (select one)
                    </mobi:outputListItem>
                    <mobi:outputListItem>
                         <span onclick="ice.mobi.panelpopup.close('panelClient');">
                           System Update </span>
                    </mobi:outputListItem>
                    <mobi:outputListItem>
                         <span onclick="ice.mobi.panelpopup.close('panelClient');">
                           Status </span>
                    </mobi:outputListItem>
                    <mobi:outputListItem>
                         <span onclick="ice.mobi.panelpopup.close('panelClient');">
                           Battery Use </span>
                    </mobi:outputListItem>
                    <mobi:outputListItem>
                         <span onclick="ice.mobi.panelpopup.close('panelClient');">
                           Device Information </span>
                    </mobi:outputListItem>
                    <mobi:outputListItem>
                        <span onclick="ice.mobi.panelpopup.close('panelClient');">
                                       Device Categories
                            </span>
                    </mobi:outputListItem>
                </mobi:outputList>
         </mobi:panelPopup>
    </form:form>
    <script type="text/javascript">

        $(document).ready(function() {
            $("#panelform").submit(function() {
                if (window.ice && ice.upload)  {
                    window.ice.handleResponse = function(data)  {
                        $("#panelContent").replaceWith(unescape(data));
                        var msgElem = $("#message");
                        if( msgElem.length > 0 ){
                            $('html, body').animate({ scrollTop:msgElem.offset().top }, 500);
                        }
                    }
                    ice.upload($(this).attr("id"));
                    return false;  
                }

                var formData = new FormData(this);

                $.ajax({
                    url: $(this).attr("action"),
                    data: formData,
                    cache: false,
                    contentType: false,
                    processData: false,
                    type: 'POST',
                    success: function(html) {
                        $("#panelContent").replaceWith(html);
                        var msgElem = $("#message");
                        if( msgElem.length > 0 ){
                            $('html, body').animate({ scrollTop:msgElem.offset().top }, 500);
                        }
                    }
                });

                return false;  
            });         
        });

    </script>
    
</div>
