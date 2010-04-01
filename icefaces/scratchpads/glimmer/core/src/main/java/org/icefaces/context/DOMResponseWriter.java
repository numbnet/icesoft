/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icefaces.context;

import org.icefaces.util.DOMUtils;
import org.w3c.dom.*;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.PartialViewContext;

public class DOMResponseWriter extends ResponseWriter {

    private static Logger log = Logger.getLogger("org.icefaces.context");

    private static final String OLD_DOM = "org.icefaces.old-dom";
    private static final String STATE_FIELD_MARKER = "~com.sun.faces.saveStateFieldMarker~";
    private static final String XML_MARKER = "<?xml";
    private static final String DOCTYPE_MARKER = "<!DOCTYPE";

    private Writer writer;
    private Document document;
    private Node cursor;
    private List<Node> stateNodes = new ArrayList<Node>();
    private boolean suppressNextNode = false;

    public DOMResponseWriter(Writer writer) {
        this(DOMUtils.getNewDocument(), writer);
    }

    public DOMResponseWriter(Document document, Writer writer) {
        this.writer = writer;
        this.document = document;
    }

    /**
     * <p>Return the content type (such as "text/html") for this {@link
     * javax.faces.context.ResponseWriter}.  Note: this must not include the "charset="
     * suffix.</p>
     */
    public String getContentType() {
        //TODO:  Should we make this switchable for other content types
        //A somewhat dated article of interest appears here: http://hixie.ch/advocacy/xhtml
        return "text/html";
    }

    /**
     * <p>Return the character encoding (such as "ISO-8859-1") for this
     * {@link javax.faces.context.ResponseWriter}.  Please see <a
     * href="http://www.iana.org/assignments/character-sets">the
     * IANA</a> for a list of character encodings.</p>
     */
    public String getCharacterEncoding() {
        //TODO:  What is the logic for actually determining what this should be?
        return "UTF-8";
    }

    /**
     * Write a portion of an array of characters.
     *
     * @param cbuf Array of characters
     * @param off  Offset from which to start writing characters
     * @param len  Number of characters to write
     * @throws java.io.IOException If an I/O error occurs
     */
    public void write(char[] cbuf, int off, int len) throws IOException {
        //TODO: I believe we only need to implement the single basic write() method as the
        //other write methods all eventually call down to this anyway.
        if (0 == len) {
            return;
        }

        if (null == document) {
            writer.write(cbuf, off, len);
            return;
        }
        
        try {
            String data = new String(cbuf, off, len);

            //Write out the <?xml or <!DOCTYPE preamble as text rather than
            //trying to handle them as DOM nodes, but only if it's not an
            //Ajax request.  In that case we ignore them.
            if (data.startsWith(XML_MARKER) || data.startsWith(DOCTYPE_MARKER)) {
                PartialViewContext pvc = FacesContext.getCurrentInstance().getPartialViewContext();
                if( pvc == null || !pvc.isAjaxRequest() ){
                    writer.write(data);
                }
                return;
            }

            appendToCursor(data);
        } catch (Exception e) {
            if (log.isLoggable(Level.INFO)) {
                log.log(Level.INFO, "cannot write " + new String(cbuf, off, len), e);
            }
        }
    }

    public void write(String str) throws IOException {
        if ("".equals(str)) {
            return;
        }
        if (STATE_FIELD_MARKER.equals(str)) {
            //TODO: suppress insertion of state node into DOM rather than
            //remove later.  This special case is caused by the fact that
            //during partial responses the Sun implementation does not
            //write the state marker
            Node stateNode = document.createTextNode(str);
            stateNodes.add(stateNode);
            appendToCursor(stateNode);
            return;
        }
        if (null == document) {
            if (log.isLoggable(Level.FINEST)) {
                log.finest(writer.getClass() + " raw write str " + str);
            }
            writer.write(str);
            return;
        }
        try {
            appendToCursor(document.createTextNode(str));
        } catch (Exception e) {
            log.log(Level.SEVERE, "failed to write " + str, e);
        }
    }


    /**
     * <p>Flush any ouput buffered by the output method to the
     * underlying Writer or OutputStream.  This method
     * will not flush the underlying Writer or OutputStream;  it
     * simply clears any values buffered by this {@link javax.faces.context.ResponseWriter}.</p>
     */
    public void flush() throws IOException {
    }

    /**
     * Close the stream, flushing it first.  Once a stream has been closed,
     * further write() or flush() invocations will cause an IOException to be
     * thrown.  Closing a previously-closed stream, however, has no effect.
     *
     * @throws java.io.IOException If an I/O error occurs
     */
    public void close() throws IOException {
    }

    /**
     * <p>Write whatever text should begin a response.</p>
     *
     * @throws java.io.IOException if an input/output error occurs
     */
    public void startDocument() throws IOException {

        //TODO: Should we always create a new document here?  The constructor takes one
        //as an argument which would always get replaced when this method is called.
        document = DOMUtils.getNewDocument();
        cursor = document;
    }

    /**
     * <p>Write whatever text should end a response.  If there is an open
     * element that has been created by a call to <code>startElement()</code>,
     * that element will be closed first.</p>
     *
     * @throws java.io.IOException if an input/output error occurs
     */
    public void endDocument() throws IOException {
        boolean isPartialRequest = FacesContext.getCurrentInstance().getPartialViewContext().isPartialRequest();

        //full-page requests write directly to the response
        if (!isPartialRequest) {
            DOMUtils.printNode(document, writer);
        }

        if (null != document.getDocumentElement()) {
            for (Node stateNode : stateNodes) {
                stateNode.getParentNode().removeChild(stateNode);
            }
            stateNodes.clear();
            FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(OLD_DOM, document);
        }

        document = null;
        cursor = null;
//        savedJSFStateCursor = null;
    }

    /**
     * <p>Write the start of an element, up to and including the
     * element name.  Once this method has been called, clients can
     * call the <code>writeAttribute()</code> or
     * <code>writeURIAttribute()</code> methods to add attributes and
     * corresponding values.  The starting element will be closed
     * (that is, the trailing '>' character added)
     * on any subsequent call to <code>startElement()</code>,
     * <code>writeComment()</code>,
     * <code>writeText()</code>, <code>endElement()</code>,
     * <code>endDocument()</code>, <code>close()</code>,
     * <code>flush()</code>, or <code>write()</code>.</p>
     *
     * @param name      Name of the element to be started
     * @param component The {@link javax.faces.component.UIComponent} (if any) to which
     *                  this element corresponds
     * @throws java.io.IOException  if an input/output error occurs
     * @throws NullPointerException if <code>name</code>
     *                              is <code>null</code>
     */
    public void startElement(String name, UIComponent component) throws IOException {

        if (suppressNextNode)  {
            //this node has already been created and is just a placeholder
            //in the tree
            suppressNextNode = false;
            return;
        }
        //TODO:  Does this ever happen - ie does startDocument not get called for some reason?
        if (null == document) {
            document = DOMUtils.getNewDocument();
        }
        pointCursorAt(appendToCursor(document.createElement(name)));
    }

    /**
     * <p>Write the end of an element, after closing any open element
     * created by a call to <code>startElement()</code>.  Elements must be
     * closed in the inverse order from which they were opened; it is an
     * error to do otherwise.</p>
     *
     * @param name Name of the element to be ended
     * @throws java.io.IOException  if an input/output error occurs
     * @throws NullPointerException if <code>name</code>
     *                              is <code>null</code>
     */
    public void endElement(String name) throws IOException {
        pointCursorAt(cursor.getParentNode());
    }

    /**
     * <p>Write an attribute name and corresponding value, after converting
     * that text to a String (if necessary), and after performing any escaping
     * appropriate for the markup language being rendered.
     * This method may only be called after a call to
     * <code>startElement()</code>, and before the opened element has been
     * closed.</p>
     *
     * @param name     Attribute name to be added
     * @param value    Attribute value to be added
     * @param property Name of the property or attribute (if any) of the
     *                 {@link javax.faces.component.UIComponent} associated with the containing element,
     *                 to which this generated attribute corresponds
     * @throws IllegalStateException if this method is called when there
     *                               is no currently open element
     * @throws java.io.IOException   if an input/output error occurs
     * @throws NullPointerException  if <code>name</code> is
     *                               <code>null</code>
     */
    public void writeAttribute(String name, Object value, String property) throws IOException {
        if (null == value) {
            return;
        }
        //TODO: As per the javadoc, is there any escaping we should be aware of here?
        Attr attribute = document.createAttribute(name.trim());
        attribute.setValue(String.valueOf(value));
        appendToCursor(attribute);
    }

    /**
     * <p>Write a URI attribute name and corresponding value, after converting
     * that text to a String (if necessary), and after performing any encoding
     * appropriate to the markup language being rendered.
     * This method may only be called after a call to
     * <code>startElement()</code>, and before the opened element has been
     * closed.</p>
     *
     * @param name     Attribute name to be added
     * @param value    Attribute value to be added
     * @param property Name of the property or attribute (if any) of the
     *                 {@link javax.faces.component.UIComponent} associated with the containing element,
     *                 to which this generated attribute corresponds
     * @throws IllegalStateException if this method is called when there
     *                               is no currently open element
     * @throws java.io.IOException   if an input/output error occurs
     * @throws NullPointerException  if <code>name</code> is
     *                               <code>null</code>
     */
    public void writeURIAttribute(String name, Object value, String property) throws IOException {
        //TODO: Should there be more comprehensvie encoding here?
        String stringValue = String.valueOf(value);
        if (stringValue.startsWith("javascript:")) {
            writeAttribute(name, stringValue, property);
        } else {
            writeAttribute(name, stringValue.replace(' ', '+'), property);
        }
    }

    /**
     * <p>Write a comment containing the specified text, after converting
     * that text to a String (if necessary), and after performing any escaping
     * appropriate for the markup language being rendered.  If there is
     * an open element that has been created by a call to
     * <code>startElement()</code>, that element will be closed first.</p>
     *
     * @param comment Text content of the comment
     * @throws java.io.IOException  if an input/output error occurs
     * @throws NullPointerException if <code>comment</code>
     *                              is <code>null</code>
     */
    public void writeComment(Object comment) throws IOException {
        //TODO: We don't always consistently check for null document?  Should we? Escaping?
        String commentString = String.valueOf(comment);
        if (null == document) {
            writer.write(commentString);
            return;
        }
        appendToCursor(document.createComment(commentString));
    }

    /**
     * <p>Write an object, after converting it to a String (if necessary),
     * and after performing any escaping appropriate for the markup language
     * being rendered.  If there is an open element that has been created
     * by a call to <code>startElement()</code>, that element will be closed
     * first.</p>
     *
     * @param text     Text to be written
     * @param property Name of the property or attribute (if any) of the
     *                 {@link javax.faces.component.UIComponent} associated with the containing element,
     *                 to which this generated text corresponds
     * @throws java.io.IOException  if an input/output error occurs
     * @throws NullPointerException if <code>text</code>
     *                              is <code>null</code>
     */
    public void writeText(Object text, String property) throws IOException {
        //TODO: Escaping?  Null checks and exceptions?
        String textString = String.valueOf(text);
        if (textString.length() == 0) {
            return;
        }
        appendToCursor(textString);
    }

    /**
     * <p>Write text from a character array, after any performing any
     * escaping appropriate for the markup language being rendered.
     * If there is an open element that has been created by a call to
     * <code>startElement()</code>, that element will be closed first.</p>
     *
     * @param text Text to be written
     * @param off  Starting offset (zero-relative)
     * @param len  Number of characters to be written
     * @throws IndexOutOfBoundsException if the calculated starting or
     *                                   ending position is outside the bounds of the character array
     * @throws java.io.IOException       if an input/output error occurs
     * @throws NullPointerException      if <code>text</code>
     *                                   is <code>null</code>
     */
    public void writeText(char[] text, int off, int len) throws IOException {
        //TODO: Escaping?  Null checks?
        if (len == 0) {
            return;
        }
        writeText(new String(text, off, len), null);
    }

    /**
     * <p>Create and return a new instance of this {@link javax.faces.context.ResponseWriter},
     * using the specified <code>Writer</code> as the output destination.</p>
     *
     * @param writer The <code>Writer</code> that is the output destination
     */
    public ResponseWriter cloneWithWriter(Writer writer) {
        //TODO: Should we be creating a brand new one here or using the same document
        //with a new writer?
        if (writer.getClass().getName().endsWith("FastStringWriter")) {
            return new BasicResponseWriter(writer, getContentType(), getCharacterEncoding());
        }
        return new DOMResponseWriter(null, writer);
    }


    //
    private Node appendToCursor(String data) {
        return appendToCursor(document.createTextNode(data));
    }

    private Node appendToCursor(Node node) {
        try {
            if (log.isLoggable(Level.FINEST)) {
                log.finest("appending " + DOMUtils.toDebugString(node) + " into " + DOMUtils.toDebugString(cursor));
            }
            return cursor.appendChild(node);

        } catch (DOMException e) {
            String message = "failed to append " + DOMUtils.toDebugString(node) + " into " + DOMUtils.toDebugString(cursor);
            log.log(Level.SEVERE, message, e);
            throw new RuntimeException(message, e);
        }
    }

    private Node appendToCursor(Attr node) {
        try {
            if (log.isLoggable(Level.FINEST)) {
                log.finest("Appending " + DOMUtils.toDebugString(node) + " into " + DOMUtils.toDebugString(cursor));
            }
            Node result = ((Element) cursor).setAttributeNode(node);
            if ("id".equals(node.getName()))  {
                ((Element) cursor).setIdAttributeNode(node, true);
            }
            return result;
        } catch (DOMException e) {
            String message = "failed to append " + DOMUtils.toDebugString(node) + " into " + DOMUtils.toDebugString(cursor);
            log.log(Level.SEVERE, message, e);
            throw new RuntimeException(message, e);
        } catch (ClassCastException e) {
            String message = "cursor is not an element: " + DOMUtils.toDebugString(cursor);
            log.log(Level.SEVERE, message, e);
            throw new RuntimeException(message, e);
        }
    }

    private void pointCursorAt(Node node) {
        if (log.isLoggable(Level.FINEST)) {
            log.finest("moving cursor to " + DOMUtils.toDebugString(node));
        }
        cursor = node;
    }

    /**
     * <p>Prepare for rendering into subtrees.</p>
     */
    public void startSubtreeRendering()  {
        //subtree rendering will replace specified
        //subtrees in the old DOM
        document = getOldDocument();
    }

    /**
     * <p>Seek to the subtree specified by the id parameter and clear it
     * to prepare for new rendered output.</p>
     * @param id DOM id of component subtree
     * @return old DOM subtree
     */
    public Node seekSubtree(String id)  {
        Node oldSubtree = null;
        //seek to position in document
        cursor = document.getElementById(id);
        oldSubtree = cursor;
        if (null == cursor)  {
            log.severe("Unable to seek to component DOM subtree " + id);
        }
        Node newSubtree = document.createElement(cursor.getNodeName());
        Node cursorParent = cursor.getParentNode();
        //remove subtree for fresh rendering operation
        cursorParent.replaceChild(newSubtree, cursor);
        cursor = newSubtree;
        suppressNextNode = true;
        return oldSubtree;
    }

    public Document getDocument() {
        return document;
    }

    public Document getOldDocument() {
        return (Document) FacesContext.getCurrentInstance().getViewRoot().getAttributes().get(OLD_DOM);
    }

    public Node getCursorParent() {
        return cursor;
    }

    public void setCursorParent(Node cursorParent) {
        this.cursor = cursorParent;
    }

}
