package org.icemobile.renderkit;

import java.io.IOException;

public interface IResponseWriter {
    
    public void writeAttribute(String name, Object value) throws IOException;
    public void writeAttribute(String name, boolean value) throws IOException;
    public void writeAttribute(String name, int value) throws IOException;
    public void startElement(String name, Object component) throws IOException;
    public void startElement(String name) throws IOException;
    public void endElement(String name) throws IOException;
    public void writeText(String text) throws IOException;
    public void closeOffTag() throws IOException;

}
