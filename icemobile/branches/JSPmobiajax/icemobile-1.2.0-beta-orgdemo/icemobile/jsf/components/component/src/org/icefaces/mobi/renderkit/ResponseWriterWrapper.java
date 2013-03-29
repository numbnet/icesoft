package org.icefaces.mobi.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import org.icemobile.renderkit.IResponseWriter;

public class ResponseWriterWrapper implements IResponseWriter{
    
    private ResponseWriter wrapped;
    
    public ResponseWriterWrapper(ResponseWriter writer){
        wrapped = writer;
    }

    public void writeAttribute(String name, Object value) throws IOException {
        wrapped.writeAttribute(name, value, null);
    }

    public void writeAttribute(String name, boolean value) throws IOException {
        wrapped.writeAttribute(name, Boolean.valueOf(value), null);
    }

    public void writeAttribute(String name, int value) throws IOException {
        wrapped.writeAttribute(name, Integer.valueOf(value), null);
    }

    public void startElement(String name, Object component) throws IOException {
        wrapped.startElement(name, (UIComponent)component);        
    }

    public void endElement(String name) throws IOException {
        wrapped.endElement(name);
    }

    public void writeText(String text) throws IOException {
        wrapped.writeText(text,null);
    }

    public void write(String input) throws IOException {
        wrapped.write(input);
    }
    public void startElement(String name) throws IOException {
        wrapped.startElement(name, null);        
    }

    public void closeOffTag() throws IOException {
        //not necessary in jsp content        
    }

}
