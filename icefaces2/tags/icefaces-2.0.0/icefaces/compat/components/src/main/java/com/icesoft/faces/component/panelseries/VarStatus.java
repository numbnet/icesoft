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

package com.icesoft.faces.component.panelseries;

import java.io.Serializable;

/**
 * @author mcollette
 * @since 1.8
 */
public class VarStatus implements Serializable {
    private int begin;
    private int end;
    private int index;

    VarStatus(int begin, int end, int index) {
        this.begin = begin;
        this.end = end;
        this.index = index;
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public int getIndex() {
        return index;
    }

    public boolean isFirst() {
        return (begin == index);
    }

    public boolean isLast() {
        return (end == index);
    }
}
