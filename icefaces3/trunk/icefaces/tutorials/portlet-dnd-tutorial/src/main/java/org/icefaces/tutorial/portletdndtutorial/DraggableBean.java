/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icefaces.tutorial.portletdndtutorial;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.icefaces.ace.event.DragDropEvent;
import org.icefaces.bean.WindowDisposed;
import org.icefaces.application.PushRenderer;

import java.util.*;

@ManagedBean
@WindowDisposed
@ViewScoped
public class DraggableBean implements java.io.Serializable {

    @ManagedProperty(value = "#{windowScopedBean}")
    private WindowScopedBean windowScopedBean;

	private List<Item> items;
	
	@PostConstruct
	public void postConstruct() {
		items = new ArrayList<Item>();
		items.add(new Item("Item1", items));
		items.add(new Item("Item2", items));
		items.add(new Item("Item3", items));
		items.add(new Item("Item4", items));
		items.add(new Item("Item5", items));
		PushRenderer.addCurrentView("tutorial");
	}

	public void handleDrag(DragDropEvent e) {
		Item item = (Item) e.getData();
		windowScopedBean.setDraggedItem(item);
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public void setWindowScopedBean(WindowScopedBean windowScopedBean) {
		this.windowScopedBean = windowScopedBean;
	}
}