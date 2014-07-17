/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

/*
ThemeRoller's various jQuery UI elements and other plugins are initialized here. 
*/
/*
$(function(){
	$( "#load-mask" ).height($(window).height()).width($(window).width() + 15);
});*/

TR.isDOMReady = false;
TR.isIFrameReady = false;
TR.panelReady = false;

TR.initializeUI = function() {
	
	if ( !TR.isDOMReady || !TR.isIFrameReady || !TR.panelReady ) {
		return;
	}

	var hexDigits = new Array("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f");
		
	//size the load mask and show the rest of the content after 3000ms
	resize();
			
	var colors = [];

	var p = $( "#picker" ).css( "opacity", 0.25 );
	var selected;
	$( ".colorwell" )
	  	.each(function() { 
	  		$( this ).css( "opacity", 0.75 ); 
	  	})
	  	.focus(function() {
			if( selected ) {
		  		$( selected ).css( "opacity", 0.75 ).removeClass( "colorwell-selected" );
			}
			p.css( "opacity", 1 );
			$( selected = this ).css( "opacity", 1 ).addClass( "colorwell-selected" );
	  	});
	
	
	$(window).resize( resize );
	
	//sizing content to the right of the TR panel
	function resize() {
		var top_border = $( "#tr_panel" ).css( "border-top-width" ).substring( 0, 1 ),
			toolbar_height = $( "#toolbar" ).outerHeight();
		$( "#tr_panel" ).height( $(window).height() - top_border - toolbar_height );
		$( "#content" ).height( $(window).height() - $("#header-wrapper").outerHeight() - 3 - toolbar_height );
	}
	
	//global array of color values in quickswatch colors for lightness/saturation adjustments
	var quickswatch = $( "#quickswatch" );
    quickswatch.find( ".color-drag:not(.disabled)" ).each(function() {
		colors.push( rgbtohex($(this).css("background-color")) );
	});

	//colorwell refers to the inputs with color values
	$( ".colorwell" ).focus(function() {
		colorwell = $(this);
		var pos = $( this ).offset();
		var name = $( this ).attr( "data-name" );
		if(name.indexOf( "shadow-color" ) == -1) {
			$( "#colorpicker" ).css({
				"position": "absolute", 
				"left": 40, 
				"top": pos.top + 21
			});
		} else {
			$( "#colorpicker" ).css({
				"position": "absolute", 
				"left": 100, 
				"top": pos.top + 21
			});
		}
		$( "#colorpicker" ).show();
	}).blur(function(e) {
		$( "#colorpicker" ).hide();
		colorwell = null;
	});
	
	//colorwell refers to the inputs with color values
	$( ".colorwell-toggle" ).focus(function() {
		colorwell = $(this);
		if( colorwell.is(":hidden") ) {
			return;
		}
		var pos = $( this ).offset();
		var name = $( this ).attr( "data-name" );
		if(name.indexOf( "shadow-color" ) == -1) {
			$( "#colorpicker" ).css({
				"position": "absolute", 
				"left": pos.left, 
				"top": pos.top + 21
			});
		} else {
			$( "#colorpicker" ).css({
				"position": "absolute", 
				"left": pos.left, 
				"top": pos.top + 21
			});
		}
		$( "#colorpicker" ).show();
	}).blur(function(e) {
		$( "#colorpicker" ).hide();
		colorwell = null;
	});
	
	//Inspector Radio behavior
	$( "#inspector-button" ).click(function() {
		var $this = $( this ),
			active = $this.hasClass( "active" );
			
		if( !active ) {
			$this.addClass( "active" ).find( "img" ).attr( "src", "images/inspector-active.png" );
			$this.find( "strong" ).text( "on" );
		} else {
			$this.removeClass( "active" ).find( "img" ).attr( "src", "images/inspector.png" );
			$this.find( "strong" ).text( "off" );
		}
	});

	// Accordion
	$( ".accordion" ).accordion({ 
		header: "h3", 
		active: false, 
		clearStyle: true, 
		collapsible: true 
	});

	// Tabs
	$( "#tabs" ).tabs({
		add: function( event, ui ) {
			$( ui.panel ).append( "&nbsp;" );
		}
	});
	
	$( "#devicetabs" ).tabs();

	// Slider
	$( ".slider" ).slider({ 
		max : 80, 
		value: 40 
	});

	//radius sliders has different range of values
	$( ".slider[data-type=radius]" ).slider("option", {
		max: 2,
		step: .1
	});
	
	//Lightness and Saturation sliders
	$( "#lightness_slider" ).slider({
		width: 100,
		max: 50,
		min: -40,
	});
	
	$( "#saturation_slider" ).slider({
		width: 100,
		value: 0,
		min: -100,
		max: 100,
	});
	
	$( "#saturation_slider" ).bind("slide", function() {
		var sat_val = $( this ).slider( "value" );
		var sat_percent = sat_val / 100;	
		if( sat_percent >= 0 ) {
			var sat_str = "+=";
		} else {
			var sat_str = "-=";
			sat_percent = sat_percent - (2 * sat_percent);
		}
		
		var lit_val = $( "#lightness_slider" ).slider( "value" );
		var lit_percent = lit_val / 100;	
		if( lit_percent >= 0 ) {
			var lit_str = "+=";
		} else {
			var lit_str = "-=";
			lit_percent = lit_percent - (2 * lit_percent);
		}
		
		
		for( var i = 1; i< colors.length; i++ ) {
			var orig = $.Color( colors[i] );
			quickswatch.find( ".color-drag:nth-child(" + (i + 1) + ")" )
				.css("background-color", orig.saturation(sat_str + sat_percent).lightness(lit_str + lit_percent) );
		}
	});
	
	$( "#lightness_slider" ).bind("slide", function() {
		var sat_val = $( "#saturation_slider" ).slider( "value" );
		var sat_percent = sat_val / 100;	
		if( sat_percent >= 0 ) {
			var sat_str = "+=";
		} else {
			var sat_str = "-=";
			sat_percent = sat_percent - (2 * sat_percent);
		}
		
		var lit_val = $( this ).slider( "value" );
		var lit_percent = lit_val / 100;	
		if( lit_percent >= 0 ) {
			var lit_str = "+=";
		} else {
			var lit_str = "-=";
			lit_percent = lit_percent - (2 * lit_percent);
		}
		
		for( var i = 1; i < colors.length; i++ ) {
			var orig = $.Color( colors[i] );
			quickswatch.find( ".color-drag:nth-child(" + (i + 1) + ")" )
				.css("background-color", orig.saturation(sat_str + sat_percent).lightness(lit_str + lit_percent) );
		}
	});

  // Recent color dropper

	function processFarbChange( color ) {
		$( "#most_recent_colors input" ).val( color );

		TR.updateMostRecent( color );
	}

	$( "#recent-color-picker" ).click( function( evt ) {
		evt.preventDefault();
		var well = $( this ).parent().find( "input" ), pos = $(this).offset();

		$( this ).hide();
		well.show();
		well.focus();

		TR.addMostRecent( well.val() );

		event.preventDefault();
	});

	$( "#most_recent_colors .colorwell-toggle" ).blur(function() {
		var well = $("#most_recent_colors .colorwell-toggle");
		well.hide();
		$('#recent-color-picker').show();
	});
	
	function rgbtohex(rgb) {
		rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
		return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
	}
	
	function hex(x) {
		return isNaN(x) ? "00" : hexDigits[(x - x % 16) / 16] + hexDigits[x % 16];
	}
	
	TR.initThemeRoller();
}

TR.tabletIframeLoadCallback = function()
{
    TR.isIFrameReady = true;
    TR.initializeUI();
};

$(function() {
	TR.isDOMReady = true;
	TR.initPanel();
	//if chrome show recent colors
	if( window.chrome ){
	    $('#most_recent_colors').show();
	}
});
