package org.icefaces.generator;

import java.util.Iterator;

import org.icefaces.generator.artifacts.Artifact;
import org.icefaces.generator.context.ComponentContext;
import org.icefaces.generator.context.GeneratorContext;

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
