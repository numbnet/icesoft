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
	initialize:function(ele, lang, _for, basePath, width, height, toolbar, custConPath, skin) {
	this._for = _for; 
	if (lang =="" || lang == null) {
	   lang ="en";
	}
	var valueHolder = $(ele + 'valueHolder');
	var value = valueHolder.value;
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
          this.thirdPartyObject.Config['SkinPath'] = basePath+ 'editor/skins/'+ skin +'/' ;
	this.thirdPartyObject.ToolbarSet  = toolbar;
	if (custConPath !="null") {
	     this.thirdPartyObject.Config["CustomConfigurationsPath"] = custConPath;
	}
	this.thirdPartyObject.CreateIce(ele);
	}
};

Object.extend(Ice.FCKeditor,  Ice.Repository);

Ice.FCKeditorUtility = Class.create();
Ice.FCKeditorUtility = {
    updateValue: function(ele) {
        try {
            var oEditor = FCKeditorAPI.GetInstance(ele) ;
            if (oEditor != null) {
               if (toogleState(oEditor)) return;
                var valueHolder = $(ele + 'valueHolder');
                var editorValue = $(ele);                
                editorValue.value = valueHolder.value  
                oEditor.SetHTML( editorValue.value) ;
               if (oEditor.EditorWindow.parent.FCK.HasFocus) { 
                    oEditor.EditorWindow.focus();
                    oEditor.focus();                    
                }  
            }
        } catch(err) {}
    },
    
    updateFields: function(ele) {
	    try {
		    var oEditor = FCKeditorAPI.GetInstance(ele) ;   
		    if (!oEditor) return;
		    var editorValue = $(ele);
		    var saveOnSubmit = $(ele + 'saveOnSubmit');
		    if (saveOnSubmit && editorValue && oEditor.GetXHTML(true).length > 0) { 
		        var valueHolder = $(ele + 'valueHolder');
		        editorValue.value = oEditor.GetXHTML(true);
		        valueHolder.value = editorValue.value;    
		    }
	    } catch (err) {}     
    }
};

FCKeditor.prototype.CreateIce = function(eleId)
{
	var editor = document.getElementById(eleId+'editor') ;
	editor.innerHTML = this.CreateHtml() ;
}

function FCKeditor_OnComplete( editorInstance ){
    toogleState(editorInstance);
	editorInstance.LinkedField.form.onsubmit = function() {
		return FCKeditorSave(editorInstance);
	}
}

function toogleState(editorInstance) {

       var disabled = $(editorInstance.Name + 'Disabled');
       if (!disabled) return false;
       if (disabled.value == "true") {
            if (document.all) {
                editorInstance.EditorDocument.body.disabled = true;
            } else {
                editorInstance.EditorDocument.designMode = "off";
            } 
            editorInstance.EditorWindow.parent.FCK.ToolbarSet.Disable();
            editorInstance.EditorWindow.parent.FCKToolbarButton.prototype.RefreshState = function(){return false;};
            editorInstance.EditorWindow.parent.FCKToolbarSpecialCombo.prototype.RefreshState = function(){return false;};            
            return true;     
       } else {
            if (document.all) {
                editorInstance.EditorDocument.body.disabled = false;
            } else {
                editorInstance.EditorDocument.designMode = "on";
            } 
            editorInstance.EditorWindow.parent.FCK.ToolbarSet.Enable();
            editorInstance.EditorWindow.parent.FCK.ToolbarSet.RefreshModeState();                        
            return false;      
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
            var editorWindow = editIns.EditorWindow;
            var hasFocus = true;
            if (editorWindow) {
                hasFocus = editorWindow.parent.FCK.HasFocus;
            }
            if(editIns.IsDirty() && hasFocus) {
                element.value = editIns.GetXHTML(true);
                editIns.ResetIsDirty();
            }
            //set the current contents of the editor
            var valueHolder = $(editIns.Name + 'valueHolder');
            var editorValue = $(editIns.Name);
            editorValue.value = editIns.GetXHTML(true);
            valueHolder.value = editorValue.value;
        }
    } 

	var form = Ice.util.findForm(element);
	iceSubmit(form,element,new Object());
	return false;
}

