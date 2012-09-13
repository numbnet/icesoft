package org.icefaces.ace.model.tree;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 8/16/12
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface KeySegmentConverter<K> {
    // Return key segment object based on node object
    // Key segment  equality is used to identify the source node object
    // The .toString() of the key segment object will be used to write DOM node IDs.
    public Object getSegment(K node);
    // Used to create complete NodeKey with segment objects parsed from their .toString()
    // representations.
    public NodeKey parseSegments(String[] segments);
}
