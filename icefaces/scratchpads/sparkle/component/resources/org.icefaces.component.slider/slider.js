ice.yui.slider = {
   register:function(clientId, varName, callback, yuiProps, jsfProps) {
        var obj = null;
        ice.yui.loadModule('slider');
        ice.yui.use(function(Y){
                var _thumbImage;
                if (jsfProps['thumbImage']) {
                    _thumbImage = jsfProps['thumbImage'];
                } else {
                    _thumbImage= 'http://yui.yahooapis.com/3.0.0/build/slider/assets/skins/sam/thumb-classic-x.png';                
                }
              
                obj = new Y.Slider({
                    axis: yuiProps.axis,
                    thumbImage:_thumbImage 
                }).render('.class'+ varName);
                logger.info('Object created '+ obj);
                
                var sliderElement = document.getElementById(clientId);
                
                var submitHandler = null;

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
                                
                //name of the event that will invoke submit
                var submitOn = jsfProps.submitOn;

                if (submitOn == 'slideInterval') {
                    //this event is not known by YUI3 slider, this represents "thumbDrag" event
                    //now set the submitOn with "thumbDrag"
                    submitOn = 'thumbDrag';

                    var slideInterval = jsfProps.slideInterval;   
                    //check range         
                    if (slideInterval < 100)
                        slideInterval = 100;
                    else if (slideInterval > 1000) {
                        slideInterval = 1000;
                    }    
                    logger.info('sliderInterval '+ slideInterval);
                    //create a slider timeout handler
                    var sliderTimeoutHandler = null;
                    
                    //get the reference of submitHandler
                    var originalSubmitHandler = submitHandler;
                    
                    //create a timeout based submitHandler
                    submitHandler = function(event) {
                        if (sliderTimeoutHandler != null) return;

                        sliderTimeoutHandler = setTimeout (function(){
                            //invoke submit
                            originalSubmitHandler(event);
                            //cleanup
                            clearTimeout(sliderTimeoutHandler);
                            sliderTimeoutHandler = null;
                        },  slideInterval);
                    }
                } 
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

