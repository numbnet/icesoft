/**
 *  gMap Widget
 */
ice.ace.gMap = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id);
    this.jq = ice.ace.jq(this.jqId + '_gMap');
    this.stateHolder = ice.ace.jq(this.jqId + '_active');
    var _self = this;

    //Create accordion
    this.jq.gMap(this.cfg);

    if(this.cfg.dynamic && this.cfg.cache) {
        this.markAsLoaded(this.jq.children('div').get(this.cfg.active));
    }
}

var GMapRepository = new Array();

function GMapWrapper(eleId, realGMap){
    this.eleId = eleId;
    this.realGMap = realGMap;
    this.controls = new Object();
    this.overlays = new Object();
    this.geoMarker = new Object();
    this.directions = new Object();
    this.services = new Object();
    this.layers = new Object();
    this.geoMarkerAddress;
    this.geoMarkerSet = false;

}
GMapWrapper.prototype.getElementId = function() {
    return this.eleId;
}

GMapWrapper.prototype.getRealGMap = function() {
        return this.realGMap;
}

GMapWrapper.prototype.getControlsArray = function(){
        return this.controls;
}


ice.ace.gMap.getGeocoder = function(id) {
        var geocoder = GMapRepository[id + 'geo'];
        if (geocoder == null) {
            GMapRepository[id + 'geo'] = new GClientGeocoder();
            return GMapRepository[id + 'geo'];
        } else {
            return geocoder;
        }
    }

ice.ace.gMap.getGDirection = function(id, text_div) {
        var gdirection = GMapRepository[id + 'dir'];
        if (gdirection == null) {
            var map = ice.ace.gMap.getGMapWrapper(id).getRealGMap();
            var directionsPanel = document.getElementById(text_div);
            GMapRepository[id + 'dir'] = new GDirections(map, directionsPanel);
            return GMapRepository[id + 'dir'];
        } else {
            return gdirection;
        }
    },

ice.ace.gMap.getGMapWrapper = function (id) {
        var gmapWrapper = GMapRepository[id];
        if (gmapWrapper) {
            var gmapComp = document.getElementById(id);
            //the googlemap view must be unrendered, however
            //javascript object still exist, so recreate the googlemap
            //with its old state.
            if (!gmapComp.hasChildNodes()) {
                gmapWrapper = ice.ace.gMap.recreate(id, gmapWrapper);
            }
        } else {
            //googleMap not found create a fresh new googleMap object
            gmapWrapper = ice.ace.gMap.create(id);
        }
        return gmapWrapper;
    },

ice.ace.gMap.loadDirection = function(id,from, to) {
        var wrapper = ice.ace.gMap.getGMapWrapper(id);
        var map = ice.ace.gMap.getGMapWrapper(id).getRealGMap();
        service=new google.maps.DirectionsService();
        var origin = (from == "(") ? "origin: new google.maps.LatLng" + from + ", " : "origin: \"" + from + "\", ";
        var destination = (to == "(") ? "destination: new google.maps.LatLng" + to + ", " : "destination: \"" + to + "\", ";
        var request = "({" + origin + destination +"travelMode:google.maps.TravelMode.DRIVING})";
        function directionsCallback(response, status) {
            if (status != google.maps.DirectionsStatus.OK) {
                alert('Error was: ' + status);
            } else {
                var renderer = (wrapper.directions[id] != null)? wrapper.directions[id]:new google.maps.DirectionsRenderer();
                renderer.setMap(map);
                renderer.setDirections(response);
                wrapper.directions[id]=renderer;
            }
        }
        service.route(eval(request), directionsCallback);
    },

ice.ace.gMap.addOverlay = function (ele, overlayId, ovrLay) {
        var gmapWrapper = ice.ace.gMap.getGMapWrapper(ele);
        var overlay = gmapWrapper.overlays[overlayId];
        if (overlay == null) {
            overlay = eval(ovrLay);
            gmapWrapper.getRealGMap().addOverlay(overlay);
            gmapWrapper.overlays[overlayId] = overlay;
        }
    },

ice.ace.gMap.removeOverlay = function(ele, overlayId) {
        var gmapWrapper = ice.ace.gMap.getGMapWrapper(ele);
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

ice.ace.gMap.addMapLayer = function (ele, layerId, layerType, url, options) {
        var gmapWrapper = ice.ace.gMap.getGMapWrapper(ele);
        var layer;
        switch (layerType) {
            case "Bicycling":
            case "BicyclingLayer":
                layer=new google.maps.BicyclingLayer();
                layer.setMap(gmapWrapper.getRealGMap());
                break;
            case "Fusion":
            case "FusionTable":
            case "FusionTables":
                //This is still in it's experimental stage, and I can't get access to the API to make my own fusion table yet. (Trusted Testers Only)
                //So I cannot verify if it works. Double check when Fusion Tables is properly released.
                var markerOps = "({" + options + "})";
                layer=new google.maps.FusionTablesLayer(eval(options));
                layer.setMap(gmapWrapper.getRealGMap());
                break;
            case "Kml":
            case "KmlLayer":
                var markerOps = "({" + options + "})";
                layer=new google.maps.KmlLayer(url, eval(options));
                layer.setMap(gmapWrapper.getRealGMap());
                break;
            case "Traffic":
            case "TrafficLayer":
                layer=new google.maps.TrafficLayer();
                layer.setMap(gmapWrapper.getRealGMap());
                break;
            default:
                console.log("ERROR: Not a valid layer type");
                return;
        }//switch
        gmapWrapper.layers[layerId]=layer;

    },

ice.ace.gMap.removeMapLayer = function(ele, layerId) {
        var gmapWrapper = ice.ace.gMap.getGMapWrapper(ele);
        var layer = gmapWrapper.layers[layerId];
        if (layer != null) {
            layer.setMap(null);
        } else {
            //nothing found just return
            return;
        }
        var newLayerArray = new Object();
        for (layerObj in gmapWrapper.layers) {
            if (layerId != layerObj) {
                newLayerArray[layerObj] = gmapWrapper.layers[layerObj];
            }
        }
        gmapWrapper.layers = newLayerArray;
    },

ice.ace.gMap.locateAddress = function (clientId, address) {
        var map = ice.ace.gMap.getGMapWrapper(clientId).getRealGMap();
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode( {'address': address}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                map.setCenter(results[0].geometry.location);
            } else {
                alert("Geocode was not successful for the following reason: " + status);
            }
        });
    },

ice.ace.gMap.create = function (ele) {
        var gmapWrapper = new GMapWrapper(ele, new google.maps.Map(document.getElementById(ele),{mapTypeId: google.maps.MapTypeId.ROADMAP}));
        var hiddenField = document.getElementById(ele);
        var mapTypedRegistered = false;

        initializing = false;
        GMapRepository[ele] = gmapWrapper;
        return gmapWrapper;
    },

ice.ace.gMap.submitEvent = function(ele, map, eventName, zoomLevel) {
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

ice.ace.gMap.recreate = function(ele, gmapWrapper) {
        ice.ace.gMap.remove(ele);
        var controls = gmapWrapper.controls;
        var geoMarker = gmapWrapper.geoMarker;
        var geoMarkerAddress = gmapWrapper.geoMarkerAddress;
        gmapWrapper = ice.ace.gMap.create(ele);
        gmapWrapper.geoMarker = geoMarker;
        gmapWrapper.geoMarkerAddress = geoMarkerAddress;
        gmapWrapper.geoMarkerSet = 'true';
        var tempObject = new Object();
        for (control in controls) {
            if (tempObject[control] == null) {
                ice.ace.gMap.removeControl(ele, control);
                ice.ace.gMap.addControl(ele, control)
            }
        }
        return gmapWrapper;
    },

ice.ace.gMap.addControl = function(ele, controlName) {
        var gmapWrapper = ice.ace.gMap.getGMapWrapper(ele);
        var control = gmapWrapper.controls[controlName];
        if (control == null) {
            var mapOption = "({"+ controlName +":true})";
            gmapWrapper.getRealGMap().setOptions(eval(mapOption));
            gmapWrapper.controls[controlName] = controlName;
        }
    },

ice.ace.gMap.removeControl = function(ele, controlName) {
        var gmapWrapper = ice.ace.gMap.getGMapWrapper(ele);
        var control = gmapWrapper.controls[controlName];
        if (control != null) {
            var mapOption = "({"+ controlName +":false})";
            gmapWrapper.getRealGMap().setOptions(eval(mapOption));
        }
        var newCtrlArray = new Object();
        for (control in gmapWrapper.controls) {
            if (controlName != control) {
                newCtrlArray[control] = gmapWrapper.controls[control];
            }
        }
        gmapWrapper.controls = newCtrlArray;
    },

ice.ace.gMap.remove = function(ele) {
        var newRepository = new Array();
        for (map in GMapRepository) {
            if (map != ele) {
                newRepository[map] = GMapRepository[map];
            }
        }
        GMapRepository = newRepository;
    },

ice.ace.gMap.addMarker = function(ele, markerID, Lat, Lon, options) {
        var wrapper = ice.ace.gMap.getGMapWrapper(ele);
        var overlay = wrapper.overlays[markerID];
        if (overlay == null || overlay.getMap() == null) {
            var markerOps = "({map:wrapper.getRealGMap(), position: new google.maps.LatLng(" + Lat + "," + Lon + "), " + options + "});";
            var marker = new google.maps.Marker(eval(markerOps));
            wrapper.overlays[markerID] = marker;
        }
    },

ice.ace.gMap.removeMarker = function(ele, overlayId) {
        var gmapWrapper = ice.ace.gMap.getGMapWrapper(ele);
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

ice.ace.gMap.addOptions = function(ele, options){
        var map = ice.ace.gMap.getGMapWrapper(ele).getRealGMap();
        var fullOps="({" + options + "})";
        map.setOptions(eval(fullOps));
    },

ice.ace.gMap.gService = function(ele, name, locationList, options)
    {
        var wrapper = ice.ace.gMap.getGMapWrapper(ele);
        var map = ice.ace.gMap.getGMapWrapper(ele).getRealGMap();
        var service;
        var points = locationList.split(":");
        switch (name) {
            case "directions":
            case "Directions":
            case "DirectionsService":
                //Required options: travelMode, 2 points/addresses (First=origin, last=dest, others=waypoints
                service=new google.maps.DirectionsService();
                var origin = (points[0].charAt(0) == "(") ? "origin: new google.maps.LatLng" + points[0] + ", " : "origin: \"" + points[0] + "\", ";
                var lastElement = points.length - 1;
                var destination = (points[lastElement].charAt(0) == "(") ? "destination: new google.maps.LatLng" + points[lastElement] + ", " : "destination: \"" + points[lastElement] + "\", ";
                if(points.length >= 3){
                    var waypoints = [];
                    for (var i = 1; i < points.length-1; i++){
                        var point = (points[i].charAt(0) == "(") ? "{location:new google.maps.LatLng" + points[i] + "}": "{location:\"" + points[i] + "\"}";
                        waypoints[i-1] = point;
                    }
                    var waypointsString = "waypoints: [" + waypoints + "], ";
                    var request = "({" + origin + destination + waypointsString + options + "})";
                }else{
                    var request = "({" + origin + destination + options + "})";
                }
            function directionsCallback(response, status) {
                if (status != google.maps.DirectionsStatus.OK) {
                    alert('Error was: ' + status);
                } else {
                    var renderer = (wrapper.services[ele] != null)? wrapper.services[ele]:new google.maps.DirectionsRenderer();
                    renderer.setMap(map);
                    renderer.setDirections(response);
                    wrapper.services[ele]=renderer;
                }
            }
                service.route(eval(request), directionsCallback);
                break;
            case "elevation":
            case "Elevation":
            case "ElevationService":
                //Required options: travelMode, 2 points/addresses
                service=new google.maps.ElevationService();
                var waypoints = [];
                for (var i = 0; i < points.length; i++){
                    var point = "new google.maps.LatLng" + points[i];
                    waypoints[i] = point;
                }
                var waypointsString = "locations: [" + waypoints + "]";
                var request = "({"+ waypointsString +"})";
                alert(request);
            function elevationCallback(response, status) {
                if (status != google.maps.ElevationStatus.OK) {
                    alert('Error was: ' + status);
                } else {
                    for (var i = 0; i < response.length; i++){
                        alert(response[i].elevation);
                    }
                }
            }
                service.getElevationForLocations(eval(request), elevationCallback);
                break;
            case "maxZoom":
            case "MaxZoom":
            case "MaxZoomService":
                service=new google.maps.MaxZoomService();
                var point = eval("new google.maps.LatLng" + points[0]);
            function maxZoomCallback(response){
                if (response.status != google.maps.MaxZoomStatus.OK) {
                    alert('Error occurred in contacting Google servers');
                } else {
                    alert("Max zoom at point is: " + response.zoom);
                }
            }
                service.getMaxZoomAtLatLng(point, maxZoomCallback);
                break;
            case "distance":
            case "Distance":
            case "DistanceMatrix":
            case "DistanceMatrixService":
                //Required options: travelMode, 2 points/addresses
                service=new google.maps.DistanceMatrixService();
                var origin = (points[0].charAt(0) == "(") ? "origins: [new google.maps.LatLng" + points[0] + "], " : "origins: [\"" + points[0] + "\"], ";
                var destination = (points[1].charAt(0) == "(") ? "destinations: [new google.maps.LatLng" + points[1] + "], " : "destinations: [\"" + points[1] + "\"], ";
                var request = "({" + origin + destination + options + "})";
            function distanceCallback(response, status) {
                if (status != google.maps.DistanceMatrixStatus.OK) {
                    alert('Error was: ' + status);
                } else {
                    alert("Distance is:" + response.rows[0].elements[0].distance.text + " in " + response.rows[0].elements[0].duration.text);
                }
            }
                service.getDistanceMatrix(eval(request), distanceCallback);
                break;
            default:
                console.log("Not a valid service name");
                return;
        }//switch
    },

ice.ace.gMap.removeGOverlay = function(ele, overlayID){
        var wrapper = ice.ace.gMap.getGMapWrapper(ele);
        if (wrapper.overlays[overlayID] != null)
        {
            wrapper.overlays[overlayID].setMap(null);
        }
    },

ice.ace.gMap.gOverlay = function(ele, overlayID, shape, locationList, options) {
        var wrapper = ice.ace.gMap.getGMapWrapper(ele);
        var map = ice.ace.gMap.getGMapWrapper(ele).getRealGMap();
        var overlay;
        var points = locationList.split(":");
        for (var i=0; i<points.length ; i++){
            points[i] = "new google.maps.LatLng" + points[i] + "";
        }
        switch (shape) {
            case "Line":
            case "line":
            case "Polyline":
                var overlayOptions = (options != null && options.length>0) ? "({map:map, path:[" + points + "], " + options + "})" : "({map:map, path:[" + points + "]})";
                overlay = new google.maps.Polyline(eval(overlayOptions));
                break;
            case "Polygon":
            case "polygon":
                var overlayOptions = (options != null && options.length>0) ? "({map:map, paths:[" + points + "], " + options + "})" : "({map:map, paths:[" + points + "]})";
                overlay = new google.maps.Polygon(eval(overlayOptions));
                break;
            case "Rectangle":
            case "rectangle":
                //needs SW corner in first point, NE in second
                var overlayOptions = (options != null && options.length>0) ? "({map:map, bounds:new google.maps.LatLngBounds(" + points[0] +
                    ","  + points[1] + "), " + options + "})" : "({map:map, bounds:new google.maps.LatLngBounds(" + points[0] + ","  + points[1] + ")})";
                overlay = new google.maps.Rectangle(eval(overlayOptions));
                break;
            case "Circle":
            case "circle":
                //Requires radius option
                var overlayOptions = (options != null && options.length>0) ? "({map:map, center: " + points[0] + ", " + options + "})" : "({map:map, center: " + points[0] + "})";
                overlay = new google.maps.Circle(eval(overlayOptions));
                break;
            default:
                console.log("Not a valid shape");
                return;
        }//switch
        wrapper.overlays[overlayID]=overlay;
    }




ice.ace.gMap.setMapType = function(ele, type) {
        var gmapWrapper = ice.ace.gMap.getGMapWrapper(ele);
        //if the chart is recreated, so add any geoCoderMarker that was exist before.
        if (gmapWrapper.geoMarkerSet
            && gmapWrapper.geoMarker != null
            && gmapWrapper.geoMarkerAddress != null
            ) {
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
            }
        }
    }

ice.ace.gMap.makeGMap = function(context,gmap){
    if(gmap.getType().equalsIgnoreCase("Map"))
        gmap.setType("ROADMAP");
    if(gmap.getOptions() != null && gmap.getOptions().length() != 0)
        var options = "," + gmap.getOptions();
    else
        var options = "";
    var mapOptions = "{center: new google.maps.LatLng("+ gmap.getLatitude() + "," + gmap.getLongitude() + ")," +
        "zoom:" + gmap.getZoomLevel()+ ", mapTypeId:google.maps.MapTypeId." + gmap.getType().toUpperCase() + options+ "}";

    var map = new google.maps.Map(document.getElementById('"+ clientId +"'), eval(mapOptions));
    if(gmap.getAddress() != null && gmap.getAddress().length() != 0){
        if(gmap.isIntialized() == false || gmap.isLocateAddress() == true){
            gmap.setIntialized(true);
            var geocoder = new google.maps.Geocoder();
            geocoder.geocode( {'address':'" + gmap.getAddress() + "'}, function(results, status) {
                if (status == google.maps.GeocoderStatus.OK) {
                    map.setCenter(results[0].geometry.location);
                } else {
                    alert('Geocode was not successful for the following reason:'  + status);
                }
            });
        }

    }
}