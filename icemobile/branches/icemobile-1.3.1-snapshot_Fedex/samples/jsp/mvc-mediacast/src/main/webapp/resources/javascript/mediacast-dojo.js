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

function enhanceGet(linkSelector){
    require(["dojo/query", "dojo/on", "dojo/request", "dojo/dom-attr", "dojo/domReady!"], 
        function(query, on, request, attr) {
            query(linkSelector).on("click", function(evt){
                request.get( attr.get(this, "href") ).then( function(data){
                    document.body.innerHTML = data;
                });
                
                return false;
            });
        }
    )
}
function enhanceForm(){
    require(["dojo/dom", "dojo/query", "dojo/on", "dojo/dom-attr", "dojo/dom-construct"
            ,"dojo/dom-form", "dojo/request/iframe", "dojo/domReady!"], 
        function(dom, query, on, attr, domConstruct, form, iframe) {
            var uploadForm = dom.byId("uploadForm");
            if( uploadForm ){
                var updateRegion = dom.byId("root");
                // Attach the onsubmit event handler of the form
                on(uploadForm, "submit", function(evt){
                    // prevent the page from navigating after submit
                    evt.stopPropagation();
                    evt.preventDefault();
                    
                    if( attr.get(dom.byId('operation'), "value") == 'cancel'){
                        console.log('cancel submit');
                        return true;
                    }
                    
                    if (window.ice && ice.upload) {
                        window.ice.handleResponse = function (data) {
                            domConstruct.place(unescape(data), "root", "replace");
                        };
                        console.log('ice.upload');
                        ice.upload(attr.get(this, "id"));
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
                            formData = form.toObject("uploadForm");
                            mimeType = "application/x-www-form-urlencoded";
                        }
                        console.log("starting iframe xhr post " + new Date());
                        
                        //pause push connection to avoid connection contention on iphone
                        var ios = /(iPhone|iPod|iPad).*AppleWebKit/i.test(navigator.userAgent);
                        if( ios ){
                            ice.push.connection.pauseConnection();
                        }
                        
                        iframe(attr.get(this, "action"), {
                            handleAs: "html",
                            form: dom.byId("uploadForm"),
                            method: "POST",
                            data: formData
                          }).then(function(response, ioArgs){
                              if( response ){
                                  var responseFragment = query("#root", response)[0]
                                  domConstruct.place(responseFragment, "root", "replace");
                              }
                              //resume push connection to receieve further notifications
                              if( ios ){
                                  ice.push.connection.resumeConnection();
                              }
                              console.log("finished iframe xhr post " + new Date())
                              return response;
                          }, function(err){
                              console.log("ERROR: " + response, ioArgs);
                              return response;
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
            }
        }
    )
}

function cancelBtnHandler(){
    require(["dojo/dom", "dojo/on", "dojo/dom-attr", "dojo/domReady!"], 
        function(dom, on, attr) {
            var cancelBtn = document.getElementById('cancelBtn');
            if( cancelBtn ){
                on(cancelBtn, "click", function(evt){
                    attr.set(dom.byId('operation'), 'cancel');
                });
            }
        }
    )
}

function updateViewerPanel(id,action){
    var url = 'viewer?id='+id+'&view=tablet';
    if( action ){
        url += '&action='+action;
    }
    require(["dojo/dom", "dojo/dom-construct", "dojo/request/xhr", "dojo/domReady!"], 
        function(dom, domConstruct, xhr) {
              xhr( url, {
                handleAs: "html",
                preventCache: true,
                method: "GET"
              }).then(function(data){
                  if( action ){
                      var dir = (action == 'back' ? 'right' : 'left');
                      dojo.fadeOut('viewerPanel').play();
                      domConstruct.place(data, "viewerPanel", "replace");
                      dojo.fadeIn('viewerPanel').play();
                  }
                  else{
                      domConstruct.place(data, "viewerPanel", "replace");
                      ice.mobi.tabsetController.showContent('tabs', dom.byId('tabstab_1'), {tIndex: 1} );
                  }
              });
        }
    );
}

function getGalleryUpdate(){
    require(["dojo/request/xhr", "dojo/domReady!"], 
    function(xhr) {
        var updated = getUpdatedTimestamp();
        var url = 'gallery-list-json?since='+updated;
        console.log('getGalleryUpdate(): '+url);
        xhr( url, {
            handleAs: "json",
            preventCache: true,
            method: "GET"
          }).then(function(data){
              updateGalleryList(data);
          });
    });
}

function getUpdatedTimestamp(){
    var updated = 0;
    require(["dojo/dom", "dojo/dom-attr", "dojo/domReady!"], 
    function(dom, attr) {
        var updatedDataNode = dom.byId('updated');
        if( updatedDataNode ){
            var updated = Number(attr.get(updatedDataNode,'value'));
            if( isNaN(updated) ){
                updated = 0;
            }
        }
    });
    return updated;
}

function getGalleryRefresh(){
    require(["dojo/dom-construct", "dojo/request/xhr", "dojo/domReady!"], 
    function(domConstruct, xhr) {
        console.log('getGalleryRefresh()');
        xhr( 'gallery-list', {
            handleAs: "json",
            preventCache: true,
            method: "GET"
          }).then(function(data){
              domConstruct.place(data, "galleryFrm", "replace");
          });
    });
}


function updateGalleryList(json){
    require(["dojo/dom", "dojo/query", "dojo/dom-attr", "dojo/dom-construct"
           , "dojo/fx", "dojo/fx/easing", "dojo/domReady!"], 
    function(dom, query, attr, domConstruct, fx, easing) {
        var start = "<li class='mobi-list-item'><div class='mobi-list-item-default'>";
        var end = "</div></li>";
        if( json.length > 0 ){
            for( i in json ){
                var itemNode = dom.byId(json[i].id);
                if( itemNode ){
                    domConstruct.destroy(itemNode.parentNode.parentNode); 
                }
            }
            var updated = getUpdatedTimestamp();
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
                
                var galleryList = query('#galleryList li');
                var found = false;
                if( galleryList.length === 0 ){
                    document.getElementById('galleryList').innerHTML = item;
                    found = true;
                }
                else{
                    galleryList.forEach(function(entry, i){
                        if( !found ){
                            var dataElemQ = query('div > div', entry);
                            if( dataElemQ.length > 0 ){
                                var dataElem = dataElemQ[0];
                                var created = Number(attr.get(dataElem,'data-created'));
                                if( created > msg.created){
                                    domConstruct.place(item, entry, "before");
                                    fx.anim(item, { color: 'yellow' }, 100, easing.cubicIn, function(){
                                        fx.anim(item, { color: 'black' }, 100, easing.cubicOut);
                                    });
                                    found = true;
                                }
                                else if ( i == (galleryList.length - 1) ){
                                    domConstruct.place(item, entry, "after");
                                    fx.anim(item, { color: 'yellow' }, 100, easing.cubicIn, function(){
                                        fx.anim(item, { color: 'black' }, 100, easing.cubicOut);
                                    });
                                    found = true;
                                }
                            }
                            
                        }
                    });
                }
            }
            attr.set('updated', 'value', updated);
        }
        
    });

}

function enhanceCarousel(){
    require(["dojo/query", "dojo/on", "dojo/dom-attr", "dojo/domReady!"], 
    function(query, on, attr) {
        query('#recentMessagesCarousel a').forEach( function(entry, i){
            on(entry, "click", function(){
                updateViewerPanel(attr.get(entry, "data-id"));
            });
        } );
    });
}
