package org.icefaces.component.testComponent;


import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@ResourceDependencies({
    @ResourceDependency(name="yui/yui-min.js",library="yui/3_2_0"),
	@ResourceDependency(library = "yui/2_8_1", name = "button/assets/skins/sam/button.css"),
    @ResourceDependency(library = "yui/2_8_1", name = "logger/assets/skins/sam/logger.css"),
    @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "logger/logger-min.js"),
	@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util")
//    @ResourceDependency(name="commandlink.js",library="org.icefaces.component.testcomponent")
})

    public class TestComponent extends TestComponentBase {

}