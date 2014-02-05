package org.icemobile.media;


public class VideoBean extends MediaBean{
    private String poster;
    private int height;
    private int width;


    public VideoBean(){
        this.controls=true;
        this.preload="auto";
        this.height=60;
        this.width=200;
        this.src="iPadvideo.mp4";

    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

}
