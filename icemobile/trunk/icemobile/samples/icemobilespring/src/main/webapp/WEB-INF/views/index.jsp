<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<c:if test="${!ajaxRequest}">
<html>
<jsp:include page="./inc/head.jsp"/>
<body>
</c:if>
    <div class="ajaxzone">
        <mobi:pagePanel>
            <mobi:smallView>
                <mobi:pagePanelHeader>
                    <a href="menu" id="menuLink" class="mobi-button  mobi-button-default">&#9776;</a>
                    ICEmobile Showcase
                </mobi:pagePanelHeader>
                <mobi:pagePanelBody>
                    <%@ include file="/WEB-INF/views/inc/splash-content.jsp" %>
                </mobi:pagePanelBody>
            </mobi:smallView>
            <mobi:largeView>
                 <mobi:pagePanelHeader>ICEmobile Spring MVC Showcase</mobi:pagePanelHeader>
                <mobi:pagePanelBody>
                    <div id="left">
                        <%@ include file="/WEB-INF/views/inc/menu.jsp" %>
                    </div>
                    <div id="right">
                        <%@ include file="/WEB-INF/views/inc/splash-content.jsp" %>
                    </div>
                    <script type="text/javascript">
                    addEqualizeElementHeightsAfterResizeListener('left','right');
                    </script>
                </mobi:pagePanelBody>
            </mobi:largeView>
        </mobi:pagePanel>
    </div>
    <script type="text/javascript">
        MvcUtil.enhanceLink("#menuLink","body");
    </script>
<c:if test="${!ajaxRequest}">
</body>
</html>
</c:if>