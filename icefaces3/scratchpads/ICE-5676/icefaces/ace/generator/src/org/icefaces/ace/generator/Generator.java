/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

package org.icefaces.ace.generator;

import java.util.Iterator;

import org.icefaces.ace.generator.artifacts.Artifact;
import org.icefaces.ace.generator.context.MetaContext;
import org.icefaces.ace.generator.context.GeneratorContext;

public class Generator {
	public static void main(String args[]) throws Exception {
		if (args.length < 2) {
			System.err.println("ERROR: Missing arguments in generator.");
			System.err.println("First argument should be the namespace prefix (e.g. ace).");
			System.err.println("Second argument should be the namespace identifier \n\t(e.g. http://www.icefaces.org/icefaces/components).");
			throw new Exception("Generator error.");
		}
		GeneratorContext.shortName = args[0];
		GeneratorContext.namespace = args[1];
		GeneratorContext generatorContext = GeneratorContext.getInstance();
		for (Class clazz: generatorContext.getComponents()) {
			MetaContext metaContext = generatorContext.createMetaContext(clazz);
		    Iterator<Artifact> artifacts = metaContext.getArtifacts();
			while (artifacts.hasNext()) {
				artifacts.next().build();
		    }
		}
		generatorContext.release();		
	}
}
