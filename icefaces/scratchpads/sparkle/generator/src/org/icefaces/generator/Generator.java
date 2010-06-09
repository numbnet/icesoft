package org.icefaces.generator;

import java.util.Iterator;

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
