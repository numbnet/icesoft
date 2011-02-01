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

package org.icefaces.tabset;

import javax.faces.event.ActionEvent;

public class Movie {
    private String title;
    private String director;
    private String genre;
    private String plot;
    private int tabindex;
    private boolean rendered1 = true;
    private boolean rendered2 = true;
    
    public Movie() {
        title = "XYZTitle";
        director = "XYZDirector";
        genre = "XYZGenre";
        plot = "XYZPlot";
    }
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        if (title == null || title.length() <1) return;
        this.title = title;
    }
    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        if (director == null || director.length() <1) return;        
        this.director = director;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        if (genre == null || genre.length() <1) return;        
        this.genre = genre;
    }
    public String getPlot() {
        return plot;
    }
    public void setPlot(String plot) {
        if (plot == null || plot.length() <1) return;        
        this.plot = plot;
    }

    public int getTabindex() {
        return tabindex;
    }

    public void setTabindex(int tabindex) {
        this.tabindex = tabindex;
    }

    public boolean isRendered1() {
        return rendered1;
    }

    public void setRendered1(boolean rendered1) {
        this.rendered1 = rendered1;
    }

    public boolean isRendered2() {
        return rendered2;
    }

    public void setRendered2(boolean rendered2) {
        this.rendered2 = rendered2;
    }
    
    public void showTabs(ActionEvent event) {
        rendered1 = true;
        rendered2 = true;
    }
    
    public void hideTab1(ActionEvent event) {
        rendered1 = false;
    }
    
    public void hideTab2(ActionEvent event) {
        rendered2 = false;
    }    
    
}
