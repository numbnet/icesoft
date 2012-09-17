<%@ page session="false"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE html >
<html>
	<%@ include file="/WEB-INF/jsp/contest-head.jsp"%>
<body>
	<mobi:pagePanel>
		<mobi:pagePanelHeader>
			<a id="backBtn"class="mobi-button mobi-button-back"
				href='<c:url value="/contest-gallery"/>'>Gallery</a>
			Contest Upload
		</mobi:pagePanelHeader>
		<mobi:pagePanelBody>
			<div>
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
				<div id="msg" style="margin: 0 10px;">${uploadModel.uploadMsg}</div>
				<form:form id="cameraUploadForm" method="POST" enctype="multipart/form-data"
					 modelAttribute="uploadModel">
	                <input type="hidden" name="type" value="photo"/>
	                <mobi:fieldSetGroup id="cameraUploadGrp" inset="true" style="margin-top:10px;">
	                    <mobi:fieldSetRow style="min-height:65px;">
	                    	<div>
	                    		<mobi:camera id="upload" style="width:70%;"/>
	                         	<div style="display:inline-block;float:right;">
		                            <mobi:thumb for="upload" style="margin: 0 2px"/>
		                        </div>
	                         </div>
	                     </mobi:fieldSetRow>
	                     <mobi:fieldSetRow style="min-height:0;">
	                    	 <mobi:inputtext name="email" 
	                         	styleClass="input"
	                         	type="text"
                                autoCorrect="off"
                                placeholder="Email"
                                value="${uploadModel.email}"/>
	                     </mobi:fieldSetRow>
	                     <mobi:fieldSetRow style="min-height:0;">
	                         <mobi:inputtext name="description" 
	                         	styleClass="input"
	                         	type="textarea"
                                autoCorrect="off"
                                placeholder="Description"
                                value="${uploadModel.description}"/>
	                     </mobi:fieldSetRow>
	                     <mobi:fieldSetRow style="text-align:center;">
			            	<input type="reset" class="mobi-button mobi-button-default"
			                       value="Cancel" style="width:100px;margin-top:0;margin-bottom:0;"/>
	                          <mobi:commandButton name="submit"
	                                style="float:none;width:100px;margin-top:0;margin-bottom:0;"
	                                buttonType="important"
	                                value="Share"/>
	                    </mobi:fieldSetRow>
	                </mobi:fieldSetGroup>
	                <mobi:fieldSetGroup inset="true" style="padding:10px;">
	                	<mobi:fieldSetRow style="text-align:center;padding:0;">
	                	Learn more about ICEmobile video, audio, QR code, augmented reality
						and rich UI features at <a href="http://www.icesoft.org/demos/icemobile-demos.jsf">www.icesoft.org</a>.
						</mobi:fieldSetRow>
	                </mobi:fieldSetGroup>
	            </form:form>
            </div>
		</mobi:pagePanelBody>
		<%@ include file="/WEB-INF/jsp/footer.jsp"%>
	</mobi:pagePanel>
</body>
</html>