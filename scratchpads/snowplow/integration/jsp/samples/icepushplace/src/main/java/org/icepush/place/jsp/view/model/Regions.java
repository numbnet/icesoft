/*
 *
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
 *
 */

package org.icepush.place.jsp.view.model;

import java.util.Vector;
import java.util.List;

public class Regions {

    private Vector northAmerica;
    private Vector europe;
    private Vector southAmerica;
    private Vector asia;
    private Vector africa;
    private Vector antarctica;

    public Regions(){
        northAmerica = new Vector();
        europe = new Vector();
        southAmerica = new Vector();
        asia = new Vector();
        africa = new Vector();
        antarctica = new Vector();
    }

    public List getNorthAmerica() {
        return northAmerica;
    }

    public List getEurope() {
        return europe;
    }

    public List getSouthAmerica() {
        return southAmerica;
    }

    public List getAsia() {
        return asia;
    }

    public Vector getAfrica() {
        return africa;
    }

    public List getAntarctica() {
        return (List)antarctica;
    }
}
