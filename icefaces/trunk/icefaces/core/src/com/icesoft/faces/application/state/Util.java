package com.icesoft.faces.application.state;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.FacesException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;

/**
 * This code copyright JSF 1.2
 *
 *
 */
public class Util {

      public static  UIComponent newInstance(TreeNode n, Map classMap)
            throws FacesException {

        try {
            Class t = (Class) ((classMap != null) ? classMap.get(n.componentType) : null);
            if (t == null) {
                t = loadClass(n.componentType, n);
                if (t != null && classMap != null) {
                    classMap.put(n.componentType, t);
                } else {
                    throw new NullPointerException();
                }
            }

            UIComponent c = (UIComponent) t.newInstance();
            c.setId(n.id);

            return c;
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }


    public static void captureChild(List tree,
                                     int parent,
                                     UIComponent c) {

        if (!c.isTransient()) {
            TreeNode n = new TreeNode(parent, c);
            int pos = tree.size();
            tree.add(n);
            captureRest(tree, pos, c);
        }

    }


    public static void captureFacet(List tree,
                                     int parent,
                                     String name,
                                     UIComponent c) {

        if (!c.isTransient()) {
            FacetNode n = new FacetNode(parent, name, c);
            int pos = tree.size();
            tree.add(n);
            captureRest(tree, pos, c);
        }

    }


    public static void captureRest(List tree,
                                    int pos,
                                    UIComponent c) {

        // store children
        int sz = c.getChildCount();
        if (sz > 0) {
            List child = c.getChildren();
            for (int i = 0; i < sz; i++) {
                captureChild(tree, pos, (UIComponent) child.get(i));
            }
        }

        // store facets
        Map m = c.getFacets();
        if (m.size() > 0) {
            Set s = m.entrySet();
            Iterator i = s.iterator();
            while (i.hasNext()) {
                Map.Entry entry = (Map.Entry) i.next();
                captureFacet(tree,
                             pos,
                             (String) entry.getKey(),
                             (UIComponent) entry.getValue() );
            }
        }

    }

     public static ClassLoader getCurrentLoader(Object fallbackClass) {
        ClassLoader loader =
                Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = fallbackClass.getClass().getClassLoader();
        }
        return loader;
    }

    public static Class loadClass(String name,
                                     Object fallbackClass)
           throws ClassNotFoundException {
           ClassLoader loader = getCurrentLoader(fallbackClass);
           // Where to begin...
           // JDK 6 introduced CR 6434149 where one couldn't pass
           // in a literal for an array type ([Ljava.lang.String) and
           // get the Class representation using ClassLoader.loadClass().
           // It was recommended to use Class.forName(String, boolean, ClassLoader)
           // for all ClassLoading requests.
           // HOWEVER, when trying to eliminate the need for .groovy extensions
           // being specified in the faces-config.xml for Groovy-based artifacts,
           // by using a an adapter to the GroovyScriptEngine, I found that the class
           // instance was cached somewhere, so that no matter what change I made,
           // Class.forName() always returned the same instance.  I haven't been
           // able to determine why this happens in the appserver environment
           // as the same adapter in a standalone program works as one might expect.
           // So, for now, if the classname starts with '[', then use Class.forName()
           // to avoid CR 643419 and for all other cases, use ClassLoader.loadClass().
           if (name.charAt(0) == '[') {
               return Class.forName(name, true, loader);
           } else {
               return loader.loadClass(name);
           }
        }
    

    public static UIViewRoot restoreTree(Object[] tree, Map classMap)
                throws FacesException {

            UIComponent c;
            FacetNode fn;
            TreeNode tn;
            for (int i = 0; i < tree.length; i++) {
                if (tree[i]instanceof FacetNode) {
                    fn = (FacetNode) tree[i];
                    c = newInstance(fn, classMap);
                    tree[i] = c;
                    if (i != fn.parent) {
                        ((UIComponent) tree[fn.parent]).getFacets()
                                .put(fn.facetName, c);
                    }

                } else {
                    tn = (TreeNode) tree[i];
                    c = newInstance(tn, classMap);
                    tree[i] = c;
                    if (i != tn.parent) {
                        ((UIComponent) tree[tn.parent]).getChildren().add(c);
                    }
                }
            }
            return (UIViewRoot) tree[0];

        }


    private static class TreeNode implements Externalizable {

        private static final String NULL_ID = "";

        public String componentType;
        public String id;

        public int parent;

        private static final long serialVersionUID = -835775352718473281L;


        // ------------------------------------------------------------ Constructors


        public TreeNode() { }


        public TreeNode(int parent, UIComponent c) {

            this.parent = parent;
            this.id = c.getId();
            this.componentType = c.getClass().getName();

        }


        // --------------------------------------------- Methods From Externalizable

        public void writeExternal(ObjectOutput out) throws IOException {

            out.writeInt(this.parent);
            out.writeUTF(this.componentType);
            if (this.id != null) {
                out.writeUTF(this.id);
            } else {
                out.writeUTF(NULL_ID);
            }
        }


        public void readExternal(ObjectInput in)
                throws IOException, ClassNotFoundException {

            this.parent = in.readInt();
            this.componentType = in.readUTF();
            this.id = in.readUTF();
            if (id.length() == 0) {
                id = null;
            }
        }

    }


    private static final class FacetNode extends TreeNode {


        public String facetName;

        private static final long serialVersionUID = -3777170310958005106L;


        // ------------------------------------------------------------ Constructors

        public FacetNode() { }

        public FacetNode(int parent,
                         String name,
                         UIComponent c) {

            super(parent, c);
            this.facetName = name;

        }


        // ---------------------------------------------------------- Public Methods

        public void readExternal(ObjectInput in)
                throws IOException, ClassNotFoundException {

            super.readExternal(in);
            this.facetName = in.readUTF();
        }

        public void writeExternal(ObjectOutput out) throws IOException {

            super.writeExternal(out);
            out.writeUTF(this.facetName);
        }
    }
}
