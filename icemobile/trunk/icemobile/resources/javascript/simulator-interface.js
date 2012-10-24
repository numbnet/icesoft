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