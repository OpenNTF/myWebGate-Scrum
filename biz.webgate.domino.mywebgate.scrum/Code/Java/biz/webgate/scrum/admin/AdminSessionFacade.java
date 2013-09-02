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

import java.util.List;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class AdminSessionFacade {
	
	public WelcomePage getWelcomePage() {
		return AdminStorageService.getInstance().getWelcomePage(ExtLibUtil.getCurrentSession());
	}
	public boolean saveSettings(WelcomePage wp) {
		return AdminStorageService.getInstance().saveSettings(ExtLibUtil.getCurrentSession(), wp);
	}
	
	public UserProfile createUserProfile(String userName) {
		return UserProfileStorageService.getInstance().createUserProfile(ExtLibUtil.getCurrentSession(), userName);
	}
	
	public boolean saveUserProfile(UserProfile up) {
		return UserProfileStorageService.getInstance().saveUserProfile(ExtLibUtil.getCurrentSession(), up);
	}
	
	public UserProfile getUserProfile(String userName) {
		return UserProfileStorageService.getInstance().getUserProfile(ExtLibUtil.getCurrentSession(), userName);
	}
	
	public boolean sendLink(String strURL, String strID, List<String> sendTo, String strComment) {		
		return MailerFactory.sendLink(strURL, strID, sendTo, strComment, ExtLibUtil.getCurrentSession());		
	}
}
