function tabClickTasks(eClick) {
	dojo.query("#home_myTasks li").removeClass("lotusSelected");
	eClick.parentElement.className = "lotusSelected";
}
function tabClickBugs(eClick) {
	dojo.query("#home_myBugs li").removeClass("lotusSelected");
	eClick.parentElement.className = "lotusSelected";
}
function tabClickProject(eClick) {
	dojo.query("#projectDetails li").removeClass("lotusSelected");
	eClick.parentElement.className = "lotusSelected";
}

function setTimestamp(toSet, fieldId) {
	var old = document.getElementById(fieldId).value;
	if (old != "") {
		document.getElementById(fieldId).value = old + "\n\n" + toSet + "\n";
	} else {
		document.getElementById(fieldId).value = toSet + "\n";
	}
	document.getElementById(fieldId).focus();
}