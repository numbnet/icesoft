ice.yui.slider = {
   register:function(clientId, varName, callback) {
        var obj = null;
        ice.yui.loadModule('slider');
        ice.yui.use(function(Y){
                obj = new Y.Slider({
                    thumbImage: 'http://yui.yahooapis.com/3.0.0/build/slider/assets/skins/sam/thumb-classic-x.png'
                }).render('.class'+ varName);
                logger.info('Object created '+ obj);
                callback(obj);
            });
   },
   
   updateProperties:function(clientId, varName, props, events) {
	     ice.yui.updateProperties(clientId, varName, props, events, this);
   },
   
   getInstance:function(clientId, callback, varName) {
        ice.yui.getInstance(clientId, varName, this, callback);
   }
};

