<%@ include file="/WEB-INF/jsp/include.jsp"%>
<div>
    <form:form id="carouselForm" method="POST">
        <div style="border-top: 1px solid #999999;">
            <mobi:carousel id="recentMessagesCarousel"
                collection="${carouselItems}"
                selectedIndex="${mediaView.selectedIndex}"
                style="border-top: 1px solid #999999;">
                <mobi:carouselItem ref="myitem" type="java.lang.String" />
            </mobi:carousel>
        </div>
    </form:form>
</div>
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
