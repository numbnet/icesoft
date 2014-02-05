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

<!DOCTYPE html >
<html >
<head>
    <title>JSP audio</title>
    <mobi:deviceResource/>
</head>
<body>


<form >
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            <label>mobi audio tag</label>
    	         <mobi:openResource id="test1"
                            value="audio_clip.mp3"
                            label="resource label"/>
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