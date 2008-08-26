/*
 * Helper
 */

package com.icesoft.faces.mock.test;

import javax.faces.component.UIComponent;

/**
 *
 * @author fye
 */
public class ICECompsListHelper {

    private static UIComponent[] uiComponents = new UIComponent[]{
     new com.icesoft.faces.component.effect.ApplyEffect(),
     new com.icesoft.faces.component.panelborder.PanelBorder(),
     new com.icesoft.faces.component.ext.UIColumn(),
     new com.icesoft.faces.component.ext.ColumnGroup(),
     new com.icesoft.faces.component.ext.UIColumns(),
     new com.icesoft.faces.component.datapaginator.DataPaginator(),
     new com.icesoft.faces.component.inputfile.InputFile(),
     new com.icesoft.faces.component.gmap.GMap(),
     new com.icesoft.faces.component.gmap.GMapControl(),
     new com.icesoft.faces.component.gmap.GMapDirection(),
     new com.icesoft.faces.component.gmap.GMapGeoXml(),
     new com.icesoft.faces.component.gmap.GMapLatLng(),
     new com.icesoft.faces.component.gmap.GMapLatLngs(),
     new com.icesoft.faces.component.gmap.GMapMarker(),
     new com.icesoft.faces.component.ext.HeaderRow(),
     new com.icesoft.faces.component.ext.HtmlCheckbox(),
     new com.icesoft.faces.component.ext.HtmlCommandButton(),
     new com.icesoft.faces.component.ext.HtmlCommandLink(),
     new com.icesoft.faces.component.ext.HtmlDataTable(),
     new com.icesoft.faces.component.ext.HtmlForm(),
     new com.icesoft.faces.component.ext.HtmlGraphicImage(),
     new com.icesoft.faces.component.ext.HtmlInputHidden(),
     new com.icesoft.faces.component.ext.HtmlInputSecret(),
     new com.icesoft.faces.component.ext.HtmlInputText(),
     new com.icesoft.faces.component.ext.HtmlInputTextarea(),
     new com.icesoft.faces.component.ext.HtmlMessage(),
     new com.icesoft.faces.component.ext.HtmlMessages(),
     new com.icesoft.faces.component.ext.HtmlOutputFormat(),
     new com.icesoft.faces.component.ext.HtmlOutputLabel(),
     new com.icesoft.faces.component.ext.HtmlOutputLink(),
     new com.icesoft.faces.component.ext.HtmlOutputText(),
     new com.icesoft.faces.component.ext.HtmlPanelGrid(),
     new com.icesoft.faces.component.ext.HtmlPanelGroup(),
     new com.icesoft.faces.component.ext.HtmlRadio(),
     new com.icesoft.faces.component.ext.HtmlSelectBooleanCheckbox(),
     new com.icesoft.faces.component.ext.HtmlSelectManyCheckbox(),
     new com.icesoft.faces.component.ext.HtmlSelectManyListbox(),
     new com.icesoft.faces.component.ext.HtmlSelectManyMenu(),
     new com.icesoft.faces.component.ext.HtmlSelectOneListbox(),
     new com.icesoft.faces.component.ext.HtmlSelectOneMenu(),
     new com.icesoft.faces.component.ext.HtmlSelectOneRadio(),
     new com.icesoft.faces.component.inputrichtext.InputRichText(),
     new com.icesoft.faces.component.menubar.MenuBar(),
     new com.icesoft.faces.component.menubar.MenuItem(),
     new com.icesoft.faces.component.menubar.MenuItemSeparator(),
     new com.icesoft.faces.component.menubar.MenuItems(),
     new com.icesoft.faces.component.menupopup.MenuPopup(),
     new com.icesoft.faces.component.ext.OutputBody(),
     new com.icesoft.faces.component.outputchart.OutputChart(),
     new com.icesoft.faces.component.outputconnectionstatus.OutputConnectionStatus(),
     new com.icesoft.faces.component.outputdeclaration.OutputDeclaration(),
     new com.icesoft.faces.component.ext.OutputHead(),
     new com.icesoft.faces.component.ext.OutputHtml(),
     new com.icesoft.faces.component.outputmedia.OutputMedia(),
     new com.icesoft.faces.component.style.OutputStyle(),
     new com.icesoft.faces.component.panelcollapsible.PanelCollapsible(),
     new com.icesoft.faces.component.paneldivider.PanelDivider(),
     new com.icesoft.faces.component.panellayout.PanelLayout(),
     new com.icesoft.faces.component.panelpopup.PanelPopup(),
     new com.icesoft.faces.component.panelseries.PanelSeries(),
     new com.icesoft.faces.component.panelstack.PanelStack(),
     new com.icesoft.faces.component.paneltabset.PanelTab(),
     new com.icesoft.faces.component.paneltabset.PanelTabSet(),
     new com.icesoft.faces.component.paneltooltip.PanelTooltip(),
     new com.icesoft.faces.component.portlet.Portlet(),
     new com.icesoft.faces.component.outputprogress.OutputProgress(),
     new com.icesoft.faces.component.ext.RowSelector(),
     new com.icesoft.faces.component.selectinputdate.SelectInputDate(),
     new com.icesoft.faces.component.selectinputtext.SelectInputText(),
     new com.icesoft.faces.component.commandsortheader.CommandSortHeader(),
     new com.icesoft.faces.component.tree.TreeNode(),
     new com.icesoft.faces.component.tree.Tree(),
     new com.icesoft.faces.component.panelpositioned.PanelPositioned(),
     new com.icesoft.faces.component.panelseries.UISeries()
    };

    protected UIComponent[] getComponents(){
        return uiComponents;
    }
}