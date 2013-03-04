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
<form:form id="datatableform" method="POST">

    <c:if test="${viewSize eq 'large'}">
        <h3>Data Table</h3>
    </c:if>

    <mobi:dataTable id="dt" value="${dataTableBean.dataList}"
        var="car" largeViewDetailOrientation="left" clientSide="true">
        <mobi:column headerText="ID" value="${car.id}" property="id"
            optimizeExpression="true"/>
        <mobi:column headerText="Name" value="${car.name}" property="name"
            optimizeExpression="true"/>
        <mobi:column headerText="Chassis" value="${car.chassis}" property="chassis"
            optimizeExpression="true"/>
        <mobi:column headerText="Weight" value="${car.weight}lbs." property="weight"
            optimizeExpression="true"/>
        <mobi:column headerText="Accel" value="${car.acceleration}" property="acceleration"
            optimizeExpression="true" minDeviceWidth="600px"/>
        <mobi:column headerText="MPG" value="${car.mpg}" property="mpg"
            optimizeExpression="true"  minDeviceWidth="440px"/>
        <mobi:dataTableDetail>
            <h2 style="font-size:20px;font-style:italic;">{{name}}</h2>
            <hr/>
            <label style="width: 40%;display:inline-block">ID</label>
            <span style="width: 40%;display:inline-block">{{id}}</span>
            <label style="width: 40%;display:inline-block">Chassis</label>
            <span style="width: 40%;display:inline-block">{{chassis}}</span>
            <label style="width: 40%;display:inline-block">Weight</label>
            <span style="width: 40%;display:inline-block">{{weight}}</span>
            <label style="width: 40%;display:inline-block">Acceleration</label>
            <span style="width: 40%;display:inline-block">{{acceleration}}</span>
            <label style="width: 40%;display:inline-block">Miles per Gallon</label>
            <span style="width: 40%;display:inline-block">{{mpg}}</span>
        </mobi:dataTableDetail>
    </mobi:dataTable>


</form:form>

<script type="text/javascript">
    MvcUtil.enhanceForm("#datatableform");
</script>
