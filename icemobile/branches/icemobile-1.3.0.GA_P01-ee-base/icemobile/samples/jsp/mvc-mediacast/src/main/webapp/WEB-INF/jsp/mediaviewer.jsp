<%--
  ~ Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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
			<mobi:fieldsetGroup inset="true">
				<mobi:fieldsetRow id="titleRow" style="padding:5px;">
					<h3>${media.title}</h3>
				</mobi:fieldsetRow>
			</mobi:fieldsetGroup>

			<c:if test="${media.showPhoto}">
				<mobi:fieldsetGroup inset="true">
					<mobi:fieldsetRow>
						<label>Photo:</label>
					</mobi:fieldsetRow>
					<mobi:fieldsetRow style="text-align:center;">
						<img id="largePhoto" width="100%" 
							src='<c:url value="/resources/uploads/${media.largePhoto.name}"/>'
							class="imageViewer" />
					</mobi:fieldsetRow>
				</mobi:fieldsetGroup>
			</c:if>

			<c:if test="${media.showVideo}">
				<mobi:fieldsetGroup inset="true">
					<mobi:fieldsetRow>
						<label>Video:</label>
					</mobi:fieldsetRow>
					<mobi:fieldsetRow style="text-align:center;">
						<video id="videoPlayer"
							src='<c:url value="/resources/uploads/${media.video.name}"/>' class="imageViewer"
							controls="controls"></video>
					</mobi:fieldsetRow>
				</mobi:fieldsetGroup>
			</c:if>

			<c:if test="${media.showAudio}">
				<mobi:fieldsetGroup inset="true">
					<mobi:fieldsetRow>
						<label>Audio:</label>
					</mobi:fieldsetRow>
					<mobi:fieldsetRow style="text-align:center;">
						<audio id="audioPlayer"
							src='<c:url value="/resources/uploads/${media.audio.name}"/>' class="imageViewer"
							controls="controls"></audio>
					</mobi:fieldsetRow>
				</mobi:fieldsetGroup>
			</c:if>

			<mobi:fieldsetGroup inset="true">
				<mobi:fieldsetRow>
					<label>Description: </label>
					<c:out value="${media.description}" />
				</mobi:fieldsetRow>
				<mobi:fieldsetRow>
					<label>Geolocation: </label>
					<div id="geoGrid">
						<label>Longitude</label><span>${media.longitude}</span>
						<label>Latitude</label><span>${media.latitude}</span>
						<label>Altitude</label><span>${media.altitude}</span>
						<label>Direction</label><span>${media.direction}</span>
					</div>
				</mobi:fieldsetRow>
				<mobi:fieldsetRow>
					<label>Tags: </label>
					<c:forEach items="${media.tags}" var="tag">
						<img src='<c:url value="/resources/images/tag.gif"/>' />
						<span style="vertical-align:top;display:inline-block;margin-right:10px;">
							<c:out value="${tag}"/>&#160;
						</span>
					</c:forEach>
				</mobi:fieldsetRow>
			</mobi:fieldsetGroup>

			<mobi:fieldsetGroup inset="true">
				<mobi:fieldsetRow style="text-align:center;">
					<form:form id="delMediaFrm" method="DELETE">
						<mobi:commandButton name="Delete" buttonType='attention'
                                    value="Delete"/>
					</form:form>
				</mobi:fieldsetRow>
			</mobi:fieldsetGroup>

		</mobi:pagePanelBody>
		<%@ include file="/WEB-INF/jsp/footer.jsp"%>
	</mobi:pagePanel>
</body>
</html>