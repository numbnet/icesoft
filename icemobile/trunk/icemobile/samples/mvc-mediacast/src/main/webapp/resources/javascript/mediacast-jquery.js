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
                $('#ajaxloader').fadeOut();
                $(document.body).html(html);
            },
            error:function(){
                $('#ajaxloader').fadeOut();
            }
        });
        return false;
    });
}
function enhanceForm()  {
    var theForm = $("#uploadForm");
    var updateTarget = $("#root");
    //submitting the form will update 
    $(document).ready(function () {
        $(theForm).submit(function () {
            console.log('submit');
            if( $('#operation').val() == 'cancel'){
                console.log('cancel submit');
                return true;
            }
            var updateRegion = $(updateTarget);
            if (window.ice && ice.upload) {
                window.ice.handleResponse = function (data) {
                    updateRegion.replaceWith(unescape(data));
                };
                console.log('ice.upload');
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
                        console.log('ajax response: '+html);
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
            console.log('submit return false');
            return false;
        });
    });
}
function cancelBtnHandler(){
    $('#cancelBtn').click(function(e) {
        $('#operation').val('cancel');
    });
}
function updateViewerPanel(id,action){
    var url = 'viewer?id='+id+'&view=tablet';
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

function getGalleryUpdate(){
    var updated = Number($('#updated').val());
    if( isNaN(updated) ){
        updated = 0;
    }
    var url = 'gallery-list-json?since='+updated;
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
        url:'gallery-list',
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
                + "<a class='mediaLink' href='#' onclick=\"updateViewerPanel('"+msg.id+"');\">";
            if( msg.photo ){
                item += "<img src='resources/uploads/"+msg.photoFileName+"' class='p'>";
            }
            else if( msg.video ){
                item += "<img src='resources/images/movieIcon.png' class='p'>";
            }
            else if( msg.audio ){
                item += "<img src='resources/images/soundIcon.png' class='p'>";
            }
            item += "<span class='desc'>"+msg.description+"</span></a>";
            
            var galleryList = $('#galleryList').children();
            var found = false;
            if( galleryList.length === 0 ){
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

function enhanceCarousel(){
    $('#recentMessagesCarousel').find("a").each(function(){
        $(this).click(function(){
            updateViewerPanel(this.getAttribute("data-id"));
        });
        $(this).attr("href","#");
    });
}
