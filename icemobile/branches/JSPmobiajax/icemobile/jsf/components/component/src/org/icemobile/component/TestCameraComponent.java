/*
 * ******* GENERATED CODE - DO NOT EDIT *******
 */

package org.icemobile.component;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponentBase;

import org.icemobile.component.TestCamera;
import org.icemobile.renderkit.TestCameraRenderer;
import org.icemobile.context.JSFRenderContext;

public class TestCameraComponent extends UIComponentBase {
    TestCameraRenderer testCameraRenderer = new TestCameraRenderer();

    public String getFamily()  {
        return "org.icefaces.TestCamera";
    }

    public boolean isDisabled()  {
        //replace with generated attribute getter
        return false;
    }

    public void setDisabled(boolean disabled)  {
        //replace with generated attribute setter
    }

    public void decode(FacesContext context)  {
        testCameraRenderer.encodeBegin(
                new JSFRenderContext(context),
                new TestCameraComponentWrapper(this));
    }

    public void encodeBegin(FacesContext context) throws IOException  {
        testCameraRenderer.encodeBegin(
                new JSFRenderContext(context),
                new TestCameraComponentWrapper(this));
    }

    public void encodeEnd(FacesContext context) throws IOException  {
        testCameraRenderer.encodeEnd(
                new JSFRenderContext(context),
                new TestCameraComponentWrapper(this));
    }

}