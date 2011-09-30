/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icefaces.mobile.util;

import javax.activation.MimetypesFileTypeMap;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: jguglielmin
 * Date: 11-09-27
 * Time: 11:00 AM
 * To change this template use File | Settings | File Templates.
 */


@FacesValidator("org.icefaces.mobile.util.VideoValidator")
public class VideoValidator implements Validator{
    private static final String KEY_FILE = "file";
    private static final String MPEG = "video/mpeg";
    private static final String MOV = "video/mov";
    private static final String GP3 = "video/3gpp";
    private static final String STREAM = "application/octet-stream";

    public VideoValidator(){

    }

    public void validate(FacesContext context, UIComponent component, Object value)
        throws ValidatorException{
          Map compMap = (HashMap)value;
          if (compMap !=null){
            File imageFile = (File) compMap.get(KEY_FILE);
            String filePth = imageFile.getAbsolutePath();
            MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
            String mimeType = mimeTypesMap.getContentType(filePth);
            if (mimeType.equals(MPEG) || mimeType.equals(MOV) || mimeType.equals(GP3)
                    || mimeType.equals(STREAM)){
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
