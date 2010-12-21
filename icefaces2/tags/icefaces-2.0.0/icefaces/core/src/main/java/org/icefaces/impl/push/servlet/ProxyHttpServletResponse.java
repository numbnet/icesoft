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

package org.icefaces.impl.push.servlet;

import org.icefaces.impl.push.servlet.ProxyServletOutputStream;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;

import java.util.logging.Logger;
import java.io.PrintStream;
import java.io.OutputStream;

public class ProxyHttpServletResponse implements HttpServletResponse {
    private static Logger log = Logger.getLogger(ProxyHttpServletResponse.class.getName());
    private FacesContext facesContext;

    public ProxyHttpServletResponse(FacesContext facesContext) {
        this.facesContext = facesContext;
    }
    
    public  java.lang.String getCharacterEncoding() {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String getContentType() {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  javax.servlet.ServletOutputStream getOutputStream()       throws java.io.IOException {
        log.severe("ProxyHttpServletResponse unsupported operation");
        return new ProxyServletOutputStream(facesContext.getExternalContext().getResponseOutputStream());
    }

    public  java.io.PrintWriter getWriter()       throws java.io.IOException {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  void setCharacterEncoding(java.lang.String encoding) {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return;
    }

    public  void setContentLength(int length) {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return;
    }

    public  void setContentType(java.lang.String type) {
        log.severe("ProxyHttpServletResponse unsupported operation");
        facesContext.getExternalContext().setResponseContentType(type);
    }

    public  void setBufferSize(int size) {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return;
    }

    public  int getBufferSize() {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return -1;
    }

    public  void flushBuffer()       throws java.io.IOException {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return;
    }

    public  void resetBuffer() {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return;
    }

    public  boolean isCommitted() {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return false;
    }

    public  void reset() {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return;
    }

    public  void setLocale(java.util.Locale locale) {
        if (true) throw new UnsupportedOperationException();
        return;
    }

    public  java.util.Locale getLocale() {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }


    public  void addCookie(javax.servlet.http.Cookie cookie) {
        facesContext.getExternalContext().addResponseCookie(cookie.getName(), cookie.getValue(), null);
    }

    public  boolean containsHeader(java.lang.String name) {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return false;
    }

    public  java.lang.String encodeURL(java.lang.String url) {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String encodeRedirectURL(java.lang.String url) {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String encodeUrl(java.lang.String url) {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String encodeRedirectUrl(java.lang.String url) {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  void sendError(int code, java.lang.String message)       throws java.io.IOException {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return ;
    }

    public  void sendError(int code)       throws java.io.IOException {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return ;
    }

    public  void sendRedirect(java.lang.String url)       throws java.io.IOException {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return ;
    }

    public  void setDateHeader(java.lang.String name, long date) {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return ;
    }

    public  void addDateHeader(java.lang.String name, long date) {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return ;
    }

    public  void setHeader(java.lang.String name, java.lang.String value) {
        facesContext.getExternalContext().addResponseHeader(name, value);
    }

    public  void addHeader(java.lang.String name, java.lang.String value) {
        facesContext.getExternalContext().addResponseHeader(name, value);
    }

    public  void setIntHeader(java.lang.String name, int value) {
        facesContext.getExternalContext().addResponseHeader(name, String.valueOf(value));
    }

    public  void addIntHeader(java.lang.String name, int value) {
        facesContext.getExternalContext().addResponseHeader(name, String.valueOf(value));
    }

    public  void setStatus(int code) {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return ;
    }

    public  void setStatus(int code, java.lang.String message) {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return ;
    }

    public  int getStatus() {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return -1;
    }

    public  java.lang.String getHeader(java.lang.String name) {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public java.util.Collection<String> getHeaders(java.lang.String name) {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public java.util.Collection<String> getHeaderNames() {
        log.severe("ProxyHttpServletResponse unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }


}

class ProxyServletOutputStream extends ServletOutputStream {
    private PrintStream printStream;

    public ProxyServletOutputStream(OutputStream stream)  {
        this.printStream = new PrintStream(stream);
    }

    public void write(int value)       throws java.io.IOException {
        printStream.write(value);
    }

    public void print(String value)       throws java.io.IOException {
        printStream.print(value);
    }

    public void print(boolean value)       throws java.io.IOException {
        printStream.print(value);
    }

    public void print(char value)       throws java.io.IOException {
        printStream.print(value);
    }

    public void print(int value)       throws java.io.IOException {
        printStream.print(value);
    }

    public void print(long value)       throws java.io.IOException {
        printStream.print(value);
    }

    public void print(float value)       throws java.io.IOException {
        printStream.print(value);
    }

    public void print(double value)       throws java.io.IOException {
        printStream.print(value);
    }

    public void println()       throws java.io.IOException {
        printStream.println();
    }

    public void println(String value)       throws java.io.IOException {
        printStream.println(value);
    }

    public void println(boolean value)       throws java.io.IOException {
        printStream.println(value);
    }

    public void println(char value)       throws java.io.IOException {
        printStream.println(value);
    }

    public void println(int value)       throws java.io.IOException {
        printStream.println(value);
    }

    public void println(long value)       throws java.io.IOException {
        printStream.println(value);
    }

    public void println(float value)       throws java.io.IOException {
        printStream.println(value);
    }

    public void println(double value)       throws java.io.IOException {
        printStream.println(value);
    }


}
