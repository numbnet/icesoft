package org.icefaces.mobile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

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
