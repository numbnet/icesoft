import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.Enumeration;

@ManagedBean (name="demoBean")
@CustomScoped(value = "#{window}")
public class demoBean {
    private String autocompleteReturn;
    private String geoLat = "0";
    private String geoLong = "0";
    private String autoLat;
    private String autoLong;
    private String distance;
    private String bearing;
    private String baseURL;

    public String getAutocompleteReturn() {
        return autocompleteReturn;
    }

    public void setAutocompleteReturn(String autocompleteReturn) {
        this.autocompleteReturn = autocompleteReturn;
        String trimmed = autocompleteReturn.substring(1,autocompleteReturn.length()-2);
        String[] split = trimmed.split(", ");
        autoLat=split[0];
        autoLong=split[1];
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
        if(geoLat==null || autoLat == null){
            distance = "undefined";
            return distance;
        }
        double lat1 = Double.parseDouble(geoLat);
        double lat2 = Double.parseDouble(autoLat);
        double lon1 = Double.parseDouble(geoLong);
        double lon2 = Double.parseDouble(autoLong);
        int R = 6371; // km
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        BigDecimal rounded = new BigDecimal(Double.toString(d));
        distance = rounded.setScale(3,BigDecimal.ROUND_HALF_UP).toString() + " Km";
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getBearing() {
        if(geoLat==null || autoLat == null){
            bearing = "undefined";
            return bearing;
        }
        double lat1 = Double.parseDouble(geoLat);
        double lat2 = Double.parseDouble(autoLat);
        double lon1 = Double.parseDouble(geoLong);
        double lon2 = Double.parseDouble(autoLong);
        double dLon = Math.toRadians(lon2-lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1)*Math.sin(lat2) -
                Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);
        double brng = Math.toDegrees(Math.atan2(y, x));
        if (brng < 0)
            brng += 360;
        bearing = Double.toString(brng);
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }
    public String getBaseURL()  {
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
}