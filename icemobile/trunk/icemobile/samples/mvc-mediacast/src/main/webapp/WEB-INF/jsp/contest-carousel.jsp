<%@ page session="false"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<div>
    <form:form id="carouselForm" method="POST">
        <div style="border-top: 1px solid #999999;">
            <mobi:carousel id="recentMessagesCarousel"
                collection="${carouselItems}"
                selectedIndex="${mediaView.selectedIndex}"
                style="border-top: 1px solid #999999;"
            >
                <mobi:carouselItem ref="myitem" type="java.lang.String" />
            </mobi:carousel>
        </div>
    </form:form>
</div>
