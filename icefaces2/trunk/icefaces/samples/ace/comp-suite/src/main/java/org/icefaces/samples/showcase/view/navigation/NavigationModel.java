package org.icefaces.samples.showcase.view.navigation;

import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.metadata.context.ContextBase;
import org.icefaces.samples.showcase.metadata.context.Menu;
import org.icefaces.samples.showcase.util.FacesUtils;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Map;

/**
 *
 */
@ManagedBean(name = NavigationModel.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class NavigationModel implements Serializable {

    public static final String BEAN_NAME = "navigationModel";

    // TODO NAVIGATION controller should define this from meta data.
    public static final String DEFAULT_MENU = AceMenu.BEAN_NAME;
    public static final String GROUP_PARAM = "grp";
    public static final String EXAMPLE_PARAM = "exp";

    // references to resolved menu and examples.
    private Menu currentComponentGroup;
    private ComponentExampleImpl currentComponentExample;
    // param for url
    private String componentGroup;
    private String componentExample;

//    public void viewEvent() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        Map map = context.getExternalContext().getRequestParameterMap();
//        componentGroup = (String) map.get("grp");
//        componentExample = (String) map.get("exp");
//    }

//    //    @PostConstruct
//    public void initMetaData() {
//        // assign default navigation
//        currentComponentGroup = (AceMenu) FacesUtils.getManagedBean(DEFAULT_MENU);
//        String beanName = currentComponentGroup.getDefaultExample().getExampleBeanName();
//        currentComponentExample = (ComponentExampleImpl) FacesUtils.getManagedBean(beanName);
//    }

    public Menu getCurrentComponentGroup() {
        return currentComponentGroup;
    }

    public void setCurrentComponentGroup(Menu currentComponentGroup) {
        this.currentComponentGroup = currentComponentGroup;
    }

    public ComponentExampleImpl getCurrentComponentExample() {
        return currentComponentExample;
    }

    public void setCurrentComponentExample(ComponentExampleImpl currentComponentExample) {
        this.currentComponentExample = currentComponentExample;
    }

    public String getComponentGroup() {
        return componentGroup;
    }

    public void setComponentGroup(String componentGroup) {
        this.componentGroup = componentGroup;
    }

    public String getComponentExample() {
        return componentExample;
    }

    public void setComponentExample(String componentExample) {
        this.componentExample = componentExample;
    }
}
