<%--
  ~ Copyright 2004-2013 ICEsoft Technologies Canada Corp.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the
  ~ License. You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an "AS
  ~ IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
  ~ express or implied. See the License for the specific language
  ~ governing permissions and limitations under the License.
  --%>
<%@ page import="org.icemobile.media.VideoBean"  %>
<%@ include file="../includes/pageInfo.jsp"%>
<jsp:useBean id="videoInfo" class="org.icemobile.media.VideoBean" scope="page"/>
<jsp:setProperty name="videoInfo" property="*" />
<!DOCTYPE html >
<html >
<head>
    <title>JSP video</title>
    <mobi:deviceResource/>
</head>
<body>

<p>
   <h5>data from bean</h5>
<ul>
  <li>controls: <%= videoInfo.isControls() %>  </li>
  <li>type (mediaType): <%=videoInfo.getType() %> </li>
  <li>preload : <%=videoInfo.getPreload() %> </li>
  <li>autoPlay: <%= videoInfo.isAutoPlay() %>  </li>
  <li>loop: <%= videoInfo.isLoop() %>  </li>
  <li>width: <%= videoInfo.getWidth() %></li>
  <li>height: <%= videoInfo.getHeight() %></li>
  <li>style: <%= videoInfo.getStyle() %></li>
  <li>styleClass: <%= videoInfo.getStyleClass() %></li>
  <li>linkLabel: <%= videoInfo.getLinkLabel() %></li>
  <li>muted:  <%= videoInfo.isMuted() %></li></li>
</ul>
</p>

<form method="post">
    <mobi:fieldsetGroup>
       <mobi:fieldsetRow>
           <label>preload</label>
           <select name="preLoad" >
              <option value="none" ${videoInfo.preload=='none' ? 'selected' : ''}>none</option>
              <option value="auto" ${videoInfo.preload=='auto' ? 'selected' : ''}>auto</option>
              <option value="metadata" ${videoInfo.preload=='metadata' ? 'selected' : ''}>metadata</option>
          </select>
       </mobi:fieldsetRow>
  <!--     <mobi:fieldsetRow>
           <label>src</label>
           <select name="src" >
              <option value="sample_3GPP2.3g2" ${videoInfo.src=='sample_3GPP2.3g2' ? 'selected' : ''}>3G2 example</option>
              <option value="iPadvideo.mp4" ${videoInfo.src=='sampleIPad.mp4' ? 'selected' : ''}>iPad example</option>
              <option value="androidvideo.mp4" ${videoInfo.preload=='androidvideo.mp4' ? 'selected' : ''}>Android example</option>
          </select>
       </mobi:fieldsetRow> -->
       <mobi:fieldsetRow>
           <label>controls: </label>
           <mobi:flipswitch id="fs2" labelOn="true" labelOff="false"
                            name="controls" value="${videoInfo.controls}"/>
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>loop: </label>
           <mobi:flipswitch id="fs3" labelOn="true" labelOff="false"
                            name="loop" value="${videoInfo.loop}"/>
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>autoPlay: </label>
           <mobi:flipswitch id="fs4" labelOn="true" labelOff="false"
                            name="autoPlay" value="${videoInfo.autoPlay}"/>
       </mobi:fieldsetRow>
 	   <mobi:fieldsetRow>
			<label>Style</label>
			<mobi:inputText name="style" type="text" autoCorrect="off"
				placeholder="Style string" value="${videoInfo.style}" />
		</mobi:fieldsetRow>
	    <mobi:fieldsetRow>
			<label>StyleClass</label>
			<mobi:inputText name="styleClass" type="text" autoCorrect="off"
				placeholder="StyleClass string" value="${videoInfo.styleClass}" />
		</mobi:fieldsetRow>
 	    <mobi:fieldsetRow>
			<label>LinkLabel</label>
			<mobi:inputText name="linkLabel" type="text" autoCorrect="off"
				placeholder="LinkLabel string" value="${videoInfo.linkLabel}" />
		</mobi:fieldsetRow>
	    <mobi:fieldsetRow>
			<label>Width</label>
			<mobi:inputText name="width" type="number" autoCorrect="off"
				placeholder="width of feature" value="${videoInfo.width}" />
		</mobi:fieldsetRow>
	    <mobi:fieldsetRow>
			<label>Height</label>
			<mobi:inputText name="height" type="number" autoCorrect="off"
				placeholder="width of feature" value="${videoInfo.height}" />
		</mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label>html submit button</label>
            <input type="submit" value="Submit" />
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
</form>
<form >
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <label>videoPlayer tag</label>
    	         <mobi:videoPlayer id="videoOne"
                            url="${videoInfo.src}"
                            style="${videoInfo.style}"
                            styleClass="${videoInfo.styleClass}"
                            type="${videoInfo.type}"
                            loop = "${videoInfo.loop}"
                            width = "${videoInfo.width}"
                            height="${videoInfo.height}"
                            preload="${videoInfo.preload}"
                            linkLabel="${videoInfo.linkLabel}"
                            controls="${videoInfo.controls}"/>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
</form>

   <p>
       <ul>
         	<li><a href="../mediaComponents.html">Media Tags</a></li>
    	   <li><a href="../index.html">index</a></li>
       </ul>
   </p>
</body>

</html>