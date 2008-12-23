package com.icesoft.faces.component.inputfile;

import javax.faces.event.PhaseListener;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.context.FacesContext;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author mcollette
 * @since 1.8
 */
public class FileUploadPhaseListener implements PhaseListener {
    private static final Log log = LogFactory.getLog(FileUploadPhaseListener.class);
    
    public void beforePhase(PhaseEvent phaseEvent)  {
        if (log.isDebugEnabled())
            log.debug("FileUploadPhaseListener.beforePhase()  " + phaseEvent.getPhaseId().toString());
        
        if (PhaseId.APPLY_REQUEST_VALUES == phaseEvent.getPhaseId())  {
            UploadStateHolder stateHolder = UploadStateHolder.take();
            if (log.isDebugEnabled())
                log.debug("FileUploadPhaseListener.beforePhase()  stateHolder: " + stateHolder);
            if (stateHolder != null) {
                UploadConfig uploadConfig = stateHolder.getUploadConfig();
                FileInfo fileInfo = stateHolder.getFileInfo();
                String clientId = uploadConfig.getClientId();
                String formClientId = uploadConfig.getFormClientId();
                if (log.isDebugEnabled()) {
                    log.debug("FileUploadPhaseListener.beforePhase()  clientId: " + clientId);
                    log.debug("FileUploadPhaseListener.beforePhase()  formClientId: " + formClientId);
                    log.debug("FileUploadPhaseListener.beforePhase()  uploadConfig: " + uploadConfig);
                    log.debug("FileUploadPhaseListener.beforePhase()  fileInfo: " + fileInfo);
                }
                if (clientId != null && formClientId != null) {
                    FacesContext context = phaseEvent.getFacesContext();
                    Map parameterMap = context.getExternalContext().
                        getRequestParameterMap();
                    parameterMap.put(formClientId, formClientId);
                    parameterMap.put(clientId, stateHolder);
                }
            }
        }
    }
    
    public void afterPhase(PhaseEvent phaseEvent)  {
        if (log.isDebugEnabled())
            log.debug("FileUploadPhaseListener.afterPhase()  " + phaseEvent.getPhaseId().toString());
    }
    
    public PhaseId getPhaseId()  {
        return PhaseId.APPLY_REQUEST_VALUES;
    }
}
