package org.icefaces.samples.showcase.view.navigation;

import org.icefaces.samples.showcase.example.ace.file.FileEntryBean;
import org.icefaces.samples.showcase.example.ace.slider.SliderBean;
import org.icefaces.samples.showcase.example.ace.tab.TabSetBean;
import org.icefaces.samples.showcase.example.ace.date.DateEntryBean;
import org.icefaces.samples.showcase.example.ace.button.ButtonBean;
import org.icefaces.samples.showcase.example.ace.logger.LoggerBean;
import org.icefaces.samples.showcase.example.ace.dataTable.DataTableBean;
import org.icefaces.samples.showcase.example.ace.dataExporter.DataExporterBean;
import org.icefaces.samples.showcase.example.ace.menu.MenuBean;
import org.icefaces.samples.showcase.example.ace.menuBar.MenuBarBean;
import org.icefaces.samples.showcase.example.ace.contextMenu.ContextMenuBean;
import org.icefaces.samples.showcase.example.ace.panel.PanelBean;
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
        title = "menu.ace.title", menuLinks = {
                @MenuLink(title = "menu.ace.button.title",
                        isDefault = true,
                        exampleBeanName = ButtonBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dateentry.title",
                        exampleBeanName = DateEntryBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.fileentry.title",
                        exampleBeanName = FileEntryBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.logger.title",
                        exampleBeanName = LoggerBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.slider.title",
                        exampleBeanName = SliderBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.tabSet.title",
                        exampleBeanName = TabSetBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dataTable.title",
                        exampleBeanName = DataTableBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dataExporter.title",
                        exampleBeanName = DataExporterBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.menu.title",
                        exampleBeanName = MenuBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.menuBar.title",
                        exampleBeanName = MenuBarBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.contextMenu.title",
                        exampleBeanName = ContextMenuBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.panel.title",
                        exampleBeanName = PanelBean.BEAN_NAME)
        })
@ManagedBean(name = AceMenu.BEAN_NAME)
@ApplicationScoped
public class AceMenu extends org.icefaces.samples.showcase.metadata.context.Menu<AceMenu>
        implements Serializable {

    public static final String BEAN_NAME = "aceMenu";

    public AceMenu() {
        super(AceMenu.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getBeanName() {
        return BEAN_NAME;
    }
}
