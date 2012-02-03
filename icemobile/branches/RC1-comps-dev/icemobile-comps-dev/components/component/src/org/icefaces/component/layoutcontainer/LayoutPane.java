package org.icefaces.component.layoutcontainer;

public class LayoutPane extends LayoutPaneBase{

    public static final String LAYOUT_CONTAINER_HEADER_CLASS = "mobi-layout-header ";
    public static final String LAYOUT_CONTAINER_FOOTER_CLASS = "mobi-layout-footer ";

    public LayoutPane(){
        super();
    }
    public enum PanelType {
        header,
        footer;
        public static final PanelType DEFAULT = PanelType.header;

        public boolean equals(String pane) {
            return this.name().equals(pane);
        }
    }
}
