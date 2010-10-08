package com.icesoft.faces.component.dataexporter;

import java.beans.Beans;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.w3c.dom.Element;

import com.icesoft.faces.component.ext.HtmlCommandLink;
import com.icesoft.faces.component.ext.UIColumn;
import com.icesoft.faces.component.outputresource.OutputResourceRenderer;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.context.FileResource;
import com.icesoft.faces.renderkit.dom_html_basic.FormRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;

public class DataExporterRenderer extends OutputResourceRenderer {

	private static final String PREPARE_FILE_HIDDEN_SUFFIX = "_pfl";

	public void encodeBegin(FacesContext fc, UIComponent uiC)
			throws IOException {
		DataExporter oe = (DataExporter) uiC;
		if (oe.getUIData() != null && (oe.getType() != null
		        && oe.getType().length() > 0)) {

			if (oe.isReadyToExport()) {
				if (oe.getResource() == null) {
					File file = createFile(fc, oe.getUIData(),
							(DataExporter) uiC);
					if (file != null) {
						oe.setResource(new FileResource(file));
					}
				}
				super.encodeBegin(fc, oe);
				FormRenderer.addHiddenField(fc,
		                deriveCommonHiddenFieldName(
		                        fc, oe));

			} else {
				renderPrepareFileLink(fc, oe);
			}
		}
		else{
			DOMContext domContext = DOMContext.attachDOMContext(fc, oe);
			if (!domContext.isInitialized()) {
				domContext.createRootElement(HTML.DIV_ELEM);
			}

			Element root = (Element)domContext.getRootNode();
			root.setAttribute(HTML.ID_ATTR, oe.getClientId(fc)
					+ CONTAINER_DIV_SUFFIX);
			domContext.setCursorParent(root);
		}
	}

	private void renderPrepareFileLink(FacesContext fc, DataExporter oe)
			throws IOException {
		DOMContext domContext = DOMContext.attachDOMContext(fc, oe);
		if (!domContext.isInitialized()) {
			domContext.createRootElement(HTML.DIV_ELEM);
		}

		Element root = (Element)domContext.getRootNode();
		root.setAttribute(HTML.ID_ATTR, oe.getClientId(fc)
				+ CONTAINER_DIV_SUFFIX);
		domContext.setCursorParent(root);

		HtmlCommandLink link = (HtmlCommandLink) fc.getApplication()
				.createComponent(HtmlCommandLink.COMPONENT_TYPE);
		FormRenderer.addHiddenField(fc,
                deriveCommonHiddenFieldName(
                        fc, oe));

		link.setId(oe.getId() + PREPARE_FILE_HIDDEN_SUFFIX);
		String clickToCreateFileText = oe.getClickToCreateFileText();
		if (clickToCreateFileText == null)
			clickToCreateFileText = "Click to create export file";
		link.setValue(clickToCreateFileText);
		link.setTransient(true);
		oe.getChildren().add(link);
		link.encodeBegin(fc);
		link.encodeEnd(fc);
		oe.getChildren().clear();

	}

	protected static String deriveCommonHiddenFieldName(
			FacesContext facesContext, UIComponent uiComponent) {

		if (Beans.isDesignTime()) {
			return "";
		}

		UIComponent parentNamingContainer = findForm(uiComponent);
		String parentClientId = parentNamingContainer.getClientId(facesContext);
		String hiddenFieldName = parentClientId
				+ NamingContainer.SEPARATOR_CHAR + UIViewRoot.UNIQUE_ID_PREFIX
				+ "cl";
		return hiddenFieldName;
	}

	public void decode(FacesContext context, UIComponent component) {
		validateParameters(context, component, DataExporter.class);
		String hiddenFieldKey = deriveCommonHiddenFieldName(context, component);
		String param = (String) context.getExternalContext()
				.getRequestParameterMap().get(hiddenFieldKey);
		if (param != null
				&& (component.getClientId(context) + PREPARE_FILE_HIDDEN_SUFFIX)
						.equals(param)) {
			((DataExporter) component).setReadyToExport(true);
		}

	}

	private File createFile(FacesContext fc, UIData uiData, DataExporter oe) {

		OutputTypeHandler outputHandler = null;
		ServletContext context = ((HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false))
				.getServletContext();

		try {
			File exportDir = new File(context.getRealPath("/export"));
			if (!exportDir.exists())
				exportDir.mkdirs();
			String pathWithoutExt = context.getRealPath("/export") + "/export_"
					+ new Date().getTime();

			if (oe.getOutputTypeHandler() != null)
				outputHandler = oe.getOutputTypeHandler();
			else if (DataExporter.EXCEL_TYPE.equals(oe.getType())) {
				outputHandler = new ExcelOutputHandler(pathWithoutExt + ".xls",
						fc, uiData.getId());
			} else if (DataExporter.CSV_TYPE.equals(oe.getType())) {
				outputHandler = new CSVOutputHandler(pathWithoutExt + ".csv");
			}
			renderToHandler(outputHandler, uiData, fc, oe);
			oe.setMimeType(outputHandler.getMimeType());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return outputHandler != null ? outputHandler.getFile() : null;

	}

	private String encodeParentAndChildrenAsString(FacesContext fc,
			UIComponent uic, String str) {
		Object value = uic.getAttributes().get("value");
		if (value != null)
			str += "" + value;
		else {
			ValueBinding vb = uic.getValueBinding("value");
			if (vb != null)
				str += vb.getValue(fc).toString();
		}

		if (uic.getChildCount() > 0) {
			Iterator iter = uic.getChildren().iterator();
			while (iter.hasNext()) {
				UIComponent child = (UIComponent) iter.next();
				str += encodeParentAndChildrenAsString(fc, child, str);
			}
		}
		return str;
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
			UIData uiData, FacesContext fc, DataExporter oe) {

		try {
			int rowIndex = uiData.getFirst();
			int colIndex = 0;
			int numberOfRowsToDisplay = uiData.getRows();
			int countOfRowsDisplayed = 0;
			uiData.setRowIndex(rowIndex);

			// write header
			Iterator childColumns = getRenderedChildColumnsList(uiData)
					.iterator();
			while (childColumns.hasNext()) {
				UIColumn nextColumn = (UIColumn) childColumns.next();

				UIComponent headerComp = nextColumn.getFacet("header");
				if (headerComp != null) {
					String headerText = "";
					headerText = encodeParentAndChildrenAsString(fc,
							headerComp, headerText);
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
				// StringTokenizer columnWidths =
				// getColumnWidths(uiComponent);

				colIndex = 0;
				while (childColumns.hasNext()) {
					UIColumn nextColumn = (UIColumn) childColumns.next();

					Object output = null;
					String stringOutput = "";

					Iterator childrenOfThisColumn = nextColumn.getChildren()
							.iterator();
					while (childrenOfThisColumn.hasNext()) {

						UIComponent nextChild = (UIComponent) childrenOfThisColumn
								.next();
						if (nextChild.isRendered()) {
							stringOutput += encodeParentAndChildrenAsString(fc,
									nextChild, stringOutput);
						}

					}
//					try {
//						output = Double.parseDouble(stringOutput);
//					} catch (NumberFormatException nfe) {
						output = stringOutput;
//					}
					outputHandler.writeCell(output, colIndex, rowIndex);
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
			e.printStackTrace();
		}

	}

}
