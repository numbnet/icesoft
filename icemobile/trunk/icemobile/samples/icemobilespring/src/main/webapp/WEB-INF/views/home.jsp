<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ page session="false" %>
<html>
<head>
    <title>spring-mvc-showcase</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <link href="<c:url value="/resources/form.css" />" rel="stylesheet"
          type="text/css"/>
    <link href="<c:url value="/resources/jqueryui/1.8/themes/base/jquery.ui.all.css" />"
          rel="stylesheet" type="text/css"/>
    <mobi:deviceResource  view="small"/>
    <script type="text/javascript" src="code.icepush"></script>
    <style>
        #tabs > ul li {
            width: 100px;
            margin: 0;
            padding: 0;
            text-align: center;
        }

        .ui-widget {
            font-size: inherit;
        }

        .ui-tabs {
            padding: 0;
        }

        .ui-tabs .ui-tabs-nav li a {
            padding: .3em .5em;
            width:90%;
        }

        .ui-corner-all {
            border-radius: 0;
        }

        .ui-widget-content {
            border: none;
            background: transparent;
            color: inherit;
        }

        .ui-tabs .ui-tabs-panel {
            font-family: inherit;
            padding: 0;
        }

        .ui-widget {
            font-family: inherit;
        }

        .ui-widget input, .ui-widget select, .ui-widget textarea, .ui-widget button {
            font-size: inherit;
            font-family: inherit;
        }

        @media all and (min-width: 600px) {
            #tabs > ul {
                position: fixed;
                top:0;
                width: 275px;
                border: none;
                overflow: scroll;
                height:100%;
                background:transparent;
            }

            #tabs > ul li {
                width: 100%;
                border-radius: 0;
                text-align: left;
            }
            .ui-tabs .ui-tabs-nav li a {
                padding: 10px;
            }

            #tabs > div {
                margin-left: 275px;
            }

            .ui-tabs .ui-tabs-nav {
                padding: 0;
            }
        }
    </style>
</head>
<body>
<div id="tabs">
<ul>
    <li><a href="#welcome">Welcome</a></li>
    <li><a href="<c:url value="/carousel" />" title="carousel">Carousel</a>
    </li>
    <li><a href="<c:url value="/icemobile" />" title="icemobile">Camera</a>
    </li>
    <li><a href="<c:url value="/campush" />" title="campush">Push</a></li>
    <li><a href="<c:url value="/microphone" />"
           title="microphone">Microphone</a>
    </li>
    <li><a href="<c:url value="/camcorder" />"
           title="camcorder">Camcorder</a></li>
    <li><a href="<c:url value="/mediaspot" />"
           title="mediaspot">MediaSpot</a></li>
    <li><a href="<c:url value="/flipswitch" />"
           title="flipswitch">Flipswitch</a>
    </li>
    <li><a href="<c:url value="/inputtext" />" title="inputtext">Input
        Text</a></li>
    <li><a href="<c:url value="/qrscan" />" title="qrscan">QR Scanner</a>
    </li>
    <li><a href="<c:url value="/geolocation" />"
           title="geolocation">Geolocation</a>
    </li>
    <li><a href="<c:url value="/buttons" />" title="buttons">Buttons</a>
    </li>
    <li><a href="<c:url value="/tabset" />" title="tabset">Tabset</a>
    </li>
    <li><a href="<c:url value="/accordion" />" title="accordion">Accordion</a></li>
    <li><a href="<c:url value="/datetime" />" title="datetime">Time/Date</a></li>
    <li><a href="<c:url value="/list" />" title="list">List Demo</a></li>
    <li><a href="<c:url value="/pagePanel" />" title="pagePanel">Page Panel</a></li>

    <li><a href="#icehtml">HTML</a></li>
</ul>
<div id="welcome">
    <h4>ICEmobile </h4>
    <mobi:fieldSetGroup inset="true">
        <mobi:fieldSetRow>
            Welcome to the ICEmobile Spring MVC demo.
        </mobi:fieldSetRow>
    </mobi:fieldSetGroup>
</div>
<div id="icehtml">
    <h4>ICEmobile HTML</h4>
    <mobi:fieldSetGroup inset="true">
    <mobi:fieldSetRow>
    <div style="padding:20px;">
        <p>
            ICEmobile applications can be developed with a pure HTML/JavaScript
            front-end and a Spring Controller back-end.
        </p>
        <ul>
            <li><a id="camlink" href="<c:url value="/resources/camera.html" />">Camera
                HTML Example</a></li>
            <li><a id="miclink"
                   href="<c:url value="/resources/microphone.html" />">Microphone
                HTML Example</a></li>
        </ul>
        </mobi:fieldSetRow>
        </mobi:fieldSetGroup>
    </div>
</div>
</div>
<script type="text/javascript"
        src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
<script type="text/javascript"
        src="<c:url value="/resources/jqueryform/2.8/jquery.form.js" />"></script>
<script type="text/javascript"
        src="<c:url value="/resources/jqueryui/1.8/jquery.ui.core.js" />"></script>
<script type="text/javascript"
        src="<c:url value="/resources/jqueryui/1.8/jquery.ui.widget.js" />"></script>
<script type="text/javascript"
        src="<c:url value="/resources/jqueryui/1.8/jquery.ui.tabs.js" />"></script>
<script type="text/javascript"
        src="<c:url value="/resources/json2.js" />"></script>
<script>
    MvcUtil = {};
    MvcUtil.showSuccessResponse = function (text, element) {
        MvcUtil.showResponse("success", text, element);
    };
    MvcUtil.showErrorResponse = function showErrorResponse(text, element) {
        MvcUtil.showResponse("error", text, element);
    };
    MvcUtil.showResponse = function (type, text, element) {
        var responseElementId = element.attr("id") + "Response";
        var responseElement = $("#" + responseElementId);
        if (responseElement.length == 0) {
            responseElement = $('<span id="' + responseElementId + '" class="' + type + '" style="display:none">' + text + '</span>').insertAfter(element);
        } else {
            responseElement.replaceWith('<span id="' + responseElementId + '" class="' + type + '" style="display:none">' + text + '</span>');
            responseElement = $("#" + responseElementId);
        }
        responseElement.fadeIn("slow");
    };
    MvcUtil.xmlencode = function (xml) {
        //for IE
        var text;
        if (window.ActiveXObject) {
            text = xml.xml;
        }
        // for Mozilla, Firefox, Opera, etc.
        else {
            text = (new XMLSerializer()).serializeToString(xml);
        }
        return text.replace(/\&/g, '&' + 'amp;').replace(/</g, '&' + 'lt;')
                .replace(/>/g, '&' + 'gt;').replace(/\'/g, '&' + 'apos;').replace(/\"/g, '&' + 'quot;');
    };
    MvcUtil.enhanceForm = function(theForm)  {
        //submitting the form will update 
        //the containing div with class ajaxzone
        $(document).ready(function () {
            $(theForm).submit(function () {
                var updateRegion = $(this).closest("div.ajaxzone");
                if (window.ice && ice.upload) {
                    window.ice.handleResponse = function (data) {
                        updateRegion.replaceWith(unescape(data));
                        $('html, body').animate({ scrollTop:$("#message").offset().top }, 500);
                    }
                    ice.upload($(this).attr("id"));
                    return false;
                }

                var formData = new FormData(this);

                $.ajax({
                    url:$(this).attr("action"),
                    data:formData,
                    cache:false,
                    contentType:false,
                    processData:false,
                    type:'POST',
                    success:function (html) {
                        updateRegion.replaceWith(html);
                        $('html, body').animate({ scrollTop:$("#message").offset().top }, 500);
                    }
                });

                return false;
            });
        });
    }
</script>
<script type="text/javascript">
    $(document).ready(function () {
        $("#tabs").tabs();

        // Append '#' to the window location so "Back" returns to the selected tab
        // after a redirect or a full page refresh (e.g. Views tab).

        // However, note this general disclaimer about going back to previous tabs:
        // http://docs.jquery.com/UI/API/1.8/Tabs#Back_button_and_bookmarking

        $("#tabs").bind("tabsselect", function (event, ui) {
            window.location.hash = ui.tab.hash;
        });


        $("a.textLink").click(function () {
            var link = $(this);
            $.ajax({ url:link.attr("href"), dataType:"text", success:function (text) {
                MvcUtil.showSuccessResponse(text, link);
            }, error:function (xhr) {
                MvcUtil.showErrorResponse(xhr.responseText, link);
            }});
            return false;
        });

        $("a.utf8TextLink").click(function () {
            var link = $(this);
            $.ajax({ url:link.attr("href"), dataType:"text", beforeSend:function (req) {
                req.setRequestHeader("Accept", "text/plain;charset=UTF-8");
            }, success:function (text) {
                MvcUtil.showSuccessResponse(text, link);
            }, error:function (xhr) {
                MvcUtil.showErrorResponse(xhr.responseText, link);
            }});
            return false;
        });

        $("form.textForm").submit(function (event) {
            var form = $(this);
            var button = form.children(":first");
            $.ajax({ type:"POST", url:form.attr("action"), data:"foo", contentType:"text/plain", dataType:"text", success:function (text) {
                MvcUtil.showSuccessResponse(text, button);
            }, error:function (xhr) {
                MvcUtil.showErrorResponse(xhr.responseText, button);
            }});
            return false;
        });

        $("#readForm").submit(function () {
            var form = $(this);
            var button = form.children(":first");
            $.ajax({ type:"POST", url:form.attr("action"), data:"foo=bar&fruit=apple", contentType:"application/x-www-form-urlencoded", dataType:"text", success:function (text) {
                MvcUtil.showSuccessResponse(text, button);
            }, error:function (xhr) {
                MvcUtil.showErrorResponse(xhr.responseText, button);
            }});
            return false;
        });

        $("#writeForm").click(function () {
            var link = $(this);
            $.ajax({ url:this.href, dataType:"text", beforeSend:function (req) {
                req.setRequestHeader("Accept", "application/x-www-form-urlencoded");
            }, success:function (form) {
                MvcUtil.showSuccessResponse(form, link);
            }, error:function (xhr) {
                MvcUtil.showErrorResponse(xhr.responseText, link);
            }});
            return false;
        });

        $("form.readXmlForm").submit(function () {
            var form = $(this);
            var button = form.children(":first");
            $.ajax({ type:"POST", url:form.attr("action"), data:"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><javaBean><foo>bar</foo><fruit>apple</fruit></javaBean>", contentType:"application/xml", dataType:"text", success:function (text) {
                MvcUtil.showSuccessResponse(text, button);
            }, error:function (xhr) {
                MvcUtil.showErrorResponse(xhr.responseText, button);
            }});
            return false;
        });

        $("a.writeXmlLink").click(function () {
            var link = $(this);
            $.ajax({ url:link.attr("href"),
                beforeSend:function (req) {
                    req.setRequestHeader("Accept", "application/application+xml");
                },
                success:function (xml) {
                    MvcUtil.showSuccessResponse(MvcUtil.xmlencode(xml), link);
                },
                error:function (xhr) {
                    MvcUtil.showErrorResponse(xhr.responseText, link);
                }
            });
            return false;
        });

        $("form.readJsonForm").submit(function () {
            var form = $(this);
            var button = form.children(":first");
            var data = form.hasClass("invalid") ?
                    "{ \"foo\": \"bar\" }" :
                    "{ \"foo\": \"bar\", \"fruit\": \"apple\" }";
            $.ajax({ type:"POST", url:form.attr("action"), data:data, contentType:"application/json", dataType:"text", success:function (text) {
                MvcUtil.showSuccessResponse(text, button);
            }, error:function (xhr) {
                MvcUtil.showErrorResponse(xhr.responseText, button);
            }});
            return false;
        });

        $("a.writeJsonLink").click(function () {
            var link = $(this);
            $.ajax({ url:this.href, dataType:"json", success:function (json) {
                MvcUtil.showSuccessResponse(JSON.stringify(json), link);
            }, error:function (xhr) {
                MvcUtil.showErrorResponse(xhr.responseText, link);
            }});
            return false;
        });

        $("#readAtom").submit(function () {
            var form = $(this);
            var button = form.children(":first");
            $.ajax({ type:"POST", url:form.attr("action"), data:'<?xml version="1.0" encoding="UTF-8"?> <feed xmlns="http://www.w3.org/2005/Atom"><title>My Atom feed</title></feed>', contentType:"application/atom+xml", dataType:"text", success:function (text) {
                MvcUtil.showSuccessResponse(text, button);
            }, error:function (xhr) {
                MvcUtil.showErrorResponse(xhr.responseText, button);
            }});
            return false;
        });

        $("#writeAtom").click(function () {
            var link = $(this);
            $.ajax({ url:link.attr("href"),
                beforeSend:function (req) {
                    req.setRequestHeader("Accept", "application/atom+xml");
                },
                success:function (feed) {
                    MvcUtil.showSuccessResponse(MvcUtil.xmlencode(feed), link);
                },
                error:function (xhr) {
                    MvcUtil.showErrorResponse(xhr.responseText, link);
                }
            });
            return false;
        });

        $("#readRss").submit(function () {
            var form = $(this);
            var button = form.children(":first");
            $.ajax({ type:"POST", url:form.attr("action"), data:'<?xml version="1.0" encoding="UTF-8"?> <rss version="2.0"><channel><title>My RSS feed</title></channel></rss>', contentType:"application/rss+xml", dataType:"text", success:function (text) {
                MvcUtil.showSuccessResponse(text, button);
            }, error:function (xhr) {
                MvcUtil.showErrorResponse(xhr.responseText, button);
            }});
            return false;
        });

        $("#writeRss").click(function () {
            var link = $(this);
            $.ajax({ url:link.attr("href"),
                beforeSend:function (req) {
                    req.setRequestHeader("Accept", "application/rss+xml");
                },
                success:function (feed) {
                    MvcUtil.showSuccessResponse(MvcUtil.xmlencode(feed), link);
                },
                error:function (xhr) {
                    MvcUtil.showErrorResponse(xhr.responseText, link);
                }
            });
            return false;
        });

        $("#byHeader").click(function () {
            var link = $(this);
            $.ajax({ url:this.href, dataType:"text", beforeSend:function (req) {
                req.setRequestHeader("FooHeader", "foo");
            }, success:function (form) {
                MvcUtil.showSuccessResponse(form, link);
            }, error:function (xhr) {
                MvcUtil.showErrorResponse(xhr.responseText, link);
            }});
            return false;
        });

    });
</script>
</body>
</html>
