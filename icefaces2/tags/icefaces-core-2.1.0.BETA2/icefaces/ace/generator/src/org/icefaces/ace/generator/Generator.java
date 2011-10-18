/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.ace.generator;

import java.util.Iterator;

import org.icefaces.ace.generator.artifacts.Artifact;
import org.icefaces.ace.generator.context.ComponentContext;
import org.icefaces.ace.generator.context.GeneratorContext;

public class Generator {
	public static void main(String args[]) {
		GeneratorContext generatorContext = GeneratorContext.getInstance();
		for (Class clazz: generatorContext.getComponents()) {
			ComponentContext componetnContext = generatorContext.createComponentContext(clazz);
		    Iterator<Artifact> artifacts = componetnContext.getArtifacts();
			while (artifacts.hasNext()) {
				artifacts.next().build();
		    }
		}
		generatorContext.release();		
	}
}
