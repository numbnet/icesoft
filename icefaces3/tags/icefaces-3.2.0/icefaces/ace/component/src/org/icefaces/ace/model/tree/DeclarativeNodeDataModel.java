package org.icefaces.ace.model.tree;

import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 8/15/12
 * Time: 8:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeclarativeNodeDataModel<K> extends NodeDataModel<K> {
    TreeModelAdaptor adaptor;

    public DeclarativeNodeDataModel(TreeModelAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public K navToKey(NodeKey key) {
        return null;
    }

    @Override
    public K navToParent() {
        return null;
    }

    @Override
    public K navToChild(Object keySegment) {
        return null;
    }

    @Override
    public boolean isNodeAvailable() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Iterator<Map.Entry<NodeKey, K>> children() {
        return null;
    }

    @Override
    public KeySegmentConverter getConverter() {
        return null;
    }

    @Override
    public void setConverter(KeySegmentConverter converter) {

    }

    @Override
    public Object getWrappedData() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setWrappedData(Object data) {
        if (!(data instanceof TreeModelAdaptor))
            throw new IllegalArgumentException(String.valueOf(data));
        else
            adaptor = (TreeModelAdaptor) data;
    }
}
