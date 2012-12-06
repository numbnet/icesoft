package org.icemobile.renderkit;

import org.icemobile.util.Constants;
import org.icemobile.component.ICarousel;
import org.icemobile.util.ClientDescriptor;

import java.io.IOException;
import java.util.logging.Logger;

import static org.icemobile.util.HTML.*;

public class CarouselCoreRenderer extends BaseCoreRenderer {
    private static final Logger logger =
            Logger.getLogger(CarouselCoreRenderer.class.toString());

    StringBuilder baseClass = new StringBuilder(ICarousel.CAROUSEL_CLASS);
    StringBuilder scrollerClass = new StringBuilder(ICarousel.SCROLLER_CLASS) ;
    StringBuilder listClass = new StringBuilder(ICarousel.LIST_CLASS) ;
    StringBuilder pagClass = new StringBuilder("") ; //currently a problem with existing css

    public void encodeBegin(ICarousel component, IResponseWriter writer)
            throws IOException {
        String userDefinedClass = component.getStyleClass();
        if (userDefinedClass!=null){
            baseClass.append(" ").append(userDefinedClass);
            scrollerClass.append(" ").append(userDefinedClass);
            listClass.append(" ").append(userDefinedClass);
            pagClass.append(" ").append(userDefinedClass);
        }
        String clientId = component.getClientId();
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTR, clientId);
        writer.writeAttribute(NAME_ATTR, clientId);
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTR, clientId+"_carousel");
        writeStandardLayoutAttributes(writer, component, baseClass.toString() );
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTR, clientId+"_scroller");
        writer.writeAttribute(CLASS_ATTR, scrollerClass.toString());
        writer.startElement(UL_ELEM, component);
        writer.writeAttribute(ID_ATTR, clientId+"_lister");
        writer.writeAttribute(CLASS_ATTR, listClass.toString());
    }


    public void encodeEnd(ICarousel carousel, IResponseWriter writer)
            throws IOException{
        String clientId= carousel.getClientId();
        writer.endElement(UL_ELEM);
        writer.endElement(DIV_ELEM);
        writer.endElement(DIV_ELEM);
        //now do the dots paginator for the carousel
        writer.startElement(DIV_ELEM, carousel);
        writer.writeAttribute(ID_ATTR, clientId+"_dots");
        Object prevLabel = carousel.getPreviousLabel();
        if (prevLabel !=null){
            renderPagination( carousel, writer, String.valueOf(prevLabel),clientId, "prev" );
        }
        Object nextLabel = carousel.getNextLabel();
        if (nextLabel !=null ){
            renderPagination(carousel, writer, String.valueOf(nextLabel),clientId, "next" );
        }
        writer.startElement(DIV_ELEM, carousel);
        writer.writeAttribute("class", ICarousel.CAROUSEL_CURSOR_CLASS);
        writer.writeAttribute("style", carousel.getStyle());
        writer.startElement(DIV_ELEM, carousel);
        writer.writeAttribute("class", ICarousel.CAROUSEL_CURSOR_CURSOR_CENTER_CLASS);
        writer.startElement(UL_ELEM, null);
        writer.writeAttribute(ID_ATTR, clientId+"_ullist");
        writer.writeAttribute("class", ICarousel.CAROUSEL_CURSOR_LISTCLASS);
        int size = carousel.getRowCount();
        int selected = carousel.getSelectedItem();
        if (selected > size - 1 || selected < 0) {
            selected = 0;
        }
        for (int i = 0; i < size; i++) {
            writer.startElement(LI_ELEM, null);
            writer.writeText(String.valueOf(i + 1));
            writer.endElement(LI_ELEM);
        }
        //do the list of dots for pagination MAKE THIS OPTIONAL??
        writer.endElement(UL_ELEM);
        writer.endElement(DIV_ELEM);
        writer.endElement(DIV_ELEM);
        writer.endElement(DIV_ELEM);
        writer.startElement(DIV_ELEM);
        writer.writeAttribute(ID_ATTR, clientId+"_upd");
        writer.writeAttribute(NAME_ATTR, clientId+"_upd");
        encodeHiddenSelected(writer, clientId, selected, carousel.getName());
        renderScript(carousel, writer);
        writer.endElement(DIV_ELEM);
        writer.endElement(DIV_ELEM);
    }
    private void renderPagination(ICarousel carousel, IResponseWriter writer,
                                  String value, String id,String ind) throws IOException{
        String call = "ice.mobi.carousel.scrollTo('";
        ClientDescriptor cd = carousel.getClient();
        String eventStr = isTouchEventEnabled(cd) ?
                Constants.TOUCH_START_EVENT : Constants.CLICK_EVENT;
        writer.startElement(DIV_ELEM, carousel);
        writer.writeAttribute(ID_ATTR, id+"_"+ind);
        StringBuilder prevBuilder = new StringBuilder(call).append(id).append("', '").append(ind).append("'); return false");
        writer.writeAttribute(eventStr, prevBuilder.toString());
        writer.writeText(value);
        writer.endElement(DIV_ELEM);
    }
    public void encodeIScrollLib(ICarousel carousel, IResponseWriter writer)
            throws IOException{
        String src = carousel.getIScrollSrc();
        String clientId = carousel.getClientId();
        writer.startElement(SPAN_ELEM, carousel);
        writer.writeAttribute(ID_ATTR, clientId+"_libIScr");
        writer.startElement("script", carousel);
        writer.writeAttribute("text", "text/javascript");
        writer.writeAttribute("src", src);
        writer.endElement("script");
        writer.endElement(SPAN_ELEM);
    }

    /**
     * differs from version in BaseCoreRenderer in that it provides an index to the javascript
     * for update
     * @param writer
     * @param id
     * @param selectedIndex
     * @throws IOException
     */
    private void encodeHiddenSelected(IResponseWriter writer, String id, int selectedIndex,
                                      String name) throws IOException {
        writer.startElement("input");
        writer.writeAttribute("id", id + "_hidden");
        writer.writeAttribute("name", name);
        writer.writeAttribute("type", "hidden");
        writer.writeAttribute("value", String.valueOf(selectedIndex));
        writer.endElement("input");
    }

    /**
     *
     * @param carousel
     * @param writer
     * @throws IOException
     */
    private void renderScript(ICarousel carousel, IResponseWriter writer )
            throws IOException {
        writer.startElement(SPAN_ELEM, carousel);
        String clientId= carousel.getClientId();
        writer.writeAttribute(ID_ATTR, clientId + "_script");
        writer.startElement(SCRIPT_ELEM, null);
        writer.writeAttribute("text", "text/javascript");
        StringBuilder scriptSB = new StringBuilder("setTimeout(function () {ice.mobi.carousel.loaded('");
        scriptSB.append(clientId).append("'");
        StringBuilder cfg = carousel.getJSConfigOptions();
        if (null != cfg){
            scriptSB.append(cfg);
        }
        scriptSB.append(");").append("}, 1);");
        writer.writeText(scriptSB.toString());
       // logger.info("writing script to page -"+scriptSB.toString());
        writer.endElement(SCRIPT_ELEM);
        writer.endElement(SPAN_ELEM);
    }
}
