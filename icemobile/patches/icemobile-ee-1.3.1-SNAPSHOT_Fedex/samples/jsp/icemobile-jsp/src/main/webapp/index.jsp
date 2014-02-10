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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<!DOCTYPE html >
<html>
    <head>
    	<title>Hello ICEmobile!</title>
		<mobi:deviceResource />
    </head>
    <body>
    	<mobi:pagePanel>
            <mobi:pagePanelHeader >
                Hello ICEmobile!
            </mobi:pagePanelHeader>
			<mobi:pagePanelBody>
			    <mobi:fieldsetGroup  style="margin:5px;">
			        <mobi:fieldsetRow>Item 1</mobi:fieldsetRow>
			        <mobi:fieldsetRow>Item 2</mobi:fieldsetRow>
			        <mobi:fieldsetRow>Item 3</mobi:fieldsetRow>
			    </mobi:fieldsetGroup>
			</mobi:pagePanelBody>
            <mobi:pagePanelFooter>
               &copy; Acme Inc 2012
            </mobi:pagePanelFooter>
        </mobi:pagePanel>
    </body>
</html>