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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
 *
 */
var GMapRepository = new Array();

var GMapWrapper = Class.create();
GMapWrapper.prototype = {
  initialize: function(eleId, realGMap) {
    this.eleId  = eleId;
    this.realGMap = realGMap;
    this.controls = new Object();
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
	   var geocoder = GMapRepository[id+'geo'];
	   if(geocoder == null) {
	   	  GMapRepository[id+'geo'] = new GClientGeocoder();
	   	  return GMapRepository[id+'geo'];
	   } else {
	      return geocoder;
	   }
	},
	
	getGDirection: function(id, text_div) {
       var gdirection = GMapRepository[id+'dir'];
       if(gdirection == null) {
          var map = Ice.GoogleMap.getGMapWrapper(id).getRealGMap();
          var directionsPanel = document.getElementById(text_div);  
          GMapRepository[id+'dir'] = new GDirections(map, directionsPanel);
          return GMapRepository[id+'dir'];
       } else {
          return gdirection;
       }
    },
    
	getGMapWrapper:function (id) {
       var gmapWrapper = GMapRepository[id];
       if(gmapWrapper) {
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

    getOverlay:function(id) {
        return  GMapRepository[id+'overlay'];
    },
    
    createOverlay: function(id, overlayFunc) {
       var overlay = GMapRepository[id+'overlay'];
       if(overlay == null) {
          GMapRepository[id+'overlay'] = eval(overlayFunc);
          return GMapRepository[id+'overlay'];
       } else {
          return overlay;
       }
    },
    	
	addOverlay:function (mapid, overlayId, ovrLay) {
	   var overLay = Ice.GoogleMap.getOverlay(overlayId);
       var map = Ice.GoogleMap.getGMapWrapper(mapid).getRealGMap();
       if(overLay == null) {
          overLay = Ice.GoogleMap.createOverlay(overlayId, ovrLay);
          map.addOverlay(overLay);        
       }
	    
	},
	
    locateAddress: function(clientId,  address) {
         var gLatLng = function(point) {
	         if (!point) {
	             alert(address + ' not found');                
	         } else {
	            var gmapWrapper = Ice.GoogleMap.getGMapWrapper(clientId);
	            if (gmapWrapper)  {
	                 gmapWrapper.getRealGMap().setCenter(point, 13);
	                 var marker = new GMarker(point);
	                 gmapWrapper.getRealGMap().addOverlay(marker);
	                 marker.openInfoWindowHtml(address);
                     Ice.GoogleMap.submitEvent(clientId, gmapWrapper.getRealGMap(), "geocoder" );
	             } else {
	                 //FOR IS DEFINED BUT MAP IS NOT FOUND,
	                 //LOGGING CAN BE DONE HERE
	             }
	          } //outer if
           }; //function ends here  
           
           var geocoder = Ice.GoogleMap.getGeocoder(clientId);
           geocoder.getLatLng(address, gLatLng);
    },

    create:function (ele) {
        var gmapWrapper = new GMapWrapper(ele, new GMap2(document.getElementById(ele)));
        var hiddenField = document.getElementById(ele+'hdn');           
        var mapTypedRegistered = false;

        GEvent.addListener(gmapWrapper.getRealGMap(), "zoomend", function(oldLevel,  newLevel) {
             Ice.GoogleMap.submitEvent(ele, gmapWrapper.getRealGMap(), "zoomend", newLevel );
        }); 
                
        GEvent.addListener(gmapWrapper.getRealGMap(), "dragend", function() {
             Ice.GoogleMap.submitEvent(ele, gmapWrapper.getRealGMap(), "dragend" );
        }); 
        
        GEvent.addListener(gmapWrapper.getRealGMap(), "maptypechanged", function() {
            if (mapTypedRegistered) {
                var type = $(ele+'type');
                type.value = gmapWrapper.getRealGMap().getCurrentMapType().getName();
                Ice.GoogleMap.submitEvent(ele, gmapWrapper.getRealGMap(), "maptypechanged" );
            }
            mapTypedRegistered = true;
        });                 
        initializing = false;
        GMapRepository[ele]= gmapWrapper;   
        return gmapWrapper;
    },  
    
    submitEvent: function(ele, map, eventName, zoomLevel) {
        try{
            var center = map.getCenter();
            var lat = $(ele+'lat');
            var lng = $(ele+'lng');
            var event = $(ele+'event');
            var zoom = $(ele+'zoom');
            var type = $(ele+'type');
            lat.value = center.lat();
            lng.value = center.lng();
            event.value = eventName;  
            if (zoomLevel == null) {          
                zoom.value = map.getZoom();
            } else {
                zoom.value = zoomLevel;
            }
            var form = Ice.util.findForm(lat);              
            var nothingEvent = new Object();
            iceSubmitPartial(form, lat, nothingEvent);
            //reset event value, so the decode method of gmap can
            //make deceison before decode
            event.value="";
         } catch(e){}    
    },
    
    recreate:function(ele, gmapWrapper) {
        Ice.GoogleMap.remove(ele);
        var controls = gmapWrapper.controls;
        gmapWrapper = Ice.GoogleMap.create(ele);
        var tempObject = new Object();
        for (control in controls){
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
            control =  eval('new '+ controlName + '()');
            gmapWrapper.getRealGMap().addControl(control);
            gmapWrapper.controls[controlName] = control;
        }
    },
    
    removeControl:function(ele, controlName) {
        var gmapWrapper = Ice.GoogleMap.getGMapWrapper(ele);
        var control = gmapWrapper.controls[controlName];
        if (control != null) {
            gmapWrapper.getRealGMap().removeControl(control);
        }
        var newCtrlArray = new Object();
        for (control in gmapWrapper.controls) {
            if (controlName != control) {
                newCtrlArray[control] = gmapWrapper.controls[control];
            }
        }
        gmapWrapper.controls = newCtrlArray;
    },
    
    remove:function(ele) {
        var newRepository = new Array();
        for (map in GMapRepository) {
            if(map != ele) {
                newRepository[map] = GMapRepository[map];
            }
        }
        GMapRepository = newRepository;
    },
    
    setMapType:function(ele, type) {
        var gmapWrapper = Ice.GoogleMap.getGMapWrapper(ele);
        if (gmapWrapper.getRealGMap().getCurrentMapType() != null ) {
             //set the map type only when difference found
             if (gmapWrapper.getRealGMap().getCurrentMapType().getName() != type) {
					switch(type)
					{
					case "Satellite":
	                  gmapWrapper.getRealGMap().setMapType(G_SATELLITE_MAP);
					  break
	                case "Hybrid":
	                  gmapWrapper.getRealGMap().setMapType(G_HYBRID_MAP);
	                  break				  
					case "Map":
	                  gmapWrapper.getRealGMap().setMapType(G_NORMAL_MAP);
	                  break
					}//switch
             }//inner if
        }//outer if        
    }//setMapType    
}


/*
var iceGmap = Class.create();
iceGmap.prototype = {
  initialize: function(eleId, gmapObj) {
    this.eleId  = eleId;
    this.gmapObj = gmapObj;
    this.controls = new Object();
    this.geocoder = new GClientGeocoder();
  },

  getElementId: function() {
    return this.eleId;
  },

  getGmapObject: function() {
      return this.gmapObj;
  },
    
  getControlsArray: function() {
      return this.controls;
  }
 };
 
Ice.GoogleMap = Class.create();
Ice.GoogleMap = {
	getInstance:function(ele) {
		var map = Ice.GoogleMap.getMap(ele);
		if(map) {
		   var gmapComp = document.getElementById(ele);
		   if (!gmapComp.hasChildNodes()) {
				map = Ice.GoogleMap.recreate(ele, map);
			}
		} else {
			map = Ice.GoogleMap.create(ele);
		}
		return map.getGmapObject();
	},

	getMap:function(ele) {
		return GMapRepository[ele];
	},
	
	addControl:function(ele, controlName) {
	    var iceGmap = Ice.GoogleMap.getMap(ele);
	    var control = iceGmap.controls[controlName];
	    if (control == null) {
			control =  eval('new '+ controlName + '()');
	    	iceGmap.getGmapObject().addControl(control);
	    	iceGmap.controls[controlName] = control;
	    }
	},
	
	removeControl:function(ele, controlName) {
		var iceGmap = Ice.GoogleMap.getMap(ele);
		var control = iceGmap.controls[controlName];
		if (control != null) {
			iceGmap.getGmapObject().removeControl(control);
		}
		var newCtrlArray = new Object();
		for (control in iceGmap.controls) {
			if (controlName != control) {
				newCtrlArray[control] = iceGmap.controls[control];
			}
		}
		iceGmap.controls = newCtrlArray;
	},
	
	openInfoWindow:function(ele, position, body) {
		var iceGmap = Ice.GoogleMap.getInstance(ele);
		iceGmap.openInfoWindow(iceGmap.getCenter(), body);
	},
	
	closeInfoWindow:function(ele) {
		var iceGmap = Ice.GoogleMap.getInstance(ele);
		iceGmap.closeInfoWindow();
	},
	
	create:function (ele) {
		var map = new iceGmap(ele, new GMap2(document.getElementById(ele)));
		var hiddenField = document.getElementById(ele+'hdn');			
		var eventInit = true;
		GEvent.addListener(map.getGmapObject(), "dragend", function() {
  			hiddenField.value='position::'+map.getGmapObject().getCenter();
		    var form = Ice.util.findForm(hiddenField);  			
 			var nothingEvent = new Object();
         	iceSubmitPartial(form, hiddenField, nothingEvent);
	    });	
		GEvent.addListener(map.getGmapObject(), "zoomend", function(oldzoom, zoom) {
  			hiddenField.value='zoom::'+map.getGmapObject().getZoom();
		    var form = Ice.util.findForm(hiddenField);  			
 			var nothingEvent = new Object();
         	iceSubmitPartial(form, hiddenField, nothingEvent);
         	
	    });	    
	    	
		GEvent.addListener(map.getGmapObject(), "infowindowclose", function() {
  			hiddenField.value='windowClose::true';
			var form = Ice.util.findForm(hiddenField);  			
 			var nothingEvent = new Object();
        	iceSubmitPartial(form, hiddenField, nothingEvent);
  			hiddenField.value+='';         			
		});		      		
		GMapRepository[ele]= map;	
		return map;
	},	
	
	recreate:function(ele, map) {
		Ice.GoogleMap.remove(ele);
		var controls = map.controls;
    	map = Ice.GoogleMap.create(ele);
		var tempObject = new Object();
		for (control in controls){
	   	 	if (tempObject[control]== null) {
		   	 	Ice.GoogleMap.removeControl(ele, control);
		   	 	Ice.GoogleMap.addControl(ele, control)
	   	 	}
		}
		return map;
	},
	
	remove:function(ele) {
		var newRepository = new Array();
		for (map in GMapRepository) {
			if(map != ele) {
				newRepository[map] = GMapRepository[map];
			}
		}
		GMapRepository = newRepository;
	},
	
	locateAddress:function(ele, address) {
	    var iceGmap = Ice.GoogleMap.getMap(ele);
	    if (iceGmap.geocoder) {
			iceGmap.geocoder.getLatLng(
          			address,
          			function(point) {
            			if (!point) {
              				//alert(address + " not found");
            			} else {
              				iceGmap.getGmapObject().setCenter(point, 13);
              				var marker = new GMarker(point);
              				iceGmap.getGmapObject().addOverlay(marker);
              				marker.openInfoWindowHtml(address);
							var hiddenField = document.getElementById(ele+'hdn');	              				
  			hiddenField.value='position::'+iceGmap.getGmapObject().getCenter();
		    var form = Ice.util.findForm(hiddenField);  			
 			var nothingEvent = new Object();
         	iceSubmitPartial(form, hiddenField, nothingEvent);
            			}
          			}
        	);	    	
	    } else {
	    	//alert ('geo coder not found');
	    }
	},
	
	setZoom:function(ele, level) {
		 var iceGmap = Ice.GoogleMap.getMap(ele);
		 iceGmap.getGmapObject().setZoom(level);
	}
	
}
*/