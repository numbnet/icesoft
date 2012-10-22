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
        <mobi:smallView>
            <mobi:pagePanel>
                <mobi:pagePanelHeader><a id="menuLink" class="mobi-button mobi-button-default ui-icon ui-icon-home ui-icon-shadow"
                        href='<c:url value="/"/>'>&#160;</a>
                        <span>ICEmobile Showcase</span>
                </mobi:pagePanelHeader>
                <mobi:pagePanelBody>
                    <%@ include file="/WEB-INF/views/inc/menu.jsp" %>
                </mobi:pagePanelBody>
            </mobi:pagePanel>
        </mobi:smallView>
    </div>
    <script type="text/javascript">
        MvcUtil.enhanceAllLinks(".ajaxzone","body");
        window.scrollTo(0, 0);
    </script>
<c:if test="${!ajaxRequest}">
    </body>
    </html>
</c:if>
