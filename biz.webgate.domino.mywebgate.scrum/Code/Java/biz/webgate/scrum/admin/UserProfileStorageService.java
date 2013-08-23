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

import java.util.ArrayList;
import java.util.List;

import lotus.domino.Database;
import lotus.domino.Session;
import biz.webgate.xpages.dss.DominoStorageService;

public class UserProfileStorageService {

	private static UserProfileStorageService m_upSS;

	private UserProfileStorageService() {
	}

	public static UserProfileStorageService getInstance() {
		if (m_upSS == null) {
			m_upSS = new UserProfileStorageService();
		}
		return m_upSS;
	}

	public UserProfile createUserProfile(Session sesCurrent, String userName) {
		UserProfile up = new UserProfile();
		try {
			up.setUserName(userName);
			List<String> lstReader = new ArrayList<String>();
			lstReader.add("[Admin]");
			lstReader.add("[Server]");
			lstReader.add("[Intern]");
			lstReader.add(userName);
			up.setAuthors(lstReader);
			
			up.setShowHPInfo(1);
			up.setDefaultArea(1);
			DominoStorageService.getInstance().saveObject(up, sesCurrent.getCurrentDatabase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return up;
	}

	public boolean saveUserProfile(Session sesCurrent, UserProfile up) {
		try {
			return DominoStorageService.getInstance().saveObject(up, sesCurrent.getCurrentDatabase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public UserProfile getUserProfile(Session sesCurrent, String userName) {
		UserProfile up = new UserProfile();
		up.setUserName(userName);
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			if (!DominoStorageService.getInstance().getObject(up, userName,ndbCurrent))
				return createUserProfile(sesCurrent, userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return up;
	}

}