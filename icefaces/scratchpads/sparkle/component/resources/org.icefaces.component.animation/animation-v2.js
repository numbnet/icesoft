//----- Set global Y
var Y = null;
function setY(y) {
	Y = y;
}


var anim;
YUI().use("plugin", "anim", "json",  function(Y) {

	setY(Y);
	
	//------ Escape client ID
	var _one = Y.one;
	Y.one = function(id) {
		id = id.replace(':', '\\:');
		return _one(id);
	}

		
    function chain(ref) {
		this.anim = ref;
	}
	
	 

	function AnimBase(params) { 
		this.chain = new chain(this);
		AnimBase.superclass.constructor.apply(this, arguments); 
		this.toggleReverse = false;

	}
	
	Y.extend(AnimBase, Y.Anim , { 
		next: function() {
			var index = parseInt(this.index);
		    console.info(''+ index +  ' :  '+ this.list.length);
			if (index < this.list.length) {
				return this.list[++index];
			} else {
				alert("no more elements");
			}
		},
		
		previous: function() {
			if (this.index >0) {
				return this.list[this.index - 1];
			} else {
				throw ("no more elements");
			}
		},

		runAsChain: function(toggleReverse) {
			starter = this;
			var currentAnim = this;
			
			starter.on("end", function() {
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
				   } catch (e){
						console.info(e);
						return;
						
				   }
				   currentAnim.set("reverse", !currentAnim.get("reverse"));
				}
				
				
				
				if (!currentAnim) {
			
					if (toggleReverse) {
						starter.toggleReverse = true;
					} else {
					   return;
					}
					currentAnim = lastRunningAnim;
					currentAnim.set("reverse", !currentAnim.get("reverse"));
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
			
			
			console.info( 'going to run '+ starter.effectName);	
			starter.run();
		}
	});
	

	
	// create a Fade effect
	function Fade(params) {console.info('Fade constructor ');
		Fade.superclass.constructor.apply(this, arguments);  
		this.effectName = "Fade";
 		this.set("to", {opacity:0});
		this.set("from", {opacity:1});
		this.cycle = false;
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
		   console.info('registering '+ args.name);
		   var node = Y.one(args.node);
		   if (!node.animation) {
				node.plug(AnimPlugin);
		   }
		   node.animation.add(args);
        },
		
		run: function(args) {
			new ice.yui3.effects[args.name](args).run();
		}
	}	
	 
});

    

