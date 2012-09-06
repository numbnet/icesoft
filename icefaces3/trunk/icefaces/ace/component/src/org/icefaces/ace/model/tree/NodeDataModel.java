package org.icefaces.ace.model.tree;

import org.icefaces.ace.model.SimpleEntry;

import javax.faces.model.DataModel;
import java.util.Iterator;
import java.util.Map;

public abstract class NodeDataModel<K> extends DataModel<K> {
    static final char SEPARATOR_CHAR = ':';
    NodeKey key = NodeKey.ROOT_KEY;
    K data;

    public K getData() {
        return data;
    }

    protected void setData(K data) {
        this.data = data;
    }

    public NodeKey getKey() {
        return key;
    }

    // Alias to navToKey.
    public void setKey(NodeKey key) {
        navToKey(key);
    }

    public Map.Entry<NodeKey, K> getEntry() {
        return new SimpleEntry<NodeKey, K>(key, data);
    }

    protected boolean atNullRoot() {
        return key != null && key.getKeys() == null;
    }

    // NodeDataModel Contract
    // Absolute navigation
    public abstract K navToKey(NodeKey key);
    // 1-step relative navigation
    public abstract K navToParent();
    public abstract K navToChild(Object keySegment);
    // For full tree traversals
    public abstract Iterator<Map.Entry<NodeKey, K>> children();

    public abstract KeySegmentConverter getConverter();
    public abstract void setConverter(KeySegmentConverter converter);
    public abstract boolean isNodeAvailable();
    public abstract boolean isLeaf();
    public abstract boolean isMutable();


    // Data Model Impl.
    @Override
    public boolean isRowAvailable() {
        return isNodeAvailable();
    }

    @Override
    public int getRowCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public K getRowData() {
        return getData();
    }

    @Override
    public int getRowIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRowIndex(int rowIndex) {
        throw new UnsupportedOperationException();
    }
}
