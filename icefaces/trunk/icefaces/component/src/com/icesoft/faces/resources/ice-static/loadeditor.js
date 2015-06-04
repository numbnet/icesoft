inputrichtext = {};

inputrichtext.ie11 = !!navigator.userAgent.match(/Trident.*rv\:11\./);
inputrichtext.pendingInit = [];

inputrichtext.init = function(f, context) {
	if (inputrichtext.loaded) {
		f.call();
	} else {
		inputrichtext.pendingInit.push(f);
		if (!inputrichtext.loading) {
			inputrichtext.loading = true;
			inputrichtext.loadEditor(context);
		}
	}
};

// load appropriate CKEDITOR version for IE11 or for all the other browsers
inputrichtext.loadEditor = function(context) {
	var pathIE11 = context+"/xmlhttp/ice-static/editorIE11/";
	var pathOther = context+"/xmlhttp/ice-static/editor/";
	var path = inputrichtext.ie11 ? pathIE11 : pathOther;
	var head = document.getElementsByTagName('head')[0];

	var initPending = function() {
		if (window.renderEditor) {
			inputrichtext.loaded = true;
			for (var i = 0; i < inputrichtext.pendingInit.length; i++) {
				var f = inputrichtext.pendingInit[i];
				f.call();
			}
			inputrichtext.pendingInit.length = 0;
		} else {
			setTimeout(initPending,50);
		}
	};

	var loadRichTextEntry = function() {
		if (window.CKEDITOR) {
			var initScript = document.createElement('script');
			initScript.setAttribute('type', 'text/javascript');
			initScript.setAttribute('src', path + "ckeditor_ext.js");
			head.appendChild(initScript);
			setTimeout(initPending,50);
		} else {
			setTimeout(loadRichTextEntry,50);
		}
	};

	var loadCKEDITOR = function() {
		var ckeditor = document.createElement('script');
		ckeditor.setAttribute('type', 'text/javascript');
		ckeditor.setAttribute('src', path + "ckeditor.js");
		head.appendChild(ckeditor);
		setTimeout(loadRichTextEntry,50);
	};

	setTimeout(loadCKEDITOR, 1);
};

function _uev(editor) { }; // placeholder