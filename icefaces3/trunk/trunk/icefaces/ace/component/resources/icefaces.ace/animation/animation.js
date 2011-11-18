 

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

ice.util = {
	createHiddenField : function(parent, id) {
	   var inp = document.createElement("input"); 
	   inp.setAttribute('type', 'hidden');
	   inp.setAttribute('id', id);
	   inp.setAttribute('name', id);
	   parent.appendChild(inp);	   
	   return inp;
	},
	
	createElement:function(parent, name) {
		var element = document.createElement(name); 
		parent.appendChild(element);
		return element;
	},
	
	removeElement: function(element) {
		element.parentNode.removeChild(element);
	}
}

 
ice.animation = {
	events : ["transition", "click", "hover"],
	animations : ["fade", "highlight"],
	defaultAnimations:{},
	loadDefaultAnims: function(Y) {
	 
		Y.on("domready", function() {
			var element = ice.util.createElement(document.body, "div");
			element.setAttribute("id", "themeElement");
			element.setAttribute("class", "default_display_value");
			var node = Y.one("#themeElement");
			
			node.addClass("default_display_value"); 
			default_display_value = node.getStyle("display");
			if("none" != default_display_value) {
				//console.info("default_display_value must be set to none, to register theme based animations");
				return;
			}
			
			for (i=0; i< ice.animation.events.length; i++) {
				for (j=0; j< ice.animation.animations.length; j++) {
					var styleClass = 'default_' + ice.animation.events[i] + '_'+  ice.animation.animations[j];
					node.addClass(styleClass);
					var display = node.getStyle("display");
					if (display == "block") {
						ice.animation.defaultAnimations[ice.animation.events[i]] = ice.animation.animations[j];
						//console.info(ice.animation.animations[j] + " registered for "+ ice.animation.events[i]);
					}
					node.removeClass(styleClass);
				}
			}
			ice.util.removeElement(element);
		}, Y);
	},
	
	getAnimation: function (clientId, eventName) {
	    var node = ice.yui3.y.one('#'+ clientId);
 	   
        var animation = null;
	    if (node) {
			if (node["animation"]) {
				animation = node.animation.getAnimation(eventName);
			} else if (node._node.animation) {//backdoor for IE
				animation = node._node.animation.getAnimation("transition");
			} else {//none of the effect has been defined by the developer, return the theme default if any
			    if (ice.animation.defaultAnimations[eventName]) { 
					animation = ice.animation.register({name: ice.animation.defaultAnimations[eventName], event:eventName, node:'#'+ clientId});
				}
				 
			}
	    } 
		return animation;
	}
};


var anim;
ice.yui3.effects = {};
ice.yui3.use(function(Y) {
	//------ Escape client ID

	function AnimPlugin(config) { 
			AnimPlugin.superclass.constructor.apply(this, arguments);
			this.get("host")["animList"] = {}; 
		}
		AnimPlugin.NAME = 'AnimPlugin';
		AnimPlugin.NS = "animation";


		Y.extend(AnimPlugin, Y.Plugin.Base, {
			add: function(args, effect) { 
			   var event = args.event;
			   var animations = this.get("host")["animList"];
			   


				var index = -1;
				if (!animations[event]) {
					animations[event] = [];
					index = 0;
				} else { //more then one effects are define on same event chain them 
					index = [animations[event].length];
				}
				effect.index = index;
				effect.event = event;
				effect.list = animations[event];
				animations[event][index] = effect;	
				this.get("host")._node.animation = this;								
				return effect;
			},
		
			getAnimation: function(eventName) { 
			   for (a in this.get("host").animList) {
					if (a == eventName) {
						return this.get("host").animList[a][0]; 
					}
				}
				return null;
			}
		});	
	ice.animation.AnimPlugin = AnimPlugin;
	ice.animation.loadDefaultAnims(Y);
	
    function chain(ref) {
		this.anim = ref;
		this.anim.on("chainend", function() {
			this.serialize();
		});
	}
	
	
	chain.prototype.set = function(name, value) {
		for (i = this.anim.index; i < this.anim.list.length; i++) {
			this.anim.list[i].set(name, value);
		}
	}
	 
	chain.prototype.on = function(name, callback) {
		if (name == "end") {
			this.anim.on("chainend", callback);
		}
	}	 


		/*
			This method can be used as anim.run(). The difference is that anim.run() would run  
			animation on source anim only while anim.chain.run() would run animation on source and all of 
			the animations bounded to the same event type. 

			The chain is based on 0 based index. If a chain is being run from index 2 so only 
			animation on higher indexs will be a part of a chain
		
		*/
	 
	chain.prototype.run = function(toggleReverse) {
			starter = this.anim;
			var currentAnim = this.anim;
			if(!starter.chainRunEndInstallted) {
				starter.on("end", function() {
					starter.chainRunEndInstallted = true;
					var lastRunningAnim = currentAnim;
					
					if (!starter.toggleReverse) {
						if (!currentAnim){ 
						   currentAnim = starter.next();
						} else {
						   currentAnim = currentAnim.next();
						}
					} else {
					   currentAnim.set("reverse", !currentAnim.get("reverse"));
					   //chain should be reversed should get previous anim
					  // console.info('revering chain ');
					 //  console.info('revering chain current anim '+ currentAnim);
					   try {
							currentAnim = currentAnim.previous();
							if (currentAnim == starter) {
								//Anim can not go beyond the starting point
								throw ("finished");
							}
					   } catch (e){
							//console.info(e);
							//END point when toggleReverse
							return;
							
					   }
					   currentAnim.set("reverse", !currentAnim.get("reverse"));
					}
					
					
					
					if (!currentAnim) {
				
						if (toggleReverse) {
							starter.toggleReverse = true;
						} else {
						   //it means chain only have one 
						   starter.fire("chainend");
						   return;
						}
						currentAnim = lastRunningAnim;
						currentAnim.set("reverse", !currentAnim.get("reverse"));
						//END point of first run
						starter.fire("chainend");
						//console.info('Chain list ends...' + lastRunningAnim);
						
					} 
					
					if (currentAnim) {
						   //console.info( 'going to run '+ currentAnim.effectName);		
							endhandle = currentAnim.on("end", function() { 
								   endhandle.detach();
								   starter.fire("end");
							});				
							currentAnim.run();
						}
				
				
				
				
				
				});
			}
			
			//console.info( 'going to run '+ starter.effectName);	
			starter.run();
		}
	

	function AnimBase(params) { 

		this.params = params;
 
		AnimBase.superclass.constructor.apply(this, arguments); 
		if (!this.params['duration']) {
			this.set("duration", 0.5);
		} else {
			try {
		    //miliseconds to second
				this.set("duration", this.params['duration'] / 1000);
			} catch (e) {
				this.set("duration", 0.5);
			}
		}
	    if (params['easing']) {  
			//TODO check for valid names
			this.set("easing", Y.Easing[params['easing']]);
		}		

		this.cloneNode = Y.one(params.node).cloneNode(false);
		this.cloneNode.setStyle("display", "none");
		var ancestor= Y.one(this.params["node"]).ancestor();
		ancestor.insert(this.cloneNode); 					
		this.setInitialValues();
 		ancestor.removeChild(this.cloneNode); 	
		this.chain = new chain(this);
		this.toggleReverse = false;
		this.containerId = null;
		this.on("start", function() {
			this.fire("prerequisite");
		});
	}
	
	Y.extend(AnimBase, Y.Anim , {
		/*
			helper for chain execution
		*/
		next: function() {
			var index = parseInt(this.index);
		    //console.info(''+ index +  ' :  '+ this.list.length);
			if (index < this.list.length) {
				return this.list[++index];
			} else {
				alert("no more elements");
			}
		},
		
		/*
			helper for chain execution
		*/		
		previous: function() {
			if (this.index >0) {
				return this.list[this.index - 1];
			} else {
				throw ("no more elements");
			}
		},
		
		getContainerId: function() {
			return this.containerId;
		},
		
		/*
			Only required by those components which doesn't apply effect or root element instead a sub element. In that case
			setting containerId exclusivly helps anim.serialize() to identify source component's clientid.
			
		*/
		setContainerId: function(cid) {
			this.containerId = cid;
		},
		
		/* 
		   responsible to set style of anim node to a hidden field, that would be used by AnimationBehavior.decode()
		   format: effect_style[animation_parent_clientid]
		*/
		serialize: function() {
			var elementId;
			var element  = this.get('node')._node;
			if (this.getContainerId() != null) {
				elementId = this.getContainerId();
			} else {
				elementId = element.id;

			}   
			var _form = formOf(element);
	        var effectStyleElementId = 'effect_style'+ elementId;
					
			var sourceId = Y.Node.getDOMNode(this.get('node')).id;
			var effectStyleElement = document.getElementById(effectStyleElementId);			
			  if (!effectStyleElement) {
				effectStyleElement = ice.util.createHiddenField(_form, effectStyleElementId);
			  }
			  effectStyleElement.value = document.getElementById(sourceId).style.cssText ;			
		},
		
		setInitialValues: function() {
			//console.info('setInitialValues is not overriden by the '+ this.effectName);
		},

		setInitialValue: function (param, prop, initVal, validator) {
			if (!this.params[param]) {
					this.cloneNode.addClass(this.effectName + '_'+  param);
					var val = this.cloneNode.getStyle(prop);
					this.cloneNode.removeClass(this.effectName + '_'+  param);
					//console.info(this.effectName + '_'+  param + "  : "+ val);
					if (!val) {
						val = initVal;
					}
					//console.info('looking for validator ');
					if (validator) {
					   
						val = validator(val);
					}
					//console.info('setting color '+ val );
					var obj = {};
					obj[prop] = val;
					this.set(param, obj);
			}
		}
	});
	

	
	// create a Fade effect
	function Fade(params) {//console.info('Fade constructor ');
		//must be set before calling super class constructor
		this.effectName = "fade";
		Fade.superclass.constructor.apply(this, arguments);  
 		this.cycle = false;
		this.on("prerequisite", function() {
			if (this.get("reverse")) {
				this.get("node").setStyle("visibility", "visible");
				this.get("node").setStyle("opacity", "0");			
			}
		})
	}
	
	Y.extend(Fade, AnimBase , { 
		setInitialValues: function() {
			this.setInitialValue("to", "opacity", 0);
			this.setInitialValue("from", "opacity", 1);
		}	
	});
	
	
	// create a Highlight effect
	function Highlight(params) {//console.info('Highlight constructor ');
		this.effectName = "highlight";
		Highlight.superclass.constructor.apply(this, arguments);  
 		this.set("iterations", 1);
		this.cycle = false;
	}
	
	Y.extend(Highlight, AnimBase , { 
		setInitialValues: function() {
			
			this.setInitialValue("to", "backgroundColor", "red");

			var node = this.params["node"];
			this.setInitialValue("from", "backgroundColor", "yellow", function(val) {
			   //console.info('executring validator');
			   if (val.toLowerCase() == "transparent") {
				   val = "white"; //most common case
				   node = Y.one(node);
				   pnode = node.ancestor(function(ele) {  
					if(ele.getStyle("backgroundColor").toLowerCase() != "transparent") 
						return true;
					else
						return false;
				   }, true);
				   if(pnode) {
					  val = pnode.getStyle("backgroundColor");
				   }
			   }
			    
				return val;
			});
		}	
	});
	
	

	
	// create a Highlight effect
	function Anim(params) {//console.info('Highlight constructor ');
		this.effectName = "Anim";
		Anim.superclass.constructor.apply(this, arguments);  
	}
	
	Y.extend(Anim, AnimBase , { 
	
	});
	
	//------ Register Fade effect
	
	
	
	ice.yui3.effects["fade"] = Fade;
	ice.yui3.effects["highlight"] = Highlight;	
	ice.yui3.effects["anim"] = Anim;	
	
 	
});


ice.animation.register = function(args, callback) {
		   var effect  = null;
		   ice.yui3.use( function(Y) { 
		        //console.info("registering "+ args.name);
				effect = new ice.yui3.effects[args.name.toLowerCase()](args); 
				
	//---- Animation plugin
				var node = Y.one(args.node);
				 
				if (!node["animation"]) { 
					node.plug(ice.animation.AnimPlugin);
				}

			    anim = node.animation.add(args, effect);
			 
		   }); 
		   return effect;

}
		
ice.animation.run = function(args) {
	new ice.yui3.effects[args.name.toLowerCase()](args).run();
}    

