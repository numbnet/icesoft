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
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;

@ManagedBean(name="ajaxTest")
@SessionScoped
public class AjaxTest implements Serializable{

    private ArrayList itemList= new ArrayList();
    private String compToFind="carForm:carOne";
    private boolean found = false;

      public AjaxTest(){
          itemList.add(new Item("Item A1", "Item A2"));
           itemList.add(new Item("Item B1", "Item B2"));
           itemList.add(new Item("Item C1", "Item C2"));
           itemList.add(new Item("Item D1", "Item D2"));
       }

    public String getCompToFind() {
        return compToFind;
    }

    public void setCompToFind(String compToFind) {
        this.compToFind = compToFind;
    }

    private String input="test";
       private Double sliderValue = 0.0;
       private int valueA = 0;
       private int valueB = 0;
       private String execute = "@this";
       private String render = "@all";

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

       public String getInput() {
           return input;
       }

       public void setInput(String input) {
           this.input = input;
       }

      public void clear() {
           valueA = 0;
           valueB = 0;
       }

       public void findComp(){
           found = false;
           FacesContext fc = FacesContext.getCurrentInstance();
           UIViewRoot view = fc.getViewRoot();
           UIComponent uic = (UIComponent)view.findComponent(this.compToFind);
           if (null != uic){
               found=true;
               System.out.println("found component "+compToFind+" with id from uic="+uic.getClientId());
           }
       }

       public boolean getFound(){
           return this.found;
       }
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
