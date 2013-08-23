function addValue(tmpTextArray, value:java.lang.String, scopeKey, mvSeparator){
	if (value.trim() == "") {
		return;
	}
	if(value.right(1) == mvSeparator){
		value = value.substr(0,value.length - 1);
	}
	if(tmpTextArray instanceof java.util.ArrayList) {
		var vecCurrent:java.util.ArrayList = tmpTextArray;
		vecCurrent.add(value);
	}else if (tmpTextArray instanceof java.util.Vector) {
		var vecCurrent:java.util.Vector = tmpTextArray;
		vecCurrent.addElement(value);
	} else {
		if (tmpTextArray == "" || tmpTextArray == null) {
			viewScope.put(scopeKey,value);
			//print (scopeKey + " --- " + value);
		} else {
			var vecCurrent:java.util.Vector = new java.util.Vector();
			vecCurrent.addElement(tmpTextArray);
			vecCurrent.addElement(value);
			viewScope.put(scopeKey,vecCurrent);
			//print (scopeKey + " --- " + vecCurrent.size);
		}
	}
}