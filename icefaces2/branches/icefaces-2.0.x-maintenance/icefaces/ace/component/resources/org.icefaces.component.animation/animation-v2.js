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

ice.yui3.use(function(Y) {

    var anim;

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
					   console.info('revering chain ');
					   console.info('revering chain current anim '+ currentAnim);
					   try {
							currentAnim = currentAnim.previous();
							if (currentAnim == starter) {
								//Anim can not go beyond the starting point
								throw ("finished");
							}
					   } catch (e){
							console.info(e);
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
						console.info('Chain list ends...' + lastRunningAnim);
						
					} 
					
					if (currentAnim) {
						   console.info( 'going to run '+ currentAnim.effectName);		
							endhandle = currentAnim.on("end", function() { 
								   endhandle.detach();
								   starter.fire("end");
							});				
							currentAnim.run();
						}
				
				
				
				
				
				});
			}
			
			console.info( 'going to run '+ starter.effectName);	
			starter.run();
		}
	

	function AnimBase(params) { 
		AnimBase.superclass.constructor.apply(this, arguments); 
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
		    console.info(''+ index +  ' :  '+ this.list.length);
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
				effectStyleElement = this.createHiddenField(_form, effectStyleElementId);
			  }
			  effectStyleElement.value = document.getElementById(sourceId).style.cssText ;			
		},
		
		createHiddenField:function(parent, id) {
		   var field = document.createElement('input'); 
		   field.setAttribute('type', 'hidden');
		   field.setAttribute('id', id);
		   field.setAttribute('name', id);
		   parent.appendChild(field);
		   return field;
	    }	
	});
	

	
	// create a Fade effect
	function Fade(params) {console.info('Fade constructor ');
		Fade.superclass.constructor.apply(this, arguments);  
		this.effectName = "Fade";
 		this.set("to", {opacity:0});
		this.set("from", {opacity:1});
		this.cycle = false;
		this.on("prerequisite", function() {
			if (this.get("reverse")) {
				this.get("node").setStyle("visibility", "visible");
				this.get("node").setStyle("opacity", "0");			
			}
		})
	}
	
	Y.extend(Fade, AnimBase , { 

	});
	
	
	// create a Highlight effect
	function Highlight(params) {console.info('Highlight constructor ');
		Highlight.superclass.constructor.apply(this, arguments);  
		this.effectName = "Highlight";
 		this.set("to", {backgroundColor:"red"});
		this.set("from", {backgroundColor:"yellow"});
		this.set("iterations", 1);
		this.cycle = false;
	}
	
	Y.extend(Highlight, AnimBase , { 
	});
	//------ Register Fade effect
	
	ice.yui3.effects["Fade"] = Fade;
	ice.yui3.effects["Highlight"] = Highlight;	
 	
	
	//---- Animation plugin
	function AnimPlugin(config) { 
		AnimPlugin.superclass.constructor.apply(this, arguments);
		this.get("host")["animList"] = {}; 
    }
	AnimPlugin.NAME = 'AnimPlugin';
	AnimPlugin.NS = "animation";


	Y.extend(AnimPlugin, Y.Plugin.Base, {
		add: function(args) {  
		   var event = args.event;
		   var animations = this.get("host")["animList"];
		       
             
		        var effect = new ice.yui3.effects[args.name](args); 
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
	
 
	ice.animation = {
		register: function(args) {
		   console.info(args.node + ' registering '+ args.name);
		   var node = Y.one(args.node);
		   if (!node.animation) {
				node.plug(AnimPlugin);
		   }
		   return node.animation.add(args);
        },
		
		run: function(args) {
			new ice.yui3.effects[args.name](args).run();
		}
	}	
	 
});

    

