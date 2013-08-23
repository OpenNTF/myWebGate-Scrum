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
package biz.webgate.mywebgate.api.people;

import com.ibm.xsp.extlib.social.impl.PersonImpl;

public class MyWebGatePeopleData extends PersonImpl.Properties {
	String m_FirstName;
	String m_LastName;
	String m_FullName;
	String m_DisplayName;
	String m_SocialID;
	String m_Gender;

	String m_MailFile;
	String m_MailServer;
	String m_DominoUNID;

	String m_PersonalTitle;
	String m_JopbTitle;
	String m_OfficePhoneNumber;
	String m_OfficeFaxPhoneNumber;
	String m_OfficeStreetAddress;
	String m_OfficeCity;
	String m_OfficeZip;
	String m_OfficeState;
	String m_OfficeCountry;
	String m_OfficeLocation;
	String m_OfficeNumber;
	String m_CompanyName;
	String m_Department;
	String m_CellPhoneNumber;
	String m_EMailAddress;

	String m_EmployeeID;
	String m_Manager;
	String m_Deputy;
	String m_Assistant;
	String m_WebPage;

	String m_ThemeImageURL;
	String m_ProfilePhotoURL;
	String m_AvatarPhotoURL;
	String m_ProfileURL;

}
