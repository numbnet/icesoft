ice.skin = "yui-skin-sam";

ice.yui3.effectHelper = {
    effects:{},
 
	serialize:function(Y, effect) {
		if (effect != null) {
			var elementId;
			var element  = effect.get('node')._node;
			if (effect.getContainerId() != null) {
				elementId = effect.getContainerId();
			} else {
				elementId = element.id;

			}   
			var effectElementId = 'effect_'+ elementId;
	        var effectStyleElementId = 'effect_style'+ elementId;
			
		 
			var effectElement = document.getElementById(effectElementId);
            var effectClass = effect.effectClass+ "To";
			if (effect.revert  && effect.get('reverse')) {
				effectClass = effect.effectClass+ "From";
			}
			var _form = formOf(element);	
			if (!effectElement) {
				effectElement = this.createHiddenField(_form, effectElementId);
			}
			effectElement.value = effectClass;
			
			var sourceId = Y.Node.getDOMNode(effect.get('node')).id;
			var effectStyleElement = document.getElementById(effectStyleElementId);			
			  if (!effectStyleElement) {
				effectStyleElement = this.createHiddenField(_form, effectStyleElementId);
			  }
			  effectStyleElement.value = document.getElementById(sourceId).style.cssText ;
		}
	},
	
	getStyledAnimElement:function(Y, effect) {
		var node = Y.one('#styledAnimElement');
		if (!node) {
	       var div = document.createElement('div'); 
	 	   div.setAttribute('style', 'display:none');
	 	   div.setAttribute('class', ice.skin);
	 	   window.document.body.appendChild(div);
		   
	       cdiv = document.createElement('div'); 
	 	   cdiv.setAttribute('style', 'display:none');
	 	   cdiv.setAttribute('class', effect.componentStyleClass);
		   
		   div.appendChild(cdiv);
		   
	       var field = document.createElement('input'); 
		   field.setAttribute('type', 'hidden');
		   field.setAttribute('id', 'styledAnimElement');	   
		   cdiv.appendChild(field);
		   node = Y.one('#styledAnimElement');
		}
		return node;
	},
	
	createHiddenField:function(parent, id) {
	   var field = document.createElement('input'); 
	   field.setAttribute('type', 'hidden');
	   field.setAttribute('id', id);
	   field.setAttribute('name', id);
	   parent.appendChild(field);
	   return field;
   }	

};
ice.yui3.effects = {};
YUI().use("anim", "json", function(Y) {
	var _one = Y.one;
	Y.one = function(id) {
		id = id.replace(':', '\\:');
		return _one(id);
	}
    function  EffectBase(param, callback) {
		this.orignalRun = EffectBase.superclass.run;
		this.effectClass = "";
		this.containerId;
		this.param = param;
		this.revert = (param['revert']);
		this.preRevert = callback;
		this.componentStyleClass = param['componentStyleClass'] || 'dummy';
		
        if (Y.Lang.isString(param)) {
            EffectBase.superclass.constructor.apply(this, []);
            this.set('node', '#'+ param);
        } else {
            EffectBase.superclass.constructor.apply(this, arguments);        	
        }
        
        //if no argument being set, send the empty object
        if (!param || Y.Lang.isString(param)) {
            param = {};
        }
        this.on('durationChange', function() {
        	this.durationChanged = true;
        });
        
        this.setDefaults(param);
		this.on('start', this.beforeStart);
		this.on('end', this.afterComplete);
		
		this.on('end', function() {
			ice.yui3.effectHelper.serialize(Y, this);	
		
			if (this.revert) {
				if (this.get('reverse')) {
				   if (this.chainEffect) {
				       this.chainEffect();
				   }
					return; 
				}
				if (this.preRevert) {
					this.preRevert(this);
				}
				this.set('reverse', true);
				
				this.run();
				
			} else {
					   if (this.chainEffect) {
						   this.chainEffect();
					   }
			}
		}); 
  		
    }
 
    Y.extend(EffectBase, Y.Anim , {
		beforeStart: function() {},
		afterComplete: function() {},		
        setDefaults: function() {},
        chain: function(effect, preEffect) {
        	this.chainEffect = function() {
        		if (Y.Lang.isFunction(preEffect)) {
        			preEffect();	
        		}
        		if (!effect.get('node')) {
        			effect.set('node', this.get('node'));
        		}
        		if (!effect.durationChanged) {
        			effect.set('duration', this.get('duration'));
        		}        		
        		effect.run();
			}
        },
		
		setContainerId: function(cid) {
			this.containerId = cid;
		},
		
		
		getContainerId: function() {
			return this.containerId;
		},
		
		run: function() {
 	
    console.info('addding classs '+ this.effectClass);
		    this.get('node').addClass(this.effectClass+"From");
			this.setDefaults();
			this.orignalRun();

		},
		
		setPreRevert: function(callback) {
			this.preRevert = callback;
		},
		
		setPeerName: function(peerName) {
			this.peerName = peerName;
		},
 
		getPeer:function() {
			     if(this.peerName) {
					return new ice.yui3.effects[this.peerName](this.param);
			     } else {
					console.info('No peer is defined for this component');
					return this;					
				 }
              
		},
		
		getEffectClass: function() {
			return this.effectClass;
		}

			
    });

    function  Fade(param, callback) {
    	Fade.superclass.constructor.apply(this, arguments);
		this.effectClass = "Fade";
    }
 
    Y.extend(Fade, EffectBase, {
	    beforeStart:function() {
			if (this.get("reverse")) {
				this.get("node").setStyle('visibility', 'visible');		
				this.get("node").setStyle('opacity', '0');		
			}
		},
		
		
        setDefaults: function() {
		      this.setPeerName('Appear');
              if (!this.param['to']) {
    			  var node = ice.yui3.effectHelper.getStyledAnimElement(Y, this);
				  
 				 node.addClass("FadeTo");

    			  var opacity = node.getStyle('opacity');
				  if (!opacity) {
				      opacity = this.get('node').getStyle('opacity');
				  }
				  
				  if (!opacity || Y.UA.ie > 0) {
				      opacity = 0;
				  }
            	  this.set('to', {
            		  'opacity': opacity
            		  });
				node.removeClass("FadeTo");	  
    		 		  
              }
              if (!this.param['from']) {
    			  var node = ice.yui3.effectHelper.getStyledAnimElement(Y, this);

    			  node.addClass("FadeFrom");
    			  var opacity = node.getStyle('opacity');
				 
    			  if (!opacity) {
    				  opacity = this.get('node').getStyle('opacity');
    			  }
				  
				  if (!opacity || Y.UA.ie > 0) {
				      opacity = 1;
				  }				  
   
            	  this.set('from', { 
            		  'opacity': opacity
            		  });
            	  node.removeClass("FadeFrom");
              }             
        },
		
		getEffectClass: function() {
			if (this.get("reverse")) {
				return "Appear";
			} else {
				return this.effectClass;
			}
		}
    });

    function  Appear(param, callback) {
	     console.info('new Appear '+ param);
        Appear.superclass.constructor.apply(this, arguments);
		this.effectClass = "Appear";
		this.peerName = "Fade";
    }
 
    Y.extend(Appear, EffectBase, {
        setDefaults: function() {
              if (!this.param['to']) {
    			  var node = ice.yui3.effectHelper.getStyledAnimElement(Y, this);
				  
 				 node.addClass("AppearTo");

    			  var opacity = node.getStyle('opacity');
				  if (!opacity) {
				      opacity = this.get('node').getStyle('opacity');
				  }
				  
				  if (!opacity) {
				      opacity = 0;
				  }
				  
            	  this.set('to', {
            		  'opacity': opacity
            		  });
				node.removeClass("AppearTo");	  
    		 		  
              }
              if (!this.param['from']) {
    			  var node = ice.yui3.effectHelper.getStyledAnimElement(Y, this);

    			  node.addClass("AppearFrom");
    			  var opacity = node.getStyle('opacity');
				 
    			  if (!opacity) {
    				  opacity = this.get('node').getStyle('opacity');
    			  }
				  
				  if (!opacity) {
				      opacity = 1;
				  }				  
   
            	  this.set('from', { 
            		  'opacity': opacity
            		  });
            	  node.removeClass("AppearFrom");
              }  	  
    	}        
    });

    function  BlindUp(param, callback) {
        BlindUp.superclass.constructor.apply(this, arguments);
		this.effectClass = "BlindUp";		
    }
 
    Y.extend(BlindUp, EffectBase, {
        setDefaults: function(param) {
	        if (!param['from']) {
	          this.set('from', {height: function(node) {
			    return node.get('scrollHeight');
			  }});
	        }
			
			if (!param['to']) {
				this.set('to', {height:0});
			}
    	}        
    });


    function  BlindDown(param, callback) {
        BlindDown.superclass.constructor.apply(this, arguments);
		this.effectClass = "BlindUp";		
    }
 
    Y.extend(BlindDown, EffectBase, {
        setDefaults: function(param) {
	        if (!param['to']) {
	          this.set('to', {height: function(node) {
			  
			    return node.get('scrollHeight');
			  }});
	        }
			
			if (!param['from']) {
				this.set('from', {height:0});
			}
    	}        
    });
	

    function  Highlight(param, callback) {
    	Highlight.superclass.constructor.apply(this, arguments);
		this.effectClass = "Highlight";
    }
 
    Y.extend(Highlight, EffectBase, {
        setDefaults: function() {
              if (!this.param['to']) {
    			  var node = ice.yui3.effectHelper.getStyledAnimElement(Y, this);
				  
 				 node.addClass("HighlightTo");

    			  var bgColor = node.getStyle('backgroundColor');
            	  this.set('to', {
            		  backgroundColor: bgColor
            		  });
				node.removeClass("HighlightTo");	  
    		 		  
              }
              if (!this.param['from']) {
    			  var node = ice.yui3.effectHelper.getStyledAnimElement(Y, this);

    			  node.addClass("HighlightFrom");
    			  var bgColor = node.getStyle('backgroundColor');
				 
    			  if (!bgColor) {
    				  bgColor = this.get('node').getStyle('backgroundColor');
    			  }
   
            	  this.set('from', { 
            		  backgroundColor: bgColor
            		  });
            	  node.removeClass("HighlightFrom");
              }              
        }
    });
    ice.yui3.effects['Fade'] = Fade;
    ice.yui3.effects['Appear'] = Appear;
    ice.yui3.effects['BlindUp'] = BlindUp;	
    ice.yui3.effects['BlindDown'] = BlindDown;		
    ice.yui3.effects['Highlight'] = Highlight;	
});

