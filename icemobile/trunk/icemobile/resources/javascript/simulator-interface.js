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
    
})();
if (!window.ice.mobile) {
    window.ice.mobile = {};
}
if (!window.ice.mobile.simulator) {
    window.ice.mobile.simulator = {
        IMAGE_GALLERY_ID: 'org.icemobile.simulator.imageGallery',
        CLOSE_IMAGE_GALLERY_ID: 'closeImageGallery',
        template:
        "<div align='center' class='simTitle'>Simulated Camera</div>" +
        "<input type='button' class='camera' name='cam##' " +
          "onclick='alert(this.name);' >" +
        "<div style='position:absolute;bottom:0;right:2;'><input type='button' onclick='ice.mobile.simulator.closeImageGallery()' class='simClose' value='close'></div>",
        images: {},
        addImage: function addImage(thumbPath, imagePath){
            images[thumbPath] = imagePath;
        },
        openImageGallery: function openImageGallery(){
            var imageGallery = document.getElementById(
                    this.IMAGE_GALLERY_ID);
            if( imageGallery ){
                closeImageGallery();
            }
            var rootDiv = document.createElement("div");
            rootDiv.setAttribute("id",this.IMAGE_GALLERY_ID);
            rootDiv.setAttribute("style",
                "width: 200px; height: 300px; " +
                " background-color: #567; position:absolute; " +
                "left: 50%; top: 50%; " +
                "margin-left: -100px; margin-top: -150px;"
            );

            rootDiv.innerHTML = this.template.replace(/##/g, "zzz");

            document.body.appendChild(rootDiv);
        },
        closeImageGallery: function closeImageGallery(){
            var imageGallery = document.getElementById(
                    this.IMAGE_GALLERY_ID);
            document.body.removeChild(imageGallery);
        }
        
    };
}