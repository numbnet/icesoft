/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

if (!window['ice']) {
    window.ice = {};
}
if (!window['ice.mobi']) {
    window.ice.mobi = {};
}
if (!window.console) {
    console = {};
    if (ice.logInContainer) {
        console.log = ice.logInContainer;
    } else {
        log = function() {
        };
    }
}
(function(im) {
    
    /************************ PRIVATE ******************************/
    function addListener(obj, event, fnc){
        if (obj.addEventListener){
            obj.addEventListener(event, fnc, false);
        } else if (obj.attachEvent) {
            obj.attachEvent("on"+event, fnc);
        } else {
            ice.log.debug(ice.log, 'WARNING:- this browser does not support addEventListener or attachEvent');
        }
    }
    function removeListener(obj, event, fnc){
        if (obj.addEventListener){
            obj.removeEventListener(event, fnc, false);
        } else if (obj.attachEvent){
            obj.detachEvent("on"+event, fnc);
        } else {
            ice.log.debug(ice.log, 'WARNING cannot remove listener for event='+event+' node='+obj);
        }
    }
    function mobiserial(formId, typed) {
        var form = document.getElementById(formId);
        var els = form.elements;
        var len = els.length;
        var qString = [];
        var addField = function(name, value) {
            var tmpStr = "";
            if (qString.length > 0) {
                tmpStr = "&";
            }
            tmpStr += encodeURIComponent(name) + "=" + encodeURIComponent(value);
            qString.push(tmpStr);
        };
        for (var i = 0; i < len; i++) {
            var el = els[i];
            if (!el.disabled) {
                var prefix = "";
                if (typed) {
                    var vtype = el.getAttribute("data-type");
                    if (vtype) {
                        prefix = vtype + "-";
                    } else {
                        prefix = el.type + "-";
                    }
                }
                switch (el.type) {
                    case 'submit':
                    case 'button':
                    case 'fieldset':
                        break;
                    case 'text':
                    case 'password':
                    case 'hidden':
                    case 'textarea':
                        addField(prefix + el.name, el.value);
                        break;
                    case 'select-one':
                        if (el.selectedIndex >= 0) {
                            addField(prefix + el.name, el.options[el.selectedIndex].value);
                        }
                        break;
                    case 'select-multiple':
                        for (var j = 0; j < el.options.length; j++) {
                            if (el.options[j].selected) {
                                addField(prefix + el.name, el.options[j].value);
                            }
                        }
                        break;
                    case 'checkbox':
                    case 'radio':
                        if (el.checked) {
                            addField(prefix + el.name, el.value);
                        }
                        break;
                    default:
                        addField(prefix + el.name, el.value);
                }
            }
        }
        // concatenate the array
        return qString.join("");
    }
    function getDeviceCommand()  {
        var sxkey = "#icemobilesx";
        var sxlen = sxkey.length;
        var locHash = "" + window.location.hash;
        if (sxkey === locHash.substring(0, sxlen))  {
            return locHash.substring(sxlen + 1);
        }
        return null;
    }
    function deviceCommandExec(command, id, options)  {
        var ampchar = String.fromCharCode(38);
        var uploadURL;
        var sessionid;
        var params;
        var element;
        var formID;

        if (options)  {
            if (options.postURL)  {
                uploadURL = options.postURL;
            }
            if (options.JSESSIONID)  {
                sessionid = options.JSESSIONID;
            }
            if (options.parameters)  {
                params = options.parameters;
            }
            if (options.element)  {
                element = options.element;
            }
            if (options.form)  {
                formID = options.form.getAttribute("id");
            }
        }

        if (!uploadURL)  {
            uploadURL = getUploadURL(element);
        }

        var windowLocation = window.location;
        var barURL = windowLocation.toString();
        var baseURL = barURL.substring(0,
                barURL.lastIndexOf("/")) + "/";

        var returnURL = "" + window.location;
        if ("" == window.location.hash) {
            var lastHash = returnURL.lastIndexOf("#");
            if (lastHash > 0) {
                returnURL = returnURL.substring(0, lastHash);
            }
            returnURL += "#icemobilesx";
        }

        if ("" != params) {
            params = "ub=" + escape(baseURL) + ampchar + params;
        }

        var sessionidClause = "";
        if ("" != sessionid) {
            sessionidClause = "&JSESSIONID=" + escape(sessionid);
        }
        var serializedFormClause = "";
        if (formID)  {
            serializedFormClause = "&p=" + escape(mobiserial(formID, false));
        }
        var uploadURLClause = "";
        if (uploadURL)  {
            uploadURLClause = "&u=" + escape(uploadURL);
        }
        var sxURL = "icemobile:c=" + escape(command +
                "?id=" + id + ampchar + (params ? params : '')) +
                uploadURLClause + 
                "&r=" + escape(returnURL) +
                sessionidClause +
                serializedFormClause;

        window.location = sxURL;
    }
    function getSplashClause()  {
        var splashClause = "";
        if (null != ice.mobi.splashImageURL)  {
            var splashImage = "i=" + escape(ice.mobi.splashImageURL);
            splashClause = "&s=" + escape(splashImage);
        }
        return splashClause;
    }
    function getUploadURL(element)  {
        var uploadURL;

        var windowLocation = window.location;
        var barURL = windowLocation.toString();
        var baseURL = barURL.substring(0,
                barURL.lastIndexOf("/")) + "/";

        if (!element)  {
            uploadURL = baseURL;
        } else {
            var form = formOf(element);
            formID = form.getAttribute('id');
            var formAction = form.getAttribute("action");

            if (!uploadURL) {
                uploadURL = element.getAttribute("data-posturl");
            }
            if (!uploadURL) {        
                if (0 === formAction.indexOf("/")) {
                    uploadURL = window.location.origin + formAction;
                } else if ((0 === formAction.indexOf("http://")) ||
                        (0 === formAction.indexOf("https://"))) {
                    uploadURL = formAction;
                } else {
                    uploadURL = baseURL + formAction;
                }
            }
        }
        return uploadURL;
    }
    function deviceCommand(command, id, callback, options)  {
        console.log(command + " " + id);
        ice.mobi.deviceCommandCallback = callback;
        deviceCommandExec(command, id, options);
    }
    function setInput(target, name, value, vtype)  {
        var hiddenID = name + "-hid";
        var existing = document.getElementById(hiddenID);
        if (existing)  {
            existing.setAttribute("value", value);
            return;
        }
        var targetElm = document.getElementById(target);
        if (!targetElm)  {
            return;
        }
        var hidden = document.createElement("input");

        hidden.setAttribute("type", "hidden");
        hidden.setAttribute("id", hiddenID);
        hidden.setAttribute("name", name);
        hidden.setAttribute("value", value);
        if (vtype)  {
            hidden.setAttribute("data-type", vtype);
        }
        targetElm.parentNode.insertBefore(hidden, targetElm);
    }
    function formOf(element) {
        var parent = element;
        while (null != parent) {
            if ("form" == parent.nodeName.toLowerCase()) {
                return parent;
            }
            parent = parent.parentNode;
        }
    }
    function locationWithoutViewParam() {
        var url = window.location.href;
        url = url.replace(/l=[m|t|d]/, '');
        url = url.replace(/\?&/, '\?');
        return url;
    }
    function storeLocation(id, coords) {
        if (!coords) {
            return;
        }
        var el = document.getElementById(id);
        if (!el) {
            return;
        }
        var elValue = (el.value) ? el.value : "";
        var parts = elValue.split(',');
        if (4 != parts.length) {
            parts = new Array(4)
        }
        ;
        parts[0] = coords.latitude;
        parts[1] = coords.longitude;
        parts[2] = coords.altitude;
        el.value = parts.join();
    }
    function storeDirection(id, orient) {
        if (orient.webkitCompassAccuracy <= 0) {
            return;
        }
        var el = document.getElementById(id);
        if (!el) {
            return;
        }
        var elValue = (el.value) ? el.value : "";
        var parts = elValue.split(',');
        if (4 != parts.length) {
            parts = new Array(4)
        }
        parts[3] = orient.webkitCompassHeading;
        el.value = parts.join();
    }
    function unpackDeviceResponse(data)  {
        var result = {};
        var params = data.split("&");
        var len = params.length;
        for (var i = 0; i < len; i++) {
            var splitIndex = params[i].indexOf("=");
            var paramName = unescape(params[i].substring(0, splitIndex));
            var paramValue = unescape(params[i].substring(splitIndex + 1));
            if ("!" === paramName.substring(0,1))  {
                //ICEmobile parameters are set directly
                result[paramName.substring(1)] = paramValue;
            } else  {
                //only one user value is supported
                result.name = paramName;
                result.value = paramValue;
            }
        }
        return result;
    }
    /* Page event handling */
    if (window.addEventListener) {

        window.addEventListener("pagehide", function () {
            if (ice.push) {
                ice.push.connection.pauseConnection();
            }
        }, false);

        window.addEventListener("pageshow", function () {
            if (ice.push) {
                ice.push.connection.resumeConnection();
            }
        }, false);

        window.addEventListener("hashchange", function () {
            var data = getDeviceCommand();
            var deviceParams;
            if (null != data)  {
                var name;
                var value;
                var needRefresh = true;
                if ("" != data)  {
                    deviceParams = unpackDeviceResponse(data);
                    if (deviceParams.name)  {
                        setInput(deviceParams.name, deviceParams.name,
                                deviceParams.value);
                        needRefresh = false;
                    }
                }
                if (needRefresh)  {
                    if (window.ice.ajaxRefresh)  {
                        ice.ajaxRefresh();
                    }
                }
                setTimeout( function(){
                    var loc = window.location;
                    //changing hash to temporary value ensures changes
                    //to repeated values are detected
                    history.pushState("", document.title,
                            loc.pathname + loc.search + "#clear-icemobilesx");
                    history.pushState("", document.title,
                            loc.pathname + loc.search);
                    var sxEvent = {
                        name : name,
                        value : value
                    };
                    if (deviceParams)  {
                        if (deviceParams.r)  {
                            sxEvent.response = deviceParams.r;
                        }
                        if (deviceParams.p)  {
                            sxEvent.preview = deviceParams.p;
                        }
                    }
                    if (ice.mobi.deviceCommandCallback)  {
                        ice.mobi.deviceCommandCallback(sxEvent);
                        ice.mobi.deviceCommandCallback = null;
                    }
                }, 1);
            }
        }, false);

    };

    
    /************************ PUBLIC **********************************/
    /**
     * TODO Description of ajaxRequest..
     */
    im.ajaxRequest = function(options){
        if (options.oncomplete || options.onComplete){//support for submitNotification
            var callBack = options.oncomplete || options.onComplete;
            ice.mobi.addListener(document, "DOMSubtreeModified", callBack);
        }
        if( typeof ice.mobi.userAjaxRequest === "function"){
            ice.mobi.userAjaxRequest(options);
        }
        else{ //default to form submit
            var src = options.source;
            if (!src){
                console.log("No source element to submit");
                return;
            }
            var myForm = formOf(document.getElementById(src));
            if (myForm) {
                myForm.submit();
            }else{
                console.log(" no form to submit for source element="+src);
            }
        }
    };
    /**
     * TODO Description of registerAuxUpload
     */
    im.registerAuxUpload = function registerAuxUpload(sessionid, uploadURL) {

        var splashClause = getSplashClause();

        var sxURL = "icemobile:c=register&r=" +
                            escape(window.location) + "&JSESSIONID=" + sessionid +
                            splashClause +
                            "&u=" + escape(uploadURL);

        if (window.chrome)  {
            window.location.href = sxURL;
            return;
        }

        var auxiframe = document.getElementById('auxiframe');
        if (null == auxiframe) {
            auxiframe = document.createElement('iframe');
            auxiframe.setAttribute("id", "auxiframe");
            auxiframe.setAttribute("style", "width:0px; height:0px; border: 0px");
            auxiframe.setAttribute("src", sxURL);
            document.body.appendChild(auxiframe);
        }
    };
    /**
     * TODO Description of mobilesx
     * @param element
     * @param uploadURL
     * @returns
     */
    im.mobilesx = function mobilesx(element, uploadURL) {
        var ampchar = String.fromCharCode(38);
        var form = formOf(element);
        var formID = form.getAttribute('id');
        var formAction = form.getAttribute("action");
        var command = element.getAttribute("data-command");
        var id = element.getAttribute("data-id");
        var params = element.getAttribute("data-params");
        var sessionid = element.getAttribute("data-jsessionid");
        var windowLocation = window.location;
        var barURL = windowLocation.toString();
        var baseURL = barURL.substring(0,
                barURL.lastIndexOf("/")) + "/";
        
        if (!uploadURL) {
            uploadURL = element.getAttribute("data-posturl");
        }
        
        var options = {
            postURL : uploadURL,
            JSESSIONID : sessionid,
            parameters : params,
            element : element,
            form: form
        };
        
        deviceCommandExec(command, id, options);
    };
    /**
     * TODO Description of invoke...
     */
    im.invoke = function(element)  {
        var command = element.getAttribute("data-command");
        if (ice[command])  {
            var params = element.getAttribute("data-params");
            var id = element.getAttribute("data-id");
            if ((null == id) || ("" == id)) {
                id = element.getAttribute("id");
            }
            ice[command](id,params);
        } else {
            ice.mobi.sx(element);
        }
    };
    /**
     * TODO Description of ...
     */
    im.scan = function(id, callback, options)  {
        deviceCommand("scan", id, callback, options);
    };
    /**
     * TODO Description of ...
     */
    im.camera = function(id, callback, options)  {
        deviceCommand("camera", id, callback, options);
    };
    /**
     * TODO Description of ...
     */
    im.camcorder = function(id, callback, options)  {
        deviceCommand("camcorder", id, callback, options);
    };
    /**
     * TODO Description of ...
     */
    im.microphone = function(id, callback, options)  {
        deviceCommand("microphone", id, callback, options);
    };
    /**
     * TODO Description of ...
     */
    im.fetchContacts = function(id, callback, options)  {
        deviceCommand("fetchContacts", id, callback, options);
    };
    /**
     * TODO Description of ...
     */
    im.sms = function(id, callback, options)  {
        deviceCommand("sms", id, callback, options);
    };
    /**
     * TODO Description of ...
     */
    im.open = function(id, callback, options)  {
        deviceCommand("open", id, callback, options);
    };
    /**
     * TODO Description of ...
     */
    im.augmentedReality = function(id, callback, options)  {
        deviceCommand("aug", id, callback, options);
    };
    /**
     * TODO Description of ...
     */
    im.geoSpy = function(id, callback, options)  {
        deviceCommand("geospy", id, callback, options);
    };
    /**
     * TODO Description of ..
     */
    im.geolocation = {
        watchId: 0,
        clientId: "",

        /**
         * Perform a call to watchPosition to fetch running updates to position.
         *
         * @param pClientId base id of hidden field
         * @param highAccuracy true if highAccuracy results to be fetched
         * @param maxAge oldest acceptable cached results (in ms)
         * @param timeout longest time to wait for value (in ms)
         */
        watchLocation: function (pClientId, highAccuracy, maxAge, timeout) {

            ice.mobi.geolocation.clientId = pClientId;
            ice.mobi.geolocation.clearWatch();
            // It seems like on Android that passing any argument at all for enableHighAccuracy
            // enables high accuracy.
            var geoParams = {};
            if (maxAge > 0)  {
                geoParams.maximumAge = maxAge * 1000;
            }
            if (timeout > 0)  {
                geoParams.timeout = timeout * 1000;
            }
            if (highAccuracy != 'false')  {
                geoParams.enableHighAccuracy = true;
            }
            console.log('Launching watchPosition, ' + 
                'maxAge: ' + geoParams.maximumAge + '(ms),' +
                ' timeout: ' + geoParams.timeout + '(ms)' +
                ' highAccuracy: ' + geoParams.enableHighAccuracy);

            ice.mobi.geolocation.watchId = navigator.geolocation.watchPosition(
                    this.successCallback, this.errorCallback,
                    geoParams );

            window.addEventListener('deviceorientation', ice.mobi.geolocation.orientationCallback);
            console.log('Lauching positionWatch for client: ' + pClientId + 
                    ' watchId: ' + ice.mobi.geolocation.watchId);
        },

        /**
         * Perform a single call to the navigator getCurrentPosition call.
         */
        getLocation: function (pClientId, highAccuracy, maxAge, timeout) {

            ice.mobi.geolocation.clientId = pClientId;
            ice.mobi.geolocation.clearWatch();

            var geoParams = {};
            if (maxAge > 0)  {
                geoParams.maximumAge = maxAge * 1000;
            }
            if (timeout > 0)  {
                geoParams.timeout = timeout * 1000;
            }
            if (highAccuracy != 'false')  {
                geoParams.enableHighAccuracy = true;
            }
            console.log('Launching getCurrentPosition, ' +
                    'maxAge: ' + geoParams.maximumAge + '(ms),' +
                    ' timeout: ' + geoParams.timeout + '(ms)' +
                    ' highAccuracy: ' + geoParams.enableHighAccuracy);


            navigator.geolocation.getCurrentPosition(this.oneTimeSuccessCallback, this.errorCallback,
                                                     geoParams);

            window.addEventListener('deviceorientation', ice.mobi.geolocation.orientationCallback);
        },

        successCallback: function(pos) {
            console.log('Position update for client: ' + ice.mobi.geolocation.clientId);
            try {
                var inputId = ice.mobi.geolocation.clientId + "_locHidden";
                storeLocation(inputId, pos.coords);

            } catch(e) {
                console.log('Exception: ' + e);
            }
        },

        // Success Callback for getCurrentPosition  in that it removes deviceorientation listener
        oneTimeSuccessCallback: function(pos) {
            console.log('oneTimePosition update for client: ' + ice.mobi.geolocation.clientId);
            var inputId = ice.mobi.geolocation.clientId + "_locHidden";
            storeLocation(inputId, pos.coords);
            ice.mobi.geolocation.clearWatch();
        },

        errorCallback: function(positionError) {
            console.log('Error in watchPosition, code: ' + positionError.code + ' Message: ' + positionError.message);
            ice.mobi.geolocation.clearWatch();
        },

        orientationCallback: function(orient) {
            var inputId = ice.mobi.geolocation.clientId + "_locHidden";
            storeDirection(inputId, orient);
        },

        // Clear any existing positionUpdate listeners
        clearWatch: function() {
            console.log('attempting geolocation clearWatch..');
            if (ice.mobi.geolocation.watchId > 0) {
                console.log('Existing positionWatch: ' + ice.mobi.geolocation.watchId + ' removed');
                navigator.geolocation.clearWatch(ice.mobi.geolocation.watchId);
                ice.mobi.geolocation.watchId = 0;
            }
            ice.mobi.removeListener(window,'deviceorientation', ice.mobi.geolocation.orientationCallback);
            console.log('removed geolocation deviceorientation callback');
        }
    }
    /** TODO Document use of ice.ajaxRefresh?? */
    
})(ice.mobi);

