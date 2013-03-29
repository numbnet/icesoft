package org.icefaces.mobile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean (name="audioList")
@ViewScoped
public class AudioListBean implements Serializable{
    private List<AudioBean> playList = new ArrayList<AudioBean>();
    private int rows;
    
    
    public AudioListBean() {
        playList.add(new AudioBean(1, "Learn Japanese", "../audio/japanese.mp3"));
        playList.add(new AudioBean(2, "Drew Carey", "../audio/hatejob.wav"));
        playList.add(new AudioBean(3, "Wake me Up Before You Go-Go", "../audio/WhamGoGo.mp3"));
        rows = playList.size();
    }


    public int getRows() {
        return rows;
    }


	public List<AudioBean> getPlayList() {
		return playList;
	}


	public void setPlayList(List<AudioBean> playList) {
		this.playList = playList;
	}


}