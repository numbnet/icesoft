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
package org.icefaces.application.showcase.view.bean;

import com.icesoft.faces.component.tree.IceUserObject;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 */
public class NavigationTreeNode extends IceUserObject {

    public static final String LEAF_SELECTED_ICON = "./css/images/navigation_tree/tree_node_selected.gif";
    public static final String LEAF_ICON = "./css/images/navigation_tree/tree_node.gif";
    public static final String FOLDER_CLOSE_ICON = "./css/images/navigation_tree/tree_folder_closed.gif";
    public static final String FOLDER_OPEN_ICON = "/css/images/navigation_tree/tree_folder_open.gif";

    private String nodeId;

    private boolean selected;

    public NavigationTreeNode(DefaultMutableTreeNode defaultMutableTreeNode) {
        super(defaultMutableTreeNode);

        setLeafIcon(LEAF_ICON);
        setBranchContractedIcon(FOLDER_CLOSE_ICON);
        setBranchExpandedIcon(FOLDER_OPEN_ICON);
        this.setExpanded(false);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if (selected) {
            setLeafIcon(LEAF_SELECTED_ICON);
        } else {
            setLeafIcon(LEAF_ICON);
        }

        this.selected = selected;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}
