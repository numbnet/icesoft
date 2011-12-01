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

package org.icefaces.samples.showcase.example.compat.menuPopup;

import java.io.Serializable;

public class FormattedWord implements Serializable {
    private String text;
    private String style;
    
    public FormattedWord(String text) {
        this(text, null);
    }
    
    public FormattedWord(String text, String style) {
        this.text = text;
        this.style = style;
    }
    
    public String getText() { return text; }
    public String getStyle() { return style; }
    
    public void setText(String text) { this.text = text; }
    public void setStyle(String style) { this.style = style; }
    
    public String toString() {
        return text;
    }
}
