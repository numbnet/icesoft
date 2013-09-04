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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form id="fieldset">

	<mobi:largeView><h3>FieldSets</h3></mobi:largeView>

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow>
			The fieldsetGroup tag can be used to displaying application input.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow group="true">
            Information
        </mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<mobi:inputText id="songs" name="songs" value="2500" label="Songs"/>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<mobi:inputText id="videos" name="videos" value="5" label="Videos"/>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<mobi:inputText id="photos" name="photos" value="621" label="Photos"/>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow>
			<mobi:inputText id="first" name="first" value="John" label="First Name"/>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<mobi:inputText id="last" name="last" value="Doe" label="Last Name"/>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<mobi:inputText id="address" name="address" value="123 Fake St." label="Address"/>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow group="true">
			Description
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			The fieldsetGroup displays sets of application input
			such as for settings panels or input forms. The fieldset 
			tag can contain one or more child fieldsetRows,
			which logically divides the fieldset into subsections. The tag
			has an optional inset attribute which can be useful when
			distinguishing multiple fieldsetGroups on one page.</p>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>
</form>