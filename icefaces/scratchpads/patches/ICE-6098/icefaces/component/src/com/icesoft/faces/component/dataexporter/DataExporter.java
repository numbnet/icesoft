package com.icesoft.faces.component.dataexporter;

import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.application.D2DViewHandler;
import com.icesoft.faces.component.outputresource.OutputResource;
import com.icesoft.faces.context.FileResource;

public class DataExporter extends OutputResource {
    public static final String COMPONENT_FAMILY = "com.icesoft.faces.DataExporter";
	public static final String COMPONENT_TYPE = "com.icesoft.faces.DataExporter";
	public static final String DEFAULT_RENDERER_TYPE = "com.icesoft.faces.DataExporterRenderer";
	
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
		_origDataModelHash = forComp.getValue().hashCode();
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

	public String getClickToCreateFileText() {
		if (this.clickToCreateFileText != null) {
			return clickToCreateFileText;
		}
		ValueBinding vb = getValueBinding("clickToCreateFileText");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

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
            values = new Object[11];
        }
        values[0] = super.saveState(context);
        values[1] = _for;
        values[2] = type;
        values[3] = clickToCreateFileText;
        values[4] = readyToExport? Boolean.TRUE : Boolean.FALSE;
        values[5] = _origType;
        values[6] = _origFor;
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
 
    }
    
    public String getLabel() {
        String label = super.getLabel();
        if (label == null) {
            if (resource instanceof FileResource)
            label = ((FileResource)resource).getFile().getName();
        }
        return label;
    }
	

}
