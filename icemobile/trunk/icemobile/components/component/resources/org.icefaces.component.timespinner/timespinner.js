if (!window['mobi']) {
    window.mobi = {};
}
mobi.timespinner = {
      pattern: {}, //only supports 'hh:mm a' at this time.
      opened: {},
	  init: function(clientId, hrSel, mSel, aSel, format ){
          var idPanel = clientId+"_bg";
          if (!document.getElementById(idPanel).className ){
             document.getElementById(idPanel).className = 'mobi-date-bg-inv';
          }
          var intAmPm = parseInt(aSel);
          var intMinute = parseInt(mSel);
          var intHr= parseInt(hrSel);
          if (format){
              this.pattern[clientId] = format;
              ice.log.debug(ice.log, ' pattern change not yet implemented ='+this.pattern);
          }
          this.opened[clientId]= false;
          //have to set the value controls to the correct integer
          var hrEl = document.getElementById(clientId+"_hrInt");
          var minEl = document.getElementById(clientId+"_mInt");
          var ampmEl = document.getElementById(clientId+"_ampmInt");
          if (minEl){
              minEl.innerHTML = intMinute;
          }
          if (hrEl){
              hrEl.innerHTML=intHr;
          }
          var ampm = 'AM';
          if (intAmPm >0){
            ampm = 'PM';
          }
   //       ice.log.debug(ice.log, 'val of intAmpm ='+intAmPm + ' ampm = '+ampm);
          if (ampmEl){
              ampmEl.innerHTML=ampm;
          }
          this.updateTime(clientId);
	   },
	   mUp: function(clientId){
          var mId = clientId+'_mInt';
	       var minEl = document.getElementById(mId);
           if (minEl){
               var mInt = this.getIntValue(mId);
               if (mInt == 59){
                   minEl.innerHTML = 0;
               }
               else {
                   minEl.innerHTML =mInt+ 1;
               }
           }
            this.updateTime(clientId);
	   },
       mDn: function(clientId){
           var mId = clientId+'_mInt';
	       var minEl = document.getElementById(mId);
           if (minEl){
               var mInt = this.getIntValue(mId);
               if (mInt == 0){
                   minEl.innerHTML = 59;
               }
               else {
                   minEl.innerHTML =mInt -1;
               }
           }
           this.updateTime(clientId);
	   },
       hrUp: function(clientId){
           var hrId = clientId+'_hrInt';
           var hrEl = document.getElementById(hrId);
           if (hrEl){
                var hrInt = this.getIntValue(hrId);
                if ( hrInt == 12){
                    hrEl.innerHTML = 1;
                }
                else {
                    hrEl.innerHTML = hrInt+1;
                }
            }
            this.updateTime(clientId);
        },
        hrDn: function(clientId){
            var hrEl = document.getElementById(clientId+'_hrInt');
            if (hrEl){
                var hrInt = this.getIntValue(clientId+'_hrInt');
                if (hrInt == 1){
                    hrEl.innerHTML = 12;
                }
                else {
                    hrEl.innerHTML = hrInt-1;
                }
            }
            this.updateTime(clientId);
       },
       ampmToggle: function(clientId){
            var aId = clientId+'_ampmInt';
            var aEl = document.getElementById(aId);
            if (aEl.innerHTML == "AM"){
                aEl.innerHTML = 'PM';
            }
            else {
                aEl.innerHTML = 'AM';
            }
            this.updateTime(clientId);
        },
        updateTime: function(clientId){
            var ampm = clientId+"_ampmInt";
            var ampmEl = document.getElementById(ampm);
            var mInt = this.getIntValue(clientId+"_mInt");
            var hrInt = this.getIntValue(clientId+"_hrInt");
            this.writeTitle(clientId, hrInt, mInt, ampmEl.innerHTML);
        },

        writeTitle: function(clientId, hr , min, ampm){
            if (hr < 10){
                hr = '0'+hr;
            }
            if (min < 10){
                min = '0'+min;
            }
            var time = hr+':'+min+' '+ampm;
            var titleEl = document.getElementById(clientId+'_title');
            titleEl.innerHTML = time;
        },

        getIntValue: function(id){
            var element = document.getElementById(id);
            if (element){
                var stringEl = element.innerHTML;
                return parseInt(stringEl);
            } else return 1;
        },
        select: function(clientId, cfg){
            this.cfg = cfg;
            var singleSubmit = this.cfg.singleSubmit;
            var event = this.cfg.event;
            var hasBehaviors = false;
            var behaviors =this.cfg.behaviors;
            if (behaviors){
                hasBehaviors = true;
            }
            var inputEl = document.getElementById(clientId+'_input');
            var titleEl = document.getElementById(clientId+'_title');
            inputEl.value = titleEl.innerHTML;
            if (hasBehaviors){
                if (behaviors.change){
                    behaviors.change();
                }
            }
            if (!hasBehaviors && singleSubmit){
                ice.se(event, clientId);
            }
            this.close(clientId);
        },
        toggle: function(clientId){
            if (this.opened[clientId]==false){
                this.open(clientId);
            }  else {
                this.close(clientId);
            }
        },
        open: function(clientId){
            document.getElementById(clientId+'_bg').className = "mobi-time-bg";
            document.getElementById(clientId+"_popup").className = "mobi-time-container";
            this.opened[clientId]= true;
        },
        close: function(clientId){
            document.getElementById(clientId+'_bg').className = "mobi-time";
            document.getElementById(clientId+"_popup").className = "mobi-time-container-hide";
            this.opened[clientId]= false;
        },
        unload: function(clientId){
            alert("unloading for clientId");
           /* var titleEl = document.getElementById(clientId+'_title');
            titleEl.innerHTML = "";  */
            this.pattern[clientId] = null;
            this.opened[clientId] = null;
        }

}