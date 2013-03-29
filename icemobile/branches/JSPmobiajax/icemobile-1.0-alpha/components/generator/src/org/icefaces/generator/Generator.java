/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
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
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icefaces.generator;

import org.icefaces.generator.artifacts.Artifact;
import org.icefaces.generator.context.ComponentContext;
import org.icefaces.generator.context.GeneratorContext;

import java.util.Iterator;

public class Generator {
    public static void main(String args[]) {
        GeneratorContext generatorContext = GeneratorContext.getInstance();
        for (Class clazz : generatorContext.getComponents()) {
            ComponentContext componetnContext = generatorContext.createComponentContext(clazz);
            Iterator<Artifact> artifacts = componetnContext.getArtifacts();
            while (artifacts.hasNext()) {
                artifacts.next().build();
            }
        }
        generatorContext.release();
    }
}
