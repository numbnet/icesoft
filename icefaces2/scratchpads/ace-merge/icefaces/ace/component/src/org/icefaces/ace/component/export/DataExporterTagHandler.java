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
package org.icefaces.ace.component.export;

import java.io.IOException;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

public class DataExporterTagHandler extends DataExporterTagHandlerBase {

	public DataExporterTagHandler(TagConfig tagConfig) {
		super(tagConfig);
	}

	public void apply(FaceletContext faceletContext, UIComponent parent) throws IOException, FacesException, FaceletException, ELException {
		if (ComponentHandler.isNew(parent)) {
            ActionSource actionSource = (ActionSource) parent;
            ValueExpression targetVE   = target.getValueExpression(faceletContext, Object.class);
			ValueExpression typeVE     = type.getValueExpression(faceletContext, Object.class);
			ValueExpression fileNameVE = fileName.getValueExpression(faceletContext, Object.class);

			ValueExpression pageOnlyVE = null;
			ValueExpression excludeColumnsVE = null;
			ValueExpression encodingVE = null;
			MethodExpression preProcessorME = null;
			MethodExpression postProcessorME = null;
			ValueExpression includeHeadersVE = null;
			ValueExpression includeFootersVE = null;
			ValueExpression selectedRowsOnlyVE = null;

            if (preProcessor != null)
                preProcessorME = preProcessor.getMethodExpression(faceletContext, null, new Class[]{Object.class});
            if (postProcessor != null)
                postProcessorME = postProcessor.getMethodExpression(faceletContext, null, new Class[]{Object.class});
            if (excludeColumns != null)
                excludeColumnsVE = excludeColumns.getValueExpression(faceletContext, Object.class);
			if (encoding != null)
				encodingVE = encoding.getValueExpression(faceletContext, Object.class);
			if (pageOnly != null)
				pageOnlyVE = pageOnly.getValueExpression(faceletContext, Object.class);
			if (includeHeaders != null)
				includeHeadersVE = includeHeaders.getValueExpression(faceletContext, Object.class);
			if (includeFooters != null)
				includeFootersVE = includeFooters.getValueExpression(faceletContext, Object.class);
			if (selectedRowsOnly != null)
				selectedRowsOnlyVE = selectedRowsOnly.getValueExpression(faceletContext, Object.class);

			actionSource.addActionListener(
                    new DataExporter(
                            targetVE,
                            typeVE,
                            fileNameVE,
                            pageOnlyVE,
                            excludeColumnsVE,
                            encodingVE,
                            preProcessorME,
                            postProcessorME,
							includeHeadersVE,
							includeFootersVE,
							selectedRowsOnlyVE)
            );
		}
	}

}

