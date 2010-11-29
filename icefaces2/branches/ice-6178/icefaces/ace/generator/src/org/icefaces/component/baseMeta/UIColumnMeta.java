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
        @Facet //TODO ICE-6110
        UIComponent header;
        
        /**
         * The footer facet is used for putting a component at the bottom of 
         * the column, below the row data.
         */
        @Facet //TODO ICE-6110
        UIComponent footer;    
    }
}
