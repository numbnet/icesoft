/*
 * Generated, Do Not Modify
 */
/*
 * Copyright 2010 Prime Technology.
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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.icefaces.component.dateentry;


import org.icefaces.component.utils.Utils;
import org.icefaces.impl.event.FormSubmit;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.util.*;
import java.util.logging.Logger;

public class DateEntry extends DateEntryBase {

      private static Logger logger = Logger.getLogger(DateEntry.class.getName());
      public static final String TCAL_FMT_LONG = "l, F j Y" ;
      public static final String TCAL_FMT_dmY = "d-m-Y" ;
      public static final String TCAL_FMT_Ymd = "Y-m-d";

       public DateEntry() {
        super();
    }


    private Locale appropriateLocale;
    private TimeZone appropriateTimeZone;

    public Locale calculateLocale(FacesContext facesContext) {
        if (appropriateLocale == null) {
            Object userLocale = getLocale();
            if (userLocale != null) {
                if (userLocale instanceof String) {
                    String[] tokens = ((String) userLocale).split("_");
                    if (tokens.length == 1)
                        appropriateLocale = new Locale(tokens[0], "");
                    else
                        appropriateLocale = new Locale(tokens[0], tokens[1]);
                } else if (userLocale instanceof Locale)
                    appropriateLocale = (Locale) userLocale;
                else
                    throw new IllegalArgumentException("Type:" + userLocale.getClass() + " is not a valid locale type for calendar:" + this.getClientId(facesContext));
            } else {
                appropriateLocale = facesContext.getViewRoot().getLocale();
            }
        }

        return appropriateLocale;
    }

    public TimeZone calculateTimeZone() {
        if (appropriateTimeZone == null) {
            Object usertimeZone = getTimeZone();
            if (usertimeZone != null) {
                if (usertimeZone instanceof String)
                    appropriateTimeZone = TimeZone.getTimeZone((String) usertimeZone);
                else if (usertimeZone instanceof TimeZone)
                    appropriateTimeZone = (TimeZone) usertimeZone;
                else
                    throw new IllegalArgumentException("TimeZone could be either String or java.util.TimeZone");
            } else {
                appropriateTimeZone = TimeZone.getDefault();
            }
        }

        return appropriateTimeZone;
    }


    public boolean hasTime() {
        String pattern = getPattern();

        return (pattern != null && pattern.indexOf(":") != -1);
    }



    @Override
    public void queueEvent(FacesEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        String eventName = context.getExternalContext().getRequestParameterMap().get("javax.faces.behavior.event");

 //       if (eventName != null && eventName.equals("dateSelect") && event instanceof AjaxBehaviorEvent) {
 //           customEvents.put("dateSelect", (AjaxBehaviorEvent) event);
 //       } else {
            super.queueEvent(event);
 //       }
    }



    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}