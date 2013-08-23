function hasRole (roleName) {
	return context.getUser().getRoles().contains(roleName);
}

function dspIcon( iconNr ) {
	if (iconNr == "") return "";
	var path = "/icons/vwicn";
	var idx = iconNr;
	if (idx < 10)
		path += ("00"+idx).left(3);
	else if (idx < 100)
		path += ("0"+idx).left(3);
	else
		path += idx.left(3);
	path += ".gif";
	return context.getUrl().getScheme() + "://" + context.getUrl().getHost() + path;

}
function asVector(obj) { 
	if(typeof obj === "java.util.Vector") { 
		return obj; 
	} else { 
		var x:java.util.Vector = new java.util.Vector(); 
		x.add(obj); 
		return x; 
	} 
}