package org.icefaces.dataView;

import org.icefaces.dataView.data.CarOrder;
import org.icefaces.dataView.data.DataViewFactory2;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class DataViewData implements Serializable
{
    private static final long serialVersionUID = 1L;



    private String idHeaderText="ID",
                   modelHeaderText="Model",
                   costHeaderText="Cost",
                   soldHeaderText="Sold",
                   dateListedHeaderText="Date Listed",
                   imageURLHeaderText="Image",
                   idFooterText="ID",
                   modelFooterText="Model",
                   costFooterText="Cost",
                   soldFooterText="Sold",
                   dateListedFooterText="Date Listed",
                   imageURLFooterText="Image";


    private List<CarOrder> carOrderData = new ArrayList<CarOrder>(DataViewFactory2.CARORDER);



    public String getIdHeaderText() {
        return idHeaderText;
    }

    public void setIdHeaderText(String idHeaderText) {
        this.idHeaderText = idHeaderText;
    }

    public String getModelHeaderText() {
        return modelHeaderText;
    }

    public void setModelHeaderText(String modelHeaderText) {
        this.modelHeaderText = modelHeaderText;
    }

    public String getCostHeaderText() {
        return costHeaderText;
    }

    public void setCostHeaderText(String costHeaderText) {
        this.costHeaderText = costHeaderText;
    }

    public String getSoldHeaderText() {
        return soldHeaderText;
    }

    public void setSoldHeaderText(String soldHeaderText) {
        this.soldHeaderText = soldHeaderText;
    }

    public String getDateListedHeaderText() {
        return dateListedHeaderText;
    }

    public void setDateListedHeaderText(String dateListedHeaderText) {
        this.dateListedHeaderText = dateListedHeaderText;
    }

    public String getImageURLHeaderText() {
        return imageURLHeaderText;
    }

    public void setImageURLHeaderText(String imageURLHeaderText) {
        this.imageURLHeaderText = imageURLHeaderText;
    }

    public String getIdFooterText() {
        return idFooterText;
    }

    public void setIdFooterText(String idFooterText) {
        this.idFooterText = idFooterText;
    }

    public String getModelFooterText() {
        return modelFooterText;
    }

    public void setModelFooterText(String modelFooterText) {
        this.modelFooterText = modelFooterText;
    }

    public String getCostFooterText() {
        return costFooterText;
    }

    public void setCostFooterText(String costFooterText) {
        this.costFooterText = costFooterText;
    }

    public String getSoldFooterText() {
        return soldFooterText;
    }

    public void setSoldFooterText(String soldFooterText) {
        this.soldFooterText = soldFooterText;
    }

    public String getDateListedFooterText() {
        return dateListedFooterText;
    }

    public void setDateListedFooterText(String dateListedFooterText) {
        this.dateListedFooterText = dateListedFooterText;
    }

    public String getImageURLFooterText() {
        return imageURLFooterText;
    }

    public void setImageURLFooterText(String imageURLFooterText) {
        this.imageURLFooterText = imageURLFooterText;
    }

    public List<CarOrder> getCarOrderData() {
        return carOrderData;
    }

    public void setCarOrderData(List<CarOrder> carOrderData) {
        this.carOrderData = carOrderData;
    }

}
