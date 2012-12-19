/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

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