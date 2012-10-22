package org.icefaces.mobi.component.contactlist;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icemobile.component.IContactList;
import org.icemobile.renderkit.ContactListCoreRenderer;

public class ContactListRenderer extends Renderer {

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriterWrapper writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        ContactListCoreRenderer renderer = new ContactListCoreRenderer();
        renderer.encode((IContactList)uiComponent, writer);
    }

}

