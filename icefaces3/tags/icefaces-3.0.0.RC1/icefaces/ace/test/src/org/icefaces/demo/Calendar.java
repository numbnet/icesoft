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

package org.icefaces.demo;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.util.Date;

@ManagedBean
@SessionScoped
public class Calendar {
    private Date selectedDate;

    public Calendar() {
        selectedDate = java.util.Calendar.getInstance().getTime();
        System.out.println("selectedDate.getTimezoneOffset() = " + selectedDate.getTimezoneOffset());
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }
}
