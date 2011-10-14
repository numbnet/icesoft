if (!window['mobi']) {
    window.mobi = {};
}
mobi.datespinner = {
	  init: function(clientId, yrStrt, yrEnd, yrSel, mSel, dSel ){
          var intDt = parseInt(dSel);
          var intMth = parseInt(mSel);
          var intYr= parseInt(yrSel);
          //have to set the value controls to the correct integer
          var mnthEl = document.getElementById(clientId+"_mInt");
          var yrEl = document.getElementById(clientId+"_yInt");
          var dateEl = document.getElementById(clientId+"_dInt");
          if (mnthEl){
              mnthEl.innerHTML = intMth;
          }
          if (yrEl){
              yrEl.innerHTML=intYr;
          }
          if (dateEl){
              dateEl.innerHTML=intDt;
          }
          this.updateDate(clientId);
	   },
	   mUp: function(clientId){
          var mId = clientId+'_mInt';
	       var mnthEl = document.getElementById(mId);
           if (mnthEl){
               var mInt = this.getIntValue(mId);
               if (mInt == 12){
                   mnthEl.innerHTML = 1;
               }
               else {
                   mnthEl.innerHTML =mInt+ 1;
               }
           }
            this.updateDate(clientId);
	   },
       mDn: function(clientId){
           var mId = clientId+'_mInt';
	       var mnthEl = document.getElementById(mId);
           if (mnthEl){
               var mInt = this.getIntValue(mId);
               if (mInt == 1){
                   mnthEl.innerHTML = 12;
               }
               else {
                   mnthEl.innerHTML =mInt -1;
               }
           }
           this.updateDate(clientId);
	   },
        yUp: function(clientId, yrMin, yrMax){
           var yId = clientId+'_yInt';
           var yrEl = document.getElementById(yId);
            if (yrEl){
                var yInt = this.getIntValue(yId);
                if (yInt == yrMin || yInt ==yrMax){
                    //nothing
                }
                else {
                    yrEl.innerHTML = yInt+1;
                }
            }
            this.updateDate(clientId);
        },
        yDn: function(clientId, yrMin, yrMax){
            var yrEl = document.getElementById(clientId+'_yInt');
            if (yrEl){
                yInt = this.getIntValue(clientId+'_yInt');
                if (yInt == yrMin || yInt ==yrMax){
                    //nothing
                }
                else {
                    yrEl.innerHTML = yInt-1;
                }
            }
            this.updateDate(clientId);
        },
        dUp: function(clientId){
            var dId = clientId+'_dInt';
            var dEl = document.getElementById(dId);
            var mInt = this.getIntValue(clientId+"_mInt");
            var yInt = this.getIntValue(clientId+"_yInt");
            var dInt = this.getIntValue(dId);
            var numDaysInMonth = this.daysInMonth(mInt, yInt);
            if (dEl){
               if (dInt >= numDaysInMonth ){
                       dEl.innerHTML = 1;
               }
               else {
                    dEl.innerHTML = dInt+1;
               }
            }
            this.updateDate(clientId);
         },
         dDn: function(clientId){
            var dId = clientId+'_dInt';
            var dEl = document.getElementById(dId);
            var mInt = this.getIntValue(clientId+"_mInt");
            var yInt = this.getIntValue(clientId+"_yInt");
            var dInt = this.getIntValue(dId);
            var numDaysInMonth = this.daysInMonth(mInt, yInt);
            if (dEl){
                 if (dInt == 1 || dInt > numDaysInMonth){
                    dEl.innerHTML = numDaysInMonth;
                 }
                 else {
                    dEl.innerHTML = dInt-1;
                 }
             }
             this.updateDate(clientId);
        },
        updateDate: function(clientId){
            var hiddenEl = document.getElementById(clientId+"_hidden");
            var dId = clientId+"_dInt";
            var dEl = document.getElementById(dId);
            var mInt = this.getIntValue(clientId+"_mInt");
            var yInt = this.getIntValue(clientId+"_yInt");
            var dInt = this.getIntValue(dId);
            var upDate = this.validate(yInt, mInt, dInt);
            if (!upDate){
                dInt = this.daysInMonth(mInt, yInt);
                dEl.innerHTML = dInt;
            }
            else{  //update hidden field with the new date object
               var hiddenString = 'date';
               if (mInt < 10){
                   hiddenString = dInt +'-0'+mInt+'-'+yInt;
               }
               else {
                   hiddenString = dInt +'-'+mInt+'-'+yInt;
               }
               hiddenEl.value = hiddenString;
               //update title
               this.writeTitle(clientId, dInt, mInt, yInt);
            }

        },

        writeTitle: function(clientId, iD, iM, iY){
            var dateObj = new Date();
            dateObj.setDate(iD);
            dateObj.setMonth(iM-1);
            dateObj.setYear(iY);
            ice.log.debug(ice.log, 'set dateObj to '+dateObj.toDateString());
            var titleEl = document.getElementById(clientId+'_title');
            titleEl.innerHTML = dateObj.toDateString();
        },

        daysInMonth: function(iMnth, iYr){
            var aDate = new Date(iYr, iMnth, 0);
            return aDate.getDate();
        },

        validate: function(iY, iM, iD){
            if (iY != parseInt(iY,10) || iM != parseInt(iM, 10) || iD != parseInt(iD, 10)) return false;
            iM --;
            var newDate = new Date (iY, iM, iD);
            if ((iY==newDate.getFullYear()) && (iM == newDate.getMonth()) && (iD=newDate.getDate())) {
                return newDate;
            }
            else return false;
        },

        getIntValue: function(id){
            var element = document.getElementById(id);
            if (element){
                var stringEl = element.innerHTML;
                return parseInt(stringEl);
            } else return 1;
        },
        select: function(clientId){
            var titleEl = document.getElementById(clientId+'_title');
            var inputEl = document.getElementById(clientId+'_input');
            inputEl.value = titleEl.innerHTML;
            this.close(clientId);
        },
        open: function(clientId){
            document.getElementById(clientId).className = "mobi-date-bg";
            document.getElementById(clientId+"_popup").className = "mobi-date-container";
        },
        close: function(clientId){
            document.getElementById(clientId).className = "mobi-date";
            document.getElementById(clientId+"_popup").className = "mobi-date-container-hide";
        }

}