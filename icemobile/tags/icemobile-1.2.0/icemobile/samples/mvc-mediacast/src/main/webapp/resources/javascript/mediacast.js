function enhanceGet(link){
    $(link).click(function(event){
        var elem = $('#ajaxloader');
        elem.css("position","absolute");
        elem.css("top", (($(window).height() - elem.outerHeight()) / 2) + 
                $(window).scrollTop() + "px");
        elem.css("left", (($(window).width() - elem.outerWidth()) / 2) + 
                $(window).scrollLeft() + "px");
       elem.fadeIn();
        
        $.ajax({
            url:$(this).attr("href"),
            cache:false,
            type:'GET',
            success:function (html) {
                $(document.body).fadeOut
                $(document.body).html(html);
            },
            error:function(){
                $('#ajaxloader').fadeOut();
            }
        });
        return false;
    });
}
function enhanceForm(theForm,updateTarget)  {
    //submitting the form will update 
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

function updateViewerPanel(id,action){
    var url = 'viewer?photoId='+id+'&l=t';
    if( action ){
        url += '&action='+action;
    }
     $.ajax({
        url:url,
        cache:false,
        processData:false,
        type:'GET',
        success:function (html) {
            if( action ){
                var dir = (action == 'back' ? 'right' : 'left');
                $('#viewerPanel').hide("slide", { direction: dir }, 1000)
                .delay(1000).replaceWith(html);
                $('#viewerPanel').show("slide", { direction: dir }, 1000);
            }
            else{
                $('#viewerPanel').replaceWith(html);
                ice.mobi.tabsetController.showContent('tabs', $('#tabstab_1')[0], {tIndex: 1} );
            }
        }
    });
}

function resizeElementHeight(elementId) {
    var is_firefox = navigator.userAgent.toLowerCase().indexOf('firefox') > -1;
    if( !is_firefox ){
        var element = document.getElementById(elementId);
        if( element ){
            var height = 0;
            var body = window.document.body;
            if (window.innerHeight) {
                height = window.innerHeight;
            } else if (body.parentElement.clientHeight) {
                height = body.parentElement.clientHeight;
            } else if (body) {
                if (body.clientHeight) {
                    height = body.clientHeight;
                }
            }
            element.style.height = ((height - element.offsetTop) + "px");       
        }
        
    }
}


function addResizeAfterUpdatesListener(elementId){

    // check caller to see if orientation changes are support and fall back
    // to window resize events otherwise
    var supportsOrientationChange = "onorientationchange" in window,
        orientationEvent = supportsOrientationChange ? "orientationchange" : "resize";
    
    var resizeHandler = function(updates) {
        resizeElementHeight(elementId);
    }

    // resize height on first load
    resizeElementHeight(elementId);

    // apply resize on either orientation or window size change.
    window.addEventListener(orientationEvent, resizeHandler);
    
}

function getGalleryUpdate(){
    var updated = Number($('#updated').val());
    if( isNaN(updated) ){
        updated = 0;
    }
    var url = 'photo-list-json?since='+updated;
    console.log('getGalleryUpdate(): '+url);
    $.ajax({
        url:url,
        cache:false,
        processData:false,
        type:'GET',
        success:function (json) {
            updateGalleryList(json);
        }
    });
}

function getGalleryRefresh(){
    console.log('getGalleryRefresh()');
    $.ajax({
        url:'photo-list',
        cache:false,
        processData:false,
        type:'GET',
        success:function (html) {
             $('#galleryFrm').replaceWith(html);
        }
    });
}


function updateGalleryList(json){
    var start = "<li class='mobi-list-item'><div class='mobi-list-item-default'>";
    var end = "</div></li>";
    if( json.length > 0 ){
        for( i in json ){
            $('#'+json[i].id).parent().parent().remove(); 
        }
        var updated = Number($('#updated').val());
        for( m in json ){
            var msg = json[m];
            updated = Math.max(updated, msg.created);
            var item = start+"<div id='"+msg.id+"' data-created='"+msg.created+"'>"
                + "<a class='mediaLink' href='#' onclick=\"updateViewerPanel('"+msg.id+"');\">"
                + "<img src='resources/uploads/"+msg.fileName+"' class='p'>"
                + "</a><span class='desc'>"+msg.description+"</span>";
            
            var galleryList = $('#galleryList').children();
            var found = false;
            if( galleryList.length == 0 ){
                document.getElementById('galleryList').innerHTML = item;
                found = true;
            }
            else{
                galleryList.each(function(i){
                    if( !found ){
                        var dataElemQ = $(this).find('div > div');
                        if( dataElemQ.length > 0 ){
                            var dataElem = dataElemQ[0];
                            var created = Number(dataElem.getAttribute('data-created'));
                            if( created > msg.created){
                                $(this).before(item);
                                $('#'+msg.id).parent().effect("highlight", {}, 200);
                                found = true;
                            }
                            else if ( i == (galleryList.length - 1) ){
                                $(this).after(item);
                                $('#'+msg.id).parent().effect("highlight", {}, 200);
                                found = true;
                            }
                        }
                        
                    }
                });
            }
        }
        $('#updated').val(updated);
    }
}

function isTextNode(node){
    return /^\s*$/.test(node.nodeValue);
}
