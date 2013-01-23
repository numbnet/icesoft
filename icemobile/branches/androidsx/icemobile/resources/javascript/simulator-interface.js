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
        ice.mobi.sim.simAction = function(simPick)  {
            ice.addHidden(id, id, "" + simPick, 'file');
        }
        var cssClass = "simAugL";
        var onclick = document.getElementById(id).onclick.toString();
        if (onclick.indexOf("vuforia") > -1)  {
            cssClass = "simAugM";
        }
        ice.mobi.sim.openSingleView("Simulated Augmented Reality", cssClass );
    };
    
    ice.currentContactId = "";
    ice.fetchContacts = function(id, attr) {
        ice.mobi.sim.simAction = function(simPick)  {
            ice.addHidden(id, id, "" + simPick, 'file');
        }
        ice.mobi.sim.openContacts();
    };
    
    ice.camera = function(id, attr) {
        ice.mobi.sim.simAction = function(simPick)  {
            ice.addHidden(id, id, "" + simPick, 'file');
        }
        ice.mobi.sim.openImageGallery();
    };
    
    ice.camcorder = function(id, attr) {
        ice.mobi.sim.simAction = function(simPick)  {
            ice.addHidden(id, id, "" + simPick, 'file');
        }
        ice.mobi.sim.openVideoGallery();
    };
    
    ice.microphone = function(id, attr) {
        ice.mobi.sim.simAction = function(simPick)  {
            ice.addHidden(id, id, "" + simPick, 'file');
        }
        ice.mobi.sim.openAudioGallery();
    };
    
    ice.play = function(id) {
        console.log('ice.play');
        //window.ICEaudio.playUrl(id, true);
    };
    
    ice.currentScanId = "";
    ice.scan = function(id, attr) {
        ice.mobi.sim.simAction = function(simPick)  {
            ice.addHidden(id, id, "" + simPick, 'file');
        }
        ice.mobi.sim.openSingleView("Simulated QR Scan", "simScan" );
    };
    
    var context = {
        onevent : null,
        onerror : function(param) {
            alert("JSF error " + param.source + " " + param.description);
        }
    };
    var tempInputs = [];

    ice.addHidden = function(target, name, value, vtype)  {
        var hiddenID = name + "-hid";
        var existing = document.getElementById(hiddenID);
        if (existing)  {
            existing.parentNode.removeChild(existing);
        }
        var targetElm = document.getElementById(target);
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

})();
if (!window.ice.mobi) {
    window.ice.mobi = {};
}
if (!window.ice.mobi.sim) {
    window.ice.mobi.sim = {
        GALLERY_ID: 'org.icemobile.simulator.imageGallery',
        MIC_TEMPLATE:
        "<div align='center' class='simTitle'>Sim Audio Gallery</div>" +
        "<input type='button' class='galButton mic1' name='cam##' " +
          "onclick='ice.mobi.sim.pickItem(this);' data-itemref='sim-snd.mp3' >" +
        "<div class='simClose'><input type='button' onclick='ice.mobi.sim.closeGallery()' class='simClose' value='close'></div>",
        VID_TEMPLATE:
        "<div align='center' class='simTitle'>Sim Video Gallery</div>" +
        "<input type='button' class='galButton vid1' name='cam##' " +
          "onclick='ice.mobi.sim.pickItem(this);' data-itemref='sim-vid.mp4' >" +
        "<div class='simClose'><input type='button' onclick='ice.mobi.sim.closeGallery()' class='simClose' value='close'></div>",
        CAM_TEMPLATE:
        "<div align='center' class='simTitle'>Simulated Camera</div>" +
        "<input type='button' class='galButton b1' name='cam##' " +
          "onclick='ice.mobi.sim.pickItem(this);' data-itemref='sim-one.png' >" +
        "<input type='button' class='galButton b2' name='cam##' " +
          "onclick='ice.mobi.sim.pickItem(this);' data-itemref='sim-two.png' >" +
        "<input type='button' class='galButton b3' name='cam##' " +
          "onclick='ice.mobi.sim.pickItem(this);' data-itemref='sim-three.png' >" +
        "<div class='simClose'><input type='button' onclick='ice.mobi.sim.closeGallery()' class='simClose' value='close'></div>",
        CONTACT_TEMPLATE:
        "<div align='center' class='simTitle'>Simulated Contacts</div>" +
        "<a onclick='ice.mobi.sim.pickItem(this);' data-itemref='name=Carl%20Sagan&phone=555-1212&email=seti@home.com'>" +
        "<div class='simContact'>Carl Sagan</div>" +
        "</a>" + 
        "<a onclick='ice.mobi.sim.pickItem(this);' data-itemref='name=James%20Bond&phone=007-1212'>" +
        "<div class='simContact'>James Bond</div>" +
        "</a>" + 
        "<a onclick='ice.mobi.sim.pickItem(this);' data-itemref='name=ICEsoft&phone=403-663-3322&email=product.support@icesoft.com'>" +
        "<div class='simContact'>ICEsoft Technologies</div>" +
        "</a>" + 
        "<div class='simClose'><input type='button' onclick='ice.mobi.sim.closeGallery()' class='simClose' value='close'></div>",
        SINGLE_TEMPLATE:
        "<div align='center' class='simTitle'>##title</div>" +
        "<a onclick='ice.mobi.sim.pickItem(this);' data-itemref='##itemRef'>" +
        "<div class='##itemClass'></div>" +
        "</a>" + 
        "<div class='simClose'><input type='button' onclick='ice.mobi.sim.closeGallery()' class='simClose' value='close'></div>",
        images: {},
        simAction: null,
        addImage: function addImage(thumbPath, imagePath){
            images[thumbPath] = imagePath;
        },
        pickItem: function(element) {
            this.simAction(element.getAttribute("data-itemref"));
            ice.mobi.sim.closeGallery();
        },
        openSingleView: function openSingleView(title, viewClass){
            var gallery = document.getElementById(
                    this.GALLERY_ID);
            if (gallery)  {
                this.closeGallery();
            }
            var rootDiv = document.createElement("div");
            rootDiv.setAttribute("id",this.GALLERY_ID);
            rootDiv.setAttribute("class", "simRoot");

            var htmlReplacement = this.SINGLE_TEMPLATE.replace(/##title/, title);
            htmlReplacement = htmlReplacement.replace(/##itemClass/, viewClass);
            htmlReplacement = htmlReplacement.replace(/##itemRef/, viewClass);
            
            rootDiv.innerHTML = htmlReplacement;

            document.body.appendChild(rootDiv);
        },
        openContacts: function openContacts() {
            this.openGallery(this.CONTACT_TEMPLATE);
        },
        openImageGallery: function openImageGallery() {
            this.openGallery(this.CAM_TEMPLATE);
        },
        openVideoGallery: function openImageGallery() {
            this.openGallery(this.VID_TEMPLATE);
        },
        openAudioGallery: function openImageGallery() {
            this.openGallery(this.MIC_TEMPLATE);
        },
        openGallery: function openGallery(template){
            var gallery = document.getElementById(
                    this.GALLERY_ID);
            if( gallery ){
                this.closeGallery();
            }
            var rootDiv = document.createElement("div");
            rootDiv.setAttribute("id",this.GALLERY_ID);
            rootDiv.setAttribute("class", "simRoot");

            rootDiv.innerHTML = template.replace(/##/g, "zzz");

            document.body.appendChild(rootDiv);
        },
        closeGallery: function closeGallery(){
            var gallery = document.getElementById(
                    this.GALLERY_ID);
            document.body.removeChild(gallery);
        }
        
    };
}