ice.component.slider = {
   initialize:function(clientId, yuiProps, jsfProps, bindYUI) {
        
        //get the slider html element
        var root = document.getElementById(clientId);
        
        //for short cut and to make it more like OOP
        //var super = ice.yui3;

        var Dom = YAHOO.util.Dom;
   
        var hiddenField = Dom.get(clientId+"_hidden");
        
        //set a callback to create slider component 
        ice.yui3.use(function(Y){ 
        	Y.on('domready', function(){
			   var obj = null;
		       YUI({bootstrap:false}).use('slider', function(Yui) {
					try {
			            obj = new Yui.Slider({
							//following two properties has to be set when initializing componnent
							axis: yuiProps.axis,
							thumbUrl: jsfProps.thumbUrl,
                            disabled:yuiProps.disabled
							}).render(root);
			            } catch(e){alert(e)}
						
						var invokeSubmit = function (event) {
		                    if (jsfProps.singleSubmit) {

                                var postParameters = jsfProps.postParameters;
		                        ice.se(event, root, function(param) {
									param(hiddenField.id, obj.get('value'));
									param('ice.focus', ice.focus);
									param('onevent', function(data) { 
										if (data.status == 'success') {
											if (ice.focus == clientId) {
												root.firstChild.focus();
											}
										}
									});
									if (postParameters != null) {
                                        var argCount = postParameters.length / 2;
                                        for (var idx =0; idx < argCount; idx ++ ) {
                                            param( postParameters[idx*2], postParameters[(idx*2)+1] );
                                        }
                                    }
								});
		                    }  						
						}
						
						obj.after("focus", function() {
							ice.focus = clientId;
						});
						
						obj.after("blur", function() {
							ice.focus = "";
						});
						
					    var eventName;
						obj.after("valueChange", function(event) {
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
							if (eventName == 'railMouseDown') {
								invokeSubmit(event);
							}
							eventName = "";
						});
						
						obj.after("railMouseDown", function() {
						  eventName = "railMouseDown";
						});
						
						obj.after("slideEnd", function(event) {
							invokeSubmit(event);
						});						
						

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
							var keydownTimeoutHandler = null;
		                    //listen for keydown event, to provide short-cut key support.
		                    //react on left, right, up, down, home and end key 
		                    Yui.on("keydown", function(event) {
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
							        clearTimeout(keydownTimeoutHandler);	
							        keydownTimeoutHandler = setTimeout (function(){
											//invoke submit
											invokeSubmit(event);		                            
											keydownTimeoutHandler = null;
										},  300);									
		                            //cancel event
		                            event.halt();
		                        }
		                    }, root);
		                } 

						Yui.on("click", function(event) {
							root.firstChild.focus();
						}, root);		                
						//bind the initilized js component, so it can be reused for later calls
		                bindYUI(obj);
		          });
 			   });
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

