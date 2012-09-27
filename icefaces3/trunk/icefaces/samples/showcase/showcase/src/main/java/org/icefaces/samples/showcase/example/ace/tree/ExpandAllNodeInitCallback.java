package org.icefaces.samples.showcase.example.ace.tree;

import org.icefaces.ace.model.tree.NodeState;
import org.icefaces.ace.model.tree.NodeStateCreationCallback;

import java.io.Serializable;

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
 * Date: 2012-09-27
 * Time: 10:56 AM
 */
public class ExpandAllNodeInitCallback implements NodeStateCreationCallback, Serializable {
    public NodeState initializeState(NodeState newState, Object node) {
        newState.setExpanded(true);
        return newState;
    }
}
