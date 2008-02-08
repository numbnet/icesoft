/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */
 
Ice.FCKeditor = Class.create();

Ice.FCKeditor.prototype = {
	initialize:function(ele, lang, _for, basePath, width, height, toolbar) {
	this._for = _for; 
	var valueHolder = $(ele + 'valueHolder');
	var value = valueHolder.value;
		if (Ice.Repository.getInstance(ele) == null ) {
			this.thirdPartyObject = new FCKeditor(ele);
			this.thirdPartyObject.BasePath = basePath;
			if (value != null) {
				this.thirdPartyObject.Value = value;
			}
			var editor = document.getElementById(ele) ;
			this.thirdPartyObject.EnableSafari = true;
			this.thirdPartyObject.EnableOpera = true;
			if (_for != '') {
				this.thirdPartyObject.Config[ 'ToolbarLocation' ] = 'Out:'+_for ;
			}
			this.thirdPartyObject.Width = width;
			this.thirdPartyObject.Height = height;
			this.thirdPartyObject.Config["AutoDetectLanguage"] = false ;
			this.thirdPartyObject.Config["DefaultLanguage"]    = lang ;				
			this.thirdPartyObject.ToolbarSet  = toolbar;
			this.thirdPartyObject.CreateIce(ele);
		} else {
			var componentFound = $(ele+'___Frame');
			if (componentFound == null) {
				this.thirdPartyObject = new FCKeditor(ele);
				this.thirdPartyObject.BasePath = basePath;
				if (value != '') {
					this.thirdPartyObject.Value = value;
				}
			this.thirdPartyObject.EnableSafari = true;
			this.thirdPartyObject.EnableOpera = true;
			if (_for != '') {
				this.thirdPartyObject.Config[ 'ToolbarLocation' ] = 'Out:'+_for ;
			}			
            this.thirdPartyObject.Width = width;
            this.thirdPartyObject.Height = height;			
			this.thirdPartyObject.Config["AutoDetectLanguage"] = false ;
			this.thirdPartyObject.Config["DefaultLanguage"]    = lang ;											
			this.thirdPartyObject.ToolbarSet  = toolbar;
			this.thirdPartyObject.CreateIce(ele);	
			}
		}
	}
};

Object.extend(Ice.FCKeditor,  Ice.Repository);

Ice.FCKeditorUtility = Class.create();
Ice.FCKeditorUtility = {
    updateValue: function(ele) {
        try {
            var oEditor = FCKeditorAPI.GetInstance(ele) ;
            if (oEditor != null) {
                var valueHolder = $(ele + 'valueHolder');
                var value = valueHolder.value;  
                oEditor.SetHTML( value) ;
                oEditor.Focus();
            }
        } catch(err) {}
    }
};

FCKeditor.prototype.CreateIce = function(eleId)
{
	var editor = document.getElementById(eleId+'editor') ;
	editor.innerHTML = this.CreateHtml() ;
}

function FCKeditor_OnComplete( editorInstance ){
	editorInstance.LinkedField.form.onsubmit = function() {
		return FCKeditorSave(editorInstance);
	}
}

function FCKeditorSave(editorInstance) {
    var iceInstance = Ice.Repository.getInstance(editorInstance.Name);
    if (iceInstance._for != '') {
    	var all = Ice.Repository.getAll();
    	
	    for (i=0; i < all.length; i++) {
			var instanceName = all[i].thirdPartyObject.InstanceName;
		 	var editIns = FCKeditorAPI.GetInstance(instanceName);
		 	if (editIns != null) {
		 		var element = $(instanceName);
				element.value = editIns.GetXHTML(true);
			}
		}

    } else {
        var all = Ice.Repository.getAll();
        for (i=0; i < all.length; i++) {   
            var instanceName = all[i].thirdPartyObject.InstanceName;
            var editIns = FCKeditorAPI.GetInstance(instanceName);
            //if the editor is not visible on this view, just skip and continue
            if (editIns == null) continue;
            //clear malformed parameters
            var unwantedField = $(instanceName + "___Config");
            if (unwantedField){
                unwantedField.value = "";
                unwantedField.name = unwantedField.id;
            }
            var element = $(editIns.Name);
            //Save the dirty editor only that had the focus
            if(editIns.IsDirty()) {
               element.value = editIns.GetXHTML(true);
                    //set the value for editor
				var valueHolder = $(editIns.Name + 'valueHolder');
                valueHolder.value = editIns.GetXHTML(true);
                editIns.ResetIsDirty();
            }
        }
    } 

	var form = Ice.util.findForm(element);
	iceSubmit(form,element,new Object());
	return false;
}
