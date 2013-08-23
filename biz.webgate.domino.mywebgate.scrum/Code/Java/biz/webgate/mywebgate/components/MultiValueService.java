/*
 * © Copyright WebGate Consulting AG, 2013
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
package biz.webgate.mywebgate.components;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.faces.context.FacesContext;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class MultiValueService{
	
	private static MultiValueService m_Service;

	private MultiValueService() {

	} 

	public static MultiValueService getInstance() {
		if (m_Service == null) {
			m_Service = new MultiValueService();
		}
		return m_Service;
	}
	
	//Register Custom Controls
	public void addCustomControl(String strId, Object objObject,
			String strObjectAlias, String strFieldName, HashMap<String, HashMap<String, Object>> loadedCCs) {
		if (!loadedCCs.containsKey(strObjectAlias + "|" + strId)) {
			HashMap<String, Object> curCC = new HashMap<String, Object>();
			curCC.put("ObjectAlias", strObjectAlias);
			curCC.put("Id", strId);
			curCC.put("Object", objObject);
			curCC.put("FieldName", strFieldName);

			loadedCCs.put(strObjectAlias + "|" + strId, curCC);
		}
	}
	
	
	//Unregister Custom Control 1
	public void removeCustomControl(String strObjectAlias, String strObjectId, HashMap<String, HashMap<String, Object>> loadedCCs) {

		ArrayList<String> valuesToClean = new ArrayList<String>();
		
		valuesToClean.add(strObjectAlias + "|" + strObjectId);

		removeCustomControl(valuesToClean, loadedCCs);
	}

	//Unregister Custom Control
	public void removeCustomControl(ArrayList<String> valuesToClean, HashMap<String, HashMap<String, Object>> loadedCCs) {

		for (String valueToClean : valuesToClean) {
			loadedCCs.remove(valueToClean);
		}
	}
	
	//Get Values for Custom Control - Register Type of MultiValueField
	public Object getValue(Object obj, String strFieldName, HashMap<String, String> valueFields) {
		try {
			Method mt = obj.getClass().getMethod("get" + strFieldName);
			if (!valueFields.containsKey(obj.getClass().getSimpleName()
					+ "|" + strFieldName)) {
				valueFields.put(obj.getClass().getSimpleName() + "|"
						+ strFieldName, mt.getReturnType().getSimpleName());
			}
			Object values = mt.invoke(obj);
			return values;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//Set values from registered Custom Control to the Object 
	public void updateMultiValueData(Object objObject, String strObjectAlias, HashMap<String, HashMap<String, Object>> loadedCCs, HashMap<String, String> multiValueFields) {

		for (HashMap<String, Object> ccId : loadedCCs.values()) {
			if (ccId.get("ObjectAlias").equals(strObjectAlias)) {
				Object objValues = getViewObject("textArrayValue"
						+ ccId.get("Id"));
				String strFieldName = (String) ccId.get("FieldName");
				setValue(objObject, strFieldName, objValues, multiValueFields);
			}
		}
	}
	
	//Set Values for Custom Control
	@SuppressWarnings("unchecked")
	private void setValue(Object obj, String strFieldName, Object values, HashMap<String, String> multiValueFields) {
		try {
			String strType = "";
			Method mt = null;

			multiValueFields.get(obj.getClass().getSimpleName() + "|"
					+ strFieldName);
			if (multiValueFields.containsKey(obj.getClass().getSimpleName()
					+ "|" + strFieldName)) {
				strType = multiValueFields.get(obj.getClass().getSimpleName()
						+ "|" + strFieldName);
			} else {
				strType = values.getClass().getSimpleName();
			}

			if ("List".equals(strType)) {
				mt = obj.getClass().getMethod("set" + strFieldName, List.class);
				if (values.getClass().getSimpleName().equals("String")) {
					List<String> lstrValue = new ArrayList<String>();
					lstrValue.add((String) values);

					values = lstrValue;
				}
			} else if ("ArrayList".equals(strType)) {
				mt = obj.getClass().getMethod("set" + strFieldName,
						ArrayList.class);
				if (values.getClass().getSimpleName().equals("String")) {
					ArrayList<String> lstrValue = new ArrayList<String>();
					lstrValue.add((String) values);

					values = lstrValue;
				} else if (values.getClass().getSimpleName().equals("Vector")) {
					Vector<String> vec = (Vector<String>) values;
					ArrayList<String> lstrValue = new ArrayList<String>();
					;
					for (String value : vec) {
						lstrValue.add(value);
					}
					values = lstrValue;
				}
			} else if ("String[]".equals(strType)) {
				mt = obj.getClass().getMethod("set" + strFieldName,
						new Class[] { String[].class });

				String[] strValue;

				if (values.getClass().getSimpleName().equals("Vector")) {
					Vector<String> vec = (Vector<String>) values;
					strValue = (String[]) vec.toArray(new String[vec.size()]);
					values = strValue;
				} else if (values.getClass().getSimpleName().equals("String")) {
					strValue = new String[1];
					strValue[0] = (String) values;
					values = strValue;
				}

			} else {
				mt = obj.getClass().getMethod("set" + strFieldName,
						String.class);
			}
			mt.invoke(obj, values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Get ViewScope Values for Custom Control (Used in setValue())
	@SuppressWarnings("unchecked")
	private static Object getViewObject(String objName) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		// ExternalContext extCtx = ctx.getExternalContext();
		// Map<String, Object> sessionMap = extCtx.getSessionMap();
		Map<String, Object> viewMap = ctx.getViewRoot().getViewMap();
		return viewMap.get(objName);
	}
	
	public List<String> getTypeAhead(String strSearch, String mwgServer, String mwgPath, boolean isLookup, String strLkpView, String strFtSearch, String strInputString,
				ArrayList<String> returnFields, String strReturnField, boolean isFormula, int nSortOrder) {

		List<String> entries = new ArrayList<String>();
		
		try {
			Session sesCurrent = ExtLibUtil.getCurrentSession();
			
			Database mwgDatabase = sesCurrent.getDatabase(mwgServer, mwgPath);
			if (!mwgDatabase.isOpen()) {
				System.out.println("Unable to open database: " + mwgPath);
				return null;
			}
			
			View searchView = sesCurrent.getCurrentDatabase().getView(strLkpView);
			
			String query = "(FIELD " + strReturnField + " CONTAINS *" + strSearch +"*)";

			List<String> searchOutput = new ArrayList<String>();
				
			ViewEntryCollection vecEntries;
			try {
				sesCurrent.getCurrentDatabase().updateFTIndex(true);
				searchView.FTSearch(query);
				vecEntries = searchView.getAllEntries();
			} catch (Exception e) {
				e.printStackTrace();
				vecEntries = mwgDatabase.getView(strLkpView).getAllEntriesByKey(strSearch, false);
			}
				
			ViewEntry entryNext = vecEntries.getFirstEntry();
			
			while (entryNext != null) {
				ViewEntry entry = entryNext;
				entryNext = vecEntries.getNextEntry(entry);
				Document curDoc = entry.getDocument();
				
				if(curDoc.hasItem(strReturnField)){
					if(curDoc.getItemValue(strReturnField).size() > 1){
						Vector<?> v = (curDoc.getItemValue(strReturnField));
						
						for (int i=0 ; i<v.size(); i++) {						
							if (v.get(i).toString().toLowerCase().indexOf(strSearch.toLowerCase()) > -1) {
								if (!searchOutput.contains(v.get(i).toString())) {
									if(!searchOutput.contains(v.get(i).toString())){
										searchOutput.add(v.get(i).toString());
									}
								}
							}
						}
					}else{
						if(! searchOutput.contains(curDoc.getItemValueString(strReturnField))){
							searchOutput.add(curDoc.getItemValueString(strReturnField));
						}
					}
				}
				curDoc.recycle();
				
				entry.recycle();
			
			}

			return searchOutput;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return entries;
	}
}
