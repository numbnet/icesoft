package org.icefaces.component.testComponent;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;


import org.icefaces.component.utils.HTML;
import org.icefaces.util.EnvUtils;


public class TestComponentRenderer extends Renderer {

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
//        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
//        if (requestParameterMap.containsKey("ice.event.captured")) {
//            TestComponent tc  = (TestComponent) uiComponent;
//            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
//            String clientId = tc.getClientId();
//            if (clientId.equals(source)) {
//                try {
//                    uiComponent.queueEvent(new ActionEvent(uiComponent));
//                } catch (Exception e) {}
//            }
//        }
    }

    /**
     * Write the values maintained by the test component into a table.
     * @param facesContext
     * @param uiComponent
     * @throws IOException
     */
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {

        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);

        TestComponent tc = (TestComponent) uiComponent;
        // root element

        System.out.println("TestComponent = " + tc);

        writer.startElement(HTML.DIV_ELEM, uiComponent );
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        writer.append( "<br></br>Label = " + tc.getLabel() + " this should have a default");
        writer.append( "<br></br>float one = " + tc.getFloatOne() + " this should be 0.0");
        writer.append( "<br></br>float two = " + tc.getFloatTwo() + " this should have a default value (12.0)");
        writer.append( "<br></br>Float Wrapper one =  " + tc.getFloatWrapperOne() + " this should be null");
        writer.append( "<br></br>Float Wrapper two =  " + tc.getFloatWrapperTwo() + " this should have a default value");
        writer.append( "<br></br>integer one =  " + tc.getIntegerOne()  + " this should be zero");
        writer.append( "<br></br>integer two =  " + tc.getIntegerTwo() + " this should have a default (12)");


        writer.append( "<br></br>Integer Wrapper one =  " + tc.getIntegerWrapperOne() + " this should be null");
        writer.append( "<br></br>Integer Wrapper two =  " + tc.getIntegerWrapperTwo() + " this should have a default (12)");

        writer.append( "<br></br>Boolean one =  " + tc.isBooleanWrapperOne() + " this should be null");
        writer.append( "<br></br>Boolean two =  " + tc.isBooleanWrapperTwo() + " this should have a default value (true)");

        writer.append( "<br></br>String property one =  " + tc.getStringOne() + " this should be null");
        writer.append( "<br></br>String property two =  " + tc.getStringTwo() + " this should have a default value (Hello World!)");
        writer.endElement(HTML.DIV_ELEM);


        // Now go and do some stuff to the component

        tc.setFloatOne( tc.getFloatOne() + 1.0f );
        tc.setFloatTwo( tc.getFloatTwo() + 1.0f );

        Float f = tc.getFloatWrapperOne();
        // catch the first pass through
        if (f == null) {
            f = new Float(0f);
        }
        tc.setFloatWrapperOne( f + Float.valueOf ("1.0") );
        f = tc.getFloatWrapperTwo();
        tc.setFloatWrapperTwo( f + Float.valueOf ("13.0") );

        tc.setIntegerOne( tc.getIntegerOne() + 1);
        tc.setIntegerTwo( tc.getIntegerTwo() + 1);

        tc.setBooleanOne( !tc.isBooleanOne());
        tc.setBooleanTwo( !tc.isBooleanTwo());

        Boolean b = tc.isBooleanWrapperOne();
        if (b == null) {
            b = Boolean.valueOf(false);
        }
        tc.setBooleanWrapperOne( ! b );
        tc.setBooleanWrapperTwo( ! tc.isBooleanWrapperTwo() );

    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);


    }
}