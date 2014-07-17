/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
if (!window['mobi']) {
	window.mobi = {};
}
if (!window.mobi['gmap']) {
	window.mobi.gmap = {};
}
mobi.gmap.repo = new Object();
(function() {
	
	function GMapWrapper(clientId) {
	    var ts = new Date().getTime();
		var id = clientId;
		var ele = document.getElementById(clientId);
		if( ele ){
		    var map = new google.maps.Map(ele, {mapTypeId : google.maps.MapTypeId.ROADMAP});
	        var mapTypedRegistered = false;
	        var initializing = false;
	        var controls = new Object();
	        var overlays = new Object();
	        var geoMarker = new Object();
	        var directions = new Object();
	        var services = new Object();
	        var markers = new Object();
	        markers.ts = new Date().getTime();
	        var layers = new Object();
	        var geoMarkerAddress;
	        var geoMarkerSet = false;
	        /*
	        if (cfg.dynamic && cfg.cache) {
	            this.markAsLoaded(this.jq.children('div').get(this.cfg.active));
	        }*/
		}

		return {
		    getTimestamp : function(){
				return ts;
			},
			getMap : function(){
				return map;
			},
			addOverlay: function(overlayId, ovrLay) {
				var overlay = overlays[overlayId];
				if (overlay == null) {
					overlay = eval(ovrLay);
					map.addOverlay(overlay);
					overlays[overlayId] = overlay;
				}
			},
			loadDirection: function(from, to) {
				var service = new google.maps.DirectionsService();
				var origin = (from == "(") ? "origin: new google.maps.LatLng"
						+ from + ", " : "origin: \"" + from + "\", ";
				var destination = (to == "(") ? "destination: new google.maps.LatLng"
						+ to + ", "
						: "destination: \"" + to + "\", ";
				var request = "({" + origin + destination
						+ "travelMode:google.maps.TravelMode.DRIVING})";
				function directionsCallback(response, status) {
					if (status != google.maps.DirectionsStatus.OK) {
						alert('Error was: ' + status);
					} else {
						var renderer = (directions[id] != null) ? directions[id]
								: new google.maps.DirectionsRenderer();
						renderer.setMap(map);
						renderer.setDirections(response);
						directions[id] = renderer;

					}
				}
				service.route(eval(request), directionsCallback);
			},
			getGDirection : function(text_div) {
				var gdirection = mobi.gmap.repo[id + 'dir'];
				if (gdirection == null) {
					var directionsPanel = document.getElementById(text_div);
					var dir = new GDirections(map,directionsPanel);
					mobi.gmap.repo[id + 'dir'] = dir;
					return dir;
				} else {
					return gdirection;
				}
			},
			getGeocoder : function() {
				var geocoder = mobi.gmap.repo[id + 'geo'];
				if (geocoder == null) {
					mobi.gmap.repo[id + 'geo'] = new GClientGeocoder();
					return mobi.gmap.repo[id + 'geo'];
				} else {
					return geocoder;
				}
			},
			setMapType : function(type) {
				// if the chart is recreated, so add any geoCoderMarker that was exist
				// before.
				if (geoMarkerSet && geoMarker != null && geoMarkerAddress != null) {
					map.addOverlay(geoMarker);
					geoMarker.openInfoWindowHtml(geoMarkerAddress);
					geoMarkerSet = false;
				}
				if (type == "MAP"){
					type = "ROADMAP";
				}
				if (map.getMapTypeId() != null) {
					var g = google.maps;
					switch (type) {
					case "SATELLITE":
						map.setMapTypeId(g.MapTypeId.SATELLITE);
						break;
					case "HYBRID":
						map.setMapTypeId(g.MapTypeId.HYBRID);
						break;
					case "ROADMAP":
						map.setMapTypeId(g.MapTypeId.ROADMAP);
						break;
					case "TERRAIN":
						map.setMapTypeId(g.MapTypeId.TERRAIN);
						break;
					}
				}
			},
			removeOverlay : function(id) {
				var overlay = overlays[id];
				if (overlay != null) {
					map.removeOverlay(overlay);
					delete overlays[id];
				}
			},
			addMapLayer : function(layerId, layerType, sentOptions, url) {
				var layer;
				if (sentOptions == "Skip")
					var options = "";
				else
					var options = sentOptions;
				switch (layerType.toLowerCase()) {
				case "bicycling":
				case "bicyclinglayer":
				case "bicycle":
					layer = new google.maps.BicyclingLayer();
					layer.setMap(map);
					break;
				case "fusion":
				case "fusiontable":
				case "fusiontables":
					// This is still in it's experimental stage, and I can't get
					// access to the API to make my own fusion table yet. (Google
					// Trusted Testers Only)
					// So I cannot verify if it works. Double check when Fusion
					// Tables is properly released.
					var markerOps = "({" + options + "})";
					layer = new google.maps.FusionTablesLayer(eval(options));
					layer.setMap(map);
					break;
				case "kml":
				case "kmllayer":
					var markerOps = "({" + options + "})";
					layer = new google.maps.KmlLayer(url, eval(options));
					layer.setMap(map);
					break;
				case "traffic":
				case "trafficlayer":
					layer = new google.maps.TrafficLayer();
					layer.setMap(map);
					break;
				case "transit":
				case "transitlayer":
					layer = new google.maps.TransitLayer();
					layer.setMap(map);
					break;
				default:
					console.log("ERROR: Not a valid layer type");
					return;
				}// switch
				layers[layerId] = layer;
			},
			removeMapLayer : function(id) {
				var layer = layers[id];
				if (layer != null) {
					layer.setMap(null);
					delete layers[id];
				}
			},
			locateAddress : function(address) {
				var geocoder = new google.maps.Geocoder();
				geocoder.geocode({'address' : address}, function(results, status) {
					if (status == google.maps.GeocoderStatus.OK) {
						map.setCenter(results[0].geometry.location);
					} else {
						alert("Geocode was not successful for the following reason: "
								+ status);
					}
				});
			},
			addControl : function(controlName) {
				var control = controls[controlName];
				if (control == null) {
					var mapOption = "({" + controlName + ":true})";
					map.setOptions(eval(mapOption));
					controls[controlName] = controlName;
				}
			},
			removeControl : function(controlName) {
				var control = controls[controlName];
				if (control != null) {
					var mapOption = "({" + controlName + ":false})";
					map.setOptions(eval(mapOption));
					delete controls[controlName]
				}
			},
			remove : function() {
				delete mobi.gmap.repo[id];
			},
			addMarker : function(id, lat, lon) {
				var ll = new google.maps.LatLng(lat, lon);
		        var m = new google.maps.Marker({
		            position: ll,
		            map: map
		        });
		        markers[id] = m;
			},
			removeMarker : function(id) {
				var m = markers[id];
				if( m ){
					m.setMap(null);
					delete markers[id];
				}
			},
			addOptions : function(options) {
				var fullOps = "({" + options + "})";
				map.setOptions(eval(fullOps));
			},
			gService : function(name, locationList, options) {
				var service;
				var points = locationList.split(":");
				switch (name.toLowerCase()) {
				case "direction":
				case "directions":
				case "directionsservice":
					// Required options: travelMode, 2 points/addresses
					// (First=origin, last=dest, others=waypoints
					service = new google.maps.DirectionsService();
					var origin = (points[0].charAt(0) == "(") ? "origin: new google.maps.LatLng"
							+ points[0] + ", "
							: "origin: \"" + points[0] + "\", ";
					var lastElement = points.length - 1;
					var destination = (points[lastElement].charAt(0) == "(") ? "destination: new google.maps.LatLng"
							+ points[lastElement] + ", "
							: "destination: \"" + points[lastElement] + "\", ";
					if (points.length >= 3) {
						var waypoints = [];
						for ( var i = 1; i < points.length - 1; i++) {
							var point = (points[i].charAt(0) == "(") ? "{location:new google.maps.LatLng"
									+ points[i] + "}"
									: "{location:\"" + points[i] + "\"}";
							waypoints[i - 1] = point;
						}
						var waypointsString = "waypoints: [" + waypoints + "], ";
						var request = "({" + origin + destination + waypointsString
								+ options + "})";
					} else {
						var request = "({" + origin + destination + options + "})";
					}
					function directionsCallback(response, status) {
						if (status != google.maps.DirectionsStatus.OK) {
							alert('Error was: ' + status);
						} else {
							var renderer = (services[ele] != null) ? services[ele]
									: new google.maps.DirectionsRenderer(); //TODO check
							renderer.setMap(map);
							renderer.setDirections(response);
							services[ele] = renderer;
						}
					}
					service.route(eval(request), directionsCallback);
					break;
				case "elevation":
				case "elevationservice":
					service = new google.maps.ElevationService();
					var waypoints = [];
					for ( var i = 0; i < points.length; i++) {
						var point = "new google.maps.LatLng" + points[i];
						waypoints[i] = point;
					}
					var waypointsString = "locations: [" + waypoints + "]";
					var request = "({" + waypointsString + "})";
					function elevationCallback(response, status) {
						if (status != google.maps.ElevationStatus.OK) {
							alert('Error was: ' + status);
						} else {
							for ( var i = 0; i < response.length; i++) {
								alert(response[i].elevation);
							}
						}
					}
					service.getElevationForLocations(eval(request),
							elevationCallback);
					break;
				case "maxzoom":
				case "maxzoomservice":
					service = new google.maps.MaxZoomService();
					var point = eval("new google.maps.LatLng" + points[0]);
					function maxZoomCallback(response) {
						if (response.status != google.maps.MaxZoomStatus.OK) {
							alert('Error occurred in contacting Google servers');
						} else {
							alert("Max zoom at point is: " + response.zoom);
						}
					}
					service.getMaxZoomAtLatLng(point, maxZoomCallback);
					break;
				case "distance":
				case "distancematrix":
				case "distancematrixservice":
					// Required options: travelMode, 2 points/addresses
					service = new google.maps.DistanceMatrixService();
					var origin = (points[0].charAt(0) == "(") ? "origins: [new google.maps.LatLng"
							+ points[0] + "], "
							: "origins: [\"" + points[0] + "\"], ";
					var destination = (points[1].charAt(0) == "(") ? "destinations: [new google.maps.LatLng"
							+ points[1] + "], "
							: "destinations: [\"" + points[1] + "\"], ";
					var request = "({" + origin + destination + options + "})";
					function distanceCallback(response, status) {
						if (status != google.maps.DistanceMatrixStatus.OK) {
							alert('Error was: ' + status);
						} else {
							alert("Distance is:"
									+ response.rows[0].elements[0].distance.text
									+ " in "
									+ response.rows[0].elements[0].duration.text);
						}
					}
					service.getDistanceMatrix(eval(request), distanceCallback);
					break;
				default:
					console.log("Not a valid service name");
					return;
				}// switch
			},
			removeGOverlay : function(overlayID) {
				if (overlays[overlayID] != null) {
					overlays[overlayID].setMap(null);
				}
			},
			gOverlay : function(overlayID, shape, locationList, options) {
				var overlay;
				var points = locationList.split(":");
				for ( var i = 0; i < points.length; i++) {
					points[i] = "new google.maps.LatLng" + points[i] + "";
				}
				switch (shape.toLowerCase()) {
				case "line":
				case "polyline":
					var overlayOptions = (options != null && options.length > 0) ? "({map:map, path:["
							+ points + "], " + options + "})"
							: "({map:map, path:[" + points + "]})";
					overlay = new google.maps.Polyline(eval(overlayOptions));
					break;
				case "polygon":
					var overlayOptions = (options != null && options.length > 0) ? "({map:map, paths:["
							+ points + "], " + options + "})"
							: "({map:map, paths:[" + points + "]})";
					overlay = new google.maps.Polygon(eval(overlayOptions));
					break;
				case "rectangle":
					// needs SW corner in first point, NE in second
					var overlayOptions = (options != null && options.length > 0) ? "({map:map, bounds:new google.maps.LatLngBounds("
							+ points[0] + "," + points[1] + "), " + options + "})"
							: "({map:map, bounds:new google.maps.LatLngBounds("
									+ points[0] + "," + points[1] + ")})";
					overlay = new google.maps.Rectangle(eval(overlayOptions));
					break;
				case "circle":
					// Requires radius option
					var overlayOptions = (options != null && options.length > 0) ? "({map:map, center: "
							+ points[0] + ", " + options + "})"
							: "({map:map, center: " + points[0] + "})";
					overlay = new google.maps.Circle(eval(overlayOptions));
					break;
				default:
					console.log("Not a valid shape");
					return;
				}// switch
				overlays[overlayID] = overlay;
			}
		}
	}
	
	mobi.gmap.repo = new Object();

	mobi.gmap.getWrapper = function(id) {
		var wrapper = mobi.gmap.repo[id];
		if (!wrapper) {
			wrapper = mobi.gmap.create(id);
		}
		return wrapper;
	}

	mobi.gmap.create = function(id){
		var oldWrapper = mobi.gmap.repo[id];
		if( oldWrapper ){
			oldWrapper.remove();
		}
		delete mobi.gmap.repo[id];
		
		var wrapper = GMapWrapper(id);
		wrapper.initializing = false;
		mobi.gmap.repo[id] = wrapper;
		return wrapper;
	}

})();

