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

mobi.input = {
		
    flipswitch: function(clientId, singleSubmit){
    	console.log("in javascript for clientId="+clientId);
    	var oldVal = $(clientId).bind("change", function(){
 	       if(oldVal != $(this).val()){
 	           oldVal = $(this).val();
 	           console.log("val change to " + oldVal);
 	       }
        }).val();
    	
    },

    submit: function(clientId, singleSubmit) {
        var element = document.getElementById(clientId);
        var hidden = document.getElementById(clientId + "_hidden");
        var inputEl = $element.siblings('input');
        var elVal = "unset";
        var ssubmit = singleSubmit;
        if (element) {
            //	alert ("clientId "+clientId+" found");
            elVal = element.value;
        } else {
            alert("clientId " + clientId + " NOT FOUND");
        }
        if (input){
            alert("input element is="+inputEl+" value is ="+inputEl.value);
        }else {
        	alert("could not get at input element");
        }


        if (hidden) {
            hidden.value = elVal;
            //             alert("hidden found id="+hidden.value+"singleSubmit="+singleSubmit+" elvalue="+elVal);
        } else {
            var input = document.createElement("input");
            input.setAttribute("type", "hidden");
            input.setAttribute("name", clientId + "_hidden");
            input.setAttribute("value", elVal);
            document.getElementById(clientId).appendChild(input);
            alert("had to create hidden field and singleSubmit=" + singleSubmit + " val = " + elVal);
        }
        if (ssubmit) {
            //   	alert("singleSubmit true submitting value="+hidden.value+" for clientId="+clientId);
            ice.se(null, clientId);
        }

    },
 
};

