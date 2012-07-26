package org.icefaces.ace.component.monitor;

import javax.faces.component.UIComponent;

public class SubmitMonitor extends SubmitMonitorBase implements java.io.Serializable {
    boolean isHidingIdleSubmitMonitor() {
        // When using an overlay, the submitMonitor is not shown when idle
        return !"@none".equals(getBlockUI());
    }

    String resolveBlockUI() {
        String blockUI = getBlockUI();
        if ("@all".equals(blockUI) || "@source".equals(blockUI) ||
            "@none".equals(blockUI)) {
            return blockUI;
        }
        UIComponent comp = findComponent(blockUI);
        if (comp != null) {
            return comp.getClientId();
        }
        return blockUI;
    }
}
