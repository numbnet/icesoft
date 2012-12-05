<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<div>
	<form:form id="geolocationform" method="POST"
		modelAttribute="geolocationBean" cssClass="form geolocpage">

		<c:if test="${viewSize eq 'large'}">
			<h3>GeoLocation</h3>
		</c:if>

		<mobi:geolocation id="geo1" name="geolocation"
			timeout="${geolocationBean.timeout}"
			enableHighPrecision="${geolocationBean.enableHighPrecision}"
			continuousUpdates="${geolocationBean.continuousUpdates}" />
		<!-- maximumAge="${geolocationBean.maximumAge}" -->

		<mobi:fieldsetGroup styleClass="intro">
			<mobi:fieldsetRow>
                The geolocation tag determines the user's current location.     
            </mobi:fieldsetRow>
		</mobi:fieldsetGroup>

		<mobi:fieldsetGroup styleClass="intro">
			<mobi:fieldsetRow>
                Click the 'Locate Me' button below to calculate your current 
                location and update the map. Various settings for the geolocation
                tag can also be adjusted below.
            </mobi:fieldsetRow>
		</mobi:fieldsetGroup>

		<mobi:fieldsetGroup>
			<mobi:fieldsetRow>
				<canvas id="mapCanvas" width="260" height="130" class="center"></canvas>
			</mobi:fieldsetRow>
			<mobi:fieldsetRow styleClass="mobi-center">
				<mobi:commandButton value="Locate Me" type="submit"
					buttonType="important" />
			</mobi:fieldsetRow>
		</mobi:fieldsetGroup>

		<h3>Geolocation Results:</h3>

		<mobi:fieldsetGroup>
			<mobi:fieldsetRow>
				<label>Latitude:</label>
				<span>${geolocationBean.latitude}</span>
			</mobi:fieldsetRow>
			<mobi:fieldsetRow>
				<label>Longitude:</label>
				<span>${geolocationBean.longitude}</span>
			</mobi:fieldsetRow>
			<mobi:fieldsetRow>
				<label>Direction:</label>
				<span>${geolocationBean.direction}</span>
			</mobi:fieldsetRow>
			<mobi:fieldsetRow>
				<label>Altitude:</label>
				<span>${geolocationBean.altitude}</span>
			</mobi:fieldsetRow>
		</mobi:fieldsetGroup>

		<h3>Geolocation Settings:</h3>

		<mobi:fieldsetGroup>
			<mobi:fieldsetRow>
				<label for="continuousUpdates">Continuous Updates:</label>
				<mobi:flipswitch id="continuousUpdates" name="continuousUpdates"
					labelOn="true" labelOff="false"
					value="${geolocationBean.continuousUpdates}" />
			</mobi:fieldsetRow>
			<mobi:fieldsetRow>
				<label for="enableHighPrecision">enableHighPrecision:</label>
				<select name="enableHighPrecision" style="max-width: 100px;">
					<option
						selected="${geolocationBean.enableHighPrecision eq 'true' ? 'selected' : ''}">true</option>
					<option
						selected="${geolocationBean.enableHighPrecision eq 'false' ? 'selected' : ''}">false</option>
					<option
						selected="${geolocationBean.enableHighPrecision eq 'asNeeded' ? 'selected' : ''}">asNeeded</option>
				</select>
			</mobi:fieldsetRow>
			<mobi:fieldsetRow styleClass="timeout">
				<label for="timeout">Timeout:</label>
				<div style="max-width: 50%">
					<span
						style="font-size: 11px; float: right; margin: -5px -5px 5px -15px; text-align: left;">
						(longest allowable time for reading to arrive (0-x, seconds) </span>
					<mobi:inputText id="timeout" name="timeout"
						value="${geolocationBean.timeout}" type="number"
						style="float: right;clear: both;" />
				</div>
			</mobi:fieldsetRow>
			<mobi:fieldsetRow styleClass="maxage">
				<label for="maxAge">Maximum Age:</label>
				<div style="max-width: 50%">
					<span
						style="font-size: 11px; float: right; margin: -5px -5px 5px -15px; text-align: left;">
						(oldest allowable cached Location reading (0-x, seconds) </span>
					<mobi:inputText id="maxAge" name="maxAge"
						value="${geolocationBean.maximumAge}" type="number"
						style="float: right;clear: both;" />
				</div>
			</mobi:fieldsetRow>
		</mobi:fieldsetGroup>

		<mobi:commandButton type="submit" buttonType="important"
			styleClass="submit" value="Update Settings" />
			
	   <br/>

		<script type="text/javascript">
			function showLocation() {
				var canvas = document.getElementById('mapCanvas');
				if (!canvas) {
					return;
				}
				// temporary work around for drawing location on canvas
				var lat = '${geolocationBean.latitude}';
				var lon = '${geolocationBean.longitude}';

				if (!lat && !lon) {
					lat = 0;
					lon = 0;
				}

				if (canvas.getContext) {
					var ctx = canvas.getContext('2d');
					ctx.clearRect(0, 0, 600, 400);
					var height = canvas.height;
					var width = canvas.width;

					ctx.drawImage(image, 0, 0, width, height);
					var x = Math.floor(lon * width / 2 / 180 + (width / 2));
					var y = Math.floor(-1 * lat * (height / 2) / 90
							+ (height / 2));
					ctx.fillStyle = "#FFFF00";
					ctx.beginPath();
					ctx.arc(x, y, 5, 0, Math.PI * 2, true);
					ctx.fill();
				}
			}

			var image = new Image();
			//scaled and cropped http://nf.nci.org.au/facilities/software/GMT/4.3.1/doc/html/GMT_Docs/node97.html
			image.src = "resources/images/map.png";
			image.onload = function() {
				showLocation();
			}
			showLocation();
		</script>

	</form:form>

	<script type="text/javascript">
		MvcUtil.enhanceForm("#geolocationform");
	</script>
</div>
