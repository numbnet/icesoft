package org.icemobile.jsp.tags;

import java.io.IOException;
import java.util.logging.Logger;

import org.icemobile.component.IGetEnhanced;
import org.icemobile.jsp.util.MobiJspConstants;
import org.icemobile.renderkit.GetEnhancedCoreRenderer;
import org.icemobile.util.Constants;
import org.icemobile.util.SXUtils;

public class GetEnhancedTag extends BaseSimpleTag implements IGetEnhanced{
	
	private boolean includeLink = true;
	private String androidMsg;
	private String iosMsg;
	private String blackberryMsg;
	
	private static Logger log = Logger.getLogger(GetEnhancedTag.class.getName());
	
	public void doTag() throws IOException {
		GetEnhancedCoreRenderer renderer = new GetEnhancedCoreRenderer();
        renderer.encode(this, new TagWriter(getContext()));
	}

	public boolean isIncludeLink() {
		return includeLink;
	}

	public void setIncludeLink(boolean includeLink) {
		this.includeLink = includeLink;
	}

	public String getAndroidMsg() {
		return androidMsg;
	}

	public void setAndroidMsg(String androidMsg) {
		this.androidMsg = androidMsg;
	}

	public String getIosMsg() {
		return iosMsg;
	}

	public void setIosMsg(String iosMsg) {
		this.iosMsg = iosMsg;
	}

	public String getBlackberryMsg() {
		return blackberryMsg;
	}

	public void setBlackberryMsg(String blackberryMsg) {
		this.blackberryMsg = blackberryMsg;
	}

    public String getICEmobileRegisterSXScript() {
        return SXUtils.getICEmobileRegisterSXScript(getRequest(),MobiJspConstants.SX_UPLOAD_PATH);
    }

    public boolean isIOSSmartBannerRendered() {
        return Boolean.TRUE.equals(getContext().getAttribute(Constants.IOS_SMART_APP_BANNER_KEY));
    }
	
    public void release(){
        super.release();
        includeLink = true;
        androidMsg = null;
        iosMsg = null;
        blackberryMsg = null;
    }
}
