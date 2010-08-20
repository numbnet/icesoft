package org.icefaces.component.effects;

public abstract class Effect {
	private String fromStyleClass;
	private String toStyleClass;
	private String name;
	
	Effect(String name) {
		this.name = name;
	}
	
	public String getScript(String elementId) {
		String script = "new ice.yui3.effects['" + name +"']('"+ elementId +"').run()";
		System.out.println("SCRIPT CALL : "+ script);
		return script;
	}
	
	public interface Iterator {
		public void next (String name, EffectBehavior effectBehavior);
	}
}
