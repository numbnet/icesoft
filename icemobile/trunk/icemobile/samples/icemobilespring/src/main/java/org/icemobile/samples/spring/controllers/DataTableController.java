/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icemobile.samples.spring.controllers;

import javax.servlet.http.HttpServletRequest;

import org.icemobile.samples.spring.DataTableBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"dataTableBean"})
public class DataTableController {
    
    @ModelAttribute("dataTableBean")
    public DataTableBean createDataTableBean(HttpServletRequest request) {
        DataTableBean bean = new DataTableBean();
        return bean;
    }
    
    @RequestMapping(value = "/datatable")
    public void get(Model model,
            @ModelAttribute("dataTableBean") DataTableBean dataTableBean) {
        model.addAttribute("dataTableBean", dataTableBean);
    }

}
