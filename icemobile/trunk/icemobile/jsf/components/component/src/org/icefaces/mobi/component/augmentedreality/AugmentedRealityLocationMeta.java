package org.icefaces.mobi.component.augmentedreality;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;


@Component(
        tagName = "locationItem",
        componentClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityLocation",
        rendererClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityLocationRenderer",
        generatedClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityLocationBase",
        componentType = "org.icefaces.AugmentedRealityLocation",
        rendererType = "org.icefaces.AugmentedRealityLocationRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.AugmentedRealityLocation",
        tlddoc = "This mobility component "
)

public class AugmentedRealityLocationMeta extends UIComponentBaseMeta{
    @Property(tlddoc = "label of the location item " )
    private String locationLabel;

    @Property(tlddoc = "latitude of the location item " )
    private Double locationLat;

    @Property(tlddoc = "longitude of the location item " )
    private Double locationLon;

    @Property(tlddoc = "altitude of the location item " )
    private Double locationAlt;

    @Property(tlddoc = "icon for location item")
    private String locationIcon;

}
