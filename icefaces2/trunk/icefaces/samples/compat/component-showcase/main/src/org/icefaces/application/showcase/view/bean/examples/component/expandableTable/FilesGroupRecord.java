/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package org.icefaces.application.showcase.view.bean.examples.component.expandableTable;

import java.util.ArrayList;

/**
 * <p>The <code>FilesGroupRecord</code> is responsible for storing a list of
 * child <code>FilesRecords</code>.  It is this list of child records which make
 * up an expandable node in the dataTable.  When a node is expanded its'
 * children are added to the dataTable.</p>
 */
public class FilesGroupRecord extends FilesRecord {

    // list of child FilesRecords.
    protected ArrayList childFilesRecords = new ArrayList(5);

    public ArrayList getChildFilesRecords() {
        return childFilesRecords;
    }

}