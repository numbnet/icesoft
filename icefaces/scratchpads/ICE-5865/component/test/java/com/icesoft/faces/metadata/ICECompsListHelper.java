/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */
 
/*
 * Helper
 */
package com.icesoft.faces.metadata;

import com.icesoft.jsfmeta.MetadataXmlParser;
import com.icesoft.jsfmeta.util.GeneratorUtil;
import com.sun.rave.jsfmeta.beans.ComponentBean;
import com.sun.rave.jsfmeta.beans.FacesConfigBean;
import com.sun.rave.jsfmeta.beans.RendererBean;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.render.RenderKitFactory;
import org.xml.sax.SAXException;

public class ICECompsListHelper {

    private static ComponentBean[] componentBeans;
    private static UIComponentBase[] uiComponentBases;
    private static RendererBean[] rendererBeans;
    private static FacesConfigBean facesConfigBean;
    private static ArrayList<ComponentBean> testComponentBeanList = new ArrayList<ComponentBean>();

    static {
        if (componentBeans == null || uiComponentBases == null) {
            componentBeans = getFacesConfigBean().getComponents();
            for (int i = 0; i < componentBeans.length; i++) {
                ComponentBean componentBean = componentBeans[i];
                if(componentBean.getComponentClass().startsWith("com.icesoft")){
                    testComponentBeanList.add(componentBean);
                }
            }
            componentBeans = testComponentBeanList.toArray(new ComponentBean[testComponentBeanList.size()]);
            rendererBeans = getFacesConfigBean().getRenderKit(
                    RenderKitFactory.HTML_BASIC_RENDER_KIT).getRenderers();
            uiComponentBases = new UIComponentBase[componentBeans.length];

            for (int j = 0; j < componentBeans.length; j++) {
                Object newObject = null;

                try {
                    Class namedClass = Class.forName(componentBeans[j].getComponentClass());
                    newObject = namedClass.newInstance();

                    if (newObject instanceof UIComponentBase) {
                        uiComponentBases[j] = (UIComponentBase) newObject;
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.exit(1);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    System.exit(1);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    System.exit(1);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }

    }

    //TODO: baseline Component com/sun/faces/standard-html-renderkit.xml
    private static FacesConfigBean getFacesConfigBean() {

        if (facesConfigBean == null) {
            facesConfigBean = new FacesConfigBean();
            MetadataXmlParser jsfMetaParser = new MetadataXmlParser();
            try {

                String standard_html_renderkit = "jar:" + GeneratorUtil.getBaseLineFolder("com/sun/faces/standard-html-renderkit.xml");
                String standard_html_renderkit_overlay = "jar:" + GeneratorUtil.getBaseLineFolder("com/sun/rave/jsfmeta/standard-html-renderkit-overlay.xml");
                String standard_html_renderkit_fixup = "jar:" + GeneratorUtil.getBaseLineFolder("com/sun/rave/jsfmeta/standard-html-renderkit-fixups.xml");

                String extRelativePath = GeneratorUtil.getWorkingFolder();
                String extended_faces_config = "file:" + extRelativePath + "../../../component-metadata/src/main/resources/conf/extended-faces-config.xml";
                String component_faces_config = "file:" + extRelativePath + "../../../component-metadata/src/main/resources/conf/faces-config-base.xml";

                String[] files = new String[]{standard_html_renderkit, standard_html_renderkit_overlay, standard_html_renderkit_fixup, extended_faces_config, component_faces_config};
                URL url = null;
                for (int i = 0; i < files.length; i++) {

                    url = new URL(files[i]);
                    jsfMetaParser.parse(url, facesConfigBean);

                }
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (SAXException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        return facesConfigBean;
    }

    public static UIComponent[] getComponents() {
        return uiComponentBases;
    }

    public static ComponentBean[] getComponentBeans() {
        return componentBeans;
    }

    public static RendererBean[] getRendererBean() {
        return rendererBeans;
    }

    public static RendererBean getRenderer(String componentFamily, String rendererType) {
        return getFacesConfigBean().getRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT).getRenderer(componentFamily, rendererType);
    }
}