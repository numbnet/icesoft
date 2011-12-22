/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */


if (!window['mobi']) {
    window.mobi = {};
}
mobi.BEHAVIOR_EVENT_PARAM = "javax.faces.behavior.event";
mobi.PARTIAL_EVENT_PARAM = "javax.faces.partial.event";
mobi.findForm = function(sourceId){
     var node = document.getElementById(sourceId);
     while (node.nodeName != "FORM" && node.parentNode){
         node=node.parentNode;
     }
    ice.log.debug(ice.log, 'parent form node ='+node.name);
    return node;
};
mobi.AjaxRequest = function(cfg) {

    if(cfg.onstart && !cfg.onstart.call(this)) {
       return;//cancel request
    }
    ice.log.debug(ice.log, 'creating ajax request');
    var form = mobi.findForm(cfg.source);
    if (form){
        ice.log.debug(ice.log, 'found form with name='+form.name);
        ice.log.debug(ice.log, ' length of forms ='+form.length);
    }

    var source = (typeof cfg.source == 'string') ? document.getElementById(cfg.source) : cfg.source;
    var jsfExecute = cfg.execute || '@all';
    var jsfRender = cfg.render || '@all';

    ice.fullSubmit(jsfExecute, jsfRender, null, source || form[0], function(parameter) {
        if(cfg.event) {
            parameter(mobi.BEHAVIOR_EVENT_PARAM, cfg.event);

            var domEvent = cfg.event;
            if(cfg.event == 'valueChange') {
                domEvent = 'change';
            } else if (cfg.event == 'action') {
                domEvent = 'click';
            }

            parameter(mobi.PARTIAL_EVENT_PARAM, domEvent);
        } else {
            parameter(cfg.source, cfg.source);
        }

        if(cfg.params) {
            var cfgParams = cfg.params;
            for(var p in cfgParams) {
                parameter(p, cfgParams[p]);
            }
        }
    }, function(onBeforeSubmit, onBeforeUpdate, onAfterUpdate, onNetworkError, onServerError) {
        var context = {};
        onAfterUpdate(function(responseXML) {
            if (cfg.onsuccess && !cfg.onsuccess.call(context, responseXML, null /*status*/, null /*xhr*/)) {
                return;
            }
            mobi.AjaxResponse.call(context, responseXML);
        });
        if (cfg.oncomplete) {
            onAfterUpdate(function(responseXML) {
                cfg.oncomplete.call(context, null /*xhr*/, null /*status*/, context.args);
            });
        }
        if (cfg.onerror) {
            onNetworkError(function(responseCode, errorDescription) {
                cfg.onerror.call(context, null /*xhr*/, responseCode /*status*/, errorDescription /*error description*/)
            });
            onServerError(function(responseCode, responseText) {
                cfg.onerror.call(context, null /*xhr*/, responseCode /*status*/, responseText /*error description*/)
            });
        }
    });
};

mobi.AjaxResponse = function(responseXML) {
    var xmlDoc = responseXML.documentElement;
    var extensions = xmlDoc.getElementsByTagName("extension");
    //can't do this unless the browser has JSON support ECMAScript 5
    if (! (typeof(JSON) === 'object' &&
            typeof(JSON.parse) === 'function')) {
          // Native JSON parsing is not available.
        ice.log.debug(ice.log,' do not have JSON support for parsing the response update');
    }
    this.args = {};
    for(var i = 0, l = extensions.length; i < l; i++) {
        var extension = extensions[i];
        if (extension.getAttributeNode('aceCallbackParam')) {
           // var jsonObj = ice.ace.jq.parseJSON(extension.firstChild.data);
            //no jquery available so assuming ECMAScript 5 JSON
            var jsonObj = JSON.parse(extension.firstChild.data);
            for(var paramName in jsonObj) {
                if(paramName) {
                    this.args[paramName] = jsonObj[paramName];
                }
            }
        }
    }
};

mobi.registerAuxUpload = function(sessionid, uploadURL)  {
    var auxiframe = document.getElementById('auxiframe');
    if (null == auxiframe)  {
        auxiframe = document.createElement('iframe');
        auxiframe.setAttribute("id", "auxiframe");
        auxiframe.setAttribute("style", "width:0px; height:0px; border: 0px");
        auxiframe.setAttribute("src", 
            "icemobile://c=register&r=" +
                escape(window.location) + "&JSESSIONID=" + sessionid +
                "&u=" + escape(uploadURL)
        );
        document.body.appendChild(auxiframe);
    }
};

function html5getViewState(form) {
    if (!form) {
        throw new Error("jsf.getViewState:  form must be set");
    }
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
            switch (el.type) {
                case 'submit':
                case 'button':
                    break;
                case 'text':
                case 'password':
                case 'hidden':
                case 'textarea':
                    addField(el.name, el.value);
                    break;
                case 'select-one':
                    if (el.selectedIndex >= 0) {
                        addField(el.name, el.options[el.selectedIndex].value);
                    }
                    break;
                case 'select-multiple':
                    for (var j = 0; j < el.options.length; j++) {
                        if (el.options[j].selected) {
                            addField(el.name, el.options[j].value);
                        }
                    }
                    break;
                case 'checkbox':
                case 'radio':
                    if (el.checked) {
                        addField(el.name, el.value);
                    }
                    break;
                default:
                    addField(el.name, el.value);
            }
        }
    }
    // concatenate the array
    return qString.join("");
};

if (window.addEventListener)  {
    window.addEventListener( "load",
    function() {jsf.getViewState = html5getViewState}
        , false );

    window.addEventListener("pagehide", function() { 
        ice.push.connection.pauseConnection(); 
    }, false); 
    
    window.addEventListener("pageshow", function() { 
        ice.push.connection.resumeConnection(); 
    }, false); 
}
