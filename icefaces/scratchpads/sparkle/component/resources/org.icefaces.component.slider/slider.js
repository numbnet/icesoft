ice.yui3.slider = {
   initialize:function(clientId, yuiProps, jsfProps, bindYUI) {
        
        //get the slider html element
        var root = document.getElementById(clientId);
        
        //for short cut and to make it more like OO
        var super = ice.yui3;
        
        //load slider module
        super.loadModule('slider');
        
        //represents JS Slider object
        var obj = null; 
               
        //set a callback to create slider component 
        super.use(function(Y){
        
                //initilize a YUI slider here
                obj = new Y.Slider({
                    //following two properties has to be set when initializing componnent
                    axis: yuiProps.axis,
                    thumbUrl: jsfProps.thumbUrl
                }).render(root);
                
                //create a generic submit handler
                var submitHandler = function(event) {
                    //get the value of singleSubmit property
                    var singleSubmit = jsfProps.singleSubmit;
                    
                    //get the current value of slider
                    var sliderValue = obj.get('value');
                    
                    //if aria is enabled, then set aria property so screen reader can pick it
                    if (jsfProps.aria) {
                        document.getElementById(clientId).firstChild.setAttribute("aria-valuenow", sliderValue);
                    } 
                    
                    //slider doesn't use a hidden field, so create a param
                    var params = function(parameter) {
                        //param tha represents value of slider
                        parameter(clientId+'_value', sliderValue);
                        
                        //callback to revertback the slider value in case if current slider value
                        //doesn't match with last known server value. It could be due to 
                        //the validation error.
                        parameter('onevent', function(data) { 
                                //we want to execute after DOM Update was compeleted.                                
                                if (data.status == 'success') {
                                    //last known value
                                    var lastKnownValue = super.getJSContext(clientId).getJSProps().value;   
                                    if (lastKnownValue != sliderValue) {
                                         obj.set('value', lastKnownValue);  
                                    }
                                }
                         });
                                           
                    };
                    
                    if (singleSubmit) {
                        ice.singleSubmit(event, root, params); 
                    } else {
                        ice.submit(event, root, params);                    
                    }                    
                };
                                
                //the value of submitOn property represents the event name that 
                //is reponsible to invoke a submit. The valid values are slideStart,
                //slideEnd and slideInterval.
                var submitOn = jsfProps.submitOn;

                //as "slideInterval" is not known by the YUI slider, and its 
                //created by us, which internally uses "thumbMove" event of slider.
                if (submitOn == 'slideInterval') {

                    //now set the submitOn with origional value "thumbMove"
                    submitOn = 'thumbMove';

                    //get the slideInterval time 
                    var slideInterval = jsfProps.slideInterval;   
                    
                    //boundary test         
                    if (slideInterval < 100)
                        slideInterval = 100;
                    else if (slideInterval > 1000) {
                        slideInterval = 1000;
                    }    

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
                //register submit handler
                obj.after(submitOn, submitHandler);
                
                //add aria support
                if (jsfProps.aria) {
                    //add roles and attributes to the YUI slider widget
                    root = document.getElementById(clientId);
                    root.firstChild.setAttribute("role", "slider");
                    root.firstChild.setAttribute("aria-valuemin", yuiProps.min);
                    root.firstChild.setAttribute("aria-valuemax", yuiProps.max);
                    root.firstChild.setAttribute("aria-valuenow",yuiProps.value );
                    root.firstChild.setAttribute("tabindex",jsfProps.tabindex);

                    //TODO shouldn't it be configurable?
                    var step = 5;

                    //listen for keydown event, to provide short-cut key support.
                    //react on left, right, up, down, home and end key 
                    Y.on("keydown", function(event) {
                        //get the current value of the slider
                        var valuebefore = valuenow = parseInt(root.firstChild.getAttribute("aria-valuenow"));
                          
                        var isLeft = event.keyCode == 37;
                        var isUp = event.keyCode == 38;
                        var isRight = event.keyCode == 39;
                        var isDown = event.keyCode == 40;                        
                        var isHome = event.keyCode == 36;
                        var isEnd = event.keyCode == 35;

                        if (isLeft || isDown) {
                           //decrease slider value
                           if ((valuenow - step) >= yuiProps.min) {
                              valuenow -= step;
                           }
                        } else if (isRight || isUp) {
                           //increase slider value
                           if (yuiProps.max >= valuenow + step) {
                              valuenow += step;
                           }
                        } else if (isHome) {
                           //get the min value of slider 
                           valuenow = yuiProps.min;
                        } else if (isEnd) {
                           //get the max value of slider 
                           valuenow = yuiProps.max;		
                        }

                        //if value changed?
                        if (valuebefore != valuenow)  { 
                            //update slider value on client                       
                            obj.set('value', valuenow);
                            //notify server
                            obj.fire(submitOn, submitHandler);
                            //cancel event
                            event.halt();
                        }
                    }, root);
                }
                //bind the initilized js component, so it can be reused for later calls
                bindYUI(obj);
            });
   },
   
   //delegate call to ice.yui3.updateProperties(..)  with the reference of this lib
   updateProperties:function(clientId, yuiProps, jsfProps, events) {
       ice.yui3.updateProperties(clientId, yuiProps, jsfProps, events, this);
   },
 
   //delegate call to ice.yui3.getInstance(..) with the reference of this lib 
   getInstance:function(clientId, callback) {
       ice.yui3.getInstance(clientId, callback, this);
   }
};

