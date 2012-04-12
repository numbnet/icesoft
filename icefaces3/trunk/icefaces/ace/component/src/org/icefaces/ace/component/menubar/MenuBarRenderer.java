/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: (ICE-6978) Used JSONBuilder to add the functionality of escaping JS output.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 * Contributors: ______________________
 */
package org.icefaces.ace.component.menubar;

import org.icefaces.ace.component.menu.AbstractMenu;
import org.icefaces.ace.component.menu.BaseMenuRenderer;
import org.icefaces.ace.component.menuitem.MenuItem;
import org.icefaces.ace.component.submenu.Submenu;
import org.icefaces.ace.component.multicolumnsubmenu.MultiColumnSubmenu;
import org.icefaces.ace.component.menucolumn.MenuColumn;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.ace.util.Utils;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@MandatoryResourceComponent(tagName="menubar", value="org.icefaces.ace.component.menubar.MenuBar")
public class MenuBarRenderer extends BaseMenuRenderer {

    private final static Logger logger = Logger.getLogger(MenuBarRenderer.class.getName());

    @Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		AbstractMenu menu = (AbstractMenu) component;

		if(menu.shouldBuildFromModel()) {
			menu.buildMenuFromModel();
		}

		encodeMarkup(context, menu);
	}
	
	protected void encodeScript(FacesContext context, AbstractMenu abstractMenu) throws IOException{
		ResponseWriter writer = context.getResponseWriter();
        MenuBar menubar = (MenuBar) abstractMenu;
		String clientId = menubar.getClientId(context);
		String widgetVar = this.resolveWidgetVar(menubar);

		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);

        JSONBuilder json = JSONBuilder.create();
		writer.write(widgetVar + " = new ");
        json.beginFunction("ice.ace.Menubar").
            item(clientId).
            beginMap().

                entry("autoSubmenuDisplay", menubar.isAutoSubmenuDisplay()).
				entry("direction", menubar.getDirection()).

                beginMap("animation").
                    entry("animated", menubar.getEffect()).
                    entry("duration", menubar.getEffectDuration()).
                endMap().

                entryNonNullValue("styleClass", menubar.getStyleClass()).
                entryNonNullValue("style", menubar.getStyle()).
            endMap().
        endFunction();

        writer.write(json.toString());

        writer.endElement("script");
	}

	protected void encodeMarkup(FacesContext context, AbstractMenu abstractMenu) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
        MenuBar menubar = (MenuBar) abstractMenu;
		String clientId = menubar.getClientId(context);

        writer.startElement("div", menubar);
        writer.writeAttribute("id", clientId, null);

		writer.startElement("ul", null);

		encodeMenuContent(context, menubar);

		writer.endElement("ul");
		
		encodeScript(context, menubar);

        writer.endElement("div");
	}

    protected void encodeMenuContent(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        for(Iterator<UIComponent> iterator = component.getChildren().iterator(); iterator.hasNext();) {
            UIComponent child = (UIComponent) iterator.next();

            if(child.isRendered()) {

                writer.startElement("li", null);

                if(child instanceof MenuItem) {
                    encodeMenuItem(context, (MenuItem) child);
                } else if(child instanceof Submenu) {
                    encodeSubmenu(context, (Submenu) child);
                } else if(child instanceof MultiColumnSubmenu) {
					encodeMultiColumnSubmenu(context, (MultiColumnSubmenu) child);
				}

                writer.endElement("li");
            }
        }
    }

	protected void encodeSubmenu(FacesContext context, Submenu submenu) throws IOException{
		ResponseWriter writer = context.getResponseWriter();
		UIComponent labelFacet = submenu.getFacet("label");
        String icon = submenu.getIcon();

        //title
		if(labelFacet == null) {
            String label = submenu.getLabel();

			writer.startElement("a", null);
			writer.writeAttribute("href", "#", null);

            if(icon != null) {
                writer.startElement("span", null);
                writer.writeAttribute("class", icon + " wijmo-wijmenu-icon-left", null);
                writer.endElement("span");
            }

            if(label != null) {
                writer.startElement("span", null);
                String style = submenu.getStyle();
                if (style != null && style.trim().length() > 0) {
                    writer.writeAttribute("style", style, "style");
                }
                Utils.writeConcatenatedStyleClasses(writer, "wijmo-wijmenu-text", submenu.getStyleClass());
                writer.write(submenu.getLabel());
                writer.endElement("span");
            }

			writer.endElement("a");
		}
        else {
            //backwards compatibility
            logger.info("label facet of a menubar item is deprecated, use a menuitem instead instead of a submenu.");
            encodeMenuItem(context, (MenuItem) labelFacet);
		}

        //submenus and menuitems
		if(submenu.getChildCount() > 0) {
			writer.startElement("ul", null);

			encodeMenuContent(context, submenu);

			writer.endElement("ul");
		}
	}
	
	protected void encodeMultiColumnSubmenu(FacesContext context, MultiColumnSubmenu submenu) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String label = submenu.getLabel();
		String icon = submenu.getIcon();
		
		List<MenuColumn> menuColumns = new ArrayList<MenuColumn>();
		for (UIComponent child : submenu.getChildren()) {
			if (child instanceof MenuColumn) menuColumns.add((MenuColumn) child);
		}

		writer.startElement("a", null);
		writer.writeAttribute("href", "#", null);
		
		if(icon != null) {
			writer.startElement("span", null);
			writer.writeAttribute("class", icon + " wijmo-wijmenu-icon-left", null);
			writer.endElement("span");
		}

		if(label != null) {
			writer.startElement("span", null);
			String style = submenu.getStyle();
			if (style != null && style.trim().length() > 0) {
				writer.writeAttribute("style", style, "style");
			}
			Utils.writeConcatenatedStyleClasses(writer, "wijmo-wijmenu-text", submenu.getStyleClass());
			writer.write(submenu.getLabel());
			writer.endElement("span");
		}

		writer.endElement("a");
		
		
		int totalChildren = 0;
		Map<MenuColumn, ArrayList<ArrayList<UIComponent>>> columnMap = new HashMap<MenuColumn, ArrayList<ArrayList<UIComponent>>>();
		for (MenuColumn menuColumn : menuColumns) {
			columnMap.put(menuColumn, divideColumn(menuColumn));
			for (ArrayList<UIComponent> sublist : columnMap.get(menuColumn)) {
				totalChildren += sublist.size();
			}
		}
		int totalWidth = 0;
		for (MenuColumn menuColumn : menuColumns) {
			if (menuColumn.isRendered()) {
				int columnWidth = menuColumn.getWidth();
				int autoflow = menuColumn.getAutoflow();
				int multiplier = 1;
				if (autoflow > 0) {
					multiplier = totalChildren / autoflow;
					if ((totalChildren % autoflow) > 0) multiplier++;
				}
				totalWidth += (columnWidth * multiplier);
			}
		}
		
		writer.startElement("div", null);
		writer.writeAttribute("class", "wijmo-wijmenu multi .ui-helper-reset", "class");
		writer.writeAttribute("style", "width: " + totalWidth + "px; padding: 0;", "style");
			
		for (MenuColumn menuColumn : menuColumns) {
			if (menuColumn.isRendered()) {
				for (ArrayList<UIComponent> sublist : columnMap.get(menuColumn)) {
					writer.startElement("div", null);
					writer.writeAttribute("style", "float:left; display: inline; padding:0; width: " + menuColumn.getWidth() + "px;", "style");

					if(sublist.size() > 0) {
						writer.startElement("ul", null);

						for (UIComponent item : sublist) {
							encodePlainMenuContent(context, item);
						}

						writer.endElement("ul");
					}

					writer.endElement("div");
				}
			}
		}
		
		writer.endElement("div");
	}
	
    protected void encodePlainMenuContent(FacesContext context, UIComponent component) throws IOException{
		ResponseWriter writer = context.getResponseWriter();

		if(component.isRendered()) {

			if(component instanceof MenuItem) {
				writer.startElement("li", null);
				encodeMenuItem(context, (MenuItem) component);
				writer.endElement("li");
			} else if(component instanceof Submenu) {
				encodePlainSubmenu(context, (Submenu) component);
			}
			
		}
    }

    protected void encodePlainSubmenu(FacesContext context, Submenu submenu) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String label = submenu.getLabel();

        writer.startElement("li", null);
        writer.startElement("h3", null);
        if(label != null) {
            String style = submenu.getStyle();
            if (style != null && style.trim().length() > 0) {
                writer.writeAttribute("style", style, "style");
            }
            Utils.writeConcatenatedStyleClasses(writer, "", submenu.getStyleClass());
            writer.write(label);
        }
        writer.endElement("h3");
        writer.endElement("li");
	}
	
	protected ArrayList<ArrayList<UIComponent>> divideColumn(MenuColumn menuColumn) {
	
		ArrayList<ArrayList<UIComponent>> columnList = new ArrayList<ArrayList<UIComponent>>();
		if (!menuColumn.isRendered()) return columnList;
		int maxRows = menuColumn.getAutoflow();
		if (maxRows == 0) {
			ArrayList<UIComponent> column = flattenContents(menuColumn, new ArrayList<UIComponent>());
			columnList.add(column);
		} else {
			ArrayList<UIComponent> children = flattenContents(menuColumn, new ArrayList<UIComponent>());
			
			if (children.size() <= maxRows) {
				columnList.add(children);
			} else {
				int rowCount = 0;
				ArrayList<UIComponent> currentColumn = new ArrayList<UIComponent>();
				for (UIComponent child : children) {
					rowCount++;
					currentColumn.add(child);
					if (rowCount == maxRows) {
						rowCount = 0;
						columnList.add(currentColumn);
						currentColumn = new ArrayList<UIComponent>();
					}
				}
				if (rowCount > 0) {
					columnList.add(currentColumn);
				}
			}
		}
		
		return columnList;
	}
	
	protected ArrayList<UIComponent> flattenContents(UIComponent component, ArrayList<UIComponent> result) {
		
		for (UIComponent child : component.getChildren()) {
			if (child.isRendered()) {
				if (child instanceof MenuItem || child instanceof Submenu) {
					result.add(child);
					if (child instanceof Submenu) {
						flattenContents(child, result);
					}
				}
			}
		}
		
		return result;
	}
}