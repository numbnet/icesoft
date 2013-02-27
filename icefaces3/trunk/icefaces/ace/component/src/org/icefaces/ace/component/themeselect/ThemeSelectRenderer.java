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
package org.icefaces.ace.component.themeselect;

import org.icefaces.ace.util.Constants;
import org.icefaces.render.MandatoryResourceComponent;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MandatoryResourceComponent(tagName = "themeSelect", value = "org.icefaces.ace.component.themeselect.ThemeSelect")
public class ThemeSelectRenderer extends Renderer {
    private FacesContext context;
    private ResponseWriter writer;
    private String selectedTheme;

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        this.context = context;
        super.encodeEnd(context, component);
        if (!component.isRendered()) {
            return;
        }
        writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        String selectId = "select_" + clientId;

        writer.startElement("span", component);
        writer.writeAttribute("id", clientId, "id");

        writer.startElement("select", component);
        writer.writeAttribute("id", selectId, "id");
        writer.writeAttribute("name", selectId, "id");
        renderOptions();
        writer.endElement("select");

        writer.endElement("span");
    }

    private void renderOptions() throws IOException {
        System.out.println("\nThemeSelectRenderer.renderOptions");
        selectedTheme = getSelectedTheme();

        renderOption("none", "");

        ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
        Resource resource;
        for (String theme : new String[]{"rime", "sam"}) {
            resource = resourceHandler.createResource("themes/" + theme + "/theme.css", "icefaces.ace");
            if (resource != null) {
                renderOption(theme, resource);
            }
        }
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("META-INF/resources");
        URL url;
        Pattern pattern = Pattern.compile("jar:.*/WEB-INF/lib/(?<jarName>.+)\\.jar!/META-INF/resources");
        Matcher matcher = pattern.matcher("");
        String theme;
        while (urls.hasMoreElements()) {
            url = urls.nextElement();
//            System.out.println("url = " + url);
            if (matcher.reset(url.toString()).matches()) {
                theme = matcher.group("jarName");
                System.out.println("theme = " + theme);
                resource = resourceHandler.createResource("theme.css", "ace-" + theme);
                if (resource != null) {
                    renderOption(theme, resource);
                }
            }
        }
/*
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        Set<String> libJars = extContext.getResourcePaths("/WEB-INF/lib");
        if (libJars == null) {
            return;
        }
        JarInputStream jarInputStream;
        JarEntry jarEntry;
        String entryName;
        for (String jar : libJars) {
            System.out.println("jar = " + jar);
            jarInputStream = new JarInputStream(extContext.getResourceAsStream(jar));
            while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
                entryName = jarEntry.getName();
                if (entryName.endsWith("theme.css")) {
                    System.out.println("entryName = " + entryName);
                }
                jarInputStream.closeEntry();
            }
            jarInputStream.close();
        }
*/
    }

    private void renderOption(String theme, Object href) throws IOException {
        writer.startElement("option", null);
        writer.writeAttribute("value", theme, null);
        if (theme.equals(selectedTheme)) {
            writer.writeAttribute("selected", "selected", null);
        }
        writer.writeAttribute("data-href", href, null);
        writer.write(theme);
        writer.endElement("option");
    }

    // Copied from org.icefaces.ace.renderkit.HeadRenderer.processEvent
    public String getSelectedTheme() {
        FacesContext context = FacesContext.getCurrentInstance();
        String theme = null;
        String themeParamValue = context.getExternalContext().getInitParameter(Constants.THEME_PARAM);

        if (themeParamValue != null) {
            ELContext elContext = context.getELContext();
            ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
            ValueExpression ve = expressionFactory.createValueExpression(elContext, themeParamValue, String.class);

            theme = (String) ve.getValue(elContext);
        }

        if (theme == null) theme = "";
        else theme = theme.trim();

        if ("".equals(theme) || theme.equalsIgnoreCase("sam")) {
            theme = "sam";
        }
        return theme;
    }
}
