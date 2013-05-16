/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icemobile.jsp.tags;

import static org.icemobile.util.HTML.HREF_ATTR;
import static org.icemobile.util.HTML.ID_ATTR;
import static org.icemobile.util.HTML.IMG_ELEM;
import static org.icemobile.util.HTML.INPUT_ELEM;
import static org.icemobile.util.HTML.SCRIPT_ELEM;
import static org.icemobile.util.HTML.SRC_ATTR;
import static org.icemobile.util.HTML.TYPE_ATTR;
import static org.icemobile.util.HTML.VALUE_ATTR;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

import javax.servlet.jsp.PageContext;

import org.icemobile.renderkit.IResponseWriter;

public class TagWriter implements IResponseWriter{
    
    private static final Logger LOG =
            Logger.getLogger(TagWriter.class.toString());
    
    private Writer out;
    private Stack<String> elementStack = new Stack<String>();
    private boolean lastElementClosed = true;
    private boolean selfClose = false;
    
    private static final String SPACE = " ";
    private static final String LT = "<";
    private static final String GT = ">";
    private static final String SC = "/>";
    private static final String SQ = "=\"";
    private static final String EM = "";
    private static final String EQ = "\" ";
    private static final String ET = "</";
    private static final String DISABLED = "disabled";
    private static final String STYLE = "style";
    private static final String CLASS = "class";
    private static final String DIV = "div";
    private static final String SPAN = "span";
    private static final String TEXT_JAVASCRIPT = "text/javascript";
    private static final List<String> SELFCLOSING_TAGS 
        = Arrays.asList(new String[]{"area", "br", "embed", "img", "keygen", "wbr","input","meta", "base", 
        "basefont", "bgsound", "command", "link","param", "source", "track","hr","isindex"});
    
    /**
     * Create an instance of the TagWriter class. For use with tags that
     * do not require an initial element context, such as simple tags 
     * with no tag body content.
     * @param pageContext The JSP context
     */
    public TagWriter(PageContext pageContext){
        this.out = pageContext.getOut();
    }
    
    /**
     * Create an instance of the TagWriter class. For use with tags that
     * use both doStartTag() and doEndTag() methods, and require an 
     * initial element context for the doEndTag() method.
     * @param pageContext The JSP context
     * @param elementContext The stack of starting elements.
     */
    public TagWriter(PageContext pageContext, Stack<String> elementContext){
        this(pageContext);
        elementStack = elementContext;
    }
    
    public void push(String str){
        elementStack.push(str);
    }
    
    public void writeAttribute(String name, Object value) throws IOException{
         if( value != null ){
             out.write(SPACE);
             out.write(name);
             out.write(SQ);
             out.write(value != null ? value.toString() : EM);
             out.write(EQ);
         }
    }
    
    public void writeAttribute(String name, String value) throws IOException{
        if( value != null && !"".equals(value)){
            out.write(SPACE);
            out.write(name);
            out.write(SQ);
            out.write(value != null ? value.toString() : EM);
            out.write(EQ);
        }
   }
    
    public void writeAttribute(String name, boolean value) throws IOException{
        out.write(SPACE);
        out.write(name);
        out.write(SQ);
        out.write(Boolean.toString(value));
        out.write(EQ);
    }
    
    public void writeAttribute(String name, int value) throws IOException{
        out.write(SPACE);
        out.write(name);
        out.write(SQ);
        out.write(value);
        out.write(EQ);
    }
    
    public void startElement(String name) throws IOException{
        if( SELFCLOSING_TAGS.contains(name)){
            selfClose = true;
        }
        else{
            selfClose = false;
        }
        if( !elementStack.isEmpty() && !lastElementClosed){
            out.write(GT);
        }
        out.write(LT);
        out.write(name);
        lastElementClosed = false;
        elementStack.push(name);
    }
    
    public void closeOffTag() throws IOException{
        out.write(GT);
        lastElementClosed = true;
    }
    
    public void endElement() throws IOException{
        String element = elementStack.pop();
        if( selfClose ){
            out.write(SC);
            selfClose = false;
        }
        else{
            if( !lastElementClosed){
                out.write(GT);
            }
            out.write(ET);
            out.write(element);
            out.write(GT);
        }
        lastElementClosed = true;
    }
    
    public void endElement(String name) throws IOException{
        String element = elementStack.pop();
        if( !element.equals(name) ){
            LOG.warning(" mismatched tag: " + name + ", expected " + element);
        }
        if( selfClose ){
            out.write(SC);
            selfClose = false;
        }
        else{
            if( !lastElementClosed){
                out.write(GT);
            }
            out.write(ET);
            out.write(element);
            out.write(GT);
        }
        lastElementClosed = true;
    }
    
    public void startSpan() throws IOException{
        startElement(SPAN);
    }
    
    public void startDiv() throws IOException{
        startElement(DIV);
    }
    
    public void writeDisabled(boolean disabled) throws IOException{
        if( disabled ){
            writeAttribute(DISABLED, disabled);
        }
    }
    
    public void writeStyle(String style) throws IOException{
        if( style != null ){
            writeAttribute(STYLE, style);
        }
    }
    
    public void writeStyleClassWithBase(String styleClass, String baseCSSClass) throws IOException{
        if( styleClass != null ){
            writeAttribute(CLASS, baseCSSClass + SPACE + styleClass);
        }
        else{
            writeAttribute(CLASS, baseCSSClass);
        }   
    }
    
    public void writeStyleClass(String styleClass) throws IOException{
        if( styleClass != null ){
            writeAttribute(CLASS, styleClass);
        }
        else{
            writeAttribute(CLASS, EM);
        }   
    }
    
    public void writeText(String text) throws IOException{
        if( !lastElementClosed ){
            out.write(GT);
        }
        out.write(text);
        lastElementClosed = true;
    }
    public void write(String text) throws IOException{
        writeText(text);
    }
    
    public void writeExternalScript(String src) throws IOException{
        startElement(SCRIPT_ELEM);
        writeAttribute(SRC_ATTR,src);
        writeAttribute(TYPE_ATTR,TEXT_JAVASCRIPT);
        endElement();
    }
    
    public void writeInlineScript(String script) throws IOException{
        startElement(SCRIPT_ELEM);
        writeAttribute(TYPE_ATTR,TEXT_JAVASCRIPT);
        writeText(script);
        endElement();
    }
    
    public void writeInputElement(String id, String type, String value) throws IOException{
        startElement(INPUT_ELEM);
        if( id != null ){
            writeAttribute(ID_ATTR, id);
        }
        if( type == null ){
            type = "text";
        }
        writeAttribute(TYPE_ATTR,type);
        if( value != null ){
            writeAttribute(VALUE_ATTR, value);
        }
        endElement();
    }
    
    public void writeImageElement(String id, String src, String alt, String style, String styleClass)
            throws IOException{
        //alt is required
        if( alt == null ){
            alt = "image";
        }
        startElement(IMG_ELEM);
        if( id != null ){
            writeAttribute(ID_ATTR,id);
        }
        writeAttribute(HREF_ATTR,src);
        if( style != null ){
            writeStyle(style);
        }
        if( styleClass != null ){
            writeStyleClass(styleClass);
        }
        endElement();
    }

    public void startElement(String name, Object component) throws IOException {
        startElement(name);        
    }
}
