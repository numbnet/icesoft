/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icefaces.component.baseMeta;

import org.icefaces.component.annotation.Facet;
import org.icefaces.component.annotation.Facets;

import javax.faces.component.UIComponent;

/**
 * These are the properties and facets for javax.faces.component.UIColumn
 */
public class UIColumnMeta extends UIComponentBaseMeta {
    @Facets
    class FacetsMeta {
        /**
         * The header facet is used for putting a component at the top of the
         * column, above the row data.
         */
        @Facet
        //TODO ICE-6110
                UIComponent header;

        /**
         * The footer facet is used for putting a component at the bottom of
         * the column, below the row data.
         */
        @Facet
        //TODO ICE-6110
                UIComponent footer;
    }
}
