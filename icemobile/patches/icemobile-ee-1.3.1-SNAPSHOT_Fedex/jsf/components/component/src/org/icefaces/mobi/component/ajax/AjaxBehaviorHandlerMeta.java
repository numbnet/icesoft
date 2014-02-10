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

package org.icefaces.mobi.component.ajax;

import org.icefaces.ace.meta.annotation.TagHandler;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.TagHandlerType;
import org.icefaces.ace.meta.annotation.Required;

import javax.el.MethodExpression;

@TagHandler(
    tagName = "ajax",
    tagHandlerType = TagHandlerType.TAG_HANDLER,
    tagHandlerClass = "org.icefaces.mobi.component.ajax.AjaxBehaviorHandler",
    generatedClass = "org.icefaces.mobi.component.ajax.AjaxBehaviorHandlerBase",
    extendsClass = "javax.faces.view.facelets.TagHandler",
	behaviorId = "org.icefaces.mobi.component.AjaxBehavior",
	behaviorClass = "org.icefaces.mobi.component.ajax.AjaxBehavior",
    tlddoc = "Register an AjaxBehavior instance on one UIComponent implementing the " +
    		"ClientBehaviorHolder interface. This tag may be nested within a single component " +
    		" to enable Ajax for that single component."
)
public class AjaxBehaviorHandlerMeta {

	@Property(required=Required.no, tlddoc="Method expression referencing a method that will be called when an " +
			"AjaxBehaviorEvent has been broadcast for the listener.")
	private MethodExpression listener;
	
	@Property(required=Required.no, tlddoc="If \"true\" behavior events generated from this behavior " +
			"are broadcast during Apply Request Values phase. Otherwise, the events will be broadcast " +
			"during Invoke Aplications phase.")
	private boolean immediate;
	
	@Property(required=Required.no, tlddoc="Evaluates to Collection. Identifiers of components that will participate in the \"execute\" " +
			"portion of the Request Processing Lifecycle. If a literal is specified the identifiers must " +
			"be space delimited. Any of the keywords \"@this\", \"@form\", \"@all\", \"@none\" may be specified " +
			"in the identifier list.")
	private String execute;
	
	@Property(required=Required.no, tlddoc="Evaluates to Collection. Identifiers of components that will participate in the \"render\" " +
			"portion of the Request Processing Lifecycle. If a literal is specified the " +
			"identifiers must be space delimited. Any of the keywords \"@this\", \"@form\", \"@all\", \"@none\" may be " +
			"specified in the identifier list.")
	private String render;
	
	@Property(required=Required.no, tlddoc="Javascript handler to execute before ajax request begins. The function has to return 'true' to continue with the ajax request; if 'false' or nothing is returned, the ajax request will be aborted.")
	private String onStart;
	
	@Property(required=Required.no, tlddoc="Javascript handler to execute when ajax request is completed.")
	private String onComplete;
	
	@Property(required=Required.no, tlddoc="Javascript handler to execute when ajax request succeeds.")
	private String onSuccess;
	
	@Property(required=Required.no, tlddoc="The name of the JavaScript function that will handle errors.")
	private String onError;
	
	@Property(required=Required.no, tlddoc="A value of \"true\" indicates the AjaxBehavior should not be rendered. " +
			"A value of \"false\" indicates the AjaxBehavior should be rendered. \"false\" is the default.")
	private boolean disabled;
	
	@Property(required=Required.no, tlddoc="A String identifying the type of event the Ajax action will apply to. " +
			"If specified, it must be one of the events supported by the component the Ajax behavior is being applied to. " +
			"For HTML components this would be the set of supported DOM events for the component, plus \"action\" for " +
			"Faces ActionSource components and \"valueChange\" for Faces EditableValueHolder components. " +
			"If not specified, the default event is determined for the component. The DOM event name is the" +
			" actual DOM event name (for example: \"click\") as opposed to (for example: \"onclick\").")
	private String event;
}
