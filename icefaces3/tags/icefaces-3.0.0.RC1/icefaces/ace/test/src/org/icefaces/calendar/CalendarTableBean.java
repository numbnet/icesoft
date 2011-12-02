/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
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
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.icefaces.calendar;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.util.List;
import java.util.ArrayList;

@ManagedBean
@SessionScoped
public class CalendarTableBean {
    private List calendars = new ArrayList();

    public CalendarTableBean() {
        calendars.add(new CalendarBean());
        calendars.add(new CalendarBean());
        calendars.add(new CalendarBean());
    }

    public List getCalendars() {
        return calendars;
    }

    public void setCalendars(List calendars) {
        this.calendars = calendars;
    }
}