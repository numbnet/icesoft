<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="accordionform" method="POST" modelAttribute="accordionBean">

    <h2>Accordion Component</h2>

     <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            The accordion component shows one active content pane at time.
            Non-active content panes are represented as collapsed views.
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    
    <h3>Accordion with autoHeight</h3>

    <mobi:accordion id="basicAccordion" selectedId="${accordionBean.selectedId1}">
        <mobi:accordionPane title="ICE Sailor" id="accordionPane1" style="background-color:#00ADFF">
            <%@ include file="inc-icesailor.jsp" %>
        </mobi:accordionPane>
        <mobi:accordionPane title="ICE Breaker" id="accordionPane2" style="background-color:#C0F">
             <%@ include file="inc-icebreaker.jsp" %>
        </mobi:accordionPane>
        <mobi:accordionPane title="ICE Skate" id="accordionPane3" style="background-color:#F0C82A">
            <%@ include file="inc-iceskate.jsp" %>
        </mobi:accordionPane>
    </mobi:accordion>
    
    <h3 style="margin-top:30px;">Accordion with fixedHeight</h3>
    
    <mobi:accordion id="accordionNoAutoHeight" selectedId="${accordionBean.selectedId2}"
        autoHeight="false" fixedHeight="200px">
        <mobi:accordionPane title="ICE Sailor" id="accordionPane4" style="background-color:#00ADFF">
            <%@ include file="inc-icesailor.jsp" %>
        </mobi:accordionPane>
        <mobi:accordionPane title="ICE Breaker" id="accordionPane5" style="background-color:#C0F">
             <%@ include file="inc-icebreaker.jsp" %>
        </mobi:accordionPane>
        <mobi:accordionPane title="ICE Skate" id="accordionPane6" style="background-color:#F0C82A">
            <%@ include file="inc-iceskate.jsp" %>
        </mobi:accordionPane>
    </mobi:accordion>
    
</form:form>
<script type="text/javascript">
    MvcUtil.enhanceForm("#accordionform");
</script>