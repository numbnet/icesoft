<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
    <html>
    <jsp:include page="./inc/head.jsp"/>
    <body>
</c:if>
    <div class="ajaxzone">
        <mobi:pagePanel>
            <mobi:smallView>
                 <mobi:pagePanelHeader>ICEmobile Showcase</mobi:pagePanelHeader>
                    <mobi:pagePanelBody>
                        <%@ include file="/WEB-INF/views/inc/menu.jsp" %>
                    </mobi:pagePanelBody>
                </mobi:smallView>
        </mobi:pagePanel>
    </div>
<c:if test="${!ajaxRequest}">
    </body>
    </html>
</c:if>
