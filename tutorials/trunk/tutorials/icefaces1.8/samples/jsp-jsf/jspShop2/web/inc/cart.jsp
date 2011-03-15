<f:view xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ice="http://www.icesoft.com/icefaces/component">
	<html>
	<body>
	<ice:form id="cart">
        <ice:outputConnectionStatus style="display: none; visible: false; height: 0; margin: 0; padding: 0;"/>

		<ice:dataTable value="#{store.items}" var="storeItem">
        
			<ice:column>
				<f:facet name="header">
					<ice:outputText value="Quantity" />
				</f:facet>
				<ice:outputText value="#{storeItem.purchasedQuantity}" />
			</ice:column>
            
			<ice:column>
				<f:facet name="header">
					<ice:outputText value="Item" />
				</f:facet>
				<ice:outputText value="#{storeItem.label}" />
			</ice:column>

			<ice:column>
				<f:facet name="header">
					<ice:outputText value="Subtotal" />
				</f:facet>
				<ice:outputText value="#{storeItem.subTotal}" />
			</ice:column>
            
			<ice:column>
				<f:facet name="header">
					<ice:outputText value="Remove" />
				</f:facet>
				<ice:commandButton value="Remove" action="removeitem"
					actionListener="#{storeItem.directRemove}" />
			</ice:column>
			
		</ice:dataTable>
	</ice:form>
	</body>
	</html>
</f:view>