package org.icemobile.client.blackberry.script.debug;

import java.util.Hashtable;

import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import net.rim.device.api.amms.control.camera.EnhancedFocusControl;
import net.rim.device.api.barcodelib.BitmapLuminanceSource;
import net.rim.device.api.script.ScriptableFunction;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

import org.icemobile.client.blackberry.ICEmobileContainer;
import org.icemobile.client.blackberry.utils.NameValuePair;
import org.icemobile.client.blackberry.utils.UploadUtilities;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

public class JavascriptDebugger extends ScriptableFunction {
    

    public JavascriptDebugger() { 
    }   

    /**
     *  invoke the call from javascript layer. The purpose of this is to 
     *  log messages from the javascript layer into the standard blackberry logger
     */
    public  Object invoke( Object thiz, Object[] args) { 
        
        String message = (String) args[0]; 
        ICEmobileContainer.DEBUG(message);
        return Boolean.TRUE;
    }

}