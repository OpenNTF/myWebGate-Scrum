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

import java.io.Serializable;
import java.util.List;

import biz.webgate.xpages.dss.annotations.DominoEntity;
import biz.webgate.xpages.dss.annotations.DominoStore;

@DominoStore(Form="frmUserProfile", PrimaryKeyField="UserName", PrimaryFieldClass=String.class, View="lupUserProfiles")
public class UserProfile implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@DominoEntity(FieldName="UserName")
	private String m_UserName;
	@DominoEntity(FieldName = "AuthorsNM", isAuthor = true, showNameAs = "ABBREVIATE")
	private List<String> m_Authors;
	@DominoEntity(FieldName="ShowHPInfo")
	private int m_ShowHPInfo;
	@DominoEntity(FieldName="DefaultArea")
	private int m_DefaultArea;
	
	public String getUserName() {
		return m_UserName;
	}
	public void setUserName(String userName) {
		m_UserName = userName;
	}
	
	public List<String> getAuthors() {
		return m_Authors;
	}
	public void setAuthors(List<String> authors) {
		m_Authors = authors;
	}
	
	public int getShowHPInfo() {
		return m_ShowHPInfo;
	}
	public void setShowHPInfo(int showHPInfo) {
		m_ShowHPInfo = showHPInfo;
	}
	
	public int getDefaultArea() {
		return m_DefaultArea;
	}
	public void setDefaultArea(int defaultArea) {
		m_DefaultArea = defaultArea;
	}
}