ice.yui.slider = {
   register:function(clientId, varName, callback, yuiProps, jsfProps) {
        var obj = null;
        ice.yui.loadModule('slider');
        ice.yui.use(function(Y){
                obj = new Y.Slider({
                    thumbImage: 'http://yui.yahooapis.com/3.0.0/build/slider/assets/skins/sam/thumb-classic-x.png'
                }).render('.class'+ varName);
                logger.info('Object created '+ obj);
                
                var sliderElement = document.getElementById(clientId);
                
                //name of the event that will invoke submit
                var submitOn = jsfProps.submitOn;
                
                //submit handler
                submitHandler = function(event) {
                    var singleSubmit = jsfProps.singleSubmit;
                    var sliderValue = obj.get('value');
                    var params = function(parameter) {
                        parameter(clientId+'_value', sliderValue);
                    };
                    if (singleSubmit) {
                        ice.singleSubmit(event, sliderElement, params); 
                    } else {
                        ice.submit(event, sliderElement, params);                    
                    }                    
                };
                obj.after(submitOn, submitHandler);
                
                callback(obj);
            });
   },
   
   updateProperties:function(clientId, varName, yuiProps, jsfProps, events) {
	     ice.yui.updateProperties(clientId, varName, yuiProps, jsfProps, events, this);
   },
   
   getInstance:function(clientId, callback, varName) {
        ice.yui.getInstance(clientId, varName, this, callback);
   }
};

