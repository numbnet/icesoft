<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE html >
<html>
<%@ include file="/WEB-INF/jsp/header.jsp"%>
<body>
	<mobi:pagePanel>
		<mobi:pagePanelHeader>
			<a id="backBtn" type="back" class="mobi-button mobi-button-back"
				href='<c:url value="/media"/>'>back</a>
			<span>Media Viewer</span>
		</mobi:pagePanelHeader>
		<mobi:pagePanelBody>
			<mobi:fieldSetGroup inset="true">
				<mobi:fieldSetRow id="titleRow" style="padding:5px;">
					<h3>${media.title}</h3>
				</mobi:fieldSetRow>
			</mobi:fieldSetGroup>

			<c:if test="${media.showPhoto}">
				<mobi:fieldSetGroup inset="true">
					<mobi:fieldSetRow>
						<label>Photo:</label>
					</mobi:fieldSetRow>
					<mobi:fieldSetRow style="text-align:center;">
						<img id="largePhoto" width="100%" 
							src='<c:url value="/resources/uploads/${media.photo.fileName}"/>'
							class="imageViewer" />
					</mobi:fieldSetRow>
				</mobi:fieldSetGroup>
			</c:if>

			<c:if test="${media.showVideo}">
				<mobi:fieldSetGroup inset="true">
					<mobi:fieldSetRow>
						<label>Video:</label>
					</mobi:fieldSetRow>
					<mobi:fieldSetRow style="text-align:center;">
						<video id="videoPlayer"
							src='<c:url value="/resources/uploads/${media.video.fileName}"/>' class="imageViewer"
							controls="controls"></video>
					</mobi:fieldSetRow>
				</mobi:fieldSetGroup>
			</c:if>

			<c:if test="${media.showAudio}">
				<mobi:fieldSetGroup inset="true">
					<mobi:fieldSetRow>
						<label>Audio:</label>
					</mobi:fieldSetRow>
					<mobi:fieldSetRow style="text-align:center;">
						<audio id="audioPlayer"
							src='<c:url value="/resources/uploads/${media.audio.fileName}"/>' class="imageViewer"
							controls="controls"></audio>
					</mobi:fieldSetRow>
				</mobi:fieldSetGroup>
			</c:if>

			<mobi:fieldSetGroup inset="true">
				<mobi:fieldSetRow>
					<label>Description: </label>
					<c:out value="${media.description}" />
				</mobi:fieldSetRow>
				<mobi:fieldSetRow>
					<label>Geolocation: </label>
					<div id="geoGrid">
						<label>Longitude</label><span>${media.longitude}</span>
						<label>Latitude</label><span>${media.latitude}</span>
						<label>Altitude</label><span>${media.altitude}</span>
						<label>Direction</label><span>${media.direction}</span>
					</div>
					<script src='<c:url value="/resources/javascript/openlayers/OpenLayers.mobile.js"/>'></script>
					<div style="width:100%; height:200px; z-index:0;" id="map"></div>
					<script type="text/javascript">
					window.map = new OpenLayers.Map('map');
					var wms = new OpenLayers.Layer.WMS("OpenLayers WMS",
					  "http://vmap0.tiles.osgeo.org/wms/vmap0",
					  {'layers':'basic'} );
					map.addLayer(wms);
					map.setCenter(new OpenLayers.LonLat(${media.longitude},${media.latitude}),3);
					var vectorLayer = new OpenLayers.Layer.Vector("Overlay");
					var feature = new OpenLayers.Feature.Vector(
					 new OpenLayers.Geometry.Point(${media.longitude},${media.latitude}),
					 {some:'data'},
					 {externalGraphic: '<c:url value="/resources/images/icemobile_thumb.png"/>', graphicHeight: 21, graphicWidth: 16});
					vectorLayer.addFeatures(feature);
					map.addLayer(vectorLayer);
					</script>
				</mobi:fieldSetRow>
				<mobi:fieldSetRow>
					<label>Tags: </label>
					<c:forEach items="${media.tags}" var="tag">
						<img src='<c:url value="/resources/images/tag.gif"/>' />
						<span style="vertical-align:top;display:inline-block;margin-right:10px;">
							<c:out value="${tag}"/>&#160;
						</span>
					</c:forEach>
				</mobi:fieldSetRow>
			</mobi:fieldSetGroup>

			<mobi:fieldSetGroup inset="true">
				<mobi:fieldSetRow style="text-align:center;">
					<form:form id="delMediaFrm" method="DELETE">
						<mobi:commandButton name="Delete" buttonType='attention'
                                    value="Delete"/>
					</form:form>
				</mobi:fieldSetRow>
			</mobi:fieldSetGroup>

		</mobi:pagePanelBody>
		<%@ include file="/WEB-INF/jsp/footer.jsp"%>
	</mobi:pagePanel>
</body>
</html>