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

package org.icefaces.samples.showcase.example.compat.tree;

import java.io.Serializable;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import com.icesoft.faces.component.tree.IceUserObject;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = TreeBean.BEAN_NAME,
        title = "example.compat.tree.dynamic.title",
        description = "example.compat.tree.dynamic.description",
        example = "/resources/examples/compat/tree/treeDynamic.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="treeDynamic.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tree/treeDynamic.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TreeDynamic.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tree/TreeDynamic.java")
        }
)
@ManagedBean(name= TreeDynamic.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TreeDynamic extends ComponentExampleImpl<TreeDynamic> implements Serializable {
	
	public static final String BEAN_NAME = "treeDynamic";
	
	private Random randomizer = new Random(System.nanoTime());
	private DefaultTreeModel model;
	private DefaultMutableTreeNode parentChange;
	
	public TreeDynamic() {
		super(TreeDynamic.class);
	}
	
	@PostConstruct
	protected void init() {
	    model = TreeBean.generateRandomTree(1, 1, 5, 1);
    }	
	
    public DefaultTreeModel getModel() { return model; }
    public DefaultMutableTreeNode getParentChange() { return parentChange; }

    public void setModel(DefaultTreeModel model) { this.model = model; }
    public void setParentChange(DefaultMutableTreeNode parentChange) { this.parentChange = parentChange; }
    
    public void addRandom(ActionEvent event) {
        DefaultMutableTreeNode newFolder = TreeBean.addNodeToRoot(model, "Generated Folder", false);
        
        int newLeafCount = 1+randomizer.nextInt(10);
        for (int i = 0; i < newLeafCount; i++) {
            TreeBean.addNode(newFolder, "Random Item " + i, true);
        }
    }
    
    public String addFolder() {
        if (parentChange != null) {
            int folderId = parentChange.getLevel();
            String folderText = parentChange.isRoot() ?
                                 "Folder " + (parentChange.getChildCount()+1) :
                                 "Subfolder " + folderId;
            DefaultMutableTreeNode subFolder = TreeBean.addNode(parentChange, folderText, false);
            
            for (int i = 0; i < 2; i++) {
                TreeBean.addNode(subFolder, "Subitem " + folderId + "-" + i, true);
            }
        }
        
        return null;
    }
    
    public String addLeaf() {
        if (parentChange != null) {
            TreeBean.addNode(parentChange, "Added Item " + parentChange.getLeafCount(), true);
        }
        
        return null;
    }
    
    public String removeNode() {
        if (parentChange != null) {
            parentChange.removeFromParent();
        }
        return null;
    }
}
