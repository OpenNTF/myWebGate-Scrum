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
package biz.webgate.xpages.dss.binding.util;

import java.util.HashMap;

import lotus.domino.Item;
import lotus.domino.Name;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class NamesProcessor {

	private static NamesProcessor instance = null;

	private NamesProcessor() {
	}

	public static NamesProcessor getInstance() {
		if (instance == null) {
			instance = new NamesProcessor();
		}
		return instance;
	}

	public String getPerson(HashMap<String, Object> addValues, String strValue) {
		String rcValue = strValue;
		try {
			if ((addValues.containsKey("isReader") || addValues.containsKey("isAuthor") || addValues.containsKey("isNames"))
					&& addValues.containsKey("showNameAs")) {
				if ("ABBREVIATE".equalsIgnoreCase(addValues.get("showNameAs")
						.toString())) {
					rcValue = ExtLibUtil.getCurrentSession().createName(
							strValue).getAbbreviated();
				} else if ("CN".equals(addValues.get("showNameAs"))) {
					rcValue = ExtLibUtil.getCurrentSession().createName(
							strValue).getCommon();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rcValue;
	}

	public String setPerson(String strValue, boolean isNamesValue) {
		String rcValue = strValue;
		Name person = null;
		try {
			person = ExtLibUtil.getCurrentSession().createName(strValue);
			if (person != null && isNamesValue)
				rcValue = person.getCanonical();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rcValue;
	}

	public boolean setNamesField(HashMap<String, Object> addValues, Item iNotesField) {
		boolean isNamesValue = false;
		try {
			if (addValues != null && addValues.size() > 0) {
				if (iNotesField != null) {
					if (addValues.containsKey("isReader")) {
						isNamesValue = true;
						iNotesField.setReaders(true);
					} else if (addValues.containsKey("isAuthor")) {
						isNamesValue = true;
						iNotesField.setAuthors(true);
					} else if (addValues.containsKey("isNames")) {
						isNamesValue = true;
						iNotesField.setNames(true);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isNamesValue;
	}
}
