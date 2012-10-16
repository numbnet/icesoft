(function(){
    ice.progress = function(amount) {
        var canvas = document.getElementById('progMeterCanvas');
        if (null == canvas) {
            return;
        }
        if (canvas.getContext) {
            var ctx = canvas.getContext('2d');
            ctx.clearRect(0, 0, 200, 200);
            ctx.beginPath();
            var theta = ((Math.PI * 2) * amount) / 100;
            ctx.moveTo(15, 15);
            ctx.arc(15, 15, 12, 0, theta, false);
            ctx.fillStyle = '#0B5383';
            ctx.fill();
            ctx.beginPath();
            ctx.arc(15, 15, 12, 0, theta, false);
            ctx.strokeStyle = 'gray';
            ctx.lineWidth = 2;
            ctx.stroke();
        }
    };
    
    ice.setThumbnail = function(id, value) {
        var imageTag = document.getElementById(id);
        if (!imageTag) {
            return;
        }
        imageTag.setAttribute("src", value);
    };
    
    ice.aug = function(id, attr) {
        console.log("aug not implemented in simulator")
    };
    
    ice.currentContactId = "";
    ice.fetchContacts = function(id, attr) {
    
        ice.currentContactId = id;
        window.jsLogger.logInContainer("Fetch Contact id: "
                + ice.currentContactId + ", original: " + id);
        var result = window.ICEContacts.fetchContacts(id, attr);
        return result;
    };
    
    ice.camera = function(id, attr) {
        ice.mobile.simulator.openImageGallery();
        
        
        //var result = window.ICEcamera.shootPhoto(id, attr);
        //ice.addHidden(id, id, "" + result, 'file');
    };
    
    ice.camcorder = function(id, attr) {
        alert('into camcorder');
        //var result = window.ICEvideo.shootVideo(id + '-thumb', attr);
        //ice.addHidden(id, id, "" + result, 'file');
    };
    
    ice.microphone = function(id, attr) {
        alert('into microphone');
        //var result = window.ICEaudio.recordAudio(attr);
        //ice.addHidden(id, id, "" + result, 'file');
    };
    
    ice.play = function(id) {
        console.log('ice.play');
        //window.ICEaudio.playUrl(id, true);
    };
    
    ice.currentScanId = "";
    ice.scan = function(id, attr) {
        alert('scan');
        //var result = window.ICEqrcode.scan(id, attr);
        //ice.currentScanId = id;
    };
    
    var context = {
        onevent : null,
        onerror : function(param) {
            alert("JSF error " + param.source + " " + param.description);
        }
    };
    var tempInputs = [];
    
    ice.handleResponse = function(data) {
        if (null == context.sourceid) {
            //was not a jsf upload
            return;
        }
    
        var jsfResponse = {};
        var parser = new DOMParser();
        var xmlDoc = parser.parseFromString(unescape(data), "text/xml");
    
        jsfResponse.responseXML = xmlDoc;
        jsf.ajax.response(jsfResponse, context);
    
        var form = document.getElementById(context.formid);
        for ( var i in tempInputs) {
            if (form == tempInputs[i].parentNode) {
                form.removeChild(tempInputs[i]);
            }
        }
        context.sourceid = "";
        context.formid = "";
        context.serialized = "";
        context.onevent = null;
        context.onerror = null;
    };
    
    //override primitive submit function
    ice.submitFunction = function(element, event, options) {
        var source = event ? event.target : element;
        var form = ice.formOf(element);
        if (form.elements['javax.faces.source']) {
            //submit is in progress, but callback not completed by container
            return;
        }
        var formId = form.id;
        var sourceId = element ? element.id : event.target.id;
    
        if ("@this" === options.execute) {
            options.execute = sourceId;
        } else if ("@form" === options.execute) {
            options.execute = formId;
        }
        if ("@this" === options.render) {
            options.render = sourceId;
        } else if ("@form" === options.render) {
            options.render = formId;
        }
        if (!options.execute) {
            options.execute = "@all";
        }
        if (!options.render) {
            options.render = "@all";
        }
    
        tempInputs = [];
        tempInputs.push(ice.addHiddenFormField(formId, "javax.faces.source",
                sourceId));
        tempInputs.push(ice.addHiddenFormField(formId,
                "javax.faces.partial.execute", options.execute));
        tempInputs.push(ice.addHiddenFormField(formId,
                "javax.faces.partial.render", options.render));
        tempInputs.push(ice.addHiddenFormField(formId,
                "javax.faces.partial.ajax", "true"));
        if (event) {
            tempInputs.push(ice.addHiddenFormField(formId,
                    "javax.faces.partial.event", event.type));
        }
    
        if (options) {
            for ( var p in options) {
                if ("function" != typeof (options[p])) {
                    tempInputs.push(ice.addHiddenFormField(formId, p,
                            options[p]));
                }
            }
        }
    
        context.sourceid = sourceId;
        context.formid = formId;
        context.onevent = options.onevent;
        context.onerror = options.onerror;
        ice.upload(formId);
    };
    
    ice.formOf = function(element) {
        var parent = element;
        while (null != parent) {
            if ("form" == parent.nodeName.toLowerCase()) {
                return parent;
            }
            parent = parent.parentNode;
        }
    };
    
    ice.upload = function(id) {
        var form = document.getElementById(id);
        context.serialized = ice.serialize(id, true);
        window.ICEutil.submitForm(form.action, context.serialized);
    };
    
    ice.addHidden = function(target, name, value, vtype) {
        var hiddenID = name + "-hid";
        var existing = document.getElementById(hiddenID);
        if (existing) {
            existing.parentNode.removeChild(existing);
        }
        var targetElm = document.getElementById(target);
        var hidden = document.createElement("input");
        hidden.setAttribute("type", "hidden");
        hidden.setAttribute("id", hiddenID);
        hidden.setAttribute("name", name);
        hidden.setAttribute("value", value);
        if (vtype) {
            hidden.setAttribute("data-type", vtype);
        }
    
        targetElm.parentNode.insertBefore(hidden, targetElm);
    };
    
    ice.addHiddenFormField = function(target, name, value) {
        var targetElm = document.getElementById(target);
        var hidden = document.createElement("input");
        hidden.setAttribute("type", "hidden");
        hidden.setAttribute("name", name);
        hidden.setAttribute("value", value);
        targetElm.appendChild(hidden);
        return hidden;
    };
    
    ice.getCurrentSerialized = function() {
        return context.serialized;
    }
    
    ice.serialize = function(formId, typed) {
        var form = document.getElementById(formId);
        var els = form.elements;
        var len = els.length;
        var qString = [];
        var addField = function(name, value) {
            var tmpStr = "";
            if (qString.length > 0) {
                tmpStr = "&";
            }
            tmpStr += encodeURIComponent(name) + "="
                    + encodeURIComponent(value);
            qString.push(tmpStr);
        };
        for ( var i = 0; i < len; i++) {
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
                    prefix = "selectone-";
                    if (el.selectedIndex >= 0) {
                        addField(prefix + el.name,
                                el.options[el.selectedIndex].value);
                    }
                    break;
                case 'select-multiple':
                    prefix = "selectmultiple-";
                    for ( var j = 0; j < el.options.length; j++) {
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
    };
    
    ice.mobiRefresh = function() {
        if (window.ice.ajaxRefresh) {
            window.setTimeout(function() {
                ice.push.resumeBlockingConnection();
                ice.ajaxRefresh();
            }, 50);
            return;
        }
        window.location.reload();
        return;
    };
})();
if (!window.ice.mobile) {
    window.ice.mobile = {};
}
if (!window.ice.mobile.simulator) {
    window.ice.mobile.simulator = function(){
        var IMAGE_GALLERY_ID = 'imageGallery';
        var CLOSE_IMAGE_GALLERY_ID = 'closeImageGallery';
        var images = {};
        return {
            addImage: function addImage(thumbPath, imagePath){
                images[thumbPath] = imagePath;
            },
            openImageGallery: function openImageGallery(){
                var imageGallery = document.getElementById(this.IMAGE_GALLERY_ID);
                if( imageGallery ){
                    closeImageGallery();
                }
                var rootDiv = document.createElement("div");
                rootDiv.setAttribute("id",this.IMAGE_GALLERY_ID);
                var closeBtn = document.createElement("a");
                closeBtn.setAttribute("id",this.CLOSE_IMAGE_GALLERY_ID);
                closeBtn.setAttribute("onclick", "ice.mobile.simulator.closeImageGallery();")
                var closeBtnText = document.createTextNode("Close");
                closeBtn.appendChild(closeBtnText);
                rootDiv.appendChild(closeBtn);
                document.body.appendChild(rootDiv);
                ice.mobile.simulator.imageGallery = rootDiv;
            },
            closeImageGallery: function closeImageGallery(){
                document.body.removeChild(ice.mobile.simulator.imageGallery);
                ice.mobile.simulator.imageGallery = null;
            }
        }
        
    };
}