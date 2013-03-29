<%--
  ~ Copyright 2004-2012 ICEsoft Technologies Canada Corp.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the
  ~ License. You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an "AS
  ~ IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
  ~ express or implied. See the License for the specific language
  ~ governing permissions and limitations under the License.
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<%@ page session="false" trimDirectiveWhitespaces="true"%>
<c:if test="${!ajaxRequest}">
<!doctype html>
<html>
<jsp:include page="./inc/head.jsp"/>
<body>
</c:if>

    <mobi:smallView>
        <div class="ajaxzone">
            <mobi:pagePanel>
                <mobi:pagePanelHeader>
                    <a href="menu" id="menuLink" class="mobi-button ui-icon ui-icon-menu ui-icon-shadow"></a>
                    ${param.demoTitle}
                </mobi:pagePanelHeader>
                <mobi:pagePanelBody noFooter="true">
                    <jsp:include page="${param.demoPath}" />
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
                        <a id="menuLink" class="mobi-button ui-icon ui-icon-home ui-icon-shadow"
                        href='<c:url value="/"/>'> </a>
                        ICEmobile Spring MVC Showcase
                    </mobi:pagePanelHeader>
                    <mobi:pagePanelBody noFooter="true">
                        <mobi:splitPane id="sp" scrollable="true" columnDivider="30">
                            <mobi:fragment name="left">
                                <%@ include file="/WEB-INF/views/inc/menu.jsp" %>
                            </mobi:fragment>
                            <mobi:fragment name="right">
                                <div class="ajaxzone">
                                    <jsp:include page="${param.demoPath}" />
                                </div>
                            </mobi:fragment>
                        </mobi:splitPane>
                    </mobi:pagePanelBody>
                 </mobi:pagePanel>
             </c:when>
             <c:otherwise>
                <jsp:include page="${param.demoPath}" />
             </c:otherwise>
         </c:choose>
    </mobi:largeView>
    
<c:if test="${!ajaxRequest}">
    <script type="text/javascript">
        MvcUtil.enhanceAllLinks("#sp_left",".ajaxzone");
      //precache commonly used images
        var preloaded = new Array();
        preloaded[0] = document.createElement('img');
        preloaded[0].setAttribute('src','./resources/auction/icebreaker.png');
        preloaded[1] = document.createElement('img');
        preloaded[1].setAttribute('src','./resources/auction/icecar.png');
        preloaded[2] = document.createElement('img');
        preloaded[2].setAttribute('src','./resources/auction/icesailer.png');
        preloaded[3] = document.createElement('img');
        preloaded[3].setAttribute('src','./resources/auction/iceskate.png');
    </script>
</body>
</html>
</c:if>