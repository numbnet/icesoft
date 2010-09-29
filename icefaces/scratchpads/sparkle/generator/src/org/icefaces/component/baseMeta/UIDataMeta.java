package org.icefaces.component.baseMeta;

import org.icefaces.component.annotation.Property;

import java.util.Map;

/**
 *
 */
public class UIDataMeta extends UIComponentBaseMeta {

    @Property(defaultValue="0", tlddoc="Zero-relative row number of the first row in the underlying data model to be displayed, or zero to" +
                                       " start at the beginning of the data model" )
    private int first;

    @Property(defaultValue="0", tlddoc="Zero-relative index of the row currently being accessed in the underlying DataModel, or -1 for " +
                                       "no current row" )
    private int rowIndex;

    @Property(defaultValue="0", tlddoc="The number of rows (starting with the one identified by the first property) to be displayed, or zero to " +
                                       "display the entire set of available rows" )
    private int rows;

    @Property(tlddoc="The DataModel instance representing the data to which this component is bound, or a collection of " +
                     "data for which a DataModel instance is synthesized" )
    private Object value; 

    @Property( tlddoc="The request-scope attribute (if any) under which the data object for the current row will be exposed when iterating" )
    private String var;

}
