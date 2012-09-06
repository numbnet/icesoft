package org.icefaces.ace.component.tree;

import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
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
 * Date: 2012-09-06
 * Time: 2:10 PM
 */
public class TreeDecoder {
    private FacesContext context;
    private Tree tree;
    private Map<String, String> paramMap;

    private static final String EXPANSION_SUFFIX = "_expand";
    private static final String CONTRACTION_SUFFIX = "_contract";
    private static final String SELECTION_SUFFIX = "_select";
    private static final String DESELECTION_SUFFIX = "_deselect";


    public TreeDecoder(FacesContext context, Tree tree) {
        this.context = context;
        this.tree = tree;
    }

    public void decode() {
        if (requestHasParam(tree.getClientId(context) + EXPANSION_SUFFIX))
            decodeExpansion();

        if (requestHasParam(tree.getClientId(context) + CONTRACTION_SUFFIX))
            decodeContraction();

        if (requestHasParam(tree.getClientId(context) + SELECTION_SUFFIX))
            decodeSelection();

        if (requestHasParam(tree.getClientId(context) + DESELECTION_SUFFIX))
            decodeDeselection();
    }

    private void decodeDeselection() {
        String deselectedKey = getRequestParam(tree.getClientId(context) + DESELECTION_SUFFIX);
        tree.getStateMap()
                .get(tree.getKeyConverter().parseSegments(deselectedKey.split(":")))
                .setSelected(false);
    }

    private void decodeContraction() {
        String contractedKey = getRequestParam(tree.getClientId(context) + CONTRACTION_SUFFIX);
        tree.getStateMap()
                .get(tree.getKeyConverter().parseSegments(contractedKey.split(":")))
                .setExpanded(false);
    }

    private void decodeExpansion() {
        String expandedKey = getRequestParam(tree.getClientId(context) + EXPANSION_SUFFIX);
        tree.getStateMap()
                .get(tree.getKeyConverter().parseSegments(expandedKey.split(":")))
                .setExpanded(true);
    }

    private void decodeSelection() {
        String selectedKey = getRequestParam(tree.getClientId(context) + SELECTION_SUFFIX);
        tree.getStateMap()
                .get(tree.getKeyConverter().parseSegments(selectedKey.split(":")))
                .setSelected(true);
    }

    private boolean requestHasParam(String s) {
        paramMapCheck();
        return paramMap.containsKey(s);
    }

    private String getRequestParam(String s) {
        paramMapCheck();
        return paramMap.get(s);
    }

    private void paramMapCheck() {
        if (paramMap == null)
            paramMap = context.getExternalContext().getRequestParameterMap();
    }
}
