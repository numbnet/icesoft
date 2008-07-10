package com.icesoft.faces.context;

import com.icesoft.faces.util.DOMUtils;
import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.command.Reload;
import com.icesoft.faces.webapp.command.UpdateElements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Collection;

public class PushModeSerializer implements DOMSerializer {
    private Document oldDocument;
    private CommandQueue commandQueue;
    private String viewNumber;

    public PushModeSerializer(Document currentDocument, CommandQueue commandQueue, String viewNumber) {
        this.oldDocument = currentDocument;
        this.commandQueue = commandQueue;
        this.viewNumber = viewNumber;
    }

    public void serialize(Document document) throws IOException {
        Node[] changed = DOMUtils.domDiff(oldDocument, document);
        HashMap depthMaps = new HashMap();
        for (int i = 0; i < changed.length; i++) {
            Element changeRoot =
                    DOMUtils.ascendToNodeWithID(changed[i]);
            Integer depth = new Integer(getDepth(changeRoot));
            HashSet peers = (HashSet) depthMaps.get(depth);
            if (null == peers)  {
                peers = new HashSet();
                depthMaps.put(depth, peers);
            }
            //place the node in a collection of all nodes
            //at its same depth in the DOM
            peers.add(changeRoot);
        }
        HashSet elementList = new HashSet();
        Iterator allDepths = depthMaps.keySet().iterator();
        while (allDepths.hasNext())  {
            Integer baseDepth = (Integer) allDepths.next();
            Iterator checkDepths = depthMaps.keySet().iterator();
            while (checkDepths.hasNext()) {
                Integer checkDepth = (Integer) checkDepths.next();
                if (baseDepth.intValue() < checkDepth.intValue())  {
                    pruneAncestors(baseDepth, (HashSet) depthMaps.get(baseDepth),
                                   checkDepth, (HashSet) depthMaps.get(checkDepth) );
                }
            }
        }
        Iterator allDepthMaps = depthMaps.values().iterator();
        while (allDepthMaps.hasNext())  {
            elementList.addAll((HashSet) allDepthMaps.next());
        }
/*        for (int i = 0; i < changed.length; i++) {
           for (int j = 0; j < i; j++) {
                if (changed[j] == null)
                    continue;
                // If new is already in, then discard new
                if (changeRoot == changed[j]) {
                    changeRoot = null;
                    break;
                }
                // If new is parent of old, then replace old with new
                else if (isAncestor(changeRoot, changed[j])) {
                    changed[j] = null;
                }
                // If new is a child of old, then discard new
                else if (isAncestor(changed[j], changeRoot)) {
                    changeRoot = null;
                    break;
                }
            }
            changed[i] = changeRoot;
        }

        ArrayList elementList = new ArrayList(changed.length);
        for (int i = 0; i < changed.length; i++) {
            Element element = (Element) changed[i];
            if (null != element) {
                elementList.add(element);
            }
        }
*/
        if (!elementList.isEmpty()) {
            Iterator i = elementList.iterator();
            boolean reload = false;
            while (i.hasNext()) {
                String tag = ((Element) i.next()).getTagName();
//System.out.println("update node " + tag);
                //send reload command if 'html', 'body', or 'head' elements need to be updated (see: ICE-3063)
                reload = reload || "html".equalsIgnoreCase(tag) || "head".equalsIgnoreCase(tag);
            }
            if (reload) {
                //reload document instead of applying an update for the entire page (see: ICE-2189)
                commandQueue.put(new Reload(viewNumber));
            } else {
                Element[] elements = (Element[]) elementList.toArray(new Element[elementList.size()]);
                commandQueue.put(new UpdateElements(elements));
            }
        }

        oldDocument = document;
//        System.out.println();
    }

    //prune the children by looking for ancestors in the parents collection 
    private void pruneAncestors(Integer parentDepth, Collection parents,
                                Integer childDepth, Collection children)  {
//System.out.println("checking level " + parentDepth + " for children in " + childDepth);
        Iterator parentList = parents.iterator();
        while (parentList.hasNext())  {
            Node parent = (Node) parentList.next();
            Iterator childList = children.iterator();
            while (childList.hasNext())  {
                Node child = (Node) childList.next();
                if (isAncestor(parentDepth, parent, childDepth, child))  {
//System.out.println("pruning " + child);
                    childList.remove();
                }
            }

        }
    }

    private int getDepth(Node node)  {
        int depth = 0;
        Node parent = node;
        while ((parent = parent.getParentNode()) != null) {
            depth++;
        }
        return depth;
    }

    private boolean isAncestor(Integer parentDepth, Node parent,
                               Integer childDepth, Node child)  {
        if (!parent.hasChildNodes()) {
            return false;
        }
        Node testParent = child;
        int testDepth = childDepth.intValue();
        int stopDepth = parentDepth.intValue();
        while ( ((testParent = testParent.getParentNode()) != null) &&
                (testDepth > stopDepth) ){
            testDepth--;
            if (testParent.equals(parent)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAncestor(Node test, Node child) {
        if (test == null || child == null)
            return false;
        if (!test.hasChildNodes()) {
            return false;
        }
        Node parent = child;
        while ((parent = parent.getParentNode()) != null) {
            if (test.equals(parent)) {
                return true;
            }
        }
        return false;
    }
}
