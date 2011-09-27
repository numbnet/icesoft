package org.icefaces.mobile.util;

import org.icefaces.impl.event.FacesMessagesPhaseListener;

import javax.activation.MimetypesFileTypeMap;
import javax.faces.validator.Validator;
import javax.faces.validator.FacesValidator;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import javax.faces.component.UIComponent;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import javax.activation.MimetypesFileTypeMap;
import javax.activation.MimeType;
import java.util.Iterator;


/**
 * Created by IntelliJ IDEA.
 * User: jguglielmin
 * Date: 11-09-27
 * Time: 11:00 AM
 * To change this template use File | Settings | File Templates.
 */


@FacesValidator("org.icefaces.mobile.util.CameraValidator")
public class CameraValidator implements Validator{
    private static final String CAMERA_KEY_FILE = "file";
    private static final String JPEG_FILE = "image/jpeg";
    private static final String PNG_FILE = "image/png";

    public CameraValidator(){

    }

    public void validate(FacesContext context, UIComponent component, Object value)
        throws ValidatorException{
          Map compMap = (HashMap)value;
          if (compMap !=null){
            File imageFile = (File) compMap.get(CAMERA_KEY_FILE);
            String filePth = imageFile.getAbsolutePath();
            MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
            String mimeType = mimeTypesMap.getContentType(filePth);
            if (mimeType.equals(JPEG_FILE) || mimeType.equals(PNG_FILE)){
                 clearFacesMessages(context);
            }else {
                FacesMessage msg = new FacesMessage ("incorrect filetype for camera upload", "only can have JPEG or PNG");
                 msg.setSeverity(FacesMessage.SEVERITY_ERROR);
              throw new ValidatorException (msg);
            }
          }
          else {
              FacesMessage msg = new FacesMessage("cannot validate empty value", "empty hashmap");
              msg.setSeverity(FacesMessage.SEVERITY_ERROR);
              throw new ValidatorException (msg);
          }
    }

    private void clearFacesMessages(FacesContext context){
        Iterator<FacesMessage> msgIt = FacesContext.getCurrentInstance().getMessages();
        while (msgIt.hasNext()){
             msgIt.next();
             msgIt.remove();
        }
    }

}
