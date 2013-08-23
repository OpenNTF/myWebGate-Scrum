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
package biz.webgate.xpages.dss.binding;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.Document;

public class FormulaDateBinder implements IBinder<Date>, IFormulaBinder {

	private static FormulaDateBinder m_Binder;

	private FormulaDateBinder() {

	}

	public static FormulaDateBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new FormulaDateBinder();
		}
		return m_Binder;
	}

	public Date getValue(Object objCurrent, String strJavaField) {
		// TODO Auto-generated method stub
		return null;
	}

	public void processDomino2Java(Document docCurrent, Object objCurrent,
			String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		// Im notes Field ist die Formel drin (nicht so toll aber es wird tun)
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField,
					Date.class);
			Vector<?> vecDates = docCurrent.getParentDatabase().getParent()
					.evaluate(strNotesField, docCurrent);
			if (vecDates.size() > 0) {
				DateTime dtCurrent = (DateTime) vecDates.elementAt(0);
				mt.invoke(objCurrent, dtCurrent.toJavaDate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void processJava2Domino(Document docCurrent, Object objCurrent,
			String strNotesField, String JavaField, HashMap<String, Object> addValues) {
		// TODO Auto-generated method stub

	}

}
