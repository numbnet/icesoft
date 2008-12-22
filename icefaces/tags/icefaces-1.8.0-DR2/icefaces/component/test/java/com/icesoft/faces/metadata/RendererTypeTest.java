package com.icesoft.faces.metadata;



import junit.framework.Test;
import junit.framework.TestSuite;


import com.sun.rave.jsfmeta.beans.ComponentBean;
import com.sun.rave.jsfmeta.beans.RendererBean;
import javax.faces.component.UIComponent;

public class RendererTypeTest extends ICECompsTestCase {


    public static Test suite() {
        return new TestSuite(RendererTypeTest.class);
    }

    public static void main() {
        junit.textui.TestRunner.run(RendererTypeTest.suite());
    }

    public void testNamingType() {

        ComponentBean[] oldComponentBeans = getComponentBeanInfo();
        ComponentBean[] componentBeans = new ComponentBean[oldComponentBeans.length];
        System.arraycopy(oldComponentBeans, 0, componentBeans, 0, oldComponentBeans.length);

        UIComponent[] oldUIComponentBases = getComponents();
        UIComponent[] uiComponentBases = new UIComponent[oldUIComponentBases.length];
        System.arraycopy(oldUIComponentBases, 0, uiComponentBases, 0, oldUIComponentBases.length);

        RendererBean[] oldRendererBeans = getRendererBean();
        RendererBean[] rendererBeans = new RendererBean[oldRendererBeans.length];
        System.arraycopy(oldRendererBeans, 0, rendererBeans, 0, oldRendererBeans.length);
        
        for (int i = 0; i < componentBeans.length; i++) {
            String renderTypeUIComponent = uiComponentBases[i].getRendererType();
            String renderTypeComponentBean = componentBeans[i].getRendererType();

            boolean notSameRenderType = renderTypeUIComponent != null && renderTypeComponentBean != null && !renderTypeUIComponent.trim().equalsIgnoreCase(
                    renderTypeComponentBean.trim());

            String message = "RenderType not the same for Component Class=" + componentBeans[i].getComponentClass() + "\n component renderType=" + renderTypeUIComponent + "\n faces-config declared renderType=" + renderTypeComponentBean + "\n\n";
            assertFalse(message, notSameRenderType);
            

            String familyTypeUIComponent = uiComponentBases[i].getFamily();
            String familyTypeComponentBean = componentBeans[i].getComponentFamily();

            boolean notSameRenderTypeTwo = familyTypeUIComponent != null && familyTypeComponentBean != null && !familyTypeUIComponent.trim().equalsIgnoreCase(
                    familyTypeComponentBean.trim());

            String messageTwo = "FamilyType not the same for Component Class=" + componentBeans[i].getComponentClass() + "\n component familyType=" + familyTypeUIComponent + "\n faces-config declared familyType=" + familyTypeComponentBean + "\n\n";
            assertFalse(messageTwo, notSameRenderTypeTwo);
        }
        
                
        for (int i = 0; i < rendererBeans.length; i++) {
            String message = "";
            try {
                String rendererClass = rendererBeans[i].getRendererClass();
                System.out.println("rendererClass=" + rendererClass);
                Class namedClass = Class.forName(rendererClass);
                String packageName = namedClass.getPackage().getName();

                message = "RenderType not the same for Component Class=" + componentBeans[i].getComponentClass() + "\n component renderType=" + rendererBeans[i].getRendererType() + "\n renderer class=" + rendererClass + "\n\n";
            } catch (Exception e) {
                fail(message);
            }
        }
    }

}
