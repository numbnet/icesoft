package org.icefaces.demo.pointme;

import org.icefaces.util.EnvUtils;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;



@ManagedBean(name = "demoBean")
@CustomScoped(value = "#{window}")
public class DemoBean implements Serializable{
    private String autocompleteReturn;
    private String geoLat = "0";
    private String geoLong = "0";
    private String autoLat;
    private String autoLong;
    private String distance;
    private String bearing;
    private String baseURL;
    private String address;
    private List<SearchedPosition> list = new ArrayList<SearchedPosition>();
    private String types;
    private String autoUrl;

    public String getAutocompleteReturn() {
        return autocompleteReturn;
    }

    public void setAutocompleteReturn(String autocompleteReturn) {
        this.autocompleteReturn = autocompleteReturn;
        if (!autocompleteReturn.equals("null")) {
            String trimmed = autocompleteReturn.substring(1, autocompleteReturn.length() - 2);
            String[] split = trimmed.split(", ");
            autoLat = split[0];
            autoLong = split[1];
        }
    }

    public String getGeoLat() {
        return geoLat;
    }

    public void setGeoLat(String geoLat) {
        this.geoLat = geoLat;
    }

    public String getGeoLong() {
        return geoLong;
    }

    public void setGeoLong(String geoLong) {
        this.geoLong = geoLong;
    }

    public String getAutoLat() {
        return autoLat;
    }

    public void setAutoLat(String autoLat) {
        this.autoLat = autoLat;
    }

    public String getAutoLong() {
        return autoLong;
    }

    public void setAutoLong(String autoLong) {
        this.autoLong = autoLong;
    }

    public String getDistance() {
        if (geoLat == null || autoLat == null) {
            distance = "undefined";
            return distance;
        }
        double lat1 = Double.parseDouble(geoLat);
        double lat2 = Double.parseDouble(autoLat);
        double lon1 = Double.parseDouble(geoLong);
        double lon2 = Double.parseDouble(autoLong);
        int R = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        if (d >= 1) {
            BigDecimal rounded = new BigDecimal(Double.toString(d));
            distance = rounded.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + " Km";
            return distance;
        } else {
            BigDecimal rounded = new BigDecimal(Double.toString(d * 1000));
            distance = rounded.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + " m";
            return distance;
        }
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getBearing() {
        if (geoLat == null || autoLat == null) {
            bearing = "undefined";
            return bearing;
        }
        double lat1 = Double.parseDouble(geoLat);
        double lat2 = Double.parseDouble(autoLat);
        double lon1 = Double.parseDouble(geoLong);
        double lon2 = Double.parseDouble(autoLong);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) -
                Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        double brng = Math.toDegrees(Math.atan2(y, x));
        if (brng < 0)
            brng += 360;
        bearing = Double.toString(brng);
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public String getBaseURL() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        String serverName = externalContext.getRequestHeaderMap()
                .get("x-forwarded-host");
        if (null == serverName) {
            serverName = externalContext.getRequestServerName() + ":" +
                    externalContext.getRequestServerPort();
        }
        String url = externalContext.getRequestScheme() + "://" + serverName;
        return url;
    }

    public List<SearchedPosition> getList() {
        list.clear();
        SearchedPosition position = new SearchedPosition();
        position.setLatitude(autoLat);
        position.setLongitude(autoLong);
        position.setLabel((getAddress() + ":" + getDistance()).replace(" ", "_"));
        position.setIcon(makeIcon());
        list.add(position);

        return list;
    }

    public boolean isEnhancedBrowser() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        boolean isEnhanced = EnvUtils.isEnhancedBrowser(facesContext);
        boolean isAuxUpload = EnvUtils.isAuxUploadBrowser(facesContext);
        return isEnhanced || isAuxUpload;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getAutoUrl() {
        return autoUrl;
    }

    public void setAutoUrl(String autoUrl) {
        this.autoUrl = autoUrl;
    }

    public String chooseIcon() {
        if (types != null && !types.equals("null")) {
            String[] typeArray = types.split(",");
            List<String> typeList = Arrays.asList(typeArray);
            if (typeList.contains("lodging"))
                return "lodging.png";
            if (typeList.contains("country"))
                return "country.png";
            if (typeList.contains("locality"))
                return "city.png";
            if (typeList.contains("bar"))
                return "bar.png";
            if (typeList.contains("restaurant"))
                return "restaurant.png";
            if (typeList.contains("art_gallery") || typeList.contains("amusement_park") || typeList.contains("aquarium") || typeList.contains("museum") || typeList.contains("natural_feature") || typeList.contains("zoo") || typeList.contains("point_of_interest"))
                return "sight.png";
            return "pin.png";
        } else
            return "pin.png";
    }

    public String makeIcon() {

        BufferedImage img = null;
        HttpServletRequest myRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String fileLocation = myRequest.getServletContext().getRealPath("images/"+chooseIcon());
        if (!new File(myRequest.getServletContext().getRealPath("tmp")).exists())
            new File(myRequest.getServletContext().getRealPath("tmp")).mkdir();
        try {
            if (address != null && !address.equals("null")) {
                String imgAddress = address.split(",")[0];
                String imgDistance = getDistance();
                img = ImageIO.read(new File(fileLocation));
                Graphics2D g2 = img.createGraphics();
                Font font = Font.createFont(Font.TRUETYPE_FONT, new File(myRequest.getServletContext().getRealPath("images/libre.ttf")));
				font = font.deriveFont(16f);
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics(font);
                int addressWidth = fm.stringWidth(imgAddress);
                int distanceWidth = fm.stringWidth(imgDistance);
                int imgWidth = img.getWidth();
                g2.setColor(new Color(255, 255, 255));
                if (addressWidth >= distanceWidth)
                    g2.fillRect(((imgWidth - addressWidth) / 2) - 10, 65, addressWidth + 20, 40);
                else
                    g2.fillRect(((imgWidth - distanceWidth) / 2) - 10, 65, distanceWidth + 20, 40);
                g2.setColor(new Color(0, 0, 0));
                g2.drawString(imgAddress, (imgWidth - addressWidth) / 2, 80);
                g2.drawString(imgDistance, (imgWidth - distanceWidth) / 2, 100);
                File outputfile = new File(myRequest.getServletContext().getRealPath("tmp/" + myRequest.getSession().getId() + "_icon.png"));
                ImageIO.write(img, "png", outputfile);
                //img.flush();
            }
        } catch (Throwable e) {
            System.out.println("Encountered problem:" + e);
        }

        return myRequest.getServletContext().getContextPath() +
                "/tmp/"+myRequest.getSession().getId()+"_icon.png";
    }

    public class SearchedPosition implements Serializable{
        public String latitude, longitude, label, icon;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}

