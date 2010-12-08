package org.icefaces.component.testComponent;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;


import org.icefaces.component.utils.HTML;
import org.icefaces.util.EnvUtils;
import org.icefaces.render.MandatoryResourceComponent;


@MandatoryResourceComponent("org.icefaces.component.testComponent.TestComponent")
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

        writer.startElement(HTML.DIV_ELEM, uiComponent );
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.STYLE_ATTR, "line-height: 1em;", null);

        writer.append( "<br></br>Label = " + tc.getLabel() );
        writer.append( "<br></br>float one = " + tc.getFloatOne() );
        writer.append( "<br></br>float two = " + tc.getFloatTwo() );
        writer.append( "<br></br>Float Wrapper one =  " + tc.getFloatWrapperOne() );
        writer.append( "<br></br>Float Wrapper two =  " + tc.getFloatWrapperTwo() );
        writer.append( "<br></br>integer one =  " + tc.getIntegerOne() );
        writer.append( "<br></br>integer two =  " + tc.getIntegerTwo() );

        writer.append( "<br></br>Integer Wrapper one =  " + tc.getIntegerWrapperOne() );
        writer.append( "<br></br>Integer Wrapper two =  " + tc.getIntegerWrapperTwo() );

        writer.append( "<br></br>Long Value one =  " + tc.getLongOne() );
        writer.append( "<br></br>Long vlaue two =  " + tc.getLongTwo() );
        writer.append( "<br></br>Long Wrapper one =  " + tc.getLongWrapperOne() );
        writer.append( "<br></br>Long Wrapper two =  " + tc.getLongWrapperTwo() );


        writer.append( "<br></br>Boolean one =  " + tc.isBooleanOne() );
        writer.append( "<br></br>Boolean two =  " + tc.isBooleanTwo() );

        writer.append( "<br></br>Boolean Wrapper one =  " + tc.isBooleanWrapperOne() );
        writer.append( "<br></br>Boolean Wrapper two =  " + tc.isBooleanWrapperTwo() );

        writer.append( "<br></br>String property one =  " + tc.getStringOne() );
        writer.append( "<br></br>String property two =  " + tc.getStringTwo() );
        writer.endElement(HTML.DIV_ELEM);


//        // Now go and do some stuff to the component
//
//        tc.setFloatOne( tc.getFloatOne() + 1.0f );
//        tc.setFloatTwo( tc.getFloatTwo() + 1.0f );
//
//        Float f = tc.getFloatWrapperOne();
//        // catch the first pass through
//        if (f == null) {
//            f = new Float(0f);
//        }
//        tc.setFloatWrapperOne( f + Float.valueOf ("1.0") );
//        f = tc.getFloatWrapperTwo();
//        tc.setFloatWrapperTwo( f + Float.valueOf ("13.0") );
//
//        tc.setIntegerOne( tc.getIntegerOne() + 1);
//        tc.setIntegerTwo( tc.getIntegerTwo() + 1);
//
//        tc.setBooleanOne( !tc.isBooleanOne());
//        tc.setBooleanTwo( !tc.isBooleanTwo());
//
//        Boolean b = tc.isBooleanWrapperOne();
//        if (b == null) {
//            b = Boolean.valueOf(false);
//        }
//        tc.setBooleanWrapperOne( ! b );
//        tc.setBooleanWrapperTwo( ! tc.isBooleanWrapperTwo() );

    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);


    }
}