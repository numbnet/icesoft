/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icemobile.context;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ACECompatibleExternalContextFactory extends ExternalContextFactory {
    private static Logger log =
        Logger.getLogger(
                ACECompatibleExternalContextFactory.class.getName());
    ExternalContextFactory wrapped;

    public ACECompatibleExternalContextFactory(
            ExternalContextFactory wrapped)  {
        this.wrapped = wrapped;
        log.info("ACE FileEntry request wrapping disabled.");
    }

    public ExternalContext getExternalContext(
            Object context, Object request, Object response)  {
        return new ACECompatibleExternalContext(
            wrapped.getExternalContext(context, request, response));
    }

}