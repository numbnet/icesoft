/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

package com.icesoft.icefaces.samples.showcase.navigation;


import com.icesoft.icefaces.samples.showcase.util.StyleBean;


import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.In;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Manager;

import org.jboss.seam.ScopeType;
import java.util.Enumeration;
import java.io.Serializable;
/**
 * <p>The TreeNavigation class is the backing bean for the showcase navigation
 * tree on the left hand side of the application. Each node in the tree is made
 * up of a PageContent which is responsible for the navigation action when a
 * tree node is selected.</p>
 * <p/>
 * <p>When the Tree component binding takes place the tree nodes are initialized
 * and the tree is built.  Any addition to the tree navigation must be made to
 * this class.</p>
 *
 * @since 0.3.0
 */
@Scope(ScopeType.SESSION)
@Name("treeNavigation")
public class TreeNavigation implements Serializable{
	   private static Log log =
           LogFactory.getLog(TreeNavigation.class);   
 

    // bound to components value attribute
    private DefaultTreeModel model;

    // root node of tree, for delayed construction
    private DefaultMutableTreeNode rootTreeNode;

    private PageContentBean currentPageContent;
    
    
    @In(create=true) @Out 
    private StyleBean styleBean;
    
    // initialization flag
    private boolean isInitiated=false;

    // folder icons for branch nodes
    private String themeBranchContractedIcon;
    private String themeBranchExpandedIcon;

    /**
     * Default constructor of the tree.  The root node of the tree is created at
     * this point.
     */
    public TreeNavigation() {
        // build root node so that children can be attached
        PageContentBean rootObject = new PageContentBean(this);
        rootObject.setMenuDisplayText("menuDisplayText.componentSuiteMenuGroup");
        rootObject.setMenuContentTitle(
                "submenuContentTitle.componentSuiteMenuGroup");
        rootObject.setMenuContentInclusionFile("./content/splashComponents.jspx");
        rootObject.setTemplateName("splashComponentsPanel");
        rootObject.setPageContent(true);
        rootTreeNode = new DefaultMutableTreeNode(rootObject);
        rootObject.setWrapper(rootTreeNode);
        

        model = new DefaultTreeModel(rootTreeNode);
        this.currentPageContent = rootObject;

        // xp folders (default theme)
        themeBranchContractedIcon = StyleBean.XP_BRANCH_CONTRACTED_ICON;
        themeBranchExpandedIcon = StyleBean.XP_BRANCH_EXPANDED_ICON;
        init();
    }

    /**
     * Iterates over each node in the navigation tree and sets the icon based on
     * the current theme. This is necessary for the change to register with the
     * component.
     *
     * @param currentStyle the theme on which the folder icons are based
     */
    public void refreshIcons(String currentStyle) {
        styleBean.setCurrentStyle(currentStyle);
        // set the folder icon based on the StyleBean theme
        if (currentStyle.equals("xp")) {
            themeBranchContractedIcon = StyleBean.XP_BRANCH_CONTRACTED_ICON;
            themeBranchExpandedIcon = StyleBean.XP_BRANCH_EXPANDED_ICON;
        } else if (currentStyle.equals("royale")) {
            themeBranchContractedIcon = StyleBean.ROYALE_BRANCH_CONTRACTED_ICON;
            themeBranchExpandedIcon = StyleBean.ROYALE_BRANCH_EXPANDED_ICON;
        }
        // invalid theme
        else {
            return;
        }

        // get each tree node for iteration
        Enumeration enumTree = rootTreeNode.depthFirstEnumeration();
        PageContentBean temp = null;

        // set the icon on each tree node
        while (enumTree.hasMoreElements()) {
            temp = ((PageContentBean) ((DefaultMutableTreeNode) enumTree
                    .nextElement()).getUserObject());

            if (temp != null) {
                temp.setBranchContractedIcon(themeBranchContractedIcon);
                temp.setBranchExpandedIcon(themeBranchExpandedIcon);
            }
        }
    }

    /**
     * Utility method to build the entire navigation tree.
     */
    private void init() {
        // set init flag
 //   	log.info("init()");
        isInitiated = true;
        if (rootTreeNode == null){
        	log.info("rootTreeNode is null");
        	
        }
        if (rootTreeNode != null) {

                PageContentBean branchObject =
                        (PageContentBean) rootTreeNode.getUserObject();

                /**
                 * Generate the backing bean for each tree node and put it all together
                 */

                // theme menu item --
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.themesSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.themesSubmenuItem");
                branchObject.setTemplateName("splashThemesPanel");

                branchObject.setMenuContentInclusionFile("./content/splashThemes.jspx");

                DefaultMutableTreeNode branchNode =
                        new DefaultMutableTreeNode(branchObject);
                branchObject.setLeaf(
                        true); // leaf nodes must be explicitly set - support lazy loading
                branchObject.setWrapper(branchNode);
                // finally add the new custom component branch
                rootTreeNode.add(branchNode);

                // Component menu item
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "menuDisplayText.componentsMenuGroup");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.componentsMenuGroup");


                branchObject.setTemplateName("splashComponentsPanel");

                branchObject.setPageContent(false);
                branchNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(branchNode);
                // finally add the new custom component branch
                rootTreeNode.add(branchNode);

                // component menu -> Text Entry
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.textFieldsSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.textFieldsSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/textFields.jspx");
                branchObject.setTemplateName("textFieldsContentPanel");
                DefaultMutableTreeNode leafNode =
                        new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);


                // component menu -> inputRichText
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.inputRichText");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.inputRichText");
                branchObject.setMenuContentInclusionFile("./components/inputRichText.jspx");
                branchObject.setTemplateName("inputRichTextPanel");
                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);
                
                // component menu -> Selection
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.selectionTagsSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.selectionTagsSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/selectionTags.jspx");
                branchObject.setTemplateName("selectionTagsContentPanel");
                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);

                // component menu -> Button & Links
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.buttonsAndLinksSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.buttonsAndLinksSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/buttonsAndLinks.jspx");
                branchObject.setTemplateName("buttonsAndLinksContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);

                // component menu -> Auto Complete
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.autoCompleteSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.autoCompleteSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/autoComplete.jspx");
                branchObject.setTemplateName("autoCompleteContentPanel");
 
                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);

                // component menu -> Drag and Drop
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.dragDropSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.dragDropSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/dragDrop.jspx");
                branchObject.setTemplateName("dragDropContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);

                // component menu -> Calendar
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.selectInputDateComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.selectInputDateComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/selectInputDate.jspx");
                branchObject.setTemplateName("selectInputDateContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);

                // component menu -> Tree
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.treeComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.treeComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/tree.jspx");
                branchObject.setTemplateName("treeContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);

                // component menu -> MenuBar
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.menuBarSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.menuBarSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/menuBar.jspx");
                branchObject.setTemplateName("menuBarContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);

                //component menu -> Effects
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.effectsSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.effectsSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/effects.jspx");
                branchObject.setTemplateName("effectsContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);

                //component menu -> Connection Status
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.connectionStatusSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.connectionStatusSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/connectionStatus.jspx");
                branchObject.setTemplateName("connectionStatusContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);

                // component menu -> Table
                branchObject = new PageContentBean(this);
                branchObject.setExpanded(false);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.tableComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.tableComponentSubmenuItem");

                branchObject.setTemplateName("splashTablesPanel");

                branchObject.setPageContent(false);
                DefaultMutableTreeNode branchNode2 =
                        new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(branchNode2);
                // finally add the new custom component branch
                branchNode.add(branchNode2);

                // component menu -> Table -> Table
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.tableComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.tableComponentSubmenuItem");
                 branchObject.setMenuContentInclusionFile("./components/table.jspx");
                branchObject.setTemplateName("tableContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode2.add(leafNode);

                // component menu -> Table -> Columns
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.columnsComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.columnsComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/tableColumns.jspx");
                branchObject.setTemplateName("columnsContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode2.add(leafNode);

                // component menu -> Table -> Sortable Header
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.dataSortHeaderComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.dataSortHeaderComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/commandSortHeader.jspx");
                branchObject.setTemplateName("commandSortHeaderContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode2.add(leafNode);

                // component menu -> Table -> Data Header
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.dataScrollerComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.dataScrollerComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/dataPaginator.jspx");
                branchObject.setTemplateName("tablePaginatorContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode2.add(leafNode);

                // component menu -> Table -> TableExpandable
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.tableExpandableComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.tableExpandableComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/tableExpandable.jspx");
                branchObject.setTemplateName("tableExpandableContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode2.add(leafNode);

                // component menu -> Table -> TableRowSelec5tion
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.tableRowSelectionComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.tableRowSelectionComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/tableRowSelection.jspx");
                branchObject.setTemplateName("tableRowSelectionContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode2.add(leafNode);

                // component menu -> Table -> ColumnGroup
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.columnGroupComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.columnGroupComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/columnGroup.jspx");
                branchObject.setTemplateName("columnGroupContentPanel");
                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode2.add(leafNode);

                
                // component menu -> Table -> row grouping
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.tableGroupingComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.tableGroupingComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/tableGrouping.jspx");
                branchObject.setTemplateName("tableGroupingContentPanel");
                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode2.add(leafNode);                
                
                // component menu -> Table -> resizable columns
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.tableResizableColumnsSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.tableResizableColumnsSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/resizableColumns.jspx");
                branchObject.setTemplateName("resizableColumnsContentPanel");
                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode2.add(leafNode);               
                
                // component menu -> Progress Indicator
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.outputProgressComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.outputProgressComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/outputProgress.jspx");
                branchObject.setTemplateName("outputProgressContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);

                // component menu -> File Upload
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.inputFileComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.inputFileComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/inputFile.jspx");
                branchObject.setTemplateName("inputFileContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);

                // component menu -> Chart
                branchObject = new PageContentBean(this);
                branchObject.setExpanded(false);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.chartComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.chartComponentSubmenuItem");

                branchObject.setTemplateName("splashChartsPanel");

                branchObject.setPageContent(false);
                DefaultMutableTreeNode branchNode3 =
                        new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(branchNode3);
                // finally add the new custom component branch
                branchNode.add(branchNode3);

                // component menu -> Chart -> Chart
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.chartComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.chartComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/outputChart/chart.jspx");
                branchObject.setTemplateName("chartPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode3.add(leafNode);

                // component menu -> Chart -> Dynamic Chart
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.dynamicChartComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.dynamicChartComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/outputChart/dynamicChart.jspx");
                branchObject.setTemplateName("dynamicChartPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode3.add(leafNode);

                // component menu -> Chart -> Combined Chart
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.combinedChartComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.combinedChartComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/outputChart/combinedChart.jspx");
                branchObject.setTemplateName("combinedChartPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode3.add(leafNode);

                // component menu -> GoogleMaps
                branchObject = new PageContentBean(this);
                branchObject.setExpanded(false);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.googlemaps");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.googlemaps");
                branchObject.setTemplateName("splashGoolMapsPanel");
                branchObject.setPageContent(false);
                DefaultMutableTreeNode googleMapNode =
                        new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(branchNode3);
                // finally add the new custom component branch
                branchNode.add(googleMapNode);

                // component menu -> GoogleMaps -> Demo1
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.demo1");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.demo1");
                branchObject.setMenuContentInclusionFile("./components/gmap/demo1.jspx");
                branchObject.setTemplateName("gmapDemo1Panel");
                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                googleMapNode.add(leafNode);

                // component menu -> GoogleMaps -> Demo2
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.demo2");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.demo2");
                branchObject.setMenuContentInclusionFile("./components/gmap/demo2.jspx");
                branchObject.setTemplateName("gmapDemo2Panel");
                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                googleMapNode.add(leafNode);

                // component menu -> GoogleMaps -> Demo3
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.demo3");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.demo3");
                branchObject.setMenuContentInclusionFile("./components/gmap/demo3.jspx");
                branchObject.setTemplateName("gmapDemo3Panel");
                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                googleMapNode.add(leafNode);

                // component menu -> GoogleMaps -> Demo4
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.demo4");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.demo4");
                branchObject.setMenuContentInclusionFile("./components/gmap/demo4.jspx");
                branchObject.setTemplateName("gmapDemo4Panel");
                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                googleMapNode.add(leafNode);

                // component menu -> GoogleMaps -> Demo5
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.demo5");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.demo5");
                branchObject.setMenuContentInclusionFile("./components/gmap/demo5.jspx");
                branchObject.setTemplateName("gmapDemo5Panel");
                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                googleMapNode.add(leafNode);
                
/*                // component menu -> GoogleMaps -> Demo6
                branchObject = new PageContentBean();
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.demo6");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.demo6");
                branchObject.setMenuContentInclusionFile("./components/gmap/demo6.jspx");
                branchObject.setTemplateName("gmapDemo6Panel");
                branchObject.setNavigationSelection(navigationBean);
                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                googleMapNode.add(leafNode);
*/
                
                // component menu -> Media Players
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText("submenuDisplayText.mediaSubmenuItem");
                branchObject.setMenuContentTitle("submenuContentTitle.mediaSubmenuItem");
                branchObject.setMenuContentInclusionFile("./components/media.jspx");
                branchObject.setTemplateName("mediaContentPanel");
                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                branchNode.add(leafNode);
                
                // Layout Panels menu item
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.layoutPanelMenuGroup");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.layoutPanelMenuGroup");
                branchObject.setTemplateName("splashLayoutsPanelsPanel");

                branchObject.setPageContent(false);
                branchNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(branchNode);
                // finally add the new custom component branch
                rootTreeNode.add(branchNode);

                // Layout Panels menu -> Border Panel
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.borderLayoutComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.borderLayoutComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./layoutPanels/panelBorder.jspx");
                branchObject.setTemplateName("panelBorderContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);

                // component menu -> Collapsible Panel
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.collapsiblePanelItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.collapsiblePanelItem");
                branchObject.setMenuContentInclusionFile("./layoutPanels/panelCollapsible.jspx");
                branchObject.setTemplateName("collapsiblePanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);
                
                //component menu -> Popup Panel
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.panelPopupSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.panelPopupSubmenuItem");
                branchObject.setMenuContentInclusionFile("./layoutPanels/panelPopup.jspx");
                branchObject.setTemplateName("panelPopupContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);

                // component menu -> positioned Panel
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.positionedPanelItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.positionedPanelItem");
                branchObject.setMenuContentInclusionFile("./layoutPanels/positionedPanel.jspx");
                branchObject.setTemplateName("positionPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);
                

                // component menu -> Panel Series
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.listSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.listSubmenuItem");
                branchObject.setMenuContentInclusionFile("./layoutPanels/panelSeries.jspx");
                branchObject.setTemplateName("listContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);
               
                // Layout Panels menu -> Panel stack
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.panelStackComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.panelStackComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./layoutPanels/panelStack.jspx");
                branchObject.setTemplateName("panelStackContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);

                // Layout Panels menu -> Tab Set Panel
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.tabbedComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.tabbedComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./layoutPanels/panelTabSet.jspx");
                branchObject.setTemplateName("tabbedPaneContentPanel");

                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);
                
                // Layout Panels menu -> Tool Tips
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.panelTooltipComponentSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.panelTooltipComponentSubmenuItem");
                branchObject.setMenuContentInclusionFile("./layoutPanels/panelTooltip.jspx");
                branchObject.setTemplateName("panelTooltipContentPanel");
 
                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);  
                
                // component menu -> Panel Divider
                branchObject = new PageContentBean(this);
                branchObject.setMenuDisplayText(
                        "submenuDisplayText.panelDividerSubmenuItem");
                branchObject.setMenuContentTitle(
                        "submenuContentTitle.panelDividerSubmenuItem");
                branchObject.setMenuContentInclusionFile("./layoutPanels/panelDivider.jspx");
                branchObject.setTemplateName("panelDividerContentPanel");
                leafNode = new DefaultMutableTreeNode(branchObject);
                branchObject.setWrapper(leafNode);
                branchObject.setLeaf(true);
                // finally add the new custom component branch
                branchNode.add(leafNode);
            }
    }

    /**
     * Gets the default tree model.  This model is needed for the value
     * attribute of the tree component.
     *
     * @return default tree model used by the navigation tree
     */
    public DefaultTreeModel getModel() {
        return model;
    }

    /**
     * Sets the default tree model.
     *  use this instead of component binding
     * @param model new default tree model
     */
    public void setModel(DefaultTreeModel model) {
        this.model = model;
    }

	public PageContentBean getCurrentPageContent() {
		return currentPageContent;
	}

	public void setCurrentPageContent(PageContentBean currentPageContent) {
		this.currentPageContent = currentPageContent;
	}

	@Destroy
	public void destroy(){

	}

}