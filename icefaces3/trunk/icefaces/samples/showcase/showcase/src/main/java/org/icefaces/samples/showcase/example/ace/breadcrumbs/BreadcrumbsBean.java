/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.samples.showcase.example.ace.breadcrumbs;

import org.icefaces.ace.component.menuitem.MenuItem;
import org.icefaces.ace.component.tree.Tree;
import org.icefaces.ace.model.DefaultMenuModel;
import org.icefaces.ace.model.MenuModel;
import org.icefaces.ace.model.tree.NodeState;
import org.icefaces.ace.model.tree.NodeStateCreationCallback;
import org.icefaces.ace.model.tree.NodeStateMap;
import org.icefaces.samples.showcase.example.ace.tree.LocationNodeImpl;
import org.icefaces.samples.showcase.example.ace.tree.TreeDataFactory;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.util.JavaScriptRunner;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

@ComponentExample(
        title = "example.ace.breadcrumbs.title",
        description = "example.ace.breadcrumbs.description",
        example = "/resources/examples/ace/breadcrumbs/breadcrumbs.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="breadcrumbs.xhtml",
                        resource = "/resources/examples/ace/breadcrumbs/breadcrumbs.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="BreadcrumbsBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                "/example/ace/breadcrumbs/BreadcrumbsBean.java")
        }
)
@Menu(
        title = "menu.ace.breadcrumbs.subMenu.title",
        menuLinks = {
            @MenuLink(title = "menu.ace.breadcrumbs.subMenu.main", isDefault = true, exampleBeanName = BreadcrumbsBean.BEAN_NAME)
        }
)
@ManagedBean(name= BreadcrumbsBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class BreadcrumbsBean extends ComponentExampleImpl<BreadcrumbsBean> implements Serializable {
    public static final String BEAN_NAME = "breadcrumbsBean";
    public static final Map<String, String> urlMap = new HashMap<String, String>() {{
        put("Canada", "http://en.wikipedia.org/wiki/Canada");
        put("British Columbia", "http://en.wikipedia.org/wiki/British_columbia");
        put("Alberta", "http://en.wikipedia.org/wiki/Alberta");
        put("Saskatchewan", "http://en.wikipedia.org/wiki/Saskatchewan");
        put("Manitoba", "http://en.wikipedia.org/wiki/Manitoba");
        put("Ontario", "http://en.wikipedia.org/wiki/Ontario");
        put("Quebec", "http://en.wikipedia.org/wiki/Quebec");
        put("New Brunswick", "http://en.wikipedia.org/wiki/New_Brunswick");
        put("Newfoundland", "http://en.wikipedia.org/wiki/Newfoundland");
        put("Nova Scotia", "http://en.wikipedia.org/wiki/Nova_Scotia");
    }};
    private List<LocationNodeImpl> treeRoots = TreeDataFactory.getTreeRoots();
    private NodeStateMap stateMap;
    private MenuModel menuModel1;
    private MenuModel menuModel2;
    private Tree tree;

    private NodeStateCreationCallback contractProvinceInit = new NodeStateCreationCallback() {
        public NodeState initializeState(NodeState newState, Object node) {
            LocationNodeImpl loc = (LocationNodeImpl) node;
            if (loc.getType().equals("country"))
                newState.setExpanded(true);
            return newState;
        }
    };                                                                


    public BreadcrumbsBean() {
        super(BreadcrumbsBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public List<LocationNodeImpl> getTreeRoots() {
        return treeRoots;
    }

    public void print(String text) {
        JavaScriptRunner.runScript(FacesContext.getCurrentInstance(),
                "alert('"+text+"');");
    }

    public NodeStateMap getStateMap() {
        return stateMap;
    }

    public void setStateMap(NodeStateMap stateMap) {
        this.stateMap = stateMap;
    }

    public NodeStateCreationCallback getContractProvinceInit() {
        return contractProvinceInit;
    }

    public void setContractProvinceInit(NodeStateCreationCallback contractProvinceInit) {
        this.contractProvinceInit = contractProvinceInit;
    }

    public MenuModel getMenuModel2() {
        return menuModel2;
    }

    public void setMenuModel2(MenuModel menuModel2) {
        this.menuModel2 = menuModel2;
    }

    public void treeSelectListener(AjaxBehaviorEvent event) {
        List selected = stateMap.getSelected();
        LocationNodeImpl node = (LocationNodeImpl) (selected.isEmpty() ? null : selected.get(0));
        Stack stack = new Stack();
        while (node != null) {
            stack.push(node);
            node = (LocationNodeImpl) node.getParent();
        }
        menuModel1 = new DefaultMenuModel();
        menuModel2 = new DefaultMenuModel();
        MenuItem menuItem;
        String treeId = event.getComponent().getId();
        String nodeId = "";
        while (!stack.empty()) {
            node = (LocationNodeImpl) stack.pop();
            nodeId += "-" + tree.getKeyConverter().getSegment(node);

            menuItem = new MenuItem();
            menuItem.setId(treeId + "-crumb-1" + nodeId);
            menuItem.setValue(node.getName());
            menuItem.setUrl(urlMap.get(node.getName()));
//            menuItem.setTarget("_blank");
            menuModel1.addMenuItem(menuItem);

            menuItem = new MenuItem();
            menuItem.setId(treeId + "-crumb-2" + nodeId);
            menuItem.setValue(node.getName());
            menuItem.addActionListener(new MenuItemActionListener());
            menuModel2.addMenuItem(menuItem);
        }
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public MenuModel getMenuModel1() {
        return menuModel1;
    }

    public void setMenuModel1(MenuModel menuModel1) {
        this.menuModel1 = menuModel1;
    }

    class MenuItemActionListener implements ActionListener {
        public void processAction(ActionEvent event) throws AbortProcessingException {
            String id = event.getComponent().getId();
            String nodeId = id.substring(id.lastIndexOf("-crumb-2-") + "-crumb-2-".length());
            tree.setKey(tree.getKeyConverter().parseSegments(nodeId.split("-")));
            stateMap.setAllSelected(false);
            stateMap.get(tree.getData()).setSelected(true);
        }
    }
}
