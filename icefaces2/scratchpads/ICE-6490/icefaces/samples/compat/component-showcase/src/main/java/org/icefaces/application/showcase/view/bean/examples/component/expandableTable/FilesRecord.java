/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.application.showcase.view.bean.examples.component.expandableTable;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * <p>The <code>FilesRecord</code> class contains the base information for an
 * inventory entry in a data table.  This class is meant to represent a model
 * and should only contain base inventory data</p>
 * <p/>
 * <p>The class instance variables are a direct map from the original ascii
 * expandable table specification. </p>
 */
public class FilesRecord implements Serializable{

    // simple list of files records.
    protected String description = "";
    protected String modified = "";
    protected String created = "";
    protected String size = "";
    protected String kind = "";
    protected String version = "";
    protected GregorianCalendar date;
    protected int quantity;
    protected double price;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}