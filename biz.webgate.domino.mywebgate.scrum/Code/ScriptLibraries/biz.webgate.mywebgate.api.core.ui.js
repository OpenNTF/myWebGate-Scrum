// biz.webgate.mywebgate.api.core.ui.js - Version 16112012

var g_mwgPath = "";

function getXID(id) { 	// find JSF element ID by friendly ID
		var xid;
		dojo.query('[id*=\"'+id+'\"]').forEach(function(node, i, a){
		 var ie=node.id.split(':');
		 	if (ie[ie.length-1]==id) xid=node.id;
		 });
		 return xid;
}

// Attach AJAX populated dropdown menu to oneui menu buttons
function initDropdownMenu(mwgPath) {
	g_mwgPath = mwgPath
	// -- attach apps dropdown
	var m = dojo.query(".myWebGate-menu-apps")[0]; // query by classname, because there is no ID
	if (m) {
		m.id ="mwg-ddmenu-apps-li"; // attach an id 
		var n1 = new attachDropDown( "mwg-ddmenu-apps-li", g_mwgPath+"/Services.xsp/apps-html", true );
	}
	// -- attach notifications dropdown
	m = dojo.query(".myWebGate-menu-notifications")[0];
	if (m) {
		m.id ="mwg-ddmenu-notifications-li"; // attach an id 
		var n2 = new attachDropDown( "mwg-ddmenu-notifications-li", g_mwgPath+"/Services.xsp/activitystreams/notifications/@self?details=all", false );
	}
	
	// -- attach profiles dropdown
	m = dojo.query(".myWebGate-menu-profiles")[0];
	if (m) {
		m.id ="mwg-ddmenu-profiles-li"; // attach an id 
		var n3 = new attachDropDown( "mwg-ddmenu-profiles-li", g_mwgPath+"/pgProfilesDropdown?OpenPage&", true );
	}
	
}

function attachDropDown(btnID, ajaxURL, keepCached) {
	this.ajaxURL = ajaxURL;
	this.ajaxLoaded = false;
	this.dropdownID = btnID + "-dropdown";
	this.xhrArgs = {
			url: ajaxURL,
		    handleAs: "text",
		    preventCache: true,
		    load: (function (obj) { 
		    		return function (data) {
		    			obj.setMenuBody(data)
		    		}
		    	  	})(this),
		    error: (function (obj) { 
	    			 return function (error) {
	    				 obj.setMenuBody(error)
	    			 }
	    	  		})(this)
		  }
	this.setMenuBody = function(data) {
		dojo.byId(this.dropdownID).innerHTML = data;
	}
		
	var menuNode = dojo.byId(btnID);
	var n = dojo.create("div", { 
		innerHTML: '<div class="mwg-loading"></div>', 
		className: "mwg-menu-dropdown-container",
		id: this.dropdownID,
	//	onmouseover: function() {},
	//	onmouseout:  function() {clearTimeout(this.hideAppMenu)},
		style: { position: "absolute", display: "none", zIndex: 1000 } 
		}, menuNode);
	
	dojo.connect(menuNode,"onmouseover",this,
		function() {
			dojo.addClass(btnID, 'lotusHover');
		//	dojo.fx.wipeIn({ node: this.dropdownID }).play();
			dojo.byId(this.dropdownID).style.display='';
			this.mwgHovered=true; // temporary flag to delay cache reset while menu is active
			if (this.ajaxLoaded==false) {
				var deferred = dojo.xhrGet(this.xhrArgs);
				this.ajaxLoaded=true; // prevent reload
			}
			
		}
	);
	
	dojo.connect(menuNode,"onmouseout",this,
		function(e) {
			// When using animated dropdowns, you need this hack for mouseout that gets triggered when hovering over child nodes!
		    // but still needs some tweaking because the A element in this oneui menu covers the LI element completely.
		/*	if (!e) var e = window.event;
			var reltg = (e.relatedTarget) ? e.relatedTarget : e.toElement;
			while ( reltg.tagName != 'BODY'){
				if (reltg.id == this.id){return;}
				reltg = reltg.parentNode;
			}   */
			this.mwgHovered=false;
			if (keepCached==false){ // delayed cache flag reset, because onmouseout gets triggered a gazillion times when hovering
				var self=this; // local ref for settimeout
				setTimeout(function(){
						if (self.mwgHovered==false) self.ajaxLoaded=false;					
					}, 5000);
				// TODO: find a better way for this..
			}
			
			dojo.removeClass(btnID, 'lotusHover');
			dojo.byId(this.dropdownID).style.display='none';
		}
	);
	
}

//-- ajax progress spinner
function mwgProgress(s) { // 0=hide, 1=show, 3=show+autohide
	//var p = dojo.query(".mwgProgressImg"); // styleClass output no longer available in oneuiv3
	dojo.query("li[role=mwgProgressImg]").style("visibility", s==0?"hidden":"visible");
	if (s==3) { setTimeout(
			function() {
				dojo.query("li[role=mwgProgressImg]").style('visibility','hidden')
			},1500)
	}
}

function processNotifications(nobj) {
	if (nobj.feedNotifications>0 || nobj.friendRequestsWaiting>0) {
		setNotificationIndicator('notifications', true); 
	} else {
		setNotificationIndicator('notifications', false); 
	}
	//setNotificationIndicator('notifications', nobj.friendRequestsWaiting>0?true:false); 
	//setNotificationIndicator('profiles', nobj.feedNotifications>0?true:false);
}

function setNotificationIndicator( nType, nEnable) {
	// toggles notification icon in titlebar menu dropdown
	// nType can be either notifications or profiles
	if (nEnable==true) {
		dojo.addClass('mwg-ddmenu-'+nType+'-li', 'mwg-menu-notifyIcon');
	} else {
		dojo.removeClass('mwg-ddmenu-'+nType+'-li', 'mwg-menu-notifyIcon');
	}
}

function chronos() { // this task will check for new notifications every n minutes
	var xhrArgs = { // submit to server
	    url: g_mwgPath+"/Services.xsp/activitystreams/notifications/@self",
	    handleAs: "json",
	    headers: { "Content-Type": "application/json"},
	    load: function(data) {
	    	if (data.error) {
	    		//alert("Chronos error: "+ data.error);
	    	} else {
	    		if (data.notifications) processNotifications(data.notifications);
	    	}
	    },
	    error: function(error) { 
	    		//alert("Chronos error: " + error); 
	    	}
	};
	dojo.xhrGet(xhrArgs);	
	setTimeout("chronos()", 180000);
}
