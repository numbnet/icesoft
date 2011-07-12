package org.icefaces.samples.showcase.example.ace.tab;

import org.icefaces.samples.showcase.example.ace.slider.SliderBean;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.tabSet.title",
        description = "example.ace.tabSet.description",
        example = "/resources/examples/ace/tab/tabset.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tabset.xhtml",
                    resource = "/resources/examples/ace/tab/tabset.xhtml"),
            @ExampleResource(type = ResourceType.java,
                    title="TabSetBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/tab/TabSetBean.java")
        }
)
@Menu(
        title = "menu.ace.tabSet.subMenu.title",
        menuLinks = {
                @MenuLink(title = "menu.ace.tabSet.subMenu.main",
                        isDefault = true,
                        exampleBeanName = TabSetBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.tabSet.subMenu.clientSide",
                        exampleBeanName = TabClientSideBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.tabSet.subMenu.serverSide",
                        exampleBeanName = TabServerSideBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.tabSet.subMenu.proxy",
                        exampleBeanName = TabProxyBean.BEAN_NAME)
        })
@ManagedBean(name= TabSetBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabSetBean extends ComponentExampleImpl<TabSetBean>
        implements Serializable {

    public static final String BEAN_NAME = "tabSet";

    public TabSetBean() {
        super(TabSetBean.class);
    }
}
