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

package org.icefaces.component.utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * A utility class used to easily insert a script element into the markup. It allows the writing of the script in one
 * call or writing it out gradually.
 */
public class ScriptWriter {
    private final ResponseWriter writer;
    private final UIComponent component;

    /**
     * Write script markup and script code in one method call.
     *
     * @param context   the curent FacesContext
     * @param component the component making the call
     * @param script    the code
     * @throws IOException
     */
    public static void insertScript(FacesContext context, final UIComponent component, String script) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writeScriptStart(writer, component);
        writer.write(script);
        writeScriptEnd(writer);
    }

    /**
     * Write script markup and script code in one method call.
     *
     * @param context   the curent FacesContext
     * @param component the component making the call
     * @param script    the code
     * @param id        the id of the rendered span
     * @throws IOException
     */
    public static void insertScript(FacesContext context, final UIComponent component, String script, String id) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writeScriptStart(writer, component, id);
        writer.write(script);
        writeScriptEnd(writer);
    }

    /**
     * Write the script markup and then gradually write the script code by calling the 'write' write methods on the
     * ScriptWriter instance.
     *
     * @param context   the curent FacesContext
     * @param component the component making the call
     * @return ScriptWriter instance used for streaming the code
     * @throws IOException
     */
    public static ScriptWriter insertScript(FacesContext context, final UIComponent component) throws IOException {
        return new ScriptWriter(context.getResponseWriter(), component);
    }

    public ScriptWriter(ResponseWriter writer, UIComponent component) {
        this.writer = writer;
        this.component = component;
    }

    public void startScript() throws IOException {
        writeScriptStart(writer, component);
    }

    public void write(String val) throws IOException {
        writer.write(val);
    }

    public void write(int val) throws IOException {
        writer.write(Integer.toString(val));
    }

    public void write(long val) throws IOException {
        writer.write(Long.toString(val));
    }

    public void write(double val) throws IOException {
        writer.write(Double.toString(val));
    }

    public void write(float val) throws IOException {
        writer.write(Float.toString(val));
    }

    public void write(boolean val) throws IOException {
        writer.write(Boolean.toString(val));
    }

    public void write(char val) throws IOException {
        writer.write(val);
    }

    public void write(Object val) throws IOException {
        writer.write(String.valueOf(val));
    }

    public void endScript() throws IOException {
        writeScriptEnd(writer);
    }

    private static void writeScriptStart(ResponseWriter writer, UIComponent component) throws IOException {
        writer.startElement("span", component);
        writer.writeAttribute("id", component.getClientId() + "_script", null);
        writer.startElement("script", component);
    }

    private static void writeScriptStart(ResponseWriter writer, UIComponent component, String id) throws IOException {
        writer.startElement("span", component);
        writer.writeAttribute("id", id, null);
        writer.startElement("script", component);
    }

    private static void writeScriptEnd(ResponseWriter writer) throws IOException {
        writer.endElement("script");
        writer.endElement("span");
    }
}
