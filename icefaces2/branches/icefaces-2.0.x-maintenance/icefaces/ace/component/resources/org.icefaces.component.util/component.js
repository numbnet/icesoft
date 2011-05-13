/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
};

ice.yui3 = {
    y : null,
    getY: function() {
        return ice.yui3.y;
    },
    modules: {},
    use :function(callback) {
        if (ice.yui3.y == null) {
			var Yui = ice.yui3.getNewInstance();
			Yui.use('loader', 'oop', 'event-custom', 'attribute', 'base',
                    'event', 'dom', 'node', 'event-delegate',
                    // load modules required by the animation library
                    'anim', 'plugin', 'pluginhost',
                    // Specified by animation-v2.js
                    //'json',
                    function(Y) {
                ice.yui3.y = Y;
                callback(ice.yui3.y);
            });
        } else {
            callback(ice.yui3.y);
        }
    },
    loadModule: function(module) {
        if (!this.modules[module])
            this.modules[module] = module;
    },
    getModules: function() {
        var modules = '';
        for (module in this.modules)
            modules += module + ',';
        return modules.substring(0, modules.length - 1);
    },
    //basePathPattern: /^.*\/+(.*)\/javax\.faces\.resource.*yui\/yui-min\.js(.*)\?ln=(.*)?$/,
	basePathPattern: /^(.*)javax\.faces\.resource([\/\=])yui\/yui-min\.js(.*)$/,
	basePathPatternPortlets: /^(.*)javax\.faces\.resource([\/\=])yui%2Fyui-min\.js(.*)$/,
	libraryPattern: /^(.*[\_\-\.\&\?])ln\=yui\/[0-9a-zA-Z_\.]+(.*)$/,
	libraryPatternPortlets: /^(.*[\_\-\.\&\?])ln\=yui%2F[0-9a-zA-Z_\.]+(.*)$/,
	getBasePath: function(pattern) {
		var nodes, i, src, match;
		nodes = document.getElementsByTagName('script') || [];
		for (i = 0; i < nodes.length; i++) {
			src = nodes[i].src;
			if (src) {
				match = src.match(pattern);
				if (match) {
					return match;
				}
			}
		}
		return null;
    },
	yui3Base: '',
	yui2in3Base: '',
	facesServletExtension: '',
    yui3TrailingPath: '',
    yui2in3TrailingPath: '',
	getNewInstance: function() { // Private, only for use()
		if (!(ice.yui3.yui3Base && ice.yui3.yui2in3Base)) {
			//alert('1' + match[1] + '\n' + '2' + match[2] + '\n' + '3' + match[3]);
			/*
			var basePath = match[1];
			if (basePath.indexOf(':') != -1) { // check if domain contains port number and extract base path
				basePath = basePath.substring(basePath.indexOf('/', basePath.indexOf(':')) + 1);
			}
			*/
			// determine the Faces Servlet extension (usually .jsf), also consider the case when the jsessionid is in the url
			/*
			var jsessionidIndex = match[2].indexOf(';jsessionid=');
			if (jsessionidIndex != -1) {
				ice.yui3.facesServletExtension = match[2].substring(0, jsessionidIndex);
			} else {
				ice.yui3.facesServletExtension = match[2]
			}
			ice.yui3.yui3Base = '/' + basePath + '/javax.faces.resource/' + match[3] + '/';
			ice.yui3.yui2in3Base = '/' + basePath + '/javax.faces.resource/' + 'yui/2in3/';
			*/
			var isPortlet = false;
			var whole = ice.yui3.getBasePath(ice.yui3.basePathPattern);
			if (!whole) {
				whole = ice.yui3.getBasePath(ice.yui3.basePathPatternPortlets);
				isPortlet = true;
			}
			var library;
			if (isPortlet) {
				library = whole[3].match(ice.yui3.libraryPatternPortlets);
			} else {
				library = whole[3].match(ice.yui3.libraryPattern);
			}
			ice.yui3.yui3Base = whole[1] + "javax.faces.resource" + whole[2];
			ice.yui3.yui2in3Base = whole[1] + "javax.faces.resource" + whole[2];
			ice.yui3.yui3TrailingPath = whole[3];
			ice.yui3.yui2in3TrailingPath = library[1] + "ln=yui/2in3" + library[2];
		}

        //filter:"raw"
		var Y = YUI({combine: false, base: ice.yui3.yui3Base,
			groups: {
				yui2: {
				    combine: false,
					base: ice.yui3.yui2in3Base,
					patterns:  {
						'yui2-': {
							configFn: function(me) {
								if (/-skin|reset|fonts|grids|base/.test(me.name)) {
									me.type = 'css';
									me.path = me.path.replace(/\.js/, '.css');
								}
							}
						}
					}
				}
			}
		});
		
		// create URLs with the Faces Servlet extension at the end (e.g. '.jsf')
		var oldUrlFn = Y.Loader.prototype._url;
		Y.Loader.prototype._url = function(path, name, base) {
			var trailingPath;
			if (name.indexOf('yui2-') == -1) {
				trailingPath = ice.yui3.yui3TrailingPath;
			} else {
				trailingPath = ice.yui3.yui2in3TrailingPath;
			}
			return oldUrlFn.call(this, path, name, base) + trailingPath;
		};		
		
		// make Y.one support ':'s in IDs
		var _one = Y.one;
		Y.one = function(id) { 
			if (Y.Lang.isString(id)) {
				id = id.replace(/:/g, '\\:');
			}
			return _one(id);
		}
		
		return Y;
	}
};


var JSContext = function(clientId) {
    this.clientId = clientId
};
JSContext.list = {};
JSContext.prototype = {
    setComponent:function(component) {
        this.component = component;
    },

    getComponent:function() {
        return this.component;
    },

    setJSProps:function(props) {
        this.jsProps = props;
    },

    getJSProps:function() {
        return this.jsProps;
    },
    setJSFProps:function(props) {
        this.jsfProps = props;
    },

    getJSFProps:function() {
        return this.jsfProps;
    },

    isAttached:function() {
        return document.getElementById(this.clientId)['JSContext'];
    }
};

ice.component = {
    updateProperties:function(clientId, jsProps, jsfProps, events, lib) {
        ice.yui3.use(function() {
            ice.component.getInstance(clientId, function(yuiComp) {
                // TODO Why is yuiComp undefined
                if (!yuiComp) {
                    return;
                }
                for (prop in jsProps) {
                    var propValue = yuiComp.get(prop);
                    if (propValue != jsProps[prop]) {
                        yuiComp.set(prop, jsProps[prop]);
                    }
                }
            }, lib, jsProps, jsfProps);
        });

    },
    getInstance:function(clientId, callback, lib, jsProps, jsfProps) {
        var component = document.getElementById(clientId);
        //could be either new component, or part of the DOM diff
        var context = this.getJSContext(clientId);
        if (!context || (context && !context.isAttached())) {
            context = this.createContext(clientId);
            context.setJSProps(jsProps);
            context.setJSFProps(jsfProps);
            lib.initialize(clientId, jsProps, jsfProps, function(YUIJS) {
                context.setComponent(YUIJS);
                callback(context.getComponent());
            });
        } else {
            context = this.getJSContext(clientId);
            context.setJSProps(jsProps);
            context.setJSFProps(jsfProps);
            callback(context.getComponent());
        }
    },

    getJSContext: function(clientId) {
        var component = document.getElementById(clientId);
        if (component) {
            if (component['JSContext'])
                return component['JSContext'];
            else
                return JSContext[clientId];
        }
        return null;
    },

    createContext:function(clientId) {
        var component = document.getElementById(clientId);
        component['JSContext'] = new JSContext(clientId);
        JSContext[clientId] = component['JSContext'];
        return component['JSContext'];
    },

    clientState: {
        set: function(clientId, state) {
            this.getStateHolder()[clientId] = state;
        },

        get: function(clientId) {
            return this.getStateHolder()[clientId];
        },

        has: function(clientId) {
            return (this.getStateHolder()[clientId] != null);
        },

        getStateHolder: function () {
            if (!window.document['sparkle_clientState']) {
                window.document['sparkle_clientState'] = {};
            }
            return window.document['sparkle_clientState'];
        }
    }
};

for (props in ice.component) {
    ice.yui3[props] = ice.component[props];
}
