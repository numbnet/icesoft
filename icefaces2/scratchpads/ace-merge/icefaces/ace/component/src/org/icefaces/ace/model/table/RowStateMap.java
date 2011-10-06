package org.icefaces.ace.model.table;

import org.icefaces.ace.util.CollectionUtils;
import org.icefaces.ace.util.collections.Predicate;

import javax.faces.model.DataModel;
import java.io.Serializable;
import java.util.*;

public class RowStateMap implements Map<Object, RowState>, Serializable {
    private Map<Object, RowState> map = new HashMap<Object, RowState>();

    private static Predicate selectedPredicate = new SelectedPredicate();
    private static Predicate selectablePredicate = new SelectablePredicate();
    private static Predicate editablePredicate = new EditablePredicate();
    private static Predicate expandablePredicate = new ExpandablePredicate();
    private static Predicate expandedPredicate = new ExpandedPredicate();
    private static Predicate visiblePredicate = new VisiblePredicate();


    public RowState put(Object o, RowState s) {
        return map.put(o, s);
    }

    public void add(Object o) {
        map.put(o, new RowState());
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public RowState get(Object o) {
        RowState s = map.get(o);
        if (s == null) {
            add(o);
            s = map.get(o);
        }
        return s;
    }

    public RowState remove(Object o) {
        return map.remove(o);
    }

    public void putAll(Map m) {
        map.putAll(m);
    }

    public void clear() {
        map.clear();
    }

    public Set keySet() {
        return map.keySet();
    }

    public Collection values() {
        return map.values();
    }

    public Set entrySet() {
        return map.entrySet();
    }

    public void removedUnusedStates(DataModel model) {
        Set<Object> keys = map.keySet();
        ArrayList<Object> rowDataList = new ArrayList<Object>();
        int rowsToProcess = model.getRowCount();

        while (rowsToProcess > 0) {
            rowsToProcess--;
            model.setRowIndex(rowsToProcess);
            if (model.isRowAvailable()) rowDataList.add(model.getRowData());
        }

        CollectionUtils.filter(keys, new ContainedPredicate(rowDataList));
    }

    public void setExpandableByTreeModel(TreeDataModel dataModel) {
        String currentRootId = "";
        int rowCount = dataModel.getRowCount();
        int i = 0;
        // Handle row and loop down the tree if expanded.
        while (i < rowCount) {
            dataModel.setRowIndex(i++);
            try {
                do {
                    // Decodes row/node in tree case.
                    // Handle recursive case
                    RowState currentModel = get(dataModel.getRowData());
                    
                    if (dataModel.getCurrentRowChildCount() == 0) {
                        currentModel.setExpandable(false);
                    }

                    if (dataModel.getCurrentRowChildCount() > 0) {
                        currentModel.setExpandable(true);
                        currentRootId =  currentRootId.equals("") ? (dataModel.getRowIndex()+"") : (currentRootId + "." + dataModel.getRowIndex());
                        dataModel.setRootIndex(currentRootId);
                        dataModel.setRowIndex(0);
                    } else if (dataModel.getRowIndex() < dataModel.getRowCount()-1) {
                        dataModel.setRowIndex(dataModel.getRowIndex() + 1);
                    } else if (!currentRootId.equals("")) {
                        // changing currrent node id to reflect pop
                        int lastSiblingRowIndex = dataModel.pop();
                        // if we are the last child of a set of siblings we've popped back to,
                        // continue popping until an uncounted sibling exists
                        while (lastSiblingRowIndex == (dataModel.getRowCount() - 1) && dataModel.isRootIndexSet()) {
                            lastSiblingRowIndex = dataModel.pop();
                            currentRootId = (currentRootId.lastIndexOf('.') != -1)  ?
                                currentRootId.substring(0,currentRootId.lastIndexOf('.')) :
                                "";
                        }
                        dataModel.setRowIndex(lastSiblingRowIndex + 1);
                        currentRootId = (currentRootId.lastIndexOf('.') != -1)  ?
                                currentRootId.substring(0,currentRootId.lastIndexOf('.')) :
                                "";
                    }
                    // Break out of expansion recursion to continue root node
                    if (currentRootId.equals("")) break;
                } while (true);
            } finally {
                dataModel.setRowIndex(-1);
                dataModel.setRootIndex(null);
            }
        }
    }

    public List getSelected() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), selectedPredicate));
    }
    public List getSelectable() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), selectablePredicate));
    }
    public List getEditable() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), editablePredicate));
    }
    public List getExpanded() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), expandedPredicate));
    }
    public List getExpandable() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), expandablePredicate));
    }
    public List getVisible() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), visiblePredicate));
    }
    public List getRowExpanders() { return null; }
    public List getPanelExpanders() { return null; }
    public List getType(String type) { return null; }


    
    public void setAllSelected(boolean value) {
        for (RowState s : map.values()) s.setSelected(value);
    }
    public void setAllSelectable(boolean value) {
        for (RowState s : map.values()) s.setSelectable(value);
    }
    public void setAllEditable(boolean value) {
        for (RowState s : map.values()) s.setEditable(value);
    }
    public void setAllExpanded(boolean value) {
        for (RowState s : map.values()) s.setExpanded(value);
    }
    public void setAllExpandable(boolean value) {
        for (RowState s : map.values()) s.setExpandable(value);
    }
    public void setAllVisible(boolean value) {
        for (RowState s : map.values()) s.setVisible(value);
    }
    public void setAllRowExpansion() {
        for (RowState s : map.values()) {
            s.setExpansionType(RowState.ExpansionType.ROW);
        }
    }
    public void setAllPanelExpansion() {
    for (RowState s : map.values()) {
            s.setExpansionType(RowState.ExpansionType.PANEL);
        }
    }


    
    static class SelectedPredicate implements Predicate {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((RowState)((Entry)o).getValue()).isSelected()) return true;
            return false;
        }
    }
    static class SelectablePredicate implements Predicate {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((RowState)((Entry)o).getValue()).isSelectable()) return true;
            return false;
        }
    }
    static class ExpandedPredicate implements Predicate {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((RowState)((Entry)o).getValue()).isExpanded()) return true;
            return false;
        }
    }
    static class ExpandablePredicate implements Predicate {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((RowState)((Entry)o).getValue()).isExpandable()) return true;
            return false;
        }
    }
    static class EditablePredicate implements Predicate {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((RowState)((Entry)o).getValue()).isEditable()) return true;
            return false;
        }
    }
    static class VisiblePredicate implements Predicate {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((RowState)((Entry)o).getValue()).isVisible()) return true;
            return false;
        }
    }
    class ContainedPredicate implements Predicate {
        Collection collection;

        ContainedPredicate(Collection collection) {
            this.collection = collection;
        }
        
        public boolean evaluate(Object o) { return collection.contains(o); }
    }

    static class EntrySetToKeyListTransformer {
        public static List transform(Collection o) {
            List<Object> keySet = new ArrayList<Object>();
            for (Entry e : (Collection<Entry>)o) {
                keySet.add(e.getKey());
            }
            return keySet;
        }
    }
}
