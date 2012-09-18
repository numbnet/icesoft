<%@ page session="false"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:if test="${!ajaxRequest}">
<!DOCTYPE html >
<html>
	<%@ include file="/WEB-INF/jsp/contest-head.jsp"%>
<body>
</c:if>
    <div class="ajaxzone">
	<mobi:pagePanel>
		<mobi:pagePanelHeader>
			<a id="backBtn"class="mobi-button mobi-button-back"
				href='<c:url value="/contest-gallery"/>'>Gallery</a>
			Contest Upload
		</mobi:pagePanelHeader>
		<mobi:pagePanelBody>
		    <push:region group="photos" page="/contest-carousel"/>
			<div style="clear:both;"></div>
			
			<div style="margin-top:10px;">
				<div id="msg" style="margin: 0 10px;">${uploadModel.uploadMsg}</div>
				
				<form:form id="cameraUploadForm" method="POST" enctype="multipart/form-data"
					 modelAttribute="uploadModel" htmlEscape="true">
	                <form:errors path="*" cssClass="errorblock" element="div" />
	                <mobi:fieldSetGroup inset="true" style="margin-top: 10px;font-weight: bold;text-align: center;color: blue;">
	                   <mobi:fieldSetRow>
	                       Upload a photo and you might win something!!!
	                   </mobi:fieldSetRow>
	                </mobi:fieldSetGroup>
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
	                         	type="email"
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
                <script type="text/javascript">
                    enhanceForm("#cameraUploadForm");
                </script>
            </div>
		</mobi:pagePanelBody>
		<%@ include file="/WEB-INF/jsp/footer.jsp"%>
	</mobi:pagePanel>
    </div>
<c:if test="${!ajaxRequest}">
</body>
</html>
</c:if>