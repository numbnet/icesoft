package org.icefaces.ace.component.list;

import org.icefaces.ace.json.JSONArray;
import org.icefaces.ace.json.JSONException;

import javax.faces.context.FacesContext;
import java.util.*;

/**
 * Copyright 2010-2012 ICEsoft Technologies Canada Corp.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * User: Nils
 * Date: 12-04-11
 * Time: 10:26 AM
 */
public class ListDecoder {
    private ACEList list;
    private ACEList destList = null;

    public ListDecoder(ACEList list) {
        this.list = list;
    }

    public ListDecoder processSelections(String raw) throws JSONException {
        if (raw == null || raw.length() == 0) return this;

        final JSONArray array = new JSONArray(raw);
        final Set<Object> selections = list.getSelections();

        for (int i = 0; i < array.length(); i++) {
            final int index = array.getInt(i);
            list.setRowIndex(index);
            selections.add(list.getRowData());
        }

        list.setRowIndex(-1);

        return this;
    }

    public ListDecoder processDeselections(String raw) throws JSONException {
        if (raw == null || raw.length() == 0) return this;

        final JSONArray array = new JSONArray(raw);
        final Set<Object> selections = list.getSelections();

        for (int i = 0; i < array.length(); i++) {
            int index = array.getInt(i);
            list.setRowIndex(index);
            selections.remove(list.getRowData());
        }

        list.setRowIndex(-1);

        return this;
    }

    public ListDecoder processReorderings(String raw) throws JSONException {
        if (raw == null || raw.length() == 0) return this;

        JSONArray array = new JSONArray(raw);
        Object value = list.getValue();
        List collection = null;

        if (value instanceof List) collection = (List) value;
        else if (value.getClass().isArray()) collection = Arrays.asList(value);

        for (int i = 0; i < array.length(); i++) {
            JSONArray record = array.getJSONArray(i);
            int from = record.getInt(0);
            int to = record.getInt(1);
            Collections.swap(collection, from, to);
        }

        return this;
    }

    public ListDecoder attachEmigrants(FacesContext context, String destListId) throws JSONException {
        if (destListId == null || destListId.length() == 0) return this;

        ACEList destList = (ACEList)(context.getViewRoot().findComponent(destListId));

        // List has already had immigrants attached by itself,
        // and doesn't need us to attach immigrants
        if (destList.getImmigrants() != null) return this;

        // Get immigration list from-to index records to do our
        // removals, and push built record objects if needed
        List<ImmigrationRecord> immigrants = new ArrayList<ImmigrationRecord>();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String raw = params.get(destListId + "_immigration");
        JSONArray records = new JSONArray(raw).getJSONArray(1);

        Set<Object> selected = list.getSelections();
        Object value = list.getValue();
        List collection = null;

        if (value instanceof List) collection = (List) value;
        else if (value.getClass().isArray()) collection = Arrays.asList(value);

        for (int i = 0; i < records.length(); i++) {
            JSONArray record = records.getJSONArray(i);
            Object val = collection.get(((Integer)record.get(0)).intValue());
            immigrants.add(new ImmigrationRecord(val, (Integer) record.get(1), selected.contains(val)));
        }

        destList.setImmigrants(immigrants);

        return this;
    }

    public ListDecoder removeEmigrants(FacesContext context, String destListId) {
        if (destListId == null || destListId.length() == 0) return this;

        if (destList == null)
            destList = (ACEList)(context.getViewRoot().findComponent(destListId));

        Object value = list.getValue();
        List collection = null;
        Set<Object> selected = list.getSelections();
        if (value instanceof List) collection = (List) value;
        else if (value.getClass().isArray()) collection = Arrays.asList(value);

        for (ImmigrationRecord r : destList.getImmigrants()) {
            collection.remove(r.getValue());
            selected.remove(r.getValue());
        }

        return this;
    }

    public ListDecoder fetchImmigrants(FacesContext context, String raw) throws JSONException {
        if (raw == null || raw.length() == 0) return this;

        // List has already had immigrants attached by source,
        // and doesn't need to parse immigrants itself
        if (list.getImmigrants() != null) return this;

        JSONArray array = new JSONArray(raw);
        String sourceListId = array.getString(0);
        ACEList sourceList = (ACEList)(context.getViewRoot().findComponent(sourceListId));
        Set<Object> sourceSelected = sourceList.getSelections();
        JSONArray records = array.getJSONArray(1);

        List<ImmigrationRecord> immigrants = new ArrayList<ImmigrationRecord>();
        for (int i = 0; i < records.length(); i++) {
            JSONArray record = records.getJSONArray(i);
            int from = record.getInt(0);
            int to = record.getInt(1);

            sourceList.setRowIndex(from);
            Object value = sourceList.getRowData();

            // If selected, but not deselected this request
            // decode that information for the dest list.
            boolean selected = !indexDeselectedThisRequest(context, sourceListId, from);
            if (selected)
                selected = sourceSelected.contains(value) || indexSelectedThisRequest(context, sourceListId, from);

            immigrants.add(new ImmigrationRecord(value, to, selected));
        }
        list.setImmigrants(immigrants);

        sourceList.setRowIndex(-1);

        return this;
    }

    JSONArray sourceSelections;
    private boolean indexSelectedThisRequest(FacesContext context, String listId, int from) throws JSONException {
        if (sourceSelections == null) {
            String raw = context.getExternalContext().getRequestParameterMap().get(listId+"_selections");
            if (raw == null || raw.length() == 0) return false;
            sourceSelections = new JSONArray(raw);
        }
        for (int i = 0; i < sourceSelections.length(); i++)
            if (sourceSelections.getInt(i) == from) return true;

        return false;
    }


    JSONArray sourceDeselections;
    private boolean indexDeselectedThisRequest(FacesContext context, String listId, int from) throws JSONException {
        if (sourceDeselections == null) {
            String raw = context.getExternalContext().getRequestParameterMap().get(listId+"_deselections");
            if (raw == null || raw.length() == 0) return false;
            sourceDeselections = new JSONArray(raw);
        }
        for (int i = 0; i < sourceDeselections.length(); i++)
            if (sourceDeselections.getInt(i) == from) return true;

        return false;
    }

    public ListDecoder insertImmigrants() {
        Object value = list.getValue();
        List collection = null;
        Set<Object> selected = list.getSelections();
        List<ImmigrationRecord> records = list.getImmigrants();

        if (value instanceof List) collection = (List) value;
        else if (value.getClass().isArray()) collection = Arrays.asList(value);

        if (records != null)
            for (ImmigrationRecord record : records) {
                collection.add(record.getDestination(),
                        record.getValue());
                if (record.isSelected())
                    selected.add(record.getValue());
            }

        return this;
    }
}
