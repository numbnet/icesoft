package org.icefaces.component.sliderentry;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.render.Renderer;

import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.JSONBuilder;
import org.icefaces.component.utils.ScriptWriter;
import org.icefaces.util.EnvUtils;
import org.icefaces.render.MandatoryResourceComponent;

/**
 * The sliderRender renders following elements:
 * 1. A div with a client id(e.g.)
 *  <div id="xxx" />
 *  which will be used by the YUI slider, as a slider holder
 *  
 * 2. A script node that makes a call to communicate with slider object on client
 *   <script>
 *        ice.yui.slider.updateProperties(clientId, yuiProps, jsfProps);
 *   </script>
 *   
 *   In addition to the rendering the renderer performs decode as well. This component
 *   doesn't use a hidden field for it value instead takes advantage of param support of JSF2
 */
@MandatoryResourceComponent("org.icefaces.component.sliderentry.SliderEntry")
public class SliderEntryRenderer extends Renderer{
    private final static Logger log = Logger.getLogger(SliderEntryRenderer.class.getName());
    // The decode method, in the renderer, is responsible for taking the values
    //  that have been submitted from the browser, and seeing if they correspond
    //  to this particular component, and also for the correct row(s) of any
    //  UIData container(s). So if there are 5 rows, and the user used the 3rd
    //  row's slider, this slider will decode 5 times, but should only be affected
    //  the 3rd time. Once it find that there is data relevant to it, it then
    //  extracts that data, and passes it to the component.
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        // The RequestParameterMap holds the values received from the browser
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();

        //"ice.event.captured" should be holding the event source id
        if (requestParameterMap.containsKey("ice.event.captured")) {
            SliderEntry slider = (SliderEntry)uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = uiComponent.getClientId(facesContext);

            if ("ice.ser".equals(requestParameterMap.get("ice.submit.type"))) {
                facesContext.renderResponse();
                return; 
            }
            String hiddenValue = String.valueOf(requestParameterMap.get(clientId+"_hidden"));;

			int submittedValue = 0;
            try {
                submittedValue = Integer.valueOf(hiddenValue);
                log.finer("Decoded slider value [id:value] [" + clientId + ":" + hiddenValue + "]" );
            } catch (NumberFormatException nfe) {
                log.warning("NumberFormatException  Decoding value for [id:value] [" + clientId + ":" + hiddenValue + "]");
            } catch (NullPointerException npe) {
                log.warning("NullPointerException  Decoding value for [id:value] [" + clientId + ":" + hiddenValue + "]");
            }

            //If I am a source of event?
            if (clientId.equals(source)) {
                try {
                    int value = slider.getValue();
                    //if there is a value change, queue a valueChangeEvent    
                    if (value != submittedValue) { 
                        uiComponent.queueEvent(new ValueChangeEvent (uiComponent, 
                                       new Integer(value), new Integer(submittedValue)));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // The encodeBegin method, in the renderer, is responsible for rendering
    //  the html markup, as well as the javacript necessary for initialising
    //  the YUI javascript object, in the browser. Typically the encodeEnd(-)
    //  method and possibly the encodeChildren(-) method would be used too,
    //  but we've put all the rendering here, in this one method.
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();        
        SliderEntry slider = (SliderEntry)uiComponent;

        // Write outer div
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_div", HTML.ID_ATTR);

        //YUI3 slider requires a div, where it renders slider component.
        writer.startElement(HTML.DIV_ELEM, uiComponent);

        

        // The Java UIComponent clientId corresponds with the Javascript DOM id
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        // If the application has specified a string of CSS class name(s), output it
        String styleClass = slider.getStyleClass();
        if (styleClass != null && styleClass.trim().length() > 0) {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, HTML.CLASS_ATTR);
        }
        // If the application has specified a string of CSS style(s), output it 
        String style = slider.getStyle();
        if (style != null && style.trim().length() > 0) {
            writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
        }
        writer.endElement(HTML.DIV_ELEM);

        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("name",clientId+"_hidden", null);
        writer.writeAttribute("id",clientId+"_hidden", null);
        writer.writeAttribute("value",slider.getValue(), null);
        writer.endElement("input");

        
        // pass jsProps through to YUI, pass jsfProps to custom javascript. 
        JSONBuilder jsBuilder = JSONBuilder.create().beginMap().
                entry("min", slider.getMin()).
                entry("max", slider.getMax()).
                entry("value", slider.getValue()).
                entry("axis", slider.getAxis()).
                entry("length", slider.getLength()).
                entry("clickableRail", slider.isClickableRail()).
                entry("disabled", slider.isDisabled());

        String thumbUrl = slider.getThumbUrl();
        if (thumbUrl != null && thumbUrl.trim().length() > 0) {
            jsBuilder.entry("thumbUrl", thumbUrl);
        }
        String submitOn = slider.getSubmitOn();
        if (submitOn.equals("slideStart") && slider.isClickableRail()) {
            submitOn = "slideEnd";
        }

        JSONBuilder jb = JSONBuilder.create().beginMap().
                entry("submitOn", submitOn).
                entry("singleSubmit", slider.isSingleSubmit()).
                entry("slideInterval", slider.getSlideInterval()).
                entry("aria", EnvUtils.isAriaEnabled(facesContext)).
                entry("tabindex", slider.getTabindex());


        if (thumbUrl != null && thumbUrl.trim().length() > 0) {
                jb.entry("thumbUrl", thumbUrl);
        }

        String jsProps = jsBuilder.endMap().toString();
        String jsfProps = jb.endMap().toString();
        String params = "'" + clientId + "'," +
                        jsProps
                        + "," +
                        jsfProps;
        String finalScript = "ice.component.slider.updateProperties(" + params + ");";
        log.finer("slider script contents: " + finalScript);
        ScriptWriter.insertScript(facesContext, uiComponent, finalScript);

        // End the enclosing div
        writer.endElement(HTML.DIV_ELEM);

    }
}
