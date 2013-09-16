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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="qrscanform" method="POST" modelAttribute="QRScanBean">

	<mobi:largeView><h3>QR Codes</h3></mobi:largeView>

	<mobi:getEnhanced />

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow>
			The qrscan and qrcode tags are used to read or create QR codes. 
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<h3>QR Code Scanner</h3>
	<mobi:fieldsetGroup>
		<mobi:fieldsetRow>
			<label class="ui-input-text">Scan QR code:</label>
			<mobi:scan id="scanOne" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow styleClass="qr">
			<label class="ui-input-text">Scanned:</label>
			<div style="overflow:hidden;max-width:${mobiClient.handheldBrowser ? '190px' : '400px'};display:inline-block;text-overflow:ellipsis">${QRScanBean.scanOne}</div>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:commandButton buttonType="important" styleClass="submit"
		value="Submit" type="submit" />

	<br />
	<h3>QR Code Generator</h3>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow>
			<mobi:inputText value="${QRScanBean.plaintextOne}"
				id="plaintextOne" name="plaintextOne" label="Enter plain text:"/>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow styleClass="qr">
			<label class="ui-input-text">QR Code:</label>
			<div>
				<mobi:qrcode value="${QRScanBean.plaintextOne}" />
			</div>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:commandButton buttonType="important" styleClass="submit"
		value="Create QRCode" type="submit" />

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow group="true">
			Description
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			The qrscan and qrcode tags are used to read or create QR
			codes. The ICEmobile Container is needed for reading QR codes.
			<br />
			The qrscan tag accesses the device's camera to read a QR code.
			If successful, the decoded QR Code will be assigned to the backing bean.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>
</form:form>


<script type="text/javascript">
	MvcUtil.enhanceForm("#qrscanform");
</script>
