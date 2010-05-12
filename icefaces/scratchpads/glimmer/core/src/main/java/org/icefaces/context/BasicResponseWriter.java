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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package org.icefaces.context;

import javax.faces.context.ResponseWriter;
import java.io.Writer;
import java.io.IOException;
import javax.faces.component.UIComponent;

public class BasicResponseWriter extends ResponseWriter  {
    private Writer writer;
    String contentType;
    String characterEncoding;
    boolean closeStart = false;

    public BasicResponseWriter(Writer writer, String contentType, String characterEncoding)  {
        this.writer = writer;
        this.contentType = contentType;
        this.characterEncoding = characterEncoding;
    }

    public String getContentType()  {
        return contentType;
    }

    public String getCharacterEncoding()  {
        return characterEncoding;
    }

    public void flush() throws IOException  {
        closeStartIfNecessary();
        writer.flush();
    }

    public void startDocument() throws IOException  {
    }

    public void endDocument() throws IOException {
    }

    public void startElement(String name, UIComponent component) throws IOException  {
        closeStartIfNecessary();
        writer.write('<');
        writer.write(name);
        closeStart = true;
    }

    public void endElement(String name) throws IOException  {
        closeStartIfNecessary();
        writer.write("</");
        writer.write(name);
        writer.write('>');
    }

    public void startCDATA() throws IOException  {
        closeStartIfNecessary();
        writer.write("<![CDATA[");
    }

    public void endCDATA() throws IOException  {
        writer.write("]]>");
    }

    public void writeAttribute(String name, Object value, String componentPropertyName) throws IOException  {
        writer.write(' ');
        writer.write(name);
        writer.write("=\"");
        writer.write(String.valueOf(value));
        writer.write("\"");
    }

    public void writeURIAttribute(String name, Object value, String componentPropertyName) throws IOException  {
        throw new UnsupportedOperationException("Implement writeURIAttribute");
    }

    public void writeComment(Object comment) throws IOException  {
        closeStartIfNecessary();
        //this may require escaping as well
        writer.write("<!--");
        writer.write(comment.toString());
        writer.write("-->");
    }

    public void writeText(Object text, String componentPropertyName) throws IOException {
        throw new UnsupportedOperationException("Implement writeText with escaping");
    }
    public void writeText(Object text, UIComponent component, String componentPropertyName) throws IOException  {
        closeStartIfNecessary();
        writer.write(text.toString());
    }
    public void writeText(char[] chars, int offset, int length) throws IOException  {
        throw new UnsupportedOperationException("Implement writeText with escaping");
    }

    public ResponseWriter cloneWithWriter(Writer writer)  {
        return new BasicResponseWriter(writer, getContentType(), getCharacterEncoding());
//        throw new UnsupportedOperationException("BasicResponseWriter does not support cloning");
    }
    
    public void close() throws IOException  {
        closeStartIfNecessary();
        writer.close();
    }

    public void write(char[] chars, int offset, int length) throws IOException  {
        closeStartIfNecessary();
        writer.write(chars, offset, length);
    }

    private void closeStartIfNecessary() throws IOException {
        if (closeStart)  {
            writer.write('>');
            closeStart = false;
        }
    }
}