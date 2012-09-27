package org.icefaces.ace.model.tree;

import javax.swing.tree.TreeNode;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 8/16/12
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodeModelListSequenceKeyConverter implements KeySegmentConverter<TreeNode>, Serializable {
    ListNodeDataModel listNodeModel;

    public NodeModelListSequenceKeyConverter(ListNodeDataModel listNodeModel) {
        this.listNodeModel = listNodeModel;
    }

    public Object getSegment(TreeNode node) {
        TreeNode parent = node.getParent();

        if (parent == null) {
            return Integer.valueOf(listNodeModel.roots.indexOf(node));
        } else {
            return Integer.valueOf(parent.getIndex(node));
        }
    }

    public NodeKey parseSegments(String[] segments) {
        Integer[] indexes = new Integer[segments.length];

        for (int i = 0; i < segments.length; i++) {
            indexes[i] = Integer.parseInt(segments[i]);
        }
        return new NodeKey(indexes);
    }
}
