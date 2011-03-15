<f:view xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ice="http://www.icesoft.com/icefaces/component">
	<html>
	<body>
	<ice:form id="shop">

		<ice:dataTable value="#{store.items}" var="storeItem">
			<ice:column>
				<f:facet name="header">
					<ice:outputText value="Item" />
				</f:facet>
				<ice:outputText value="#{storeItem.label}" />
			</ice:column>

			<ice:column>
				<f:facet name="header">
					<ice:outputText value="Price" />
				</f:facet>
				<ice:outputText value="#{storeItem.price}" />
			</ice:column>

			<ice:column>
				<f:facet name="header">
					<ice:outputText value="Quantity" />
				</f:facet>
				<ice:outputText value="#{storeItem.quantity}" />
			</ice:column>

			<ice:column>
				<f:facet name="header">
					<ice:outputText value="Add to Cart" />
				</f:facet>
				<ice:commandButton value="Add"
					actionListener="#{storeItem.directPurchase}" />
			</ice:column>

		</ice:dataTable>
	</ice:form>
	</body>
	</html>
</f:view>