/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.mobile;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ActionEvent;

//copied over from QA repo for ace:ajax testing
@ManagedBean
@SessionScoped
public class AjaxTestBean implements Serializable {

    public AjaxTestBean() {
        itemList = new ArrayList();
        itemList.add(new Item("Item A1", "Item A2"));
        itemList.add(new Item("Item B1", "Item B2"));
        itemList.add(new Item("Item C1", "Item C2"));
        itemList.add(new Item("Item D1", "Item D2"));
	}

    private Double sliderValue = 0.0;
	private int valueA = 0;
	private int valueB = 0;
	private int valueC = 0;
	private int ajaxEventCtr = 0;

	private String execute = "@all";
	private String render = "@all";
	private String statusMsg = "";

    public Double getSliderValue() {
        return sliderValue;
    }

    public void setSliderValue(Double sliderValue) {
        this.sliderValue = sliderValue;
    }

	public int getValueA() {
		return valueA;
	}

	public void setValueA(int value) {
		valueA++;
	}

	public int getValueB() {
		return valueB;
	}

	public void setValueB(int value) {
		valueB++;
	}

	public int getValueC() {
		return valueC;
	}

	public void setValueC(int value) {
		valueC++;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String value) {
		statusMsg = value;
	}

	public void actionListener(ActionEvent event) {
	 	if (event != null) {
	 	   statusMsg = "actionEvent received! (#" + ++ajaxEventCtr+")";

	 	} else {
	 	   statusMsg = "FAIL! - Null actionEvent received!";
	 	}
	 }


	public void ajaxEventListener(AjaxBehaviorEvent event) {
	 	if (event != null) {
	 	   statusMsg = "AjaxBehaviorEvent received! (#" + ++ajaxEventCtr+")";

	 	} else {
	 	   statusMsg = "FAIL! - Null AjaxBehaviorEvent received!";
	 	}
	 }

	 public void fAjaxEventListener(AjaxBehaviorEvent event) {
	 	if (event != null) {
	 	   statusMsg = new String( statusMsg + "fAjax ");

	 	} else {
	 	   statusMsg = "FAIL! - Null fAjaxBehaviorEvent received!";
	 	}
	 }

	 public void mobiAjaxEventListener(AjaxBehaviorEvent event) {
	 	if (event != null) {
	 	   statusMsg = new String( statusMsg + "mobiAjax ");

	 	} else {
	 	   statusMsg = "FAIL! - Null mobiAjaxBehaviorEvent received!";
	 	}
	 }


	public String getExecute() {
		return execute;
	}

	public void setExecute(String value) {
		execute = value;
	}

	public String getRender() {
		return render;
	}

	public void setRender(String value) {
		render = value;
	}

	public void clear() {
		valueA = 0;
		valueB = 0;
		valueC = 0;
		statusMsg = "";
		ajaxEventCtr = 0;
	}

    private ArrayList itemList;

    public ArrayList getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList itemList) {
        this.itemList = itemList;
    }

	public class Item implements Serializable {

		private String itemOne;
		private String itemTwo;

		public Item(String itemOne, String itemTwo) {
			this.itemOne = itemOne;
			this.itemTwo = itemTwo;
		}

		public String getItemOne() {
			return itemOne;
		}

		public void setItemOne(String itemOne) {
			this.itemOne = itemOne;
		}

		public String getItemTwo() {
			return itemTwo;
		}

		public void setItemTwo(String itemTwo) {
			this.itemTwo = itemTwo;
		}
	}
}