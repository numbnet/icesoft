function enhanceForm(theForm,updateTarget)  {
    //submitting the form will update 
    //the containing div with class ajaxzone
    $(document).ready(function () {
        $(theForm).submit(function () {
            var updateRegion = $(updateTarget);
            if (window.ice && ice.upload) {
                window.ice.handleResponse = function (data) {
                    updateRegion.replaceWith(unescape(data));
                }
                ice.upload($(this).attr("id"));
                return false;
            }
            
            try{
                var formData;
                var mimeType = false;
                if ((undefined !== window.FormData) && 
                   (!window.clientInformation || 
                       ("BlackBerry" !== window.clientInformation.platform)) )  {
                    formData = new FormData(this);
                } else {
                    formData = $(theForm).serialize();
                    mimeType = "application/x-www-form-urlencoded";
                }

                $.ajax({
                    url:$(this).attr("action"),
                    data:formData,
                    cache:false,
                    contentType:mimeType,
                    processData:false,
                    type:'POST',
                    success:function (html) {
                        updateRegion.replaceWith(html);
                    }
                });
            }
            catch(err){
                if( window.console ){
                    console.error(err);
                }
                else{
                    alert(err);
                }
            }
            return false;
        });
    });
}

function updateViewerPanel(id){
	 $.ajax({
        url:'<c:url value="/contest-viewer"/>?id='+id+'&l=t',
        cache:false,
        processData:false,
        type:'GET',
        success:function (html) {
            $('#viewerPanel').replaceWith(html);
            ice.mobi.tabsetController.showContent('tabs', $('#tabstab_1')[0], {tIndex: 1} );
        }
    });
}