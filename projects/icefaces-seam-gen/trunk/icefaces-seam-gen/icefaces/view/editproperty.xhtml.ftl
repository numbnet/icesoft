<#if !c2h.isCollection(property) && !c2h.isManyToOne(property)>
<#assign propertyIsId = property.equals(pojo.identifierProperty)>
<#if !propertyIsId || property.value.identifierGeneratorStrategy == "assigned">
<#if pojo.isComponent(property)>
<#foreach componentProperty in property.value.propertyIterator>
<#assign column = componentProperty.columnIterator.next()>
<#assign propertyType = componentProperty.value.typeName>
		<s:validateAll>
                    <h:outputLabel for="${componentProperty.name}">
                        ${componentProperty.name}
<#if !column.nullable>
                        <span class="required">*</span>
</#if>
                    </h:outputLabel>
                    <s:decorate id="${componentProperty.name}Decoration">
<#if propertyType == "date">
                        <ice:selectInputDate id="${componentProperty.name}Id" 
                              renderAsPopup="true"
<#if propertyIsId>
                                      disabled="${'#'}{${homeName}.managed}"
</#if>
<#if !column.nullable>
                                      required="true"
</#if>
                                         value="${'#'}{${homeName}.instance.${property.name}.${componentProperty.name}}">
                                
                           </ice:selectInputDate>
                      <#elseif propertyType == "time">
                        <ice:inputText id="${componentProperty.name}Id" 
                                   size="5"
<#if !column.nullable>
                               required="true"
</#if>
                                  value="${'#'}{${homeName}.instance.${property.name}.${componentProperty.name}}">
                            <s:convertDateTime type="time"/>
                        </ice:inputText>
<#elseif propertyType == "timestamp">
                        <ice:inputText id="${componentProperty.name}Id" 
                                   size="16"
<#if !column.nullable>
                               required="true"
</#if>
                                  value="${'#'}{${homeName}.instance.${property.name}.${componentProperty.name}}">
                             <s:convertDateTime type="both" dateStyle="short"/>
                        </ice:inputText>
<#elseif propertyType == "big_decimal">
                        <ice:inputText id="${componentProperty.name}Id" 
                              partialSubmit="true"
<#if !column.nullable>
                               required="true"
</#if>
                                  value="${'#'}{${homeName}.instance.${property.name}.${componentProperty.name}}"
                                   size="${column.precision+7}">
                        </ice:inputText>
<#elseif propertyType == "big_integer">
                        <ice:inputText id="${componentProperty.name}Id" 
					partialSubmit="true"
<#if propertyIsId>
                               disabled="${'#'}{${homeName}.managed}"
</#if>
<#if !column.nullable>
                               required="true"
</#if>
                                  value="${'#'}{${homeName}.instance.${property.name}.${componentProperty.name}}"
                                   size="${column.precision+6}">
                        </ice:inputText>
<#elseif propertyType == "boolean" || propertyType == "yes_no" || propertyType == "true_false">
                         <h:selectBooleanCheckbox id="${componentProperty.name}Id"
<#if !column.nullable>
                                            required="true"
</#if>
<#if propertyIsId>
                                            disabled="${'#'}{${homeName}.managed}"
</#if>
                                               value="${'#'}{${homeName}.instance.${property.name}.${componentProperty.name}}"/>
<#elseif propertyType == "string">
<#if column.length gt 160>
<#if column.length gt 800>
<#assign rows = 10>
<#else>
<#assign rows = (column.length/80)?int>
</#if>
                        <h:inputTextarea id="${componentProperty.name}Id"
                                       cols="80"
                                       rows="${rows}"
<#if propertyIsId>
                                   disabled="${'#'}{${homeName}.managed}"
</#if>
<#if !column.nullable>
                                   required="true"
</#if>
                                      value="${'#'}{${homeName}.instance.${property.name}.${componentProperty.name}}"/>
<#else>
<#if column.length gt 100>
<#assign size = 100>
<#else>
<#assign size = column.length>
</#if>
                        <h:inputText id="${componentProperty.name}Id" 
<#if propertyIsId>
                               disabled="${'#'}{${homeName}.managed}"
</#if>
<#if !column.nullable>
                              required="true"
</#if>
                                  size="${size}"
                             maxlength="${column.length}"
                                 value="${'#'}{${homeName}.instance.${property.name}.${componentProperty.name}}">
                        </h:inputText>
</#if>
<#else>
                        <h:inputText id="${componentProperty.name}Id"
<#if !column.nullable>
                               required="true"
</#if>
<#if propertyIsId>
                               disabled="${'#'}{${homeName}.managed}"
</#if>
                                  value="${'#'}{${homeName}.instance.${property.name}.${componentProperty.name}}">
                        </h:inputText>
</#if>
                    </s:decorate>
</#foreach>
<#else>
<#assign column = property.columnIterator.next()>
<#assign propertyType = property.value.typeName>

                    <h:outputLabel for="${property.name}">
                        ${property.name}
<#if !column.nullable>
                        <span class="required">*</span>
</#if>
                    </h:outputLabel>
                    <s:decorate id="${property.name}DecorationId">
<#if propertyType == "date">
                           <ice:selectInputDate id="${property.name}Id" 
                              renderAsPopup="true"

<#if propertyIsId>
                               disabled="${'#'}{${homeName}.managed}"
</#if>
<#if !column.nullable>
                               required="true"
</#if>
                                  value="${'#'}{${homeName}.instance.${property.name}}">
                         </ice:selectInputDate>
<#elseif propertyType == "time">
                        <ice:inputText id="${property.name}Id" 
                                   size="5"
					partialSubmit="true"
<#if !column.nullable>
                               required="true"
</#if>
                                  value="${'#'}{${homeName}.instance.${property.name}}">
                            <s:convertDateTime type="time"/>
                        </ice:inputText>
<#elseif propertyType == "timestamp">
                        <ice:inputText id="${property.name}Id" 
                                     size="16"
			    partialSubmit="true"
<#if !column.nullable>
                                required="true"
</#if>
                                   value="${'#'}{${homeName}.instance.${property.name}}">
                            <s:convertDateTime type="both" dateStyle="short"/>
                        </ice:inputText>
<#elseif propertyType == "big_decimal">
                        <ice:inputText id="${property.name}Id" 
			    partialSubmit="true"
<#if !column.nullable>
                                 required="true"
</#if>
                                    value="${'#'}{${homeName}.instance.${property.name}}"
                                     size="${column.precision+7}">
                        </ice:inputText>
<#elseif propertyType == "big_integer">
                        <ice:inputText id="${property.name}Id
			  " partialSubmit="true"
<#if propertyIsId>
                                 disabled="${'#'}{${homeName}.managed}"
</#if>
<#if !column.nullable>
                                 required="true"
</#if>
                                    value="${'#'}{${homeName}.instance.${property.name}}"
                                     size="${column.precision+6}"/>
<#elseif propertyType == "boolean" || propertyType == "yes_no" || propertyType == "true_false">
                        <ice:selectBooleanCheckbox id="${property.name}Id"
					partialSubmit="true"
<#if !column.nullable>
                                            required="true"
</#if>
<#if propertyIsId>
                                            disabled="${'#'}{${homeName}.managed}"
</#if>
                                               value="${'#'}{${homeName}.instance.${property.name}}"/>
<#elseif propertyType == "string">
<#if column.length gt 160>
<#if column.length gt 800>
<#assign rows = 10>
<#else>
<#assign rows = (column.length/80)?int>
</#if>
                        <ice:inputTextarea id="${property.name}Id"
                                         cols="80"
                                         rows="${rows}"
			        partialSubmit="true"
<#if propertyIsId>
                                     disabled="${'#'}{${homeName}.managed}"
</#if>
<#if !column.nullable>
                                     required="true"
</#if>
                                        value="${'#'}{${homeName}.instance.${property.name}}"/>
<#else>
<#if column.length gt 100>
<#assign size = 100>
<#else>
<#assign size = column.length>
</#if>
                        <ice:inputText id="${property.name}Id" 
			    partialSubmit="true"
<#if propertyIsId>
                                 disabled="${'#'}{${homeName}.managed}"
</#if>
<#if !column.nullable>
                               required="true"
</#if>
                                   size="${size}"
                              maxlength="${column.length}"
                                  value="${'#'}{${homeName}.instance.${property.name}}">
                        </ice:inputText>
</#if>
<#else>
                        <ice:inputText id="${property.name}Id"
			    partialSubmit="true"
<#if !column.nullable>
                                 required="true"
</#if>
<#if propertyIsId>
                                 disabled="${'#'}{${homeName}.managed}"
</#if>
                                    value="${'#'}{${homeName}.instance.${property.name}}">
                        </ice:inputText>
</#if>
                    </s:decorate>
</#if>
</#if>
</#if>
