Custom Modifications:

ICE-8130	ckeditor/plugins/wsc/dialogs/wsc.js
Spellcheck plug-in was modified to (1) use correctly the URL mapping implemented to access resources in JSF, and to (2) supply absolute paths for the resources that will be loaded by the external site (spellchecker.net) iframe.

ICE-8715	(all CSS files)
Replaced the contents of url() expressions to contain EL resource expressions instead of simple relative paths.
