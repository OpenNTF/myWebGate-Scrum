/*
 * � Copyright WebGate Consulting AG, 2012
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
package biz.webgate.xpages.dss.binding;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.Item;
import biz.webgate.xpages.dss.binding.util.NamesProcessor;

public class ListStringBinder implements IBinder<List<String>> {

	private static ListStringBinder m_Binder;

	@SuppressWarnings("unchecked")
	public void processDomino2Java(Document docCurrent, Object objCurrent,
			String strNotesField, String strJavaField,
			HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField,
					List.class);
			Vector vecResult = docCurrent.getItemValue(strNotesField);
			ArrayList<String> lstValues = new ArrayList<String>();
			for (Object strValue : vecResult) {
				lstValues.add(NamesProcessor.getInstance().getPerson(addValues,
						strValue.toString()));
			}
			mt.invoke(objCurrent, lstValues);
			// mt.invoke(objCurrent, new ArrayList<String>(vecResult));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processJava2Domino(Document docCurrent, Object objCurrent,
			String strNotesField, String strJavaField,
			HashMap<String, Object> addValues) {
		try {
			List<String> lstValues = getValue(objCurrent, strJavaField);
			Vector<String> vValues = new Vector<String>();

			if (lstValues != null) {
				boolean isNamesValue = false;
				if (addValues != null && addValues.size() > 0) {
					docCurrent.replaceItemValue(strNotesField, "");
					Item iNotesField = docCurrent.getFirstItem(strNotesField);
					isNamesValue = NamesProcessor.getInstance().setNamesField(
							addValues, iNotesField);
				}

				for (String strValue : lstValues) {
					vValues.add(NamesProcessor.getInstance().setPerson(
							strValue, isNamesValue));
				}
			}
			docCurrent.replaceItemValue(strNotesField, vValues);

			// docCurrent.replaceItemValue(strNotesField, new
			// Vector<String>(lstValues));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static IBinder<List<String>> getInstance() {
		if (m_Binder == null) {
			m_Binder = new ListStringBinder();
		}
		return m_Binder;
	}

	private ListStringBinder(){
		
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (List<String>) mt.invoke(objCurrent);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
