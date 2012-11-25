<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<div class="panelContent">

    <mobi:fieldsetGroup id="groupOne">
        <mobi:fieldsetRow>
            <form:form id="panelform" method="POST"  modelAttribute="panelPopupBean" cssClass="cleanform">
              <input type="button" value="Open Popup" onclick="ice.mobi.panelPopup.openClient('panelConfirmation');" />

                <mobi:panelPopup id="panelConfirmation"
                                     width = "200"
                                     height = "400"
                                     headerText="Group of popup items"
                                     autoCenter="true"
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
                                <a onclick="ice.mobi.panelPopup.closeClient('panelConfirmation');">
                                   This will be a dismissive link </a>
                            </mobi:outputListItem>
                        </mobi:outputList>
                        <input type="button" value="Close via markup"
                        onclick="ice.mobi.panelPopup.closeClient('panelConfirmation');" />
                    </mobi:panelPopup>

                <h3>PanelPopup Settings</h3>


        </form:form>
     </mobi:fieldsetRow>
  </mobi:fieldsetGroup>

   <!-- <script type="text/javascript">

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

    </script>  -->
    
</div>
