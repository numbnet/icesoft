CKEDITOR.plugins.add('iceSave',{
    init:function(a){
        var cmd = a.addCommand('save',{exec:CKsaveAjax})
        a.ui.addButton('IceSave',{
            label:'Save',
            command:'save'
        })
    }
})

function CKsaveAjax(editor){

    var theForm = document.getElementById(editor.name).form;
    var nothingEvent = new Object();
    document.getElementById(editor.name).value = editor.getData(); 
    iceSubmit( theForm , document.getElementById(editor.name), nothingEvent);
}

CKEDITOR.config.extraPlugins = "iceSave";

function getToolbar(toolbar) {

 	
	if (toolbar == 'Basic') {
	    return [
	            ['IceSave', 'Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink','-','About']
	            ];
	    
	} else if (toolbar == 'Default'){
		return [
		        ['Source','-','IceSave','NewPage','Preview','-','Templates'],
		        ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
		        ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
		        ['Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField'],
		        '/',
		        ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
		        ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote','CreateDiv'],
		        ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
		        ['BidiLtr', 'BidiRtl'],
		        ['Link','Unlink','Anchor'],
		        ['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','Iframe'],
		        '/',
		        ['Styles','Format','Font','FontSize'],
		        ['TextColor','BGColor'],
		        ['Maximize', 'ShowBlocks','-','About']
		        
		        ];	

	} else { 
		return toolbar;
	}
}


function renderEditor(editor, defaultToolbar, lang, _skin, _height, _width, _customConfig, saveOnSubmit) {
	CKEDITOR.config.defaultLanguage = lang;
	if (_skin == 'default' || _skin == 'silver') {
		_skin = 'v2'
	}
	if (_skin!= 'v2' &&
			_skin!= 'office2003' &&			
				_skin!= 'kama') {
		alert('invalid skin name '+ _skin);
		_skin = 'v2'
	} 
	CKEDITOR.config.skin = _skin;

	try {
		if (CKEDITOR.instances[editor]) {
			CKEDITOR.instances[editor].destroy(true);
		}
 
		var editorInstance = CKEDITOR.replace(editor, {
				toolbar : getToolbar(defaultToolbar),
				height: _height,
				width: _width,
				customConfig : _customConfig,
				htmlEncodeOutput : false
		});
		editorInstance.setData(document.getElementById(editor).value);
		if (saveOnSubmit) {
			var updateElement = function(e) {
				document.getElementById(editor).value = editorInstance.getData();
			};
			var htmlNode = document.getElementById(editor+'container');
			htmlNode.onmouseout =  updateElement;			
			editorInstance.on('blur', updateElement);		
		}
	} catch(e) {
		alert(e);	
	}
}
// Update editor value
function _uev(editor) {
	if (CKEDITOR.instances[editor]) {
		var element = document.getElementById(editor);
		if (element) {
			CKEDITOR.instances[editor].setData(element.value);
		}
	}
}