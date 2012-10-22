<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE html >
<html>
<%@ include file="/WEB-INF/jsp/header.jsp"%>
<body>
	<mobi:pagePanel>
		<mobi:pagePanelHeader>
			<fmt:message key="heading" />
		</mobi:pagePanelHeader>
		<mobi:pagePanelBody>
			<div>
				<h4>Recently Uploaded Messages</h4>
				<form:form id="carouselForm" method="POST" modelAttribute="mediaView">
				<div style="border-top: 1px solid #999999;">
					<mobi:carousel id="recentMessagesCarousel"
						collection="${mediaService.mediaImageMarkup}"
						selectedIndex="${mediaView.selectedIndex}"
						style="border-top: 1px solid #999999;">
						<mobi:carouselItem ref="myitem" type="java.lang.String" />
					</mobi:carousel>
				</div>
				</form:form>
			</div>
			<div style="clear:both;">
			</div>
			
			<div style="margin-top:10px;">
				<div class="mobi-button-group mobi-button-group-hor" style="text-align:center;">
					<a class="mobi-button mobi-button-default" href='<c:url value="/media"/>'>Media Upload</a>
					<a class="mobi-button mobi-button-default" href='<c:url value="/media/gallery"/>'>Media Gallery</a>
				</div>
				<h4 style="text-align:center;">Share from your location:</h4>
				<div id="msg">${uploadModel.uploadMsg}</div>
				<div class="mobi-button-group mobi-button-group-hor ajaxShow" style="text-align:center;">
					<a class="mobi-button mobi-button-default" 
						onclick="showUpload(this,'camera');">Photo</a>
					<a class="mobi-button mobi-button-default"
						onclick="showUpload(this,'camcorder');">Video</a>
					<a class="mobi-button mobi-button-default" 
						onclick="showUpload(this,'mic');">Audio</a>
				</div>
				<script type="text/javascript">
				function showUpload(elem,type){
					$('.upload').hide();
					$('#'+type+'Upload').show();
					$('.mobi-button').removeClass('mobi-button-selected');
					$(elem).addClass('mobi-button-selected');
				}
				</script>
				<div class="ajaxHide upload" id="cameraUpload">
					<form:form id="cameraUploadForm" method="POST" enctype="multipart/form-data"
						 modelAttribute="uploadModel">
		                <input type="hidden" name="type" value="photo"/>
		                <mobi:fieldsetGroup id="cameraUploadGrp" inset="true" style="margin-top:10px;">
		                    <mobi:fieldsetRow style="min-height:30px;">
		                    	<div>
		                         <mobi:camera id="upload"/>
		                          <div style="display:inline-block;">
		                              <mobi:thumbnail for="upload"/>
		                          </div>
		                         </div>
		                     </mobi:fieldsetRow>
		                     <mobi:fieldsetRow style="text-align:center;">
		                          <mobi:commandButton name="uploadPhoto"
		                                style="float:none;"
		                                buttonType="default"
		                                value="Add my photo to the message"/>
		                                
		                    </mobi:fieldsetRow>
		                </mobi:fieldsetGroup>
		            </form:form>
	            </div>
	            <div class="ajaxHide upload" id="camcorderUpload">
		            <form:form id="camcorderUploadForm" method="POST" enctype="multipart/form-data">
		                <input type="hidden" name="type" value="video"/>
		                <mobi:fieldsetGroup id="camcorderUploadGrp" inset="true" style="margin-top:10px;">
		                    <mobi:fieldsetRow style="min-height:30px;">
		                         <div>
		                         <mobi:camcorder id="camcorder"/>
		                          <div style="display:inline-block;">
		                              <mobi:thumbnail for="camcorder"/>
		                          </div>
		                         </div>
		                     </mobi:fieldsetRow>
		                     <mobi:fieldsetRow style="text-align:center;">
		                          <mobi:commandButton name="uploadVideo"
		                                style="float:none;"
		                                buttonType="default"
		                                value="Add my video to the message"/>
		                                
		                    </mobi:fieldsetRow>
		                </mobi:fieldsetGroup>
		            </form:form>
		        </div>
		        <div class="ajaxHide upload" id="micUpload">
		            <form:form id="micUploadForm" method="POST" enctype="multipart/form-data">
		                <input type="hidden" name="type" value="audio"/>
		                <mobi:fieldsetGroup id="micUploadGrp" inset="true" style="margin-top:10px;">
		                    <mobi:fieldsetRow style="min-height:30px;">
		                    	<div>
		                    		<mobi:microphone id="mic"/>
		                    	</div>
		                     </mobi:fieldsetRow>
		                     <mobi:fieldsetRow style="text-align:center;">
		                          <mobi:commandButton name="uploadAudio"
		                                style="float:none;"
		                                buttonType="default"
		                                value="Add my audio to the message"/>
		                                
		                    </mobi:fieldsetRow>
		                </mobi:fieldsetGroup>
		            </form:form>
		        </div>
	            
	            <form:form id="uploadForm" method="POST" modelAttribute="uploadModel">
	                <mobi:fieldsetGroup id="uploadGrp" inset="true" style="margin-top:10px;">
	                    <mobi:fieldsetRow style="min-height:30px;">
	                    	 <mobi:inputText name="title" 
	                         	styleClass="input"
	                         	type="text"
                                autoCorrect="off"
                                placeholder="Title"
                                value="${uploadModel.title}"/>
	                     </mobi:fieldsetRow>
	                     <mobi:fieldsetRow style="min-height:50px;">
	                         <mobi:inputText name="description" 
	                         	styleClass="input"
	                         	type="textarea"
                                autoCorrect="off"
                                placeholder="Description"
                                value="${uploadModel.description}"/>
	                     </mobi:fieldsetRow>
	                     <mobi:fieldsetRow style="min-height:50px;">
	                         <mobi:inputText name="tags" type="text"
	                         	styleClass="input"
                                autoCorrect="off"
                                placeholder="Tags"
                                value="${tags}"/>
	                     </mobi:fieldsetRow>
	                     <mobi:fieldsetRow style="text-align:center;">
	                          <mobi:commandButton name="submit"
	                                style="float:none;"
	                                buttonType="default"
	                                value="Submit my message"/>
	                                
	                    </mobi:fieldsetRow>
	                </mobi:fieldsetGroup>
	                <mobi:geolocation id="geolocation"/>
	            </form:form>
            </div>
		</mobi:pagePanelBody>
		<%@ include file="/WEB-INF/jsp/footer.jsp"%>
	</mobi:pagePanel>
</body>
</html>