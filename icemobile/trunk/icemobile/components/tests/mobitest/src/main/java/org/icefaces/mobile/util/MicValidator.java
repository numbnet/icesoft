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


@FacesValidator("org.icefaces.mobile.util.MicValidator")
public class MicValidator implements Validator{
    private static final String COMP_KEY_FILE = "file";
    private static final String WAV = "audio/wav";
    private static final String M4A = "audio/x-m4a";
    private static final String MPEG = "audio/mpeg";
    private static final String AMR= "audio/amr";
    private static final String STREAM = "application/octet-stream";

    public MicValidator(){

    }

    public void validate(FacesContext context, UIComponent component, Object value)
        throws ValidatorException{
          Map compMap = (HashMap)value;
          if (compMap !=null){
            File micFile = (File) compMap.get(COMP_KEY_FILE);
            String filePth = micFile.getAbsolutePath();
            MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
            String mimeType = mimeTypesMap.getContentType(filePth);
            if (mimeType.equals(WAV) || mimeType.endsWith("mp4") ||
                    mimeType.equals(M4A) || mimeType.equals(MPEG) ||
                    mimeType.equals(AMR) || mimeType.equals(STREAM)){

                 clearFacesMessages(context);
            }else {
                FacesMessage msg = new FacesMessage ("incorrect filetype for mic upload", "valid files are wav, mp4, x-m4a, mpeg, amr");
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
