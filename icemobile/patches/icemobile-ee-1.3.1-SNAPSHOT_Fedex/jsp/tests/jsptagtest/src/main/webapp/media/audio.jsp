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
<%@ page import="org.icemobile.media.AudioBean"  %>
<%@ include file="../includes/pageInfo.jsp"%>
<jsp:useBean id="audioInfo" class="org.icemobile.media.AudioBean" scope="page"/>
<jsp:setProperty name="audioInfo" property="*" />
<!DOCTYPE html >
<html >
<head>
    <title>JSP audio</title>
    <mobi:deviceResource/>
</head>
<body>

<p>
   <h5>data from bean</h5>
<ul>
  <li>controls: <%= audioInfo.isControls() %>  </li>
  <li>type (mediaType): <%=audioInfo.getType() %> </li>
  <li>preload : <%=audioInfo.getPreload() %> </li>
  <li>autoPlay: <%= audioInfo.isAutoPlay() %>  </li>
  <li>loop: <%= audioInfo.isLoop() %>  </li>
  <li>style: <%= audioInfo.getStyle() %></li>
  <li>styleClass: <%= audioInfo.getStyleClass() %></li>
  <li>linkLabel: <%=audioInfo.getLinkLabel() %></li>
</ul>
</p>

<form method="post">
    <mobi:fieldsetGroup>
       <mobi:fieldsetRow>
           <label>preload</label>
           <select name="preLoad" >
              <option value="none" ${audioInfo.preload=='none' ? 'selected' : ''}>none</option>
              <option value="auto" ${audioInfo.preload=='auto' ? 'selected' : ''}>auto</option>
              <option value="metadata" ${audioInfo.preload=='metadata' ? 'selected' : ''}>metadata</option>
          </select>
       </mobi:fieldsetRow>
 <!--      <mobi:fieldsetRow>
           <label>src</label>
           <select name="src" >
              <option value="audio_clip.mp3" ${audioInfo.src=='audio_clip.mp3' ? 'selected' : ''}>mp3 example</option>
              <option value="sampleIPad.mp4" ${audioInfo.src=='sampleIPad.mp4' ? 'selected' : ''}>iPad example</option>
              <option value="sampleAndroid.mp4" ${audioInfo.preload=='sampleAndroid.mp4' ? 'selected' : ''}>Android example</option>
          </select>
       </mobi:fieldsetRow> -->
       <mobi:fieldsetRow>
           <label>controls: </label>
           <mobi:flipswitch id="fs2" labelOn="true" labelOff="false"
                            name="controls" value="${audioInfo.controls}"/>
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>loop: </label>
           <mobi:flipswitch id="fs3" labelOn="true" labelOff="false"
                            name="loop" value="${audioInfo.loop}"/>
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>autoPlay: </label>
           <mobi:flipswitch id="fs4" labelOn="true" labelOff="false"
                            name="autoPlay" value="${audioInfo.autoPlay}"/>
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label>muted: </label>
           <mobi:flipswitch id="mutfs" labelOn="true" labelOff="false"
                            name="muted" value="${audioInfo.muted}"/>
       </mobi:fieldsetRow>
 	   <mobi:fieldsetRow>
			<label>Style</label>
			<mobi:inputText name="style" type="text" autoCorrect="off"
				placeholder="Style string" value="${audioInfo.style}" />
		</mobi:fieldsetRow>
	    <mobi:fieldsetRow>
			<label>StyleClass</label>
			<mobi:inputText name="styleClass" type="text" autoCorrect="off"
				placeholder="StyleClass string" value="${audioInfo.styleClass}" />
		</mobi:fieldsetRow>
	    <mobi:fieldsetRow>
			<label>LinkLabel</label>
			<mobi:inputText name="linkLabel" type="text" autoCorrect="off"
				placeholder="LinkLabel string" value="${audioInfo.linkLabel}" />
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
            <label>mobi audio tag</label>
    	         <mobi:audioPlayer id="audioOne"
                            url="${audioInfo.src}"
                            style="${audioInfo.style}"
                            styleClass="${audioInfo.styleClass}"
                            type="${audioInfo.type}"
                            loop = "${audioInfo.loop}"
                            preload="${audioInfo.preload}"
                            muted="${audioInfo.muted}"
                            linkLabel="${audioInfo.linkLabel}"
                            controls="${audioInfo.controls}"/>
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