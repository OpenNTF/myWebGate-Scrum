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
package biz.webgate.scrum.admin;

import lotus.domino.Document;
import lotus.domino.Session;
import lotus.domino.View;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class AdminStorageService {
	
	private static AdminStorageService m_aSS;
	private AdminStorageService() {}
	public static AdminStorageService getInstance() {
		if (m_aSS == null) {m_aSS = new AdminStorageService();}
		return m_aSS;
	}

	
	public WelcomePage getWelcomePage(Session sesCurrent) {
		//boolean debug = false;
		WelcomePage wp = new WelcomePage();
		try {
			View viwSettings = ExtLibUtil.getCurrentSession().getCurrentDatabase().getView("vwWelcomePage");
			Document docSettings = viwSettings.getFirstDocument();
			if (docSettings == null) {
				docSettings = ExtLibUtil.getCurrentSession().getCurrentDatabase().createDocument();
				docSettings.replaceItemValue("Form", "frmWelcomePage");
				docSettings.replaceItemValue("InfoText", "Default Info Text");
				docSettings.save(true,false,true);
			}
			wp.setInfoText(docSettings.getItemValueString("InfoText"));
			return wp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean saveSettings(Session sesCurrent, WelcomePage wp) {
		//boolean debug = false;
		try {
			View viwSettings = ExtLibUtil.getCurrentSession().getCurrentDatabase().getView("vwWelcomePage");
			Document docSettings = viwSettings.getFirstDocument();
			if (docSettings != null) {
				docSettings.replaceItemValue("InfoText", wp.getInfoText());
				docSettings.save(true,false,true);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
