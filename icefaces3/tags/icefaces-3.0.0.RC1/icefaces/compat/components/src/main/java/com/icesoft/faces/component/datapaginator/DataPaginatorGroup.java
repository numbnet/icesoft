package com.icesoft.faces.component.datapaginator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import com.icesoft.util.CoreComponentUtils;

public class DataPaginatorGroup {
	public static void add(FacesContext facesContext, DataPaginator dataPaginator) {
        UIData uiData = dataPaginator.findUIData();
        if (uiData == null) return;
        dataPaginator.setUIData(uiData);
        if (!uiData.getAttributes().containsKey(DataPaginatorGroup.class.getName())) {
        	uiData.getAttributes().put(DataPaginatorGroup.class.getName(), new ArrayList());
        }
        List paginatorList = (List)uiData.getAttributes().get(DataPaginatorGroup.class.getName());
        String clientId = dataPaginator.getClientId(facesContext);
        if (!paginatorList.contains(clientId)) {
        	paginatorList.add(clientId);
        }
	}
	
	public static void execute(UIData uiData, Invoker invoker) {
        if (uiData== null || !uiData.getAttributes().containsKey(DataPaginatorGroup.class.getName())) return;
        List dataPaginatorClientIdList = (List) uiData.getAttributes().get(DataPaginatorGroup.class.getName()); 
        Iterator it = dataPaginatorClientIdList.iterator();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        while (it.hasNext()) {
	        UIComponent component = CoreComponentUtils.findComponent(UINamingContainer.getSeparatorChar(facesContext) + String.valueOf(it.next()), facesContext.getViewRoot());
	        if (component != null && component.isRendered() && 
	        		component instanceof DataPaginator) {
	            invoker.invoke((DataPaginator)component);
	        }		
        }
	}
 
	public interface Invoker {
		void invoke(DataPaginator dataPaginator);
	}
}
