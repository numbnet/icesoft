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


		console.info('effect ele '+ effectElement);
		if (!effectElement) {
			var _form = formOf(element);		
			effectElementId = this.createHiddenField(_form, effectElementId);
			effectElementId.value = effect.effectClass;
			console.info('effect ele created '+ effectElement);	
		}
		
 
		}

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
    function  EffectBase(param) {
		this.orignalRun = EffectBase.superclass.run;
		this.effectClass = "";
		this.containerId;
		
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
    }
 
    Y.extend(EffectBase, Y.Anim , {
        setDefaults: function() {},
        chain: function(effect, preEffect) {
        	this.on('end', function() {
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
        	});
        },
		
		setContainerId: function(cid) {
			this.containerId = cid;
		},
		
		
		getContainerId: function() {
			return this.containerId;
		},
		
		run: function() {
			this.orignalRun();
			ice.yui3.effectHelper.serialize(this);
		}		
    });

    function  Fade(param) {
    	Fade.superclass.constructor.apply(this, arguments);
		this.effectClass = "Fade";
    }
 
    Y.extend(Fade, EffectBase, {
        setDefaults: function(param) {
              if (!param['to']) {
                this.set('to', {opacity:0});
              }
        }
    });

    function  Appear(param) {
        Fade.superclass.constructor.apply(this, arguments);
		this.effectClass = "Appear";		
    }
 
    Y.extend(Appear, EffectBase, {
        setDefaults: function(param) {
	        if (!param['to']) {
	          this.set('to', {opacity:1});
	        }
    	}        
    });

    function  BlindUp(param) {
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


    function  BlindDown(param) {
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
	

    ice.yui3.effects['Fade'] = Fade;
    ice.yui3.effects['Appear'] = Appear;
    ice.yui3.effects['BlindUp'] = BlindUp;	
    ice.yui3.effects['BlindDown'] = BlindDown;		
	
});

