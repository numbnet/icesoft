package com.icesoft.faces.component.dataexporter;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.icesoft.faces.application.D2DViewHandler;
import com.icesoft.faces.component.ext.RowSelector;
import com.icesoft.faces.component.ext.UIColumn;
import com.icesoft.faces.component.outputresource.OutputResource;
import com.icesoft.faces.context.FileResource;
import com.icesoft.faces.context.Resource;
import com.icesoft.faces.context.effects.JavascriptContext;

public class DataExporter extends OutputResource {
    public static final String COMPONENT_FAMILY = "com.icesoft.faces.DataExporter";
	public static final String COMPONENT_TYPE = "com.icesoft.faces.DataExporter";
	public static final String DEFAULT_RENDERER_TYPE = "com.icesoft.faces.DataExporterRenderer";
	private final Log log = LogFactory.getLog(DataExporter.class);
	
    private static final OutputTypeHandler NoopOutputHandler = new OutputTypeHandler("no-data") {
        public void writeHeaderCell(String text, int col) {
        }

        public void writeCell(Object output, int col, int row) {
        }

        public void flushFile() {
        }
    };

	private boolean readyToExport = false;

	private String _for;
	private String type;
	private String clickToCreateFileText;
	private String _origType;
	private transient OutputTypeHandler outputTypeHandler;
	private String _origFor;
	private transient OutputTypeHandler _origOutputTypeHandler;
	private transient int _origDataModelHash = 0;
	public final static String EXCEL_TYPE = "excel";
	public final static String CSV_TYPE = "csv";
	private Boolean ignorePagination;

	public DataExporter() {
	}
	
	   /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.UIComponent#getFamily()
     */
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.UIComponent#getRendererType()
     */
    public String getRendererType() {
        return DEFAULT_RENDERER_TYPE;
    }
    
	public UIData getUIData() {
		String forStr = getFor();
		UIData forComp = (UIData)D2DViewHandler.findComponent(forStr, this);

		if (forComp == null) {
			throw new IllegalArgumentException(
					"could not find UIData referenced by attribute @for = '"
							+ forStr + "'");
		} else if (!(forComp instanceof UIData)) {
			throw new IllegalArgumentException(
					"uiComponent referenced by attribute @for = '" + forStr
							+ "' must be of type " + UIData.class.getName()
							+ ", not type " + forComp.getClass().getName());
		}
		//compare with cached DataModel to check for updates
		if( _origDataModelHash != 0 && _origDataModelHash != forComp.getValue().hashCode()){
			reset();
		}
		Object value = forComp.getValue();
		if (null != value) {
		    _origDataModelHash = forComp.getValue().hashCode();
		}
		return forComp;
	}
	
	private void reset(){
		this.readyToExport = false;
		this.resource = null;
	}
	
	public String getFor() {
		if (_for != null) {
			if( !_for.equals(this._origFor))
				reset();
			this._origFor = _for;
			return _for;
		}
		ValueBinding vb = getValueBinding("for");
		String newFor = null;
		if( vb != null ){
			newFor = (String) vb.getValue(getFacesContext());
			if( newFor != null && !newFor.equals(this._origFor))
				reset();
			this._origFor = newFor;
		}
		
		return newFor;
	}

	public void setFor(String forValue) {
		if( forValue != null && !forValue.equals(_for))
			this.resource = null;
		_for = forValue;
	}
	
	public String getType(){
		if (type != null) {
			if( !type.equals(this._origType))
				reset();
			this._origType = type;
			return type;
		}
		ValueBinding vb = getValueBinding("type");
		String newType = null;
		if( vb != null ){
			newType = (String) vb.getValue(getFacesContext());
			if( newType != null && !newType.equals(this._origType))
				reset();
			this._origType = newType;
		}
		return newType;
	}
	
	public void setType(String type){
		if( type != null && !type.equals(this.type))
			reset();
		this.type = type;
	}

	public boolean isReadyToExport() {
		return readyToExport;
	}

	public void setReadyToExport(boolean readyToExport) {
		this.readyToExport = readyToExport;
	}
	
	/**
	 * @deprecated
	 */
	public String getClickToCreateFileText() {
		if (this.clickToCreateFileText != null) {
			return clickToCreateFileText;
		}
		ValueBinding vb = getValueBinding("clickToCreateFileText");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

     /**
     * @deprecated
     */
	public void setClickToCreateFileText(String clickToCreateFileText) {
		this.clickToCreateFileText = clickToCreateFileText;
	}

	public OutputTypeHandler getOutputTypeHandler() {
		ValueBinding vb = getValueBinding("outputTypeHandler");
		OutputTypeHandler newOutputHandler = null;
		if( vb != null ){
			newOutputHandler = (OutputTypeHandler) vb.getValue(getFacesContext());
			if( newOutputHandler != null && newOutputHandler != this._origOutputTypeHandler)
				reset();
			this._origOutputTypeHandler = newOutputHandler;
		}
		return newOutputHandler;
	}

	public void setOutputTypeHandler(OutputTypeHandler outputTypeHandler) {
		if( outputTypeHandler != null && outputTypeHandler != this.outputTypeHandler)
			reset();
		this.outputTypeHandler = outputTypeHandler;
	}
	
    private transient Object values[];
    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext)
     */
    public Object saveState(FacesContext context) {

        if(values == null){
            values = new Object[8];
        }
        values[0] = super.saveState(context);
        values[1] = _for;
        values[2] = type;
        values[3] = clickToCreateFileText;
        values[4] = readyToExport? Boolean.TRUE : Boolean.FALSE;
        values[5] = _origType;
        values[6] = _origFor;
        values[7] = ignorePagination;        
        return ((Object) (values));
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.FacesContext,
     *      java.lang.Object)
     */
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _for = (String) values[1];
        type = (String) values[2];
        clickToCreateFileText = (String) values[3];
        readyToExport = ((Boolean) values[4]).booleanValue();        
        _origType = (String) values[5];
        _origFor = (String)values[6];
        ignorePagination = (Boolean)values[7];        
    }
    
    public String getLabel() {
        String label = super.getLabel();
        if (label == null) {
            if (resource instanceof FileResource)
            label = ((FileResource)resource).getFile().getName();
        }
        return label;
    }
	
    public void broadcast(FacesEvent event)
    throws AbortProcessingException {
        super.broadcast(event);
        if (event != null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String type = getType();
            Resource res = getResource(); 
            if (res == null) {
                File output = createFile(facesContext, type);
                setResource(new FileResource(output));
                getResource();
            }
            JavascriptContext.addJavascriptCall(facesContext, "window.open('" + getPath() + "');");
        }
    }    

    private File createFile(FacesContext fc, String type) {
        UIData uiData = getUIData();
        OutputTypeHandler outputHandler = null;
        ServletContext context = ((HttpSession) FacesContext
                .getCurrentInstance().getExternalContext().getSession(false))
                .getServletContext();

        File exportDir = new File(context.getRealPath("/export"));
        if (!exportDir.exists())
            exportDir.mkdirs();
        String pathWithoutExt = context.getRealPath("/export") + "/export_"
                + new Date().getTime();

        if (getOutputTypeHandler() != null)
            outputHandler = getOutputTypeHandler();
        else if (DataExporter.EXCEL_TYPE.equals(getType())) {
            outputHandler = new ExcelOutputHandler(pathWithoutExt + ".xls",
                    fc, uiData.getId());
        } else if (DataExporter.CSV_TYPE.equals(getType())) {
            outputHandler = new CSVOutputHandler(pathWithoutExt + ".csv");
        } else {
            outputHandler = NoopOutputHandler;
        }
        renderToHandler(outputHandler, uiData, fc);
        setMimeType(outputHandler.getMimeType());

        return outputHandler.getFile();
    }

    private String encodeParentAndChildrenAsString(FacesContext fc,
            UIComponent uic) {
        StringBuilder str = new StringBuilder();
        Object value = uic.getAttributes().get("value");
        if (value != null)
            str.append(value);
        else {
            ValueBinding vb = uic.getValueBinding("value");
            if (vb != null)
                str.append(vb.getValue(fc));
        }

        if (uic.getChildCount() > 0) {
            Iterator iter = uic.getChildren().iterator();
            while (iter.hasNext()) {
                UIComponent child = (UIComponent) iter.next();
                str.append(encodeParentAndChildrenAsString(fc, child));
            }
        }
        return str.toString();
    }

    protected List getRenderedChildColumnsList(UIComponent component) {
        List results = new ArrayList();
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if ((kid instanceof UIColumn) && kid.isRendered()) {
                results.add(kid);
            }
        }
        return results;
    }

    private void renderToHandler(OutputTypeHandler outputHandler,
            UIData uiData, FacesContext fc) {

        try {
            int rowIndex = 0;
            int numberOfRowsToDisplay = 0;  
            if (!isIgnorePagination()) {
                rowIndex = uiData.getFirst();
                numberOfRowsToDisplay = uiData.getRows();
            }
            int colIndex = 0;
 
            int countOfRowsDisplayed = 0;
            uiData.setRowIndex(rowIndex);

            // write header
            Iterator childColumns = getRenderedChildColumnsList(uiData)
                    .iterator();
            while (childColumns.hasNext()) {
                UIColumn nextColumn = (UIColumn) childColumns.next();

                UIComponent headerComp = nextColumn.getFacet("header");
                if (headerComp != null) {
                    String headerText = encodeParentAndChildrenAsString(fc, headerComp);
                    if (headerText != null) {
                        outputHandler.writeHeaderCell(headerText, colIndex);
                    }
                }
                colIndex++;
            }

            while (uiData.isRowAvailable()) {
                if (numberOfRowsToDisplay > 0
                        && countOfRowsDisplayed >= numberOfRowsToDisplay) {
                    break;
                }

                // render the child columns; each one in a td
                childColumns = getRenderedChildColumnsList(uiData).iterator();
                colIndex = 0;
                while (childColumns.hasNext()) {
                    UIColumn nextColumn = (UIColumn) childColumns.next();
                    StringBuilder stringOutput = new StringBuilder();

                    Iterator childrenOfThisColumn = nextColumn.getChildren()
                            .iterator();
                    while (childrenOfThisColumn.hasNext()) {

                        UIComponent nextChild = (UIComponent) childrenOfThisColumn
                                .next();
                        if (nextChild.isRendered() && !(nextChild instanceof RowSelector)) {
                            stringOutput.append(encodeParentAndChildrenAsString(fc,
                                    nextChild));
                            //a blank to separate 
                            if (childrenOfThisColumn.hasNext()) {
                                stringOutput.append(' '); 
                            }
                        }

                    }
                    outputHandler.writeCell(stringOutput.toString(), colIndex, countOfRowsDisplayed);
                    colIndex++;
                }
                // keep track of rows displayed
                countOfRowsDisplayed++;
                // maintain the row index property on the underlying UIData
                // component
                rowIndex++;
                uiData.setRowIndex(rowIndex);

            }
            // reset the underlying UIData component
            uiData.setRowIndex(-1);

            outputHandler.flushFile();
        } catch (Exception e) {
            log.error("renderToHandler()", e);
        }

    }
    
    public void addInfo() {}
    
    /**
     * <p>Set the value of the <code>ignorePagination</code> property.</p>
     */
    public void setIgnorePagination(boolean ignorePagination) {
        this.ignorePagination = new Boolean(ignorePagination);
    }

    /**
     * <p>Return the value of the <code>ignorePagination</code> property.</p>
     */
    public boolean isIgnorePagination() {
        if (ignorePagination != null) {
            return ignorePagination.booleanValue();
        }
        ValueBinding vb = getValueBinding("ignorePagination");
        return vb != null ? ((Boolean) vb.getValue(getFacesContext()))
                .booleanValue() : false;
    }    
}
