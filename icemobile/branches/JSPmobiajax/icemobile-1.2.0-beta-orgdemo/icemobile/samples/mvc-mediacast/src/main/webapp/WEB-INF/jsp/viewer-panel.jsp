<%@ include file="/WEB-INF/jsp/include.jsp"%>
<div id="viewerPanel">
    <c:if test="${not empty media}">
        <div class="lightbox">
            <img id="largePhoto" src='resources/uploads/${media.largePhoto.name}'
                class="imageViewer" />
        </div>
        <a class="viewer-back" href="#" onclick="updateViewerPanel('${media.id}','back');"></a>
        <a class="viewer-forward" href="#" onclick="updateViewerPanel('${media.id}','forward');"></a>
        <div class="message-desc">
            <h3><c:out value="${media.description}"/></h3>
        </div>
        <c:if test="${not empty media.video}">
            <h3>Video</h3>
            <div>
                <video src="resources/uploads/${media.video.name}" controls="controls"></video>
            </div>
        </c:if>
        <c:if test="${not empty media.audio}">
            <h3>Audio</h3>
            <div>
                <audio src="resources/uploads/${media.audio.name}" controls="controls"></audio>
            </div>
        </c:if>
    </c:if>
</div>