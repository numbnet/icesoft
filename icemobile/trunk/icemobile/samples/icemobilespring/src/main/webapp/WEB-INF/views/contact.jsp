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
 
    <mobi:smallView>
        <div class="ajaxzone">
            <mobi:pagePanel>
                <mobi:pagePanelHeader>
                    <a href="menu" id="menuLink" class="mobi-button  mobi-button-default">&#9776;</a>
                    ICEmobile - Contact List
                </mobi:pagePanelHeader>
                <mobi:pagePanelBody>
                    <%@ include file="/WEB-INF/views/inc/contact-content.jsp" %>
                </mobi:pagePanelBody>
            </mobi:pagePanel>
            <script type="text/javascript">
            MvcUtil.enhanceLink(document.getElementById('menuLink'),"body");
            </script>
         </div>
    </mobi:smallView>
    
    <mobi:largeView>
        <c:choose>
            <c:when test="${!ajaxRequest}">
                <mobi:pagePanel>
                    <mobi:pagePanelHeader>
                        <a id="backBtn" class="mobi-button mobi-button-default ui-icon ui-icon-home ui-icon-shadow"
                        href='<c:url value="/"/>'> </a>
                        ICEmobile Spring MVC Showcase
                    </mobi:pagePanelHeader>
                    <mobi:pagePanelBody>
                        <div id="left">
                            <%@ include file="/WEB-INF/views/inc/menu.jsp" %>
                        </div>
                        <div id="right">
                            <div class="ajaxzone">
                                <%@ include file="/WEB-INF/views/inc/contact-content.jsp" %>
                            </div>
                        </div>
                        <script type="text/javascript">
                        resizeElementHeight('left');
                        resizeElementHeight('right');
                        </script>
                    </mobi:pagePanelBody>
                 </mobi:pagePanel>
             </c:when>
             <c:otherwise>
                <%@ include file="/WEB-INF/views/inc/contact-content.jsp" %>
             </c:otherwise>
         </c:choose>
    </mobi:largeView>

    
<c:if test="${!ajaxRequest}">
    <script type="text/javascript">
        MvcUtil.enhanceAllLinks("#left",".ajaxzone");
        $('#menu a[href=contact]').css({backgroundColor:'#EFEFEF'});
    </script>
</body>
</html>
</c:if>