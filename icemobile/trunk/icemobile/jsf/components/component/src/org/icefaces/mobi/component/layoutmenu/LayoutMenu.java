package org.icefaces.mobi.component.layoutmenu;


import javax.faces.component.StateHelper;
import java.util.HashMap;
import java.util.Map;

public class LayoutMenu extends LayoutMenuBase {
    public static final String LAYOUTMENU_CLASS = "mobi-layoutmenu ";
    public static final String LAYOUTMENU_TITLE_CLASS = "mobi-layoutmenu-title ";
    public static final String LAYOUTMENU_LIST_CLASS = "mobi-list ";
    public static final String LAYOUTMENU_INSET_CLASS="mobi-list mobi-list-inset ";
    public static final String OUTPUTLISTITEMGROUP_CLASS="mobi-list-item mobi-list-item-group ";
    public static final String OUTPUTLISTITEMDEFAULT_CLASS="mobi-list-item-default ";

    private String stackClientId;

    public void setStackClientId(String sCID){
        //assume that this menu will only ever relate to one stack so only set if the current value is null
        //and then state save
		StateHelper sh = getStateHelper();
		String clientId = getClientId();
		String valuesKey = "stackClientId" + "_rowValues";
		Map clientValues = (Map) sh.get(valuesKey);
		if (clientValues == null) {
			clientValues = new HashMap();
		}
		if (sCID== null) {
			clientValues.remove(clientId);
		} else {
			clientValues.put(clientId, sCID);
		}
			//Always re-add the delta values to the map. JSF merges the values into the main map
			//and values are not state saved unless they're in the delta map.
		sh.put(valuesKey, clientValues);

    }

    public String getStackClientId(){
        String retVal = null;
        StateHelper sh = getStateHelper();
		String valuesKey = "stackClientId"+"_rowValues";
	    Map clientValues = (Map) sh.get(valuesKey);
	    boolean mapNoValue = false;
		if (clientValues != null) {
			String clientId = getClientId();
			if (clientValues.containsKey( clientId ) ) {
				retVal = (String) clientValues.get(clientId);
			} else {
				mapNoValue=true;
			}
		}
		if (mapNoValue || clientValues == null ) {
			String defaultKey = "stackClientId" + "_defaultValues";
			Map defaultValues = (Map) sh.get(defaultKey);
			if (defaultValues != null) {
				if (defaultValues.containsKey("defValue" )) {
					retVal = (String) defaultValues.get("defValue");
				}
			}
		}
		return retVal;
    }
}
