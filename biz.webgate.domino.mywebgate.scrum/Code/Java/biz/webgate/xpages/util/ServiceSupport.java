/*
 * � Copyright WebGate Consulting AG, 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package biz.webgate.xpages.util;

import biz.webgate.xpages.dss.annotations.DominoStore;
import biz.webgate.xpages.json.annotations.JSONObject;

public class ServiceSupport {

	public static String buildCleanFieldName(DominoStore dsCurrent, String strFieldName) {
		return internalBuildCleanFieldName(dsCurrent.JavaFieldPrefix(), dsCurrent.JavaFieldPostFix(), strFieldName);
	}

	public static String buildCleanFieldName(JSONObject jsCurrent, String strFieldName) {
		return internalBuildCleanFieldName(jsCurrent.JavaFieldPrefix(), jsCurrent.JavaFieldPostFix(), strFieldName);
	}

	private static String internalBuildCleanFieldName(String strPrefix, String strPostFix, String fldName) {
		String fldNameClean = fldName;
		if (strPrefix.length() > 0) {
			fldNameClean = fldNameClean.substring(strPrefix.length());
		}
		if (strPostFix.length() > 0) {
			fldNameClean = fldNameClean.substring(0, (fldNameClean.length() - strPostFix.length()));
		}
		return fldNameClean;
	}
}
