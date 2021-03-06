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
            changed[i] = changeRoot;
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

        //Merge all remaining elements at different depths
        //Collection is a Set so duplicates will be discarded
        HashSet topElements = new HashSet();
        Iterator allDepthMaps = depthMaps.values().iterator();
        while (allDepthMaps.hasNext())  {
            topElements.addAll((HashSet) allDepthMaps.next());
        }
        
        if (!topElements.isEmpty()) {
            boolean reload = false;
            int j = 0;
            Element[] elements = new Element[topElements.size()];
            HashSet dupCheck = new HashSet();
            //copy the succsessful changed elements and check for change
            //to head or body
            for (int i = 0; i < changed.length; i++) {
                Element element = (Element) changed[i];
                String tag = element.getTagName();
                //send reload command if 'html', 'body', or 'head' elements need to be updated (see: ICE-3063)
                reload = reload || "html".equalsIgnoreCase(tag) || "head".equalsIgnoreCase(tag);
                if (topElements.contains(element))  {
                   if (!dupCheck.contains(element))  {
                        dupCheck.add(element);
                        elements[j++] = element;
                    }
                }
            }
            if (reload) {
                //reload document instead of applying an update for the entire page (see: ICE-2189)
                commandQueue.put(new Reload(viewNumber));
            } else {
                commandQueue.put(new UpdateElements(elements));
            }
        }

        oldDocument = document;
    }

    //prune the children by looking for ancestors in the parents collection 
    private void pruneAncestors(Integer parentDepth, Collection parents,
                                Integer childDepth, Collection children)  {
        Iterator parentList = parents.iterator();
        while (parentList.hasNext())  {
            Node parent = (Node) parentList.next();
            Iterator childList = children.iterator();
            while (childList.hasNext())  {
                Node child = (Node) childList.next();
                if (isAncestor(parentDepth, parent, childDepth, child))  {
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
