<%@ include file="/WEB-INF/jsp/include.jsp"%>
<div>
    <c:if test="${not empty carouselItems}">
        <form:form id="carouselForm" method="POST">
            <div style="border-top: 1px solid #999999;">
                <mobi:carousel id="recentMessagesCarousel"
                    selectedItem="0"
                    style="border-top: 1px solid #999999;">
                    <c:forEach items="${carouselItems}" var="item">
                        <mobi:carouselItem>
                             ${item}
                         </mobi:carouselItem>
                    </c:forEach>
                </mobi:carousel>
            </div>
        </form:form>
        <c:if test="${layout eq 't' }">
            <script type="text/javascript">
            $('#recentMessagesCarousel').find("a").each(function(){
            	$(this).click(function(){
            		updateViewerPanel(this.getAttribute("data-id"));
            	});
            	$(this).attr("href","#");
            });
            
            </script>
        </c:if>
     </c:if>
</div>