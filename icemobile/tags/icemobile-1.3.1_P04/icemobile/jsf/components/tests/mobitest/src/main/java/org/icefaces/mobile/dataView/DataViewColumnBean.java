package org.icefaces.dataView;

import org.icefaces.dataView.data.CarOrder;
import org.icefaces.dataView.data.DataViewFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class DataViewColumnBean implements Serializable
{
    private static final long serialVersionUID = 1L;

    private boolean idRendered=true;
    private boolean modelRendered=true;
    private boolean costRendered=true;
    private boolean soldRendered=true;
    private boolean dateListedRendered=true;

    private boolean idSortable=true;
    private boolean modelSortable=true;
    private boolean costSortable=true;
    private boolean soldSortable=true;
    private boolean dateListedSortable=true;
    private boolean imageSortable=true;

    private boolean imageURLRendered=true;

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
                   imageURLFooterText="Image",
                   idStyleClass,
                   modelStyleClass,
                   costStyleClass,
                   soldStyleClass,
                   dateListedStyleClass,
                   imageURLStyleClass;

    private Integer idReactivePriority=5,
                    modelReactivePriority=4,
                    costReactivePriority=1,
                    soldReactivePriority=6,
                    dateListedReactivePriority=2,
                    imageURLReactivePriority=3;

    private List<CarOrder> carOrderData = new ArrayList<CarOrder>(DataViewFactory.CARORDER);

    private SelectItem[] styleClasses = new SelectItem[]{
        new SelectItem("","None"),
        new SelectItem("blueText"),
        new SelectItem("orangeText")
    };

    public boolean isIdRendered() {
        return idRendered;
    }

    public void setIdRendered(boolean idRendered) {
        this.idRendered = idRendered;
    }

    public boolean isModelRendered() {
        return modelRendered;
    }

    public void setModelRendered(boolean modelRendered) {
        this.modelRendered = modelRendered;
    }


    public boolean isIdSortable() {
        return idSortable;
    }

    public void setIdSortable(boolean idSortable) {
        this.idSortable = idSortable;
    }


    public boolean isImageSortable() {
        return imageSortable;
    }

    public void setImageSortable(boolean imageSortable) {
        this.imageSortable = imageSortable;
    }

    public boolean isDateListedSortable() {
        return dateListedSortable;
    }

    public void setDateListedSortable(boolean dateListedSortable) {
        this.dateListedSortable = dateListedSortable;
    }

    public boolean isSoldSortable() {
        return soldSortable;
    }

    public void setSoldSortable(boolean soldSortable) {
        this.soldSortable = soldSortable;
    }

    public boolean isCostSortable() {
        return costSortable;
    }

    public void setCostSortable(boolean costSortable) {
        this.costSortable = costSortable;
    }

    public boolean isModelSortable() {
        return modelSortable;
    }

    public void setModelSortable(boolean modelSortable) {
        this.modelSortable = modelSortable;
    }

    public boolean isCostRendered() {
        return costRendered;
    }

    public void setCostRendered(boolean costRendered) {
        this.costRendered = costRendered;
    }

    public boolean isSoldRendered() {
        return soldRendered;
    }

    public void setSoldRendered(boolean soldRendered) {
        this.soldRendered = soldRendered;
    }

    public boolean isDateListedRendered() {
        return dateListedRendered;
    }

    public void setDateListedRendered(boolean dateListedRendered) {
        this.dateListedRendered = dateListedRendered;
    }

    public boolean isImageURLRendered() {
        return imageURLRendered;
    }

    public void setImageURLRendered(boolean imageURLRendered) {
        this.imageURLRendered = imageURLRendered;
    }

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

    public String getIdStyleClass() {
        return idStyleClass;
    }

    public void setIdStyleClass(String idStyleClass) {
        this.idStyleClass = idStyleClass;
    }

    public String getModelStyleClass() {
        return modelStyleClass;
    }

    public void setModelStyleClass(String modelStyleClass) {
        this.modelStyleClass = modelStyleClass;
    }

    public String getCostStyleClass() {
        return costStyleClass;
    }

    public void setCostStyleClass(String costStyleClass) {
        this.costStyleClass = costStyleClass;
    }

    public String getSoldStyleClass() {
        return soldStyleClass;
    }

    public void setSoldStyleClass(String soldStyleClass) {
        this.soldStyleClass = soldStyleClass;
    }

    public String getDateListedStyleClass() {
        return dateListedStyleClass;
    }

    public void setDateListedStyleClass(String dateListedStyleClass) {
        this.dateListedStyleClass = dateListedStyleClass;
    }

    public String getImageURLStyleClass() {
        return imageURLStyleClass;
    }

    public void setImageURLStyleClass(String imageURLStyleClass) {
        this.imageURLStyleClass = imageURLStyleClass;
    }

    public Integer getIdReactivePriority() {
        return idReactivePriority;
    }

    public void setIdReactivePriority(Integer idReactivePriority) {
        this.idReactivePriority = idReactivePriority;
    }

    public Integer getModelReactivePriority() {
        return modelReactivePriority;
    }

    public void setModelReactivePriority(Integer modelReactivePriority) {
        this.modelReactivePriority = modelReactivePriority;
    }

    public Integer getCostReactivePriority() {
        return costReactivePriority;
    }

    public void setCostReactivePriority(Integer costReactivePriority) {
        this.costReactivePriority = costReactivePriority;
    }

    public Integer getSoldReactivePriority() {
        return soldReactivePriority;
    }

    public void setSoldReactivePriority(Integer soldReactivePriority) {
        this.soldReactivePriority = soldReactivePriority;
    }

    public Integer getDateListedReactivePriority() {
        return dateListedReactivePriority;
    }

    public void setDateListedReactivePriority(Integer dateListedReactivePriority) {
        this.dateListedReactivePriority = dateListedReactivePriority;
    }

    public Integer getImageURLReactivePriority() {
        return imageURLReactivePriority;
    }

    public void setImageURLReactivePriority(Integer imageURLReactivePriority) {
        this.imageURLReactivePriority = imageURLReactivePriority;
    }

    public List<CarOrder> getCarOrderData() {
        return carOrderData;
    }

    public void setCarOrderData(List<CarOrder> carOrderData) {
        this.carOrderData = carOrderData;
    }

    public SelectItem[] getStyleClasses() {
        return styleClasses;
    }

    public void setStyleClasses(SelectItem[] styleClasses) {
        this.styleClasses = styleClasses;
    }
}
