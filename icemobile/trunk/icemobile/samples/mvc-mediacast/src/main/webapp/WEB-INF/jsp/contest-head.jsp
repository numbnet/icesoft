<head>
	<title><fmt:message key="title" /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black" />
	<link href="<c:url value="/resources/images/favicon.ico"/>" rel="shortcut icon" type="image/x-icon"></link>
    <link href="<c:url value="/resources/images/favicon.ico"/>" rel="icon" type="image/x-icon"></link>
    <mobi:deviceResource />
	<link rel="stylesheet" href="<c:url value="/resources/css/contest.css"/>"/>
	<script type="text/javascript" src="code.icepush"></script>
	<script type="text/javascript" src='<c:url value="/resources/javascript/jquery-1.8.1-min.js"/>'></script>
	<script type="text/javascript">document.documentElement.className = document.documentElement.className+' js';$(document).ready(function(){$('.ajaxShow').show();});</script>
	<script type="text/javascript">
    function enhanceForm(theForm)  {
        //submitting the form will update 
        //the containing div with class ajaxzone
        $(document).ready(function () {
            $(theForm).submit(function () {
                var updateRegion = $(this).closest("div.ajaxzone");
                if (window.ice && ice.upload) {
                    window.ice.handleResponse = function (data) {
                        updateRegion.replaceWith(unescape(data));
                    }
                    ice.upload($(this).attr("id"));
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
                        formData = $(theForm).serialize();
                        mimeType = "application/x-www-form-urlencoded";
                    }
    
                    $.ajax({
                        url:$(this).attr("action"),
                        data:formData,
                        cache:false,
                        contentType:mimeType,
                        processData:false,
                        type:'POST',
                        success:function (html) {
                            updateRegion.replaceWith(html);
                        }
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
                return false;
            });
        });
    }
        </script>
</head>
