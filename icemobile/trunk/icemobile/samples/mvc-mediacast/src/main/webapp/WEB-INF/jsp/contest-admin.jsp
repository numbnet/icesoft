<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE html >
<html>
    <%@ include file="/WEB-INF/jsp/contest-head.jsp"%>
<body>
    <mobi:pagePanel>
        <mobi:pagePanelHeader>
            <a id="backBtn" class="mobi-button mobi-button-default" style="float:left;line-height:20px;"
                href='<c:url value="/contest?p=upload&l=${layout}"/>'>Gallery</a>
            <span>Contest Admin</span>
        </mobi:pagePanelHeader>
        <mobi:pagePanelBody>
            <c:if test="${!admin}">
                <form:form id="adminForm" method="POST" htmlEscape="true" style="text-align:center;padding:20px;">
                     <input type="text" name="password"/>
                     <input type="submit" value="Submit"/>
                </form:form>
            </c:if>
            <c:if test="${admin}">
               <form:form id="galleryEditForm" method="POST" >
                    <mobi:outputList inset="false" id="galleryList">
                        <c:forEach var="m" items="${mediaService.mediaSortedByVotes}">
                            <mobi:outputListItem>
                                <img src='<c:url value="/resources/uploads/${m.smallPhoto.file.name}"/>' class="p"/>
                                <span class="desc"><c:out value="${m.description}"/></span>
                                <span class="vote" >${m.numberOfVotes} Votes</span>
                                X<input type="checkbox" name="delete" value="${m.id}"/>
                            </mobi:outputListItem>
                        </c:forEach>
                    </mobi:outputList>
                    <input type="submit" value="Submit"/>
                </form:form>
            </c:if>
        </mobi:pagePanelBody>
        <%@ include file="/WEB-INF/jsp/footer.jsp"%>
    </mobi:pagePanel>
</body>
</html>