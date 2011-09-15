/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icefaces.component.carousel;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.el.MethodExpression;
import javax.faces.event.ValueChangeEvent;


public class Carousel extends CarouselBase {
    public static final String CAROUSEL_CLASS = "mobi-carousel ";
    public static final String CAROUSEL_ITEM_CLASS = "mobi-carousel-list ";
    public static final String CAROUSEL_CURSOR_CLASS = "mobi-carousel-cursor ";
    public static final String CAROUSEL_CURSOR_LISTCLASS = "mobi-carousel-cursor-list ";
	
    public Carousel() {
//        super();
    }
    public void broadcast(FacesEvent event)
      throws AbortProcessingException {
    	if (event instanceof ValueChangeEvent){   	
	            if (event != null) {
	               ValueExpression ve = getValueExpression("selectedIten");	
	            if (ve != null) {
	                try {
	                    ve.setValue(getFacesContext().getELContext(), ((ValueChangeEvent)event).getNewValue());
	                } catch (ELException ee) {
	                    ee.printStackTrace();
	                }
	            } else {
	            	int tempInt = (Integer)((ValueChangeEvent)event).getNewValue();
	                this.setSelectedItem(tempInt);
	                System.out.println(" after setting the selectedItem to "+tempInt);
	            }
	            ValueChangeEvent e = (ValueChangeEvent)event;
	            MethodExpression method = getValueChangeListener();
	            if (method != null) {
	                method.invoke(getFacesContext().getELContext(), new Object[]{event});
	            }
            }
        }
    	else{
   		   super.broadcast(event);
   		   return;
    	}
    }
    
    public void queueEvent(FacesEvent event) {
        if (event.getComponent() instanceof Carousel) {
            if (isImmediate()) {
                event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
                System.out.println("invoked event for immediate true");
            }
            else {
                event.setPhaseId(PhaseId.INVOKE_APPLICATION);
                System.out.println("invoke event for immediate false");
            }
        }
        super.queueEvent(event);
    }  

}
