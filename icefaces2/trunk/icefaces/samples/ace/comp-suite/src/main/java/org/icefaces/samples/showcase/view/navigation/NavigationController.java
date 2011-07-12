package org.icefaces.samples.showcase.view.navigation;

import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.metadata.context.Menu;
import org.icefaces.samples.showcase.metadata.context.MenuLink;
import org.icefaces.samples.showcase.util.FacesUtils;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.Map;

@ManagedBean
@ApplicationScoped
public class NavigationController implements Serializable {
    public void subNavigate(ActionEvent event) {
    }

    /**
     * Main controller logic.
     */
    public void navigate() {
        Map<String,String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String newGroup = map.get(NavigationModel.GROUP_PARAM);
        String newExample = map.get(NavigationModel.EXAMPLE_PARAM);
        
        NavigationModel navigationModel = (NavigationModel)
                FacesUtils.getManagedBean(NavigationModel.BEAN_NAME);
        
        // If our new component group is valid then set it into the navigationModel
        if (newGroup != null) {
            navigationModel.setComponentGroup(newGroup);
        }
        
        // If our new component example is valid then set it into the navigationModel
        if (newExample != null) {
            navigationModel.setComponentExample(newExample);
        }
        else {
            // Reset the example if the group changed and the example was null
            // This happens if the overall group menu was selected
            // Doing this will ensure that the default component example for the current group is set
            if (newGroup != null) {
                navigationModel.setComponentExample(newExample);
            }
        }
        
        // Update our group and example to the latest
        newGroup = navigationModel.getComponentGroup();
        newExample = navigationModel.getComponentExample();
        
        // Check whether we need to load the default navigation
        if (newGroup == null && newExample == null) {
            loadDefaultNavigation(navigationModel);
        }
        // Otherwise we may just need to load the example
        else if (newGroup != null && newExample == null) {
            Menu menu = (Menu)FacesUtils.getManagedBean(newGroup);
            if (menu != null) {
                navigationModel.setCurrentComponentGroup(menu);
                ComponentExampleImpl compExample = (ComponentExampleImpl)
                        FacesUtils.getManagedBean(menu.getDefaultExample().getExampleBeanName());
                if (compExample != null) {
                    navigationModel.setCurrentComponentExample(compExample);
                }
            } else {
                loadDefaultNavigation(navigationModel);
            }
        // Otherwise check if the group/example is valid at all
        } else {
            Object tmpGroup = FacesUtils.getManagedBean(newGroup);
            Object tmpExample = FacesUtils.getManagedBean(newExample);
            if ((tmpGroup != null && tmpGroup instanceof Menu) &&
                    (tmpExample != null && tmpExample instanceof ComponentExampleImpl)) {
                ComponentExampleImpl currentExample = navigationModel.getCurrentComponentExample();
                ComponentExampleImpl setExample = (ComponentExampleImpl)tmpExample;
                
                // Determine if we need to fire the effect
                // This is necessary when the overall demo was changed, so basically when the package is different
                // Although checking against packages is not the desired approach, there isn't another option for matching beans
                //  because they have no built-in idea of a parent-child heirarchy
                if (currentExample != null) {
                    if (!setExample.getClass().getPackage().equals(currentExample.getClass().getPackage())) {
                        setExample.prepareEffect();
                    }
                }
                else {
                    setExample.prepareEffect();
                }
                
                // Apply the new group and example
                navigationModel.setCurrentComponentGroup((Menu)tmpGroup);
                navigationModel.setCurrentComponentExample(setExample);
            } else {
                loadDefaultNavigation(navigationModel);
            }
        }
    }

    private void loadDefaultNavigation(NavigationModel navigationModel) {
        // load default pages.
        navigationModel.setCurrentComponentGroup((Menu)
                FacesUtils.getManagedBean(NavigationModel.DEFAULT_MENU));
        navigationModel.setComponentGroup(NavigationModel.DEFAULT_MENU);

        String beanName = navigationModel.getCurrentComponentGroup()
                .getDefaultExample().getExampleBeanName();
        navigationModel.setCurrentComponentExample((ComponentExampleImpl)
                FacesUtils.getManagedBean(beanName));
        navigationModel.setComponentExample(beanName);
    }
    
    public static void refreshPage() {
        FacesContext context = FacesContext.getCurrentInstance();
        ViewHandler handler = context.getApplication().getViewHandler();
        String viewId = context.getViewRoot().getViewId();
        UIViewRoot root = handler.createView(context, viewId);
        
        root.setViewId(viewId);
        context.setViewRoot(root);
    }
    
    public static void reloadPage() {
        NavigationModel navigationModel = (NavigationModel)
                FacesUtils.getManagedBean(NavigationModel.BEAN_NAME);
                
        // Redirect to the current page with the current params
        loadPage("?" +
                 NavigationModel.GROUP_PARAM + "=" + navigationModel.getComponentGroup() +
                 "&" +
                 NavigationModel.EXAMPLE_PARAM + "=" + navigationModel.getComponentExample());
    }
    
    public static void loadPage(String page) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);
        }catch (Throwable e) {
        }
    }
}
