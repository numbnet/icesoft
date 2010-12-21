package org.icefaces.generator.artifacts;

import org.icefaces.generator.context.ComponentContext;

public abstract class Artifact {
	ComponentContext componentContext;
	
	public ComponentContext getComponentContext() {
		return componentContext;
	}

	public void setComponentContext(ComponentContext componentContext) {
		this.componentContext = componentContext;
	}

	public Artifact(ComponentContext componentContext) {
		this.componentContext = componentContext;
	}
	
	public String getName() {
		return this.getClass().getSimpleName();
	}
	
	public abstract void build();
}
