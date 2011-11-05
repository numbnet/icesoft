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

mobi.flipper = {                
        init: function(clientId, event, flipperEl, singleSubmit){
              ice.log.debug(ice.log, 'clientId is '+clientId);               
              if (flipperEl){
                var oldClass = flipperEl.className;
                var value = "off";
                if (oldClass.indexOf('-off ')>0){
                	flipperEl.className='mobi-flip-switch mobi-flip-switch-on ';
                	value = true;
                }else{
                	flipperEl.className='mobi-flip-switch mobi-flip-switch-off ';
                	value = false;
                }
                var hidden = clientId+"_hidden";
                var thisEl = document.getElementById(hidden);
                if (thisEl){
                    thisEl.value=value.toString();
                }
                if (singleSubmit){
                    ice.se(event, clientId);
                }
              }
              else{
                ice.log.debug(ice.log, 'do not have a switch element');
              }

         }
} ;