package org.icefaces.mobi.component.getenhanced;

import javax.faces.context.FacesContext;

import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.component.IGetEnhanced;
import org.icemobile.util.ClientDescriptor;
import org.icemobile.util.Constants;

public class GetEnhanced extends GetEnhancedBase implements IGetEnhanced {

    @Override
    public String getICEmobileRegisterSXScript() {
        return MobiJSFUtils.getICEmobileRegisterSXScript();
    }

    @Override
    public ClientDescriptor getClient() {
        return MobiJSFUtils.getClientDescriptor();
    }

    @Override
    public boolean isIOSSmartBannerRendered() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return Boolean.TRUE.equals(facesContext.getAttributes()
                .put(Constants.IOS_SMART_APP_BANNER_KEY, Boolean.TRUE));
    }

}
