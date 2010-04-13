ice.yui.slider = {
   initialize:function(clientId, yuiProps, jsfProps, bindYUI) {
        var obj = null;
        var root = document.getElementById(clientId);
        //load slider module
        ice.yui.loadModule('slider');
        
        //set a callback to create slider component 
        ice.yui.use(function(Y){
                obj = new Y.Slider({
                    //following two properties has to be set initlization time
                    axis: yuiProps.axis,
                    thumbImage: jsfProps.thumbImage 
                }).render(root);
                
                var submitHandler = null;

                //submit handler
                submitHandler = function(event) {
                    var singleSubmit = jsfProps.singleSubmit;
                    var sliderValue = obj.get('value');
                    if (jsfProps.aria) {
                        document.getElementById(clientId).firstChild.setAttribute("aria-valuenow", sliderValue);
                    } 
                    var params = function(parameter) {
                        parameter(clientId+'_value', sliderValue);
                    };
                    if (singleSubmit) {
                        ice.singleSubmit(event, root, params); 
                    } else {
                        ice.submit(event, root, params);                    
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
                
                //add aria support
                if (jsfProps.aria) {
                    //add roles and attributes to the YUI widget
                    root = document.getElementById(clientId);
                    root.firstChild.setAttribute("role", "slider");
                    root.firstChild.setAttribute("aria-valuemin", yuiProps.min);
                    root.firstChild.setAttribute("aria-valuemax", yuiProps.max);
                    root.firstChild.setAttribute("aria-valuenow",yuiProps.value );
                    root.firstChild.setAttribute("tabindex",jsfProps.tabindex);
                    var step = 5;
                    //listen for keydown event, to react on left, right, home and end key 
                    Y.on("keydown", function(event) {
                        //get the current value of the slider
                        var valuebefor = valuenow = parseInt(root.firstChild.getAttribute("aria-valuenow"));
                          
                        var isLeft = event.keyCode == 37;
                        var isUp = event.keyCode == 38;
                        var isRight = event.keyCode == 39;
                        var isDown = event.keyCode == 40;                        
                        var isHome = event.keyCode == 36;
                        var isEnd = event.keyCode == 35;

                        if (isLeft || isDown) {
                           if ((valuenow - step) >= yuiProps.min) {
                              valuenow -= step;
                           }
                        } else if (isRight || isUp) {
                           if (yuiProps.max >= valuenow + step) {
                              valuenow += step;
                           }
                        } else if (isHome) {
                           valuenow = yuiProps.min;
                        } else if (isEnd) {
                           valuenow = yuiProps.max;		
                        }

                        //if value change?
                        if (valuebefor != valuenow)  { 
                            //update slider value on client                       
                            obj.set('value', valuenow);
                            //notify server
                            obj.fire(submitOn, submitHandler);
                            //cancel event
                            event.halt();
                        }
                    }, root);
                }
                bindYUI(obj);
            });
   },
   
   updateProperties:function(clientId, yuiProps, jsfProps, events) {
	     ice.yui.updateProperties(clientId, yuiProps, jsfProps, events, this);
   },
   
   getInstance:function(clientId, callback) {
        ice.yui.getInstance(clientId, callback, this );
   }
};

