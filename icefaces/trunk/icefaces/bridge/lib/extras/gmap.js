/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */
var GMapRepository = new Array();

var GMapWrapper = Class.create();
GMapWrapper.prototype = {
    initialize: function(eleId, realGMap) {
        this.eleId = eleId;
        this.realGMap = realGMap;
        this.controls = new Object();
        this.overlays = new Object();
		this.directions = new Object();
        this.geoMarker = new Object();
        this.geoMarkerAddress;
        this.geoMarkerSet = false;
		this.currentMarker = null;
		this.currentInfoWindow = null;
    },

    getElementId: function() {
        return this.eleId;
    },

    getRealGMap: function() {
        return this.realGMap;
    },

    getControlsArray: function() {
        return this.controls;
    }
};

Ice.GoogleMap = Class.create();
Ice.GoogleMap = {
    getGeocoder: function(id) {
        var geocoder = GMapRepository[id + 'geo'];
        if (geocoder == null) {
            GMapRepository[id + 'geo'] = new GClientGeocoder();
            return GMapRepository[id + 'geo'];
        } else {
            return geocoder;
        }
    },

    getGDirection: function(id, text_div) {
        var gdirection = GMapRepository[id + 'dir'];
        if (gdirection == null) {
            var map = Ice.GoogleMap.getGMapWrapper(id).getRealGMap();
            var directionsPanel = document.getElementById(text_div);
            GMapRepository[id + 'dir'] = new GDirections(map, directionsPanel);
            return GMapRepository[id + 'dir'];
        } else {
            return gdirection;
        }
    },

    getGMapWrapper:function (id) {
        var gmapWrapper = GMapRepository[id];
        if (gmapWrapper) {
            var gmapComp = document.getElementById(id);
           //the googlemap view must be unrendered, however
            //javascript object still exist, so recreate the googlemap
            //with its old state.
            if (!gmapComp.hasChildNodes()) {
                gmapWrapper = Ice.GoogleMap.recreate(id, gmapWrapper);
            }
        } else {
            //googleMap not found create a fresh new googleMap object
            gmapWrapper = Ice.GoogleMap.create(id);
        }
        return gmapWrapper;
    },

    loadDirection:function(id, textDivId, from, to) {
        var wrapper = Ice.GoogleMap.getGMapWrapper(id);
        var map = Ice.GoogleMap.getGMapWrapper(id).getRealGMap();
        service=new google.maps.DirectionsService();
		var origin = (from == "(") ? "origin: new google.maps.LatLng" + from + ", " : "origin: \"" + from + "\", ";
		var destination = (to == "(") ? "destination: new google.maps.LatLng" + to + ", " : "destination: \"" + to + "\", ";
		var request = "({" + origin + destination +"travelMode:google.maps.TravelMode.DRIVING})";
		var directionsCallback = function(response, status) {
			if (status != google.maps.DirectionsStatus.OK) {
				alert('Error was: ' + status);
				} else {
					var renderer = (wrapper.directions[id] != null) ? wrapper.directions[id] : new google.maps.DirectionsRenderer();
					renderer.setMap(null);
					renderer.setMap(map);
					renderer.setDirections(response);
					if (textDivId) {
						var textDiv = $(textDivId);
						if (textDiv) {
							textDiv.innerHTML = '';
							renderer.setPanel(textDiv);
						}
					}
					wrapper.directions[id] = renderer;
				}
			};
		service.route(eval(request), directionsCallback);
    },

    addOverlay:function (ele, overlayId, ovrLay) {
        var gmapWrapper = Ice.GoogleMap.getGMapWrapper(ele);
        var overlay = gmapWrapper.overlays[overlayId];
        if (overlay == null) {
            overlay = eval(ovrLay);
            gmapWrapper.getRealGMap().addOverlay(overlay);
            gmapWrapper.overlays[overlayId] = overlay;
        }
    },

    removeOverlay:function(ele, overlayId) {
        var gmapWrapper = Ice.GoogleMap.getGMapWrapper(ele);
        var overlay = gmapWrapper.overlays[overlayId];
        if (overlay != null) {
            gmapWrapper.getRealGMap().removeOverlay(overlay);
        } else {
            //nothing found just return
           return;
        }
        var newOvrLyArray = new Object();
        for (overlayObj in gmapWrapper.overlays) {
            if (overlayId != overlayObj) {
                newOvrLyArray[overlayObj] = gmapWrapper.overlays[overlayObj];
            }
        }
        gmapWrapper.overlays = newOvrLyArray;
    },
	
	addKML:function (ele, overlayId, URL) {
        var gmapWrapper = Ice.GoogleMap.getGMapWrapper(ele);
		var map = Ice.GoogleMap.getGMapWrapper(ele).getRealGMap();
		var overlay = gmapWrapper.overlays[overlayId];
        if (overlay == null || overlay.getMap() == null) {
            var ctaLayer = new google.maps.KmlLayer(URL);
			ctaLayer.setMap(map); 
			gmapWrapper.overlays[overlayId] = ctaLayer;
        }
    },
	
    removeKML:function(ele, overlayId) {
        var gmapWrapper = Ice.GoogleMap.getGMapWrapper(ele);
        var overlay = gmapWrapper.overlays[overlayId];
        if (overlay != null) {
            overlay.setMap(null);
        } else {
            //nothing found just return
            return;
        }
        var newOvrLyArray = new Object();
        for (overlayObj in gmapWrapper.overlays) {
            if (overlay != overlayObj) {
                newOvrLyArray[overlayObj] = gmapWrapper.overlays[overlayObj];
            }
        }
        gmapWrapper.overlays = newOvrLyArray;
    },
	
	addMarker:function (ele, overlayId, marker) {
        var gmapWrapper = Ice.GoogleMap.getGMapWrapper(ele);
		var map = Ice.GoogleMap.getGMapWrapper(ele).getRealGMap();
		var overlay = gmapWrapper.overlays[overlayId];
        if (overlay == null || overlay.getMap() == null) {
            var mapMarker = eval(marker); 
			gmapWrapper.overlays[overlayId] = mapMarker;
        }
    },

    removeMarker:function(ele, overlayId) {
        var gmapWrapper = Ice.GoogleMap.getGMapWrapper(ele);
        var overlay = gmapWrapper.overlays[overlayId];
        if (overlay != null) {
            overlay.setMap(null);
        } else {
            //nothing found just return
            return;
        }
        var newOvrLyArray = new Object();
        for (overlayObj in gmapWrapper.overlays) {
            if (overlay != overlayObj) {
                newOvrLyArray[overlayObj] = gmapWrapper.overlays[overlayObj];
            }
        }
        gmapWrapper.overlays = newOvrLyArray;
    },
    
    locateAddress:function (clientId, address) {
		var map = Ice.GoogleMap.getGMapWrapper(clientId).getRealGMap();
        var geocoder = new google.maps.Geocoder();
		geocoder.geocode( {'address': address}, function(results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				map.setCenter(results[0].geometry.location);
				Ice.GoogleMap.showMarkerAndInfoWindow(map, Ice.GoogleMap.getGMapWrapper(clientId), results[0]);
			} else {
				alert("Geocode was not successful for the following reason: " + status);
			}
		}); 
	},
	
	showMarkerAndInfoWindow: function(map, wrapper, result) {
		if (!wrapper.currentMarker || !wrapper.currentInfoWindow) {
			wrapper.currentMarker = new google.maps.Marker({draggable:false});
			wrapper.currentInfoWindow = new google.maps.InfoWindow({});
		}
		wrapper.currentMarker.setMap(map);
		wrapper.currentMarker.setPosition(result.geometry.location);
		wrapper.currentInfoWindow.setPosition(result.geometry.location);
		wrapper.currentInfoWindow.setContent(result.formatted_address);
		wrapper.currentInfoWindow.open(map);
	},

    create:function (ele) {
        var gmapWrapper = new GMapWrapper(ele, new google.maps.Map(document.getElementById(ele),{mapTypeId: google.maps.MapTypeId.ROADMAP, zoom:8, center: new google.maps.LatLng(0,0), disableDefaultUI:"true"}));
		var hiddenField = document.getElementById(ele + 'hdn');
        var mapTypedRegistered = false;
		
        initializing = false;
        GMapRepository[ele] = gmapWrapper;
        return gmapWrapper;
    },
	
    submitEvent: function(ele, map, eventName, zoomLevel) {
        try {
            var center = map.getCenter();
            var lat = $(ele + 'lat');
            var lng = $(ele + 'lng');
            var event = $(ele + 'event');
            var zoom = $(ele + 'zoom');
            var type = $(ele + 'type');
            lat.value = center.lat();
            lng.value = center.lng();
            event.value = eventName;
            if (zoomLevel == null) {
                zoom.value = map.getZoom();
            } else {
                zoom.value = zoomLevel;
                if (zoom.value == map.getZoom()) {
                    return;
                }
            }
            var form = Ice.util.findForm(lat);
            var nothingEvent = new Object();
            iceSubmitPartial(form, lat, nothingEvent);
            //reset event value, so the decode method of gmap can
            //make deceison before decode
            event.value = "";
        } catch(e) {
        }
    },

    recreate:function(ele, gmapWrapper) {
        Ice.GoogleMap.remove(ele);
        var controls = gmapWrapper.controls;
        var geoMarker = gmapWrapper.geoMarker;
        var geoMarkerAddress = gmapWrapper.geoMarkerAddress;
        gmapWrapper = Ice.GoogleMap.create(ele);
        gmapWrapper.geoMarker = geoMarker;
        gmapWrapper.geoMarkerAddress = geoMarkerAddress;
        gmapWrapper.geoMarkerSet = 'true';
        var tempObject = new Object();
        for (control in controls) {
            if (tempObject[control] == null) {
                Ice.GoogleMap.removeControl(ele, control);
                Ice.GoogleMap.addControl(ele, control)
            }
        }
        return gmapWrapper;
    },

    addControl:function(ele, controlName) {
        var gmapWrapper = Ice.GoogleMap.getGMapWrapper(ele);
        var control = gmapWrapper.controls[controlName];
        if (control == null) {
			gmapWrapper.controls[controlName] = controlName;
			var mapOption = {};
			if (controlName == 'GScaleControl') controlName = 'scaleControl';
			else if (controlName == 'GMapTypeControl') {
				controlName = 'mapTypeControl';
				mapOption.mapTypeControlOptions = { 
					mapTypeIds: [ google.maps.MapTypeId.ROADMAP, google.maps.MapTypeId.SATELLITE, google.maps.MapTypeId.HYBRID ] 
				}; 
			} else if (controlName == 'GOverviewMapControl') {
				controlName = 'overviewMapControl';
				mapOption.overviewMapControlOptions = { opened: true };
			} else if (controlName == 'GSmallZoomControl') {
				controlName = 'zoomControl';
				mapOption.zoomControlOptions = { style: google.maps.ZoomControlStyle.SMALL }; 
			} else if (controlName == 'GSmallMapControl') {
				controlName = 'panControl';
				mapOption.zoomControl = true;
				mapOption.zoomControlOptions = { style: google.maps.ZoomControlStyle.SMALL }; 
			} else if (controlName == 'GLargeMapControl') {
				controlName = 'panControl';
				mapOption.zoomControl = true;
				mapOption.zoomControlOptions = { style: google.maps.ZoomControlStyle.LARGE }; 
			}
			mapOption[controlName] = true;
            gmapWrapper.getRealGMap().setOptions(mapOption);
        }
    },

    removeControl:function(ele, controlName) {
        var gmapWrapper = Ice.GoogleMap.getGMapWrapper(ele);
        var control = gmapWrapper.controls[controlName];
        if (control != null) {
			var newCtrlArray = new Object();
			for (ctrl in gmapWrapper.controls) {
				if (controlName != ctrl) {
					newCtrlArray[ctrl] = gmapWrapper.controls[ctrl];
				}
			}
			gmapWrapper.controls = newCtrlArray;
			var mapOption = {};
			if (controlName == 'GScaleControl') controlName = 'scaleControl';
			else if (controlName == 'GMapTypeControl') controlName = 'mapTypeControl';
			else if (controlName == 'GOverviewMapControl') controlName = 'overviewMapControl';
			else if (controlName == 'GSmallZoomControl') {
				if (!gmapWrapper.controls['GSmallMapControl'] && !gmapWrapper.controls['GLargeMapControl']) {
					controlName = 'zoomControl';
				}
				if (gmapWrapper.controls['GLargeMapControl']) {
					mapOption.zoomControlOptions = { style: google.maps.ZoomControlStyle.LARGE }; 
				}
			} else if (controlName == 'GSmallMapControl') {
				if (gmapWrapper.controls['GLargeMapControl']) {
					mapOption.zoomControlOptions = { style: google.maps.ZoomControlStyle.LARGE }; 
				} else if (gmapWrapper.controls['GSmallZoomControl']) {
					controlName = 'panControl';
				} else {
					controlName = 'panControl';
					mapOption.zoomControl = false;
				}
			} else if (controlName == 'GLargeMapControl') {
				if (gmapWrapper.controls['GSmallMapControl']) {
					mapOption.zoomControlOptions = { style: google.maps.ZoomControlStyle.SMALL }; 
				} else if (gmapWrapper.controls['GSmallZoomControl']) {
					controlName = 'panControl';
					mapOption.zoomControlOptions = { style: google.maps.ZoomControlStyle.SMALL }; 
				} else {
					controlName = 'panControl';
					mapOption.zoomControl = false;
				}
			}
			mapOption[controlName] = false;
            gmapWrapper.getRealGMap().setOptions(mapOption);
        }
    },

    remove:function(ele) {
        var newRepository = new Array();
        for (map in GMapRepository) {
            if (map != ele) {
                newRepository[map] = GMapRepository[map];
            }
        }
        GMapRepository = newRepository;
    },

    setMapType:function(ele, type) {
        var gmapWrapper = Ice.GoogleMap.getGMapWrapper(ele);
        //if the chart is recreated, so add any geoCoderMarker that was exist before.
        if (gmapWrapper.geoMarkerSet
                && gmapWrapper.geoMarker != null
                && gmapWrapper.geoMarkerAddress != null
                )
        {
            gmapWrapper.getRealGMap().addOverlay(gmapWrapper.geoMarker);
            gmapWrapper.geoMarker.openInfoWindowHtml(gmapWrapper.geoMarkerAddress);
            gmapWrapper.geoMarkerSet = false;
        }
        if (gmapWrapper.getRealGMap().getMapTypeId() != null) {
            switch (type) {
                case "Satellite":
                    gmapWrapper.getRealGMap().setMapTypeId(google.maps.MapTypeId.SATELLITE);
                    break
                case "Hybrid":
                    gmapWrapper.getRealGMap().setMapTypeId(google.maps.MapTypeId.HYBRID);
                    break
                case "Map":
                    gmapWrapper.getRealGMap().setMapTypeId(google.maps.MapTypeId.ROADMAP);
                    break
				case "Terrain":
                    gmapWrapper.getRealGMap().setMapTypeId(google.maps.MapTypeId.TERRAIN);
                    break
                }//switch
        }//outer if        
    }//setMapType    
}

