ice.skin = "yui-skin-sam";

ice.yui3.effectHelper = {
    effects:{},
 
	serialize:function(effect) {
		if (effect != null) {
			var effectElementId;
			var element  = effect.get('node')._node;
			if (effect.getContainerId() != null) {
				effectElementId = effect.getContainerId();
			} else {
				effectElementId = element.id;

			}
			effectElementId = 'effect_'+ effectElementId;
	
			console.info('effect ele id '+ effectElementId);
			var effectElement = document.getElementById(effectElementId);
            var effectClass = effect.effectClass+ "To";
			if (effect.revert  && effect.get('reverse')) {
				effectClass = effect.effectClass+ "From";
			}

			console.info('serilaize revert'+ effect.revert + ' : '+ effect.get('reverse'));
			if (!effectElement) {
				var _form = formOf(element);		
				effectElementId = this.createHiddenField(_form, effectElementId);
				effectElementId.value = effectClass;
				console.info('effect ele created '+ effectElement);	
			}
		}
	},
	
	getStyledAnimElement:function(Y) {
		var node = Y.one('#styledAnimElement');
		if (!node) {
	       var div = document.createElement('div'); 
	 	   div.setAttribute('style', 'display:none');
	 	   div.setAttribute('class', ice.skin);
	 	   window.document.body.appendChild(div);
	       var field = document.createElement('input'); 
		   field.setAttribute('type', 'input');
		   field.setAttribute('id', 'styledAnimElement');	   
		   div.appendChild(field);
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
   },	

};
ice.yui3.effects = {};
YUI().use("anim", "json", function(Y) {
    function  EffectBase(param, callback) {
		this.orignalRun = EffectBase.superclass.run;
		this.effectClass = "";
		this.containerId;
		this.param = param;
		this.revert = (param['revert']);
		
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
		
		if (param['revert']) {
			this.on('end', function() {
				if (this.get('reverse')) {
				   if (this.chainEffect) {
				       this.chainEffect();
				   }
					return; 
				}
				if (callback) {
					callback(this);
				}
				console.info( ' NOOOO  '+ this.param.node);
				this.set('reverse', true);
				
				this.run();

			});
		} else {
			this.on('end', function() {
						if (this.chainEffect) {
						   this.chainEffect();
					   }
			});
		}
   		
    }
 
    Y.extend(EffectBase, Y.Anim , {
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
			this.setDefaults();
			this.orignalRun();
			ice.yui3.effectHelper.serialize(this);
		}		
    });

    function  Fade(param, callback) {
    	Fade.superclass.constructor.apply(this, arguments);
		this.effectClass = "Fade";
    }
 
    Y.extend(Fade, EffectBase, {
        setDefaults: function() {
              if (!this.param['to']) {
    			  var node = ice.yui3.effectHelper.getStyledAnimElement(Y);
    			  node.addClass("FadeTo");
            	  this.set('to', {opacity: node.getStyle('opacity')});
            	  node.removeClass("FadeTo");
              }
              if (!this.param['from']) {
    			  var node = ice.yui3.effectHelper.getStyledAnimElement(Y);
    			  node.addClass("FadeFrom");
            	  this.set('from', {opacity: node.getStyle('opacity')});
            	  node.removeClass("FadeFrom");
              }              
        }
    });

    function  Appear(param, callback) {
        Appear.superclass.constructor.apply(this, arguments);
		this.effectClass = "Appear";		
    }
 
    Y.extend(Appear, EffectBase, {
        setDefaults: function() {
        if (!this.param['to']) {    	
		  var node = ice.yui3.effectHelper.getStyledAnimElement(Y);
		  node.addClass("AppearTo");
    	  this.set('to', {opacity: node.getStyle('opacity')});
    	  node.removeClass("AppearTo");
        }
          if (!this.param['from']) {
			  var node = ice.yui3.effectHelper.getStyledAnimElement(Y);
			  node.addClass("AppearFrom");
        	  this.set('from', {opacity: node.getStyle('opacity')});
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
    			  var node = ice.yui3.effectHelper.getStyledAnimElement(Y);
    			  node.addClass("HighlightTo");
    			  var bgColor = node.getStyle('backgroundColor');
            	  this.set('to', {
            		  backgroundColor: bgColor
            		  });
            	  node.removeClass("HighlightTo");
              }
              if (!this.param['from']) {
    			  var node = ice.yui3.effectHelper.getStyledAnimElement(Y);
    			  node.addClass("HighlightFrom");
    			  var bgColor = node.getStyle('backgroundColor');
				   console.info('from style btnbg color '+ bgColor);
    			  if (!bgColor) {
    				  bgColor = this.get('node').getStyle('backgroundColor');
    			  }
    			  console.info('btnbg color '+ bgColor);
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

