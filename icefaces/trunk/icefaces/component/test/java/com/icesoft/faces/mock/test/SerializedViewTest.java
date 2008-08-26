/*
 *
 *
 */
package com.icesoft.faces.mock.test;

import com.icesoft.faces.component.ext.HtmlForm;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.context.effects.CurrentStyle;
import com.icesoft.faces.context.effects.Move;
import com.sun.faces.application.StateManagerImpl;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewRoot;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author fye
 */
public class SerializedViewTest extends MockTestCase {

    public static Test suite() {
        return new TestSuite(SerializedViewTest.class);
    }

    public void testSerializabedSaveState() {

        UIViewRoot uiViewRoot = getViewHandler().createView(getFacesContext(), this.getClass().getName() + "_view_id");
        getFacesContext().setViewRoot(uiViewRoot);

        HtmlForm form = new HtmlForm();
        form.setPartialSubmit(true);
        HtmlInputText inputText = new HtmlInputText();
        inputText.setFocus(Boolean.TRUE);
        inputText.setDisabled(Boolean.TRUE);
        CurrentStyle currentStyle = new CurrentStyle("Style:cssStyleString");
        currentStyle.setLastCssString("style:lastStyleString");
        inputText.getAttributes().put("currentStyle",currentStyle);
        inputText.setEffect(new Move());
        form.getChildren().add(inputText);

        ICECompsListHelper helper = new ICECompsListHelper();
        UIComponent[] oldAllComps = helper.getComponents();
        UIComponent[] allComps = new UIComponent[oldAllComps.length];
        System.arraycopy(oldAllComps, 0, allComps, 0, oldAllComps.length);

        for (int i = 0; i < allComps.length; i++) {
            UIComponent uIComponent = allComps[i];
            form.getChildren().add(uIComponent);
        }
        
        uiViewRoot.getChildren().add(form);

        //save state
        Object state = uiViewRoot.processSaveState(getFacesContext());

        StateManagerImpl stateManager = new StateManagerImpl();
        List treeList = new ArrayList();
        invokePrivateMethod("captureChild",
                new Class[]{List.class, Integer.TYPE, UIComponent.class},
                new Object[]{treeList, 0, uiViewRoot},
                StateManagerImpl.class,
                stateManager);

        //tree
        Object[] oldComps = treeList.toArray();
        Object[] comps = new Object[oldComps.length];
        System.arraycopy(oldComps, 0, comps, 0, oldComps.length);
        
        //tree plus state
        MockSerializedView view = new MockSerializedView(comps, state);
        MockSerializedView readView = null;
        try {
            GZIPOutputStream zos = null;
            ObjectOutputStream oos = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            zos = new GZIPOutputStream(bos);
            oos = new ObjectOutputStream(zos);

            oos.writeObject(view);
            oos.close();
            byte[] bytes = bos.toByteArray();
            InputStream in = new ByteArrayInputStream(bytes);
            GZIPInputStream gis = new GZIPInputStream(in);
            ObjectInputStream ois = new ObjectInputStream(gis);

            readView = (MockSerializedView) ois.readObject();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ExternalizableTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExternalizableTest.class.getName()).log(Level.SEVERE, null, ex);
        }



        UIViewRoot restoreViewRoot = (UIViewRoot) invokePrivateMethod("restoreTree",
                new Class[]{Object[].class},
                new Object[]{readView.getStructure()}, StateManagerImpl.class,
                stateManager);
        restoreViewRoot.processRestoreState(getFacesContext(), readView.getState());

        UIComponent firstForm = (UIComponent) restoreViewRoot.getChildren().get(0);
        UIComponentBase first = (UIComponentBase) firstForm.getChildren().get(0);

        myStringAttribute(first, "currentStyle", "style:lastStyleString");

        myBooleanAttribute(first, "focus", Boolean.TRUE);
        myBooleanAttribute(first, "disabled", Boolean.TRUE);

        myEffectAttribute(first, "effect", "effect.move");
    }

    private void myBooleanAttribute(UIComponent first, String attributeName, boolean expectedValue) {

        Object value = first.getAttributes().get(attributeName);
        if (value != null) {
            Boolean booleanValue = (Boolean) value;
            String message = " component=" + first + " property " + attributeName + " value=" + booleanValue.toString() + " expected value=" + expectedValue;
            Logger.getLogger(SerializedViewTest.class.getName()).log(Level.INFO, message);
        } else {
            String message = " component=" + first + " property " + attributeName + "=null";
            Logger.getLogger(SerializedViewTest.class.getName()).log(Level.INFO, message);
        }
    }

    private void myStringAttribute(UIComponent first, String attributeName, String expectedValue) {

        Object value = first.getAttributes().get(attributeName);
        //Logger.getLogger(SerializedViewTest.class.getName()).log(Level.INFO, temp.getCssString());
        if (value != null) {
            String stringValue = ((CurrentStyle) value).getLastCssString();
            String message = " component=" + first + " property " + attributeName + " value=" + stringValue + " expected value=" + expectedValue;
            Logger.getLogger(SerializedViewTest.class.getName()).log(Level.INFO, message);
        } else {
            String message = " component=" + first + " property " + attributeName + "=null";
            Logger.getLogger(SerializedViewTest.class.getName()).log(Level.INFO, message);
        }
    }

    private void myEffectAttribute(UIComponent first, String attributeName, String expectedValue) {

        Object value = first.getAttributes().get(attributeName);
        //Logger.getLogger(SerializedViewTest.class.getName()).log(Level.INFO, temp.getCssString());
        if (value != null) {
            String stringValue = value.toString();
            String message = " component=" + first + " property " + attributeName + " value=" + stringValue.toString() + " expected value=" + expectedValue;
            Logger.getLogger(SerializedViewTest.class.getName()).log(Level.INFO, message);
        } else {
            String message = " component=" + first + " property " + attributeName + "=null";
            Logger.getLogger(SerializedViewTest.class.getName()).log(Level.INFO, message);
        }
    }

    private void print(String message) {
        Logger.getLogger(SerializedViewTest.class.getName()).log(Level.INFO, message);
    }
}
