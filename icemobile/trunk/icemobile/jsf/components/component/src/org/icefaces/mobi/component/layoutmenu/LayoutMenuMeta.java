package org.icefaces.mobi.component.layoutmenu;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Required;
import org.icefaces.ace.meta.baseMeta.UISeriesBaseMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


@Component(
        tagName = "layoutMenu",
        componentClass = "org.icefaces.mobi.component.layoutmenu.LayoutMenu",
        rendererClass = "org.icefaces.mobi.component.layoutmenu.LayoutMenuRenderer",
        generatedClass = "org.icefaces.mobi.component.layoutmenu.LayoutMenuBase",
        extendsClass = "org.icefaces.impl.component.UISeriesBase",
        componentType = "org.icefaces.component.LayoutMenu",
        rendererType = "org.icefaces.component.LayoutMenuRenderer",
        componentFamily = "org.icefaces.LayoutMenu",
        tlddoc = "This component renders a layout of menu button repesented by a collection of menuButtonItems " +
                " and is meant to be used with a contentStack component for layout "
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class LayoutMenuMeta extends UISeriesBaseMeta {
    @Property(tlddoc = "style will be rendered on the root element of this " +
            "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
            "this component.")
    private String styleClass;

    @Property(tlddoc = "id of contentStack this menu will be responsible for manipulating",
             required=Required.yes)
    private String contentStackId;

    @Property(tlddoc = "id of either menuItem or contentPane that menuItem reflects..not sure which yet")
    private String selectedPane;




/*    @Property(defaultValue="true", tlddoc="use a back button for users to be able to return to previously level of menu. " +
            " only visible if in submenu")
    private boolean backButton;

    @Property(defaultValue="Back", tlddoc=" label for back button if that attribute is true")
    private String backButtonLabel; */
}
