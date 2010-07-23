ice.yui3.slider = {
   initialize:function(clientId, yuiProps, jsfProps, bindYUI) {
        
        //get the slider html element
        var root = document.getElementById(clientId);
        
        //for short cut and to make it more like OOP
        var super = ice.yui3;

        var Dom = YAHOO.util.Dom;
        
        //load slider module
        super.loadModule('slider');

        var hiddenField = Dom.get(clientId+"_hidden");
        
        //set a callback to create slider component 
        super.use(function(Y){
        
                //initilize a YUI slider here
                var obj = new Y.Slider({
                    //following two properties has to be set when initializing componnent
                    axis: yuiProps.axis,
                    thumbUrl: jsfProps.thumbUrl
                }).render(root);
                
                //create a generic submit handler
                // Depending if submitOn is slideStart, slideEnd or slideInterval,
                //  this will be called either when the user commences sliding,
                //  finishes sliding, or as they're dragging the slider around,
                //  if an interval of time passes when they've paused sliding.
                // If aria is enabled, it will also submit after one of the
                //  relevant keypresses has been made.
                var submitHandler = function(event) {
                    //get the value of singleSubmit property
                    var singleSubmit = jsfProps.singleSubmit;
                    
                    //get the current value of slider
                    var sliderValue = obj.get('value');
                    
                    //if aria is enabled, then set aria property so screen reader can pick it
                    if (jsfProps.aria) {
                        // Because we're in a callback, to be safe we'll lookup
                        //  the current root element again, instead of using
                        //  the old root reference, which might be stale.
                        document.getElementById(clientId).firstChild.setAttribute("aria-valuenow", sliderValue);
                    }

                    // Strategy is now to use hidden field rather than
                    // request map value

                    hiddenField.value=sliderValue;
                    
                    if (singleSubmit) {
                        ice.singleSubmit(event, root);
                    } else {
                        ice.submit(event, root);                    
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
                    Y.on("click", function(event) {
                    	root.firstChild.focus();
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

