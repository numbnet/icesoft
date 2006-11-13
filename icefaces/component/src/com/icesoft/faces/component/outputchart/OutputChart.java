/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

package com.icesoft.faces.component.outputchart;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.HtmlCommandButton;
import com.icesoft.faces.component.ext.renderkit.FormRenderer;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.context.DOMResponseWriter;
import org.krysalis.jcharts.Chart;
import org.krysalis.jcharts.imageMap.ImageMapArea;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.beans.Beans;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

public class OutputChart extends HtmlCommandButton {

    public static String AREA_CHART_TYPE = "area";
    public static String AREA_STACKED_CHART_TYPE = "areastacked";
    public static String BAR_CHART_TYPE = "bar";
    public static String BAR_CLUSTERED_CHART_TYPE = "barclustered";
    public static String BAR_STACKED_CHART_TYPE = "barstacked";
    public static String LINE_CHART_TYPE = "line";
    public static String PIE2D_CHART_TYPE = "pie2D";
    public static String PIE3D_CHART_TYPE = "pie3D";
    public static String POINT_CHART_TYPE = "point";
    public static String STOCK_CHART_TYPE = "stock";
    public static String CUSTOM_CHART_TYPE = "custom";
    public static String DEFAULT_CHART_TYPE = BAR_CHART_TYPE;

    private static int COMPONENT_ID = 0;
    private static int CLIENT_SIDE_IMAGE_MAP_KEY = 1;
    private static String DEFAULT_HEIGHT = "400";
    private static String DEFAULT_WIDTH = "400";

    private static String DEFAULT_CHART_TITLE = "Default Chart title";
    private static String DEFAULT_YAXIS_TITLE = "Default Y title";
    private static String DEFAULT_XAXIS_TITLE = "Default X title";
    private static String DEFAULT_DATA = "20, 30, 40";
    static String ICE_CHART_COMPONENT = "iceChartComponent";
    private int imageCounter = 0;
    private AbstractChart abstractChart;
    private String width;
    private String height;

    private boolean render = false;
    private FileOutputStream out;

    private String chartTitle;
    private Object data;
    private Object labels;
    private Object legendLabel;
    private Object colors;
    private Object shapes;

    private Object xaxisLabels;
    private String xaxisTitle;
    private String yaxisTitle;
    private String style = null;
    private String styleClass = null;

    File folder = null;

    public OutputChart() {
        setRendererType("com.icesoft.faces.OutputChartRenderer");
    }


    /**
     *<p>Return the value of the <code>labels</code> property.</p> 
     */
    public Object getLabels() {
        if (labels != null) {
            return labels;
        }
        ValueBinding vb = getValueBinding("labels");
        return vb != null ? vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>labels</code> property. </p>
     */
    public void setLabels(Object labels) {
        this.labels = labels;
    }

    /**
     *<p>Return the value of the <code>legendLabel</code> property.</p> 
     */
    public Object getLegendLabel() {
        if (legendLabel != null) {
            return legendLabel;
        }
        ValueBinding vb = getValueBinding("legendLabel");
        return vb != null ? vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>legendLabel</code> property. </p>
     */
    public void setLegendLabel(Object legendLabel) {
        this.legendLabel = legendLabel;
    }

    /**
     *<p>Return a boolean flag which tells if the chart can have a 
     *clientSideImageMap or not.</p> 
     */
    public boolean isClientSideImageMap() {
        if (getActionListener() != null &&
            (!getType().equalsIgnoreCase(AREA_CHART_TYPE) &&
             !getType().equalsIgnoreCase(AREA_STACKED_CHART_TYPE))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *<p>Return the value of the <code>data</code> property.</p> 
     */
    public Object getData() {
        if (data != null) {
            return data;
        }
        ValueBinding vb = getValueBinding("data");
        if (vb != null) {
            return vb.getValue(getFacesContext());
        } else {
            setChartTitle(getChartTitle() + " with default data");
            return DEFAULT_DATA;
        }
    }

    /**
     * <p>Set the value of the <code>data</code> property. </p>
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     *<p>Return the value of the <code>colors</code> property.</p> 
     */
    public Object getColors() {
        if (colors != null) {
            return colors;
        }
        ValueBinding vb = getValueBinding("colors");
        return vb != null ? vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>colors</code> property. </p>
     */
    public void setColors(Object colors) {
        this.colors = colors;
    }

    /**
     *<p>Return the value of the <code>shapes</code> property.</p> 
     */    
    public Object getShapes() {
        if (shapes != null) {
            return shapes;
        }
        ValueBinding vb = getValueBinding("shapes");
        return vb != null ? vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>shapes</code> property. </p>
     */
    public void setShapes(Object shapes) {
        this.shapes = shapes;
    }

    /**
     *<p>Return the value of the <code>xaxisTitle</code> property.</p> 
     */  
    public String getXaxisTitle() {
        if (xaxisTitle != null) {
            return xaxisTitle;
        }
        ValueBinding vb = getValueBinding("xaxisTitle");
        return vb != null ? (String) vb.getValue(getFacesContext()) :
               DEFAULT_XAXIS_TITLE;
    }

    /**
     * <p>Set the value of the <code>xaxisTitle</code> property. </p>
     */
    public void setXaxisTitle(String xaxisTitle) {
        this.xaxisTitle = xaxisTitle;
    }

    /**
     *<p>Return the value of the <code>yaxisTitle</code> property.</p> 
     */
    public String getYaxisTitle() {
        if (yaxisTitle != null) {
            return yaxisTitle;
        }
        ValueBinding vb = getValueBinding("yaxisTitle");
        return vb != null ? (String) vb.getValue(getFacesContext()) :
               DEFAULT_YAXIS_TITLE;
    }

    /**
     * <p>Set the value of the <code>yaxisTitle</code> property. </p>
     */
    public void setYaxisTitle(String yaxisTitle) {
        this.yaxisTitle = yaxisTitle;
    }

    /**
     *<p>Return the value of the <code>xaxisLabels</code> property.</p> 
     */
    public Object getXaxisLabels() {
        if (xaxisLabels != null) {
            return xaxisLabels;
        }
        ValueBinding vb = getValueBinding("xaxisLabels");
        return vb != null ? vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>xaxisLabels</code> property. </p>
     */
    public void setXaxisLabels(Object xaxisLabels) {
        this.xaxisLabels = xaxisLabels;
    }

    /**
     *<p>Return the value of the <code>chartTitle</code> property.</p> 
     */
    public String getChartTitle() {
        if (chartTitle != null) {
            return chartTitle;
        }
        ValueBinding vb = getValueBinding("chartTitle");
        return vb != null ? (String) vb.getValue(getFacesContext()) :
               DEFAULT_CHART_TITLE;
    }

    /**
     * <p>Set the value of the <code>chartTitle</code> property. </p>
     */
    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }

    /**
     *<p>Return the value of the <code>width</code> property.</p> 
     */
    public String getWidth() {
        if (width != null) {
            return width;
        }
        ValueBinding vb = getValueBinding("width");
        return vb != null ? (String) vb.getValue(getFacesContext()) :
               DEFAULT_WIDTH;
    }

    /**
     * <p>Set the value of the <code>width</code> property. </p>
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     *<p>Return the value of the <code>height</code> property.</p> 
     */
    public String getHeight() {
        if (height != null) {
            return height;
        }
        ValueBinding vb = getValueBinding("height");
        return vb != null ? (String) vb.getValue(getFacesContext()) :
               DEFAULT_HEIGHT;
    }

    /**
     * <p>Set the value of the <code>height</code> property. </p>
     */
    public void setHeight(String height) {
        this.height = height;
    }

    private Boolean renderOnSubmit;
    private MethodBinding renderOnSubmitMethodBinding;

    public void setRenderOnSubmit(Object renderOnSubmit) {
        if (renderOnSubmit instanceof Boolean) {
            this.renderOnSubmit = (Boolean) renderOnSubmit;
        } else if (renderOnSubmit instanceof MethodBinding) {
            this.renderOnSubmitMethodBinding = (MethodBinding) renderOnSubmit;
        }
    }

    public Object getRenderOnSubmit() {
        if (renderOnSubmit != null) {
            return renderOnSubmit;
        }
        if (renderOnSubmitMethodBinding != null) {
            return ((Boolean) renderOnSubmitMethodBinding
                    .invoke(getFacesContext(), new Object[]{this}));
        }
        return Boolean.FALSE;
    }
    
    public void setValueBinding(String s, ValueBinding vb) {
        if (s != null && s.equals("renderOnSubmit")) {
            MethodBinding mb =
                    getFacesContext().getApplication().createMethodBinding(
                            vb.getExpressionString(),
                            new Class[]{OutputChart.class});
            setRenderOnSubmit(mb);
        } else {
            super.setValueBinding(s, vb);
        }
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.component.UIComponent#encodeBegin(javax.faces.context.FacesContext)
     */
    public void encodeBegin(FacesContext context) throws IOException {
    	
    	if(!Beans.isDesignTime()){
	        try {
	            if (abstractChart == null) {
	                abstractChart = AbstractChart.createChart(this);
	                if (getType().equalsIgnoreCase(OutputChart.CUSTOM_CHART_TYPE)) {
	                    getRenderOnSubmit();
	                }
	                abstractChart.encode();
	            } else if (((Boolean) getRenderOnSubmit()).booleanValue()) {
	                abstractChart.encode();
	            }
	        } catch (Throwable e) {
	            e.printStackTrace();
	        }
    	}
        super.encodeBegin(context);
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.component.UIComponent#broadcast(javax.faces.event.FacesEvent)
     */
    public void broadcast(FacesEvent event)
            throws AbortProcessingException {
        super.broadcast(event);
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.component.UIComponent#decode(javax.faces.context.FacesContext)
     */
    public void decode(FacesContext context) {

        Map requestParameterMap =
                context.getExternalContext().getRequestParameterMap();
        String chartComponentRequestIdentifier = (String) requestParameterMap
                .get(OutputChart.ICE_CHART_COMPONENT);
        if (chartComponentRequestIdentifier != null) {
            String[] submittedValue =
                    chartComponentRequestIdentifier.split("id-key");
            if (submittedValue[COMPONENT_ID].equals(getClientId(context))) {
                ImageMapArea area = (ImageMapArea) getGeneratedImageMapArea()
                        .get(submittedValue[CLIENT_SIDE_IMAGE_MAP_KEY]);
                if (area != null) {
                    setClickedImageMapArea(area);
                    queueEvent(new ActionEvent(this));
                }
            }
        }
        super.decode(context);
    }

    private String getChartFileName() {
        HttpServletRequest req =
                (HttpServletRequest) ((ExternalContext) FacesContext
                        .getCurrentInstance().getExternalContext())
                        .getRequest();
        return this.getType() + this
                .getClientId(FacesContext.getCurrentInstance())
                .replaceAll(":", "") + req.getRequestedSessionId() +
                                     imageCounter++ + ".jpeg";
    }

    /**
     *<p>Return the the path where the chart is stored.</p> 
     */
    public String getPath() {
        String folder = "/";
        if (getFacesContext() != null &&
            getFacesContext().getExternalContext() != null) {
            if (DOMResponseWriter.isStreamWriting()) {
                folder = "./web/chart/images";
            } else {
                folder =
                        ((ServletContext) getFacesContext().getExternalContext()
                                .getContext()).getRealPath("charts");
            }
        }
        return folder;
    }
    /**
     *<p>Return the value of the <code>chart</code> property.</p> 
     */
    public Chart getChart() {
        return abstractChart.getChart();
    }

    /**
     * <p>Set the value of the <code>chart</code> property. </p>
     */
    public void setChart(Chart chart) {
        abstractChart.setChart(chart);
    }

    public void render() {
        render = true;
    }
    
    public boolean isRender() {
        return render;
    }

    private String type = null;

    /**
     * <p>Set the value of the <code>type</code> property. </p>
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *<p>Return the value of the <code>type</code> property.</p> 
     */
    public String getType() {
        if (type != null) {
            return type;
        }
        ValueBinding vb = getValueBinding("type");
        return vb != null ? (String) vb.getValue(getFacesContext()) :
               DEFAULT_CHART_TYPE;
    }

    private String fileName = null;

    /**
     *<p>Return the value of the <code>fileName</code> property.</p> 
     */
    public String getFileName() {
        return fileName;
    }

    private File imageFile;

    /**
     *<p>Return the value of the <code>data</code> property.</p> 
     */
    OutputStream getNewOutputStream() {
        //removed the old image, if exist
        if (imageFile != null) {
            imageFile.delete();
        }
        imageFile = new File(getFolder(), getChartFileName());
        fileName = imageFile.getName();
        try {
            return out = new FileOutputStream(imageFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    File getFolder() {
        if (folder == null) {
            folder = new File(getPath());
            if (folder != null) {
                if (!folder.exists()) {
                    folder.mkdirs();
                }
            }
        }
        return folder;
    }

    /**
     *<p>Return the value of the <code>data</code> property.</p> 
     */
    public OutputStream getOutputStream() {
        return out;
    }

    Map getGeneratedImageMapArea() {
        return abstractChart.getGeneratedImageMapArea();
    }

    /**
     *<p>Return the value of the <code>data</code> property.</p> 
     */
    public ImageMapArea getClickedImageMapArea() {
        return abstractChart.getClickedImageMapArea();
    }

    public void setClickedImageMapArea(ImageMapArea clickedImageMapArea) {
        abstractChart.setClickedImageMapArea(clickedImageMapArea);
    }

    void generateClientSideImageMap(DOMContext domContext, Element map) {
        if (isClientSideImageMap()) {
            Iterator area = getGeneratedImageMapArea().values().iterator();
            while (area.hasNext()) {
                ImageMapArea areaMap = (ImageMapArea) area.next();
                Text areaNode = domContext.createTextNode(areaMap.toHTML(
                        "title ='" + areaMap.getLengendLabel() +
                        "' href=\"return false;\" onclick=\"document.forms['" +
                        getParentFormId() + "']['" + ICE_CHART_COMPONENT +
                        "'].value='" + getClientId(getFacesContext()) +
                        "id-key" + areaMap.hashCode() +
                        "';iceSubmitPartial(document.forms['" +
                        getParentFormId() + "'],this,event); return false;\""));
                map.appendChild(areaNode);
            }
        } else {
            //logging client side image Map was not enabled
        }
    }

    //cache parentFormId, if ClientSideImageMap is required
    private String parentFormId;

    private String getParentFormId() {
        if (parentFormId != null) {
            return parentFormId;
        }
        UIComponent uiForm = FormRenderer.findForm(this);
        if (uiForm == null) {
            //TODO logging and exception
            //The component must have to be a decendent of the for element, inorder to use clientSideImageMap
            return null;
        }
        return (parentFormId = uiForm.getClientId(getFacesContext()));
    }

    /**
     * <p>Set the value of the <code>styleClass</code> property.</p>
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     */
    public String getStyleClass() {
        if (styleClass != null) {
            return styleClass;
        }
        ValueBinding vb = getValueBinding("styleClass");
        return vb != null ? (String) vb.getValue(getFacesContext()) :
               CSS_DEFAULT.OUTPUT_CHART_DEFAULT_STYLE_CLASS;
    }

    /**
     * <p>Set the value of the <code>style</code> property.</p>
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * <p>Return the value of the <code>style</code> property.</p>
     */
    public String getStyle() {
        if (style != null) {
            return style;
        }
        ValueBinding vb = getValueBinding("style");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }
}

