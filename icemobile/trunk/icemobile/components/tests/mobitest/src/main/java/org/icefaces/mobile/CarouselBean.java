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

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

@ManagedBean(name="carousel")
@SessionScoped
public class CarouselBean implements Serializable {
	private static final Logger logger =
        Logger.getLogger(CarouselBean.class.toString());	
	int carouselLength;
    private List<ImageItem> imageList = new ArrayList<ImageItem>() ;

    private int selectedIndex;
	private String height="98px";
	private String style="display:inline-block;position:relative;top:-25px;left:0;color:white;";

	
	public CarouselBean(){
        imageList.add(new ImageItem("../images/DSC_1.jpg", "Pic One", height, style));
        imageList.add(new ImageItem ("../images/DSC_2.JPG", "Pic Two", height, style));
        imageList.add(new ImageItem ("../images/DSC_3.JPG", "Pic Three", height, style));
        imageList.add(new ImageItem ("../images/DSC_4.JPG", "Pic Four", height, style));
        imageList.add(new ImageItem ("../images/DSC_5.JPG", "Pic Five", height, style));
        imageList.add(new ImageItem ("../images/DSC_6.JPG", "Pic Six", height, style));

	}
	
    public int getCarouselLength() {
		return imageList.size();
	}

	public void setCarouselLength(int carouselLength) {
		this.carouselLength = carouselLength;
	}

	public List<ImageItem> getImageList() {
		return imageList;
	}

	public void setImageList(List<ImageItem> imageList) {
		this.imageList = imageList;
	}
         public void changedIndex(ValueChangeEvent vce){
            System.out.println(" new value is "+ vce.getNewValue().toString());
        }
    public void setSelectedIndex(int sel){
        this.selectedIndex = sel;
    }
    public int getSelectedIndex(){
        return this.selectedIndex;
    }


	public class ImageItem implements Serializable{
    	private String imageUrl;
    	private String title;
    	private String height;
    	private String style;
    	
    	public ImageItem (String url, String title, String height, String style){
    		this.imageUrl = url;
    		this.title = title;
    		this.height= height;
    		this.style = style;
    	}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getHeight() {
			return height;
		}

		public void setHeight(String height) {
			this.height = height;
		}

		public String getStyle() {
			return style;
		}

		public void setStyle(String style) {
			this.style = style;
		}


    }


}
