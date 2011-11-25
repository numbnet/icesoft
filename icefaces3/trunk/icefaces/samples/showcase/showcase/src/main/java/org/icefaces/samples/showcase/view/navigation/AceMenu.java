/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.samples.showcase.view.navigation;

import org.icefaces.samples.showcase.example.ace.file.FileEntryBean;
import org.icefaces.samples.showcase.example.ace.slider.SliderBean;
import org.icefaces.samples.showcase.example.ace.tab.TabSetBean;
import org.icefaces.samples.showcase.example.ace.date.DateEntryBean;
import org.icefaces.samples.showcase.example.ace.dataTable.DataTableBean;
import org.icefaces.samples.showcase.example.ace.dataExporter.DataExporterBean;
import org.icefaces.samples.showcase.example.ace.menu.MenuBean;
import org.icefaces.samples.showcase.example.ace.menuBar.MenuBarBean;
import org.icefaces.samples.showcase.example.ace.contextMenu.ContextMenuBean;
import org.icefaces.samples.showcase.example.ace.panel.PanelBean;
import org.icefaces.samples.showcase.example.ace.checkboxButton.CheckboxButtonBean;
import org.icefaces.samples.showcase.example.ace.pushButton.PushButtonBean;
import org.icefaces.samples.showcase.example.ace.accordionpanel.AccordionPanelBean;
import org.icefaces.samples.showcase.example.ace.confirmationdialog.ConfirmationDialogBean;
import org.icefaces.samples.showcase.example.ace.notificationpanel.NotificationPanelBean;
import org.icefaces.samples.showcase.example.ace.progressbar.ProgressBarBean;
import org.icefaces.samples.showcase.example.ace.dialog.DialogBean;
import org.icefaces.samples.showcase.example.ace.maskedEntry.MaskedEntryBean;
import org.icefaces.samples.showcase.example.ace.resizable.ResizableBean;
import org.icefaces.samples.showcase.example.ace.dragDrop.DragDropOverviewBean;
import org.icefaces.samples.showcase.example.ace.tooltip.TooltipOverviewBean;
import org.icefaces.samples.showcase.example.ace.linkButton.LinkButtonBean;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import org.icefaces.samples.showcase.example.ace.overview.AceSuiteOverviewBean;

@Menu(
        title = "menu.ace.title", menuLinks = {
                @MenuLink(title = "menu.ace.aceSuiteOverview.title", isDefault = true, exampleBeanName = AceSuiteOverviewBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.accordionpanel.title", exampleBeanName = AccordionPanelBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.checkboxButton.title", exampleBeanName = CheckboxButtonBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.confirmationdialog.title", exampleBeanName = ConfirmationDialogBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.contextMenu.title", exampleBeanName = ContextMenuBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dataExporter.title", exampleBeanName = DataExporterBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dataTable.title", exampleBeanName = DataTableBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dateentry.title", exampleBeanName = DateEntryBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dialog.title", exampleBeanName = DialogBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dragDrop.title", exampleBeanName = DragDropOverviewBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.fileentry.title", exampleBeanName = FileEntryBean.BEAN_NAME),
                        //                @MenuLink(title = "menu.ace.logger.title", exampleBeanName = LoggerBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.linkButton.title", exampleBeanName = LinkButtonBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.maskedEntry.title", exampleBeanName = MaskedEntryBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.menu.title", exampleBeanName = MenuBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.menuBar.title", exampleBeanName = MenuBarBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.notificationpanel.title", exampleBeanName = NotificationPanelBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.panel.title", exampleBeanName = PanelBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.progressbar.title", exampleBeanName = ProgressBarBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.pushButton.title", exampleBeanName = PushButtonBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.resizable.title", exampleBeanName = ResizableBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.slider.title", exampleBeanName = SliderBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.tabSet.title", exampleBeanName = TabSetBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.tooltip.title", exampleBeanName = TooltipOverviewBean.BEAN_NAME)
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
