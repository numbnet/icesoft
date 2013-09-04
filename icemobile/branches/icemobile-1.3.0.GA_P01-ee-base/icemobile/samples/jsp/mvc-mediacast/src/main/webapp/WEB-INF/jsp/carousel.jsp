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

<%@ include file="/WEB-INF/jsp/include.jsp"%>
<div>
    <c:if test="${not empty carouselItems}">
        <form:form id="carouselForm" method="POST">
            <div style="border-top: 1px solid #999999;">
                <mobi:carousel id="recentMessagesCarousel"
                    selectedItem="0"
                    style="border-top: 1px solid #999999;">
                    <c:forEach items="${carouselItems}" var="item">
                        <mobi:carouselItem>
                             ${item}
                         </mobi:carouselItem>
                    </c:forEach>
                </mobi:carousel>
            </div>
        </form:form>
        <mobi:largeView>
            <script type="text/javascript">
            enhanceCarousel();
            </script>
        </mobi:largeView>
        <mobi:smallView>
            <script type="text/javascript">
            enhanceGet("#carouselForm a");
            </script>
        </mobi:smallView>
        
     </c:if>
</div>