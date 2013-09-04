<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"   uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.util.*, java.io.*" %>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
    response.setHeader("Pragma", "no-cache");//HTTP 1.0
    response.setHeader("Expires", "0");//prevents proxy caching
%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%
    String styleId = request.getParameter("style_id");
    if( "" == styleId ){
        styleId = null;
    }
    String style = request.getParameter("style");
    if( "" == style ){
        style = null;
    }

%>
<!DOCTYPE html>
<html>             
<head>
    <meta charset="UTF-8" />

    <title>ICEmobile Theme Builder</title>
    
    <link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" />

    <link rel="stylesheet" type="text/css" href="resources/css/theme-builder.layout.css" media="all" />
    <link rel="stylesheet" type="text/css" href="resources/css/jquery.ui.css" />
    <link rel="stylesheet" type="text/css" href="resources/css/theme-builder.panel.css" />
    
    <script type="text/javascript" src="resources/javascript/theme-builder/jquery.js"></script>
    <script type="text/javascript" src="resources/javascript/theme-builder/jquery.ui.js"></script>
    <script type="text/javascript" src="resources/javascript/theme-builder/jquery.ui.tabs.paging.js"></script>
    <script type="text/javascript" src="resources/javascript/theme-builder/jquery.color.js"></script>
    <script type="text/javascript" src="resources/javascript/theme-builder/json2.js"></script>
    <script type="text/javascript" src="resources/javascript/theme-builder/theme-builder.js"></script>
    <script type="text/javascript" src="resources/javascript/theme-builder/panel.js"></script>
    <script type="text/javascript" src="resources/javascript/theme-builder/ui.js"></script>
    
    <link href="./resources/images/favicon.ico" rel="icon" type="image/x-icon"/>
    <link href="./resources/images/favicon.ico" rel="shortcut icon" type="image/x-icon"/>
</head>
<body>
    
    <div>
        
        <div id="toolbar">
            <div id="tr-logo">ICEmobile Theme Builder <span style="font-family: Courier New; font-style:italic;">(Alpha)</span></div>
            <div id="button-block-1">
                
                <div id="fix-buttons">
                    <div id="undo">
                        <img src="resources/images/undo.png" alt="Undo" />
                        <span>undo</span>
                    </div>
                    <div id="redo">
                        <img src="resources/images/redo.png" alt="Redo" />
                        <span>redo</span>
                    </div>
                </div>
                <div class="tb-button" id="inspector-button">
                    <img src="resources/images/inspector.png" alt=" "/>
                    <span>Inspector <strong>off</strong></span>
                </div>
            </div>
            <div id="button-block-2">
                <div class="tb-button" id="download-button">
                    <div class="tb-button-inner">
                        <img src="resources/images/download.png" alt="Download" />
                        <div class="text">
                            <span class="big">Copy</span>
                            <span>Custom Theme</span>
                        </div>
                    </div>
                </div>
                <div class="tb-button" id="help-button">
                    <div class="tb-button-inner">
                        <img src="resources/images/help.png" alt="Help" />
                        <div class="text">
                            <span class="big">Help</span>
                            <span>center</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div id="tr_panel">
            <div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
                <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
                    <!--Tabs and tab panels go here-->
                </ul>
            </div>
        </div>
        
        <div id="wrapper">
            <div id="header-wrapper">
                <div id="header">
                    <div id="quickswatch">
                        <h2>Drag a color onto an element below</h2>
                        <div class="colors">
                            <div class="color-drag" style="background-color: #FFFFFF"></div>
                            <div class="color-drag" style="background-color: #F2F2F2"></div>
                            <div class="color-drag" style="background-color: #E6E6E6"></div>
                            <div class="color-drag" style="background-color: #CCCCCC"></div>
                            <div class="color-drag" style="background-color: #808080"></div>
                            <div class="color-drag" style="background-color: #4D4D4D"></div>
                            <div class="color-drag" style="background-color: #000000"></div>
                            <div class="color-drag" style="background-color: #C1272D"></div>
                            <div class="color-drag" style="background-color: #ED1C24"></div>
                            <div class="color-drag" style="background-color: #F7931E"></div>
                            <div class="color-drag" style="background-color: #FFCC33"></div>
                            <div class="color-drag" style="background-color: #FCEE21"></div>
                            <div class="color-drag" style="background-color: #D9E021"></div>
                            <div class="color-drag" style="background-color: #8CC63F"></div>
                            <div class="color-drag" style="background-color: #009245"></div>
                            <div class="color-drag" style="background-color: #006837"></div>
                            <div class="color-drag" style="background-color: #00A99D"></div>
                            <div class="color-drag" style="background-color: #33CCCC"></div>
                            <div class="color-drag" style="background-color: #33CCFF"></div>
                            <div class="color-drag" style="background-color: #29ABE2"></div>
                            <div class="color-drag" style="background-color: #0071BC"></div>
                            <div class="color-drag" style="background-color: #2E3192"></div>
                            <div class="color-drag" style="background-color: #662D91"></div>
                            <div class="color-drag" style="background-color: #93278F"></div>
                            <div class="color-drag" style="background-color: #D4145A"></div>
                            <div class="color-drag" style="background-color: #ED1E79"></div>
                            <div class="color-drag" style="background-color: #C7B299"></div>
                            <div class="color-drag" style="background-color: #736357"></div>
                            <div class="color-drag" style="background-color: #C69C6D"></div>
                            <div class="color-drag" style="background-color: #8C6239"></div>
                            <div class="color-drag" style="background-color: #603813"></div>
                        </div>
                        <div id="sliders">
                            <img src="resources/images/target.png" alt=" "/>
                            <span>LIGHTNESS</span><div id="lightness_slider"></div>
                            <span>SATURATION</span><div id="saturation_slider"></div>
                        </div>
                    </div>

                    <div id="most_recent_colors" style="display: none">
                        <div class="picker">
                            <h2>Recent Colors</h2>
                        </div>
                        <div class="clear"></div>
                        <div class="colors">
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                            <div class="color-drag" style="background-color: #ddd">
                                <input type="color" name="colorpicker"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div id="devicetabs">
                <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
                    <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a href="#phoneView">Phone View</a></li>
                    <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a href="#tabletView">Tablet View</a></li>
                </ul>
                <div style="border: dotted 1px #999;display: inline-block; float: left; clear: both;" id="phoneView">
                    <div class="phone">
                        <div class="device-frame">
                            <iframe src="./showcase-small.jsf?theme=jqm&simulator=true" id="phone_iframe"></iframe>
                        </div>
                    </div>
                </div>
                
                <div style="border: dotted 1px #999;display: inline-block;" id="tabletView">
                    <div class="tablet">
                        <div class="device-frame">
                            <iframe src="./showcase-large.jsf?theme=jqm&simulator=true" id="tablet_iframe"
                                onload="TR.tabletIframeLoadCallback();"></iframe>
                        </div>
                    </div>
                </div>
            </div>
            
            <div id="download" class="dialog" title=" ">
                <h1><strong>Custom Theme CSS</strong></h1>
                <textarea rows="30" cols="40"></textarea>
                <div class="buttonpane">
                    <div class="separator"></div>
                    <img src="resources/images/target_big.png" alt=" "/>
                    <p>
                        Copy the new CSS into a new stylesheet for your project.
                    </p>
                </div>
            </div>
            
           
            <div id="style"><%
                    if( style != null ) {
                        out.println(style);
                    } else {
                        //If the file exists we add the CSS here, if not, we leave it blank for the JS to find on load
                        String filePath = "resources/css/theme-builder-starter.css";
                        File file = new File(getServletContext().getRealPath(filePath));
                        if( file.exists()){
                            BufferedReader br = null;
                            try {
                                String sCurrentLine;
                                br = new BufferedReader(new FileReader(file));
                                while ((sCurrentLine = br.readLine()) != null) {
                                    out.println(sCurrentLine);
                                } 
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (br != null)br.close();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }                       
                    }
                %>
            </div>

        </div>
    </div>
    
    
    
</body>
</html>
