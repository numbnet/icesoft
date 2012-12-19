package org.icemobile.samples.springbasic;

import java.util.Map;

public class MediaSpotBean {

    private String title;
    private String location;
    private String selection;
    private String fileName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPacked()  {
        return title + "=" + location + "," + fileName;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("properties ");
        sb.append("title=");
        sb.append("'").append(title).append("', ");
        sb.append("location=");
        sb.append("'").append(location).append("', ");
        return sb.toString();
    }
}
