/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icemobile.util;

import java.io.IOException;
import java.util.Stack;

import org.icemobile.renderkit.IResponseWriter;

public class MockResponseWriter implements IResponseWriter{
    
    private StringBuilder str = new StringBuilder();
    private Stack<String> elementStack = new Stack<String>();
    private boolean lastElementClosed = true;

    public void writeAttribute(String name, Object value) throws IOException {
        str.append(" " + name + "='" + value + "'");
        
    }

    public void writeAttribute(String name, boolean value) throws IOException {
        str.append(" " + name + "='" + value + "'");
        
    }

    public void writeAttribute(String name, int value) throws IOException {
        str.append(" " + name + "='" + value + "'");
        
    }

    public void startElement(String name, Object component) throws IOException {
        if( !elementStack.isEmpty() && !lastElementClosed){
            str.append(">");
        }
        str.append("<");
        str.append(name);
        lastElementClosed = false;
        elementStack.push(name);
        
    }

    public void startElement(String name) throws IOException {
        if( !elementStack.isEmpty() && !lastElementClosed){
            str.append(">");
        }
        str.append("<");
        str.append(name);
        lastElementClosed = false;
        elementStack.push(name);
        
    }

    public void endElement(String name) throws IOException {
        String element = elementStack.pop();
        
        if( !element.equals(name) ){
            System.out.println("******************** mismatched tag: " + name + ", expected " + element);
        }
        
        if( !lastElementClosed){
            str.append(">");
        }
        str.append("</");
        str.append(name);
        str.append(">");
        lastElementClosed = true;
        
    }

    public void writeText(String text) throws IOException {
        if( !lastElementClosed ){
            str.append(">");
        }
        str.append(text);
        lastElementClosed = true;
        
    }

    public void write(String input) throws IOException {
        if( !lastElementClosed ){
            str.append(">");
        }
        str.append(input);
        lastElementClosed = true;
        
    }

    public void closeOffTag() throws IOException {
        str.append(">");
        lastElementClosed = true;
        
    }
    
    public String toString(){
        return str.toString();
    }

}
