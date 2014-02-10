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
    uri="http://www.icepush.org/icepush/jsp/icepush.tld"
%>
<div>
    <form:form id="geolocationform" method="POST"
        modelAttribute="geolocationBean" cssClass="geolocpage"
    >

        <mobi:largeView><h3>GeoLocation</h3></mobi:largeView>

        <mobi:geolocation id="geo1" name="geolocation"
            timeout="${geolocationBean.timeout}"
            enableHighPrecision="${geolocationBean.enableHighPrecision}"
            continuousUpdates="${geolocationBean.continuousUpdates}"
        />
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
                <div id="mapctr" style="position:relative" class="center">
                    <img id="map" src="resources/images/map.png"  width="${mobiClient.handheldBrowser ? '260' : '360'}" height="${mobiClient.handheldBrowser ? '130' : '180'}"/>
                    <div id="pointer" style="position: absolute;background-color: red;border-radius:1em;width:5px;height:5px"></div>
                </div>
            </mobi:fieldsetRow>
            <mobi:fieldsetRow style="text-align:center">
                <mobi:commandButton value="Locate Me" type="submit"
                    buttonType="important" style="width:50%"/>
            </mobi:fieldsetRow>
        </mobi:fieldsetGroup>

        <h3>Geolocation Results:</h3>

        <mobi:fieldsetGroup styleClass="results">
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
                <label for="continuousUpdates">Continuous
                    Updates:</label>
                <mobi:flipswitch id="continuousUpdates"
                    name="continuousUpdates" labelOn="true"
                    labelOff="false" style="float:right"
                    value="${geolocationBean.continuousUpdates}"
                />
            </mobi:fieldsetRow>
            <mobi:fieldsetRow>
                <label for="enableHighPrecision">enableHighPrecision:</label>
                <form:select path="enableHighPrecision" items="${enableHighPrecisionTypes}" 
                    style="max-width: 100px;float:right"/>
            </mobi:fieldsetRow>
            <mobi:fieldsetRow styleClass="timeout">
                <div style="display:inline-block;max-width:50%">
                    <label>Timeout:</label>
                    <span style="font-size: 11px;float: left;clear:both;line-height:12px;">
                        (longest allowable time for reading to arrive (0-x, seconds))
                    </span>
                </div>
                <mobi:inputText id="timeout" name="timeout"
                        value="${geolocationBean.timeout}" type="number"
                         style="float:right;max-width:45%"/>&#160;
            </mobi:fieldsetRow>
            <mobi:fieldsetRow styleClass="maxage">
                <div style="display:inline-block;max-width:50%">
                    <label>Maximum Age:</label>
                    <span style="font-size: 11px;float: left;clear:both;line-height:12px;">
                        (oldest allowable cached Location reading (0-x, seconds))
                    </span>
                </div>
                <mobi:inputText id="maximumAge" name="maximumAge"
                        value="${geolocationBean.maximumAge}"
                        type="number" style="float:right;max-width:45%"/>&#160;
            </mobi:fieldsetRow>
        </mobi:fieldsetGroup>

        <mobi:commandButton type="submit" buttonType="important"
            styleClass="submit" value="Update Settings"
        />
        <br />

        <script type="text/javascript">
            function showLocation() {
                var lat = '${geolocationBean.latitude}';
                var lon = '${geolocationBean.longitude}';
                var image = document.getElementById('map');
                var height = image.clientHeight;
                var width = image.clientWidth;
                var x = Math.floor(lon * width / 2 / 180 + (width / 2));
                var y = Math.floor(-1 * lat * (height / 2) / 90 + (height / 2));
                var pointer = document.getElementById('pointer');
                pointer.style.left = ''+(x+image.offsetLeft)+'px';
                pointer.style.top = ''+(y+image.offsetTop)+'px';
                
            }

			var image = new Image();
			//scaled and cropped http://nf.nci.org.au/facilities/software/GMT/4.3.1/doc/html/GMT_Docs/node97.html
			image.src = "resources/images/map.png";
			image.onload = function() {
                showLocation();
            }
            showLocation();
            ice.mobi.addListener(window, 'resize',showLocation);
            ice.mobi.addListener(window, 'orientationchange',showLocation);
		</script>

    </form:form>

    <script type="text/javascript">
        window. setTimeout(function(){console.log("Removing location watch from application");
                                      ice.mobi.geolocation.clearWatch(); }, 120000);
    </script>

    <script type="text/javascript">
		MvcUtil.enhanceForm("#geolocationform");
		MvcUtil.enhanceAllLinksWithRemoveGeolocationWatch("#sp_left, #menuLink");
	</script>
</div>
