package org.icefaces.ace.component.tableconfigpanel;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Field;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Required;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.faces.component.UIComponent;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
    tagName = "tableConfigPanel",
    extendsClass = "javax.faces.component.UIComponentBase",
    rendererClass = "org.icefaces.ace.component.tableconfigpanel.TableConfigPanelRenderer",
    rendererType = "org.icefaces.ace.component.TableConfigPanelRenderer",
	generatedClass = "org.icefaces.ace.component.tableconfigpanel.TableConfigPanelBase",
    componentType = "org.icefaces.ace.component.TableConfigPanel",
    componentClass = "org.icefaces.ace.component.tableconfigpanel.TableConfigPanel",
    componentFamily = "org.icefaces.ace.TableConfigPanel",
    tlddoc = "")
public class TableConfigPanelMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "")
    boolean columnOrderingConfigurable;
    @Property(tlddoc = "")
    boolean columnSizingConfigurable;
    @Property(tlddoc = "")
    boolean columnVisibilityConfigurable;
    @Property(tlddoc = "")
    boolean columnNameConfigurable;
    @Property(tlddoc = "")
    boolean columnSortingConfigurable;
    @Property(name = "for")
    String forTarget;
    @Property(tlddoc = "Display modes for the 'open' control panel button. Available: paginator-button, first-col, last-col, plain",
              defaultValue = "first-col")
    String type;
}
