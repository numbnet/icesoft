package org.icefaces.samples.showcase.view.navigation;

import org.icefaces.samples.showcase.example.ace.slider.SliderBean;
import org.icefaces.samples.showcase.example.ace.tab.TabClientSideBean;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

/**
 *
 */
@Menu(
    title = "menu.ee.title", menuLinks = {
            @MenuLink(title = "menu.ee.selection.title",
                    isDefault = true,
                    isDisabled = true,
                    exampleBeanName = SliderBean.BEAN_NAME),
            @MenuLink(title = "menu.ee.table.title",
                    isDisabled = true,
                    exampleBeanName = SliderBean.BEAN_NAME),
            @MenuLink(title = "menu.ee.layout.title",
                    isDisabled = true,
                    exampleBeanName = SliderBean.BEAN_NAME),
            @MenuLink(title = "menu.ee.tree.title",
                    isDisabled = true,
                    exampleBeanName = SliderBean.BEAN_NAME),
            @MenuLink(title = "menu.ee.process.title",
                    isDisabled = true,
                    exampleBeanName = SliderBean.BEAN_NAME),
            @MenuLink(title = "menu.ee.statusbar.title",
                    isDisabled = true,
                    exampleBeanName = SliderBean.BEAN_NAME), 
            @MenuLink(title = "menu.ee.slideshow.title",
                    isDisabled = true,
                    exampleBeanName = SliderBean.BEAN_NAME), 
            @MenuLink(title = "menu.ee.schedule.title",
                    isDisabled = true,
                    exampleBeanName = SliderBean.BEAN_NAME)                    
    })
@ManagedBean(name = EeMenu.BEAN_NAME)
@ApplicationScoped
public class EeMenu  extends org.icefaces.samples.showcase.metadata.context.Menu<EeMenu>
        implements Serializable {

    public static final String BEAN_NAME = "eeMenu";

    public EeMenu() {
        super(EeMenu.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getBeanName() {
        return BEAN_NAME;
    }
}
