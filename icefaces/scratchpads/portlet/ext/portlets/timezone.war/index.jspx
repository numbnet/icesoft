<f:view xmlns:f="http://java.sun.com/jsf/core"
        xmlns:h="http://java.sun.com/jsf/html">
	<h1>
		ICEfaces: TimeZone
	</h1>
	<h:form>
		<h:panelGrid columns="2">
			<h:outputText style="font-weight:600"
                          value="Server TimeZone" />
			<h:outputText style="font-weight:600"
                          value="Selected TimeZone" />
			<h:outputText value="#{timeZoneBean.serverTimeZoneName}" />
			<h:outputText value="#{timeZoneBean.selectedTimeZoneName}" />
			<h:outputText style="font-weight:800"
                          value="#{timeZoneBean.serverTime}" />
			<h:outputText style="font-weight:800"
                          value="#{timeZoneBean.selectedTime}" />
		</h:panelGrid>
		<h:panelGrid columns="6"
                     cellspacing="0"
                     cellpadding="0">
			<h:commandButton id="GMTminus10"
                             image="/images/hawaii.jpg"
                             actionListener="#{timeZoneBean.listen}" />
			<h:commandButton id="GMTminus9"
                             image="/images/alaska.jpg"
                             actionListener="#{timeZoneBean.listen}" />
			<h:commandButton id="GMTminus8"
                             image="/images/pacific.jpg"
                             actionListener="#{timeZoneBean.listen}" />
			<h:commandButton id="GMTminus7"
                             image="/images/mountain.jpg"
                             actionListener="#{timeZoneBean.listen}" />
			<h:commandButton id="GMTminus6"
                             image="/images/central.jpg"
                             actionListener="#{timeZoneBean.listen}" />
			<h:commandButton id="GMTminus5"
                             image="/images/eastern.jpg"
                             actionListener="#{timeZoneBean.listen}"/>
		</h:panelGrid>
	</h:form>
</f:view>
