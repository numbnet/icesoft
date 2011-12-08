/*
 * Original Code developed and contributed by Prime Technology.
 * Subsequent Code Modifications Copyright 2011 ICEsoft Technologies Canada Corp. (c)
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
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */
package org.icefaces.ace.component.ajax;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.component.behavior.FacesBehavior;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.BehaviorEvent;

@ResourceDependencies({
	@ResourceDependency(library="icefaces.ace", name="jquery/jquery.js"),
	@ResourceDependency(library="icefaces.ace", name="core/core.js")
})
@FacesBehavior("org.icefaces.ace.component.AjaxBehavior")
public class AjaxBehavior extends ClientBehaviorBase {

    private String render;
    private String execute;
    private boolean global = true;
    private boolean async = false;
    private String onComplete;
    private String onError;
    private String onSuccess;
    private String onStart;
    private MethodExpression listener;
    private boolean immediate = false;
    private boolean disabled = false;
    private boolean immediateSet = false;

    private static final Set<ClientBehaviorHint> HINTS = Collections.unmodifiableSet(EnumSet.of(ClientBehaviorHint.SUBMITTING));

    private Map<String, ValueExpression> bindings;

    @Override
    public String getRendererType() {
        return "org.icefaces.ace.component.AjaxBehaviorRenderer";
    }
    
    @Override
    public Set<ClientBehaviorHint> getHints() {
        return HINTS;
    }
    
    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public String getOnComplete() {
        return onComplete;
    }

    public void setOnComplete(String onComplete) {
        this.onComplete = onComplete;
    }

    public String getOnStart() {
        return onStart;
    }

    public void setOnStart(String onStart) {
        this.onStart = onStart;
    }

    public String getOnSuccess() {
        return onSuccess;
    }

    public void setOnSuccess(String onSuccess) {
        this.onSuccess = onSuccess;
    }

    public String getOnError() {
        return onError;
    }

    public void setOnError(String onError) {
        this.onError = onError;
    }

    public String getExecute() {
        return execute;
    }

    public void setExecute(String execute) {
        this.execute = execute;
    }

    public String getRender() {
        return render;
    }

    public void setRender(String render) {
        this.render = render;
        clearInitialState();
    }

    public MethodExpression getListener() {
        return listener;
    }

    public void setListener(MethodExpression listener) {
        this.listener = listener;
        clearInitialState();
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isImmediate() {
        return immediate;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;

        this.immediateSet = true;
    }

    public boolean isImmediateSet() {
        return immediateSet;
    }

    public void broadcast(BehaviorEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        ELContext eLContext = context.getELContext();

        if (listener != null) {
            try {
                listener.invoke(eLContext, null);       //no-arg listener
            } catch (MethodNotFoundException e1) {
                MethodExpression argListener = context.getApplication().getExpressionFactory().
                        createMethodExpression(eLContext, listener.getExpressionString(), null, new Class[]{event.getClass()});

                argListener.invoke(eLContext, new Object[]{event});
            }
        }
    }

    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        Object[] values;

        Object superState = super.saveState(context);

        if (initialStateMarked()) {
            if (superState == null) {
                values = null;
            } else {
                values = new Object[] { superState };
            }
        } else {
            values = new Object[3];

            values[0] = superState;
            values[1] = listener;
            values[2] = render;
        }

        return values;
    }

    public void restoreState(FacesContext context, Object state) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (state != null) {

            Object[] values = (Object[]) state;
            super.restoreState(context, values[0]);

            if (values.length != 1) {
                listener = (MethodExpression)values[1];
                render = (String)values[2];

                // If we saved state last time, save state again next time.
                clearInitialState();
            }
        }
    }

}