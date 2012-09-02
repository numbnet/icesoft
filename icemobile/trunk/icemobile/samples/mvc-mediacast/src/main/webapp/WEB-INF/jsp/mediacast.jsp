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
						collection="${mediaStore.mediaImageMarkup}"
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
				<h4>Send a Media Message</h4>
				<div id="msg">${uploadModel.uploadMsg}</div>
				<div style="text-align:center;">
					<a class="mobi-button mobi-button-default" style="float:none;"
						onclick="$('.upload').hide();$('#cameraUploadForm').show();">Upload a Photo</a>
					<a class="mobi-button mobi-button-default" style="float:none;"
						onclick="$('.upload').hide();$('#camcorderUploadForm').show();">Upload Video</a>
					<a class="mobi-button mobi-button-default" style="float:none;"
						onclick="$('.upload').hide();$('#micUploadForm').show();">Upload Audio</a>
				</div>
				<form:form id="cameraUploadForm" method="POST" enctype="multipart/form-data"
					 modelAttribute="uploadModel" style="display:none;" class="upload">
	                <input type="hidden" name="type" value="photo"/>
	                <mobi:fieldSetGroup id="cameraUploadGrp" inset="true" style="margin-top:10px;">
	                    <mobi:fieldSetRow style="min-height:20px;">
	                    	<div>
	                         <mobi:camera id="upload"/>
	                          <div style="display:inline-block;">
	                              <mobi:thumb for="upload"/>
	                          </div>
	                         </div>
	                     </mobi:fieldSetRow>
	                     <mobi:fieldSetRow style="text-align:center;">
	                          <mobi:commandButton name="uploadPhoto"
	                                style="float:none;"
	                                buttonType="default"
	                                value="Add my photo to the message"/>
	                                
	                    </mobi:fieldSetRow>
	                </mobi:fieldSetGroup>
	            </form:form>
	            
	            <form:form id="camcorderUploadForm" method="POST" enctype="multipart/form-data" 
	            	style="display:none;" class="upload">
	                <input type="hidden" name="type" value="video"/>
	                <mobi:fieldSetGroup id="camcorderUploadGrp" inset="true" style="margin-top:10px;">
	                    <mobi:fieldSetRow style="min-height:20px;">
	                         <div>
	                         <mobi:camcorder id="camcorder"/>
	                          <div style="display:inline-block;">
	                              <mobi:thumb for="camcorder"/>
	                          </div>
	                         </div>
	                     </mobi:fieldSetRow>
	                     <mobi:fieldSetRow style="text-align:center;">
	                          <mobi:commandButton name="uploadVideo"
	                                style="float:none;"
	                                buttonType="default"
	                                value="Add my video to the message"/>
	                                
	                    </mobi:fieldSetRow>
	                </mobi:fieldSetGroup>
	            </form:form>
	            
	            <form:form id="micUploadForm" method="POST" enctype="multipart/form-data"
	            	style="display:none;" class="upload">
	                <input type="hidden" name="type" value="audio"/>
	                <mobi:fieldSetGroup id="micUploadGrp" inset="true" style="margin-top:10px;">
	                    <mobi:fieldSetRow style="min-height:20px;">
	                    	<div>
	                    		<mobi:microphone id="mic"/>
	                    	</div>
	                     </mobi:fieldSetRow>
	                     <mobi:fieldSetRow style="text-align:center;">
	                          <mobi:commandButton name="uploadAudio"
	                                style="float:none;"
	                                buttonType="default"
	                                value="Add my audio to the message"/>
	                                
	                    </mobi:fieldSetRow>
	                </mobi:fieldSetGroup>
	            </form:form>
	            
	            <form:form id="uploadForm" method="POST" modelAttribute="uploadModel">
	                <mobi:fieldSetGroup id="uploadGrp" inset="true" style="margin-top:10px;">
	                    <mobi:fieldSetRow style="min-height:20px;">
	                         <label for="title">Title</label>
	                         <input type="text" id="title" name="title"
	                         	placeholder="Add a title.."/>
	                     </mobi:fieldSetRow>
	                     <mobi:fieldSetRow style="min-height:50px;">
	                         <label for="description">Description</label>
	                         <textarea id="description" name="description" rows="5"
	                         	placeholder="Add some descriptive text..."></textarea>
	                     </mobi:fieldSetRow>
	                     <mobi:fieldSetRow style="min-height:50px;">
	                         <label for="description">Tags</label>
	                         <input id="tags" name="tags" type="text" 
	                         	placeholder="Add a space-delimited series of descriptive tags"/>
	                     </mobi:fieldSetRow>
	                     <mobi:fieldSetRow style="text-align:center;">
	                          <mobi:commandButton name="submit"
	                                style="float:none;"
	                                buttonType="default"
	                                value="Submit my message"/>
	                                
	                    </mobi:fieldSetRow>
	                </mobi:fieldSetGroup>
	            </form:form>
            </div>
		</mobi:pagePanelBody>
		<%@ include file="/WEB-INF/jsp/footer.jsp"%>
	</mobi:pagePanel>
</body>
</html>