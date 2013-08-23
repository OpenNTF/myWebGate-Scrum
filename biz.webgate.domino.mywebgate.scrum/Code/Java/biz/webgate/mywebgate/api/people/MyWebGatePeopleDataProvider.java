/*
 * © Copyright WebGate Consulting AG, 2012
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

import java.util.ArrayList;
import java.util.Set;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Name;
import lotus.domino.View;
import biz.webgate.mywebgate.api.MyWebgateSessionFacade;
import biz.webgate.mywebgate.api.settings.ApplicationSettings;

import com.ibm.xsp.extlib.social.Person;
import com.ibm.xsp.extlib.social.impl.AbstractPeopleDataProvider;
import com.ibm.xsp.extlib.social.impl.PersonImpl;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class MyWebGatePeopleDataProvider extends AbstractPeopleDataProvider {

	public static final String FIELD_FIRST_NAME = "firstName";
	public static final String FIELD_LAST_NAME = "lastName";
	public static final String FIELD_FULL_NAME = "fullName";
	public static final String FIELD_SOCIAL_ID = "socialID";
	public static final String FIELD_GENDER = "gender";

	public static final String FIELD_PERSONLATITLE = "personalTitle";
	public static final String FIELD_JOBTITLE = "jobTitle";
	public static final String FIELD_OFFICE_PHONE_NUMBER = "officePhoneNumber";
	public static final String FIELD_OFFICE_FAX_PHONE_NUMBER = "officeFaxPhoneNumber";
	public static final String FIELD_OFFICE_STREET_ADDRESS = "officeStreetAddress";
	public static final String FIELD_OFFICE_CITY = "officeCity";
	public static final String FIELD_OFFICE_ZIP = "officeZip";
	public static final String FIELD_OFFICE_STATE = "officeState";
	public static final String FIELD_OFFICE_COUNTRY = "officeCountry";
	public static final String FIELD_OFFICE_LOCATION = "officeLocation";
	public static final String FIELD_OFFICE_NUMBER = "officeNumber";
	public static final String FIELD_EMAIL = "emailAddress";

	public static final String FIELD_CELL_PHONE_NUMBER = "cellPhoneNumber";
	public static final String FIELD_COMPANY = "companyName";
	public static final String FIELD_DEPARTMENT = "department";

	public static final String FIELD_MAIL_FILE = "mailFile";
	public static final String FIELD_MAIL_SERVER = "mailServer";
	public static final String FIELD_DOMINO_UNID = "dominoUNID";

	public static final String FIELD_EMPLOYEEID = "employeeID";
	public static final String FIELD_MANAGER = "manager";
	public static final String FIELD_DEPUTY = "deputy";
	public static final String FIELD_ASSISTANT = "assistant";
	public static final String FIELD_WEBPAGE = "webPage";

	public static final String FIELD_THEME_URL = "themeImageURL";
	public static final String FIELD_PROFILE_URL = "themeProfileURL";
	public static final String FIELD_AVATAR_URL = "themeAvatarURL";
	public static final String FIELD_PERSON_URL = "profileURL";

	private ArrayList<String> m_AllField;


	public MyWebGatePeopleDataProvider() {
		super();
		try {
			m_AllField = new ArrayList<String>();
			m_AllField.add(FIELD_FIRST_NAME);
			m_AllField.add(FIELD_LAST_NAME);
			m_AllField.add(FIELD_FULL_NAME);
			m_AllField.add(FIELD_SOCIAL_ID);
			m_AllField.add(FIELD_GENDER);

			m_AllField.add(FIELD_PERSONLATITLE);
			m_AllField.add(FIELD_JOBTITLE);
			m_AllField.add(FIELD_OFFICE_PHONE_NUMBER);
			m_AllField.add(FIELD_OFFICE_FAX_PHONE_NUMBER);
			m_AllField.add(FIELD_OFFICE_STREET_ADDRESS);
			m_AllField.add(FIELD_OFFICE_CITY);
			m_AllField.add(FIELD_OFFICE_ZIP);
			m_AllField.add(FIELD_OFFICE_STATE);
			m_AllField.add(FIELD_OFFICE_COUNTRY);
			m_AllField.add(FIELD_OFFICE_LOCATION);
			m_AllField.add(FIELD_OFFICE_NUMBER);
			m_AllField.add(FIELD_EMAIL);

			m_AllField.add(FIELD_CELL_PHONE_NUMBER);
			m_AllField.add(FIELD_COMPANY);
			m_AllField.add(FIELD_DEPARTMENT);

			m_AllField.add(FIELD_MAIL_FILE);
			m_AllField.add(FIELD_MAIL_SERVER);
			m_AllField.add(FIELD_DOMINO_UNID);

			m_AllField.add(FIELD_EMPLOYEEID);
			m_AllField.add(FIELD_MANAGER);
			m_AllField.add(FIELD_DEPUTY);
			m_AllField.add(FIELD_ASSISTANT);
			m_AllField.add(FIELD_WEBPAGE);

			m_AllField.add(FIELD_THEME_URL);
			m_AllField.add(FIELD_PROFILE_URL);
			m_AllField.add(FIELD_AVATAR_URL);
			m_AllField.add(FIELD_PERSON_URL);
			System.out.println("MyWebGatePDP created");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "myWebGate.PeopleDataProvider"; // $NON-NLS-1$
	}

	@Override
	public void enumerateProperties(Set<String> propNames) {
		for (String strProp : m_AllField) {
			propNames.add(strProp);
		}
	}

	@Override
	public Class<?> getType(PersonImpl person, Object key) {
		// TODO Auto-generated method stub

		if (key.equals(Person.FIELD_THUMBNAIL_URL)) {
			return String.class;
		}
		if (m_AllField.contains(key)) {
			return String.class;
		}
		return null;
	}

	@Override
	public Object getValue(PersonImpl person, Object key) {
		if (key.equals(Person.FIELD_THUMBNAIL_URL)) {
			return getData(person).m_AvatarPhotoURL;
		}
		if (key.equals(FIELD_EMAIL)) {
			return getData(person).m_EMailAddress;
		}

		if (key.equals(FIELD_FIRST_NAME)) {
			return getData(person).m_FirstName;
		}
		if (key.equals(FIELD_LAST_NAME)) {
			return getData(person).m_LastName;
		}
		if (key.equals(FIELD_FULL_NAME)) {
			return getData(person).m_FullName;
		}
		if (key.equals(FIELD_SOCIAL_ID)) {
			return getData(person).m_SocialID;
		}
		if (key.equals(FIELD_GENDER)) {
			return getData(person).m_Gender;
		}

		if (key.equals(FIELD_PERSONLATITLE)) {
			return getData(person).m_PersonalTitle;
		}
		if (key.equals(FIELD_JOBTITLE)) {
			return getData(person).m_JopbTitle;
		}
		if (key.equals(FIELD_OFFICE_PHONE_NUMBER)) {
			return getData(person).m_OfficePhoneNumber;
		}
		if (key.equals(FIELD_OFFICE_FAX_PHONE_NUMBER)) {
			return getData(person).m_OfficeFaxPhoneNumber;
		}
		if (key.equals(FIELD_OFFICE_STREET_ADDRESS)) {
			return getData(person).m_OfficeStreetAddress;
		}
		if (key.equals(FIELD_OFFICE_CITY)) {
			return getData(person).m_OfficeCity;
		}
		if (key.equals(FIELD_OFFICE_ZIP)) {
			return getData(person).m_OfficeZip;
		}
		if (key.equals(FIELD_OFFICE_STATE)) {
			return getData(person).m_OfficeState;
		}
		if (key.equals(FIELD_OFFICE_COUNTRY)) {
			return getData(person).m_OfficeCountry;
		}
		if (key.equals(FIELD_OFFICE_LOCATION)) {
			return getData(person).m_OfficeLocation;
		}
		if (key.equals(FIELD_OFFICE_NUMBER)) {
			return getData(person).m_OfficeNumber;
		}

		if (key.equals(FIELD_CELL_PHONE_NUMBER)) {
			return getData(person).m_CellPhoneNumber;
		}
		if (key.equals(FIELD_COMPANY)) {
			return getData(person).m_CompanyName;
		}
		if (key.equals(FIELD_DEPARTMENT)) {
			return getData(person).m_Department;
		}

		if (key.equals(FIELD_MAIL_FILE)) {
			return getData(person).m_MailFile;
		}
		if (key.equals(FIELD_MAIL_SERVER)) {
			return getData(person).m_MailServer;
		}
		if (key.equals(FIELD_DOMINO_UNID)) {
			return getData(person).m_DominoUNID;
		}

		if (key.equals(FIELD_EMPLOYEEID)) {
			return getData(person).m_EmployeeID;
		}
		if (key.equals(FIELD_MANAGER)) {
			return getData(person).m_Manager;
		}
		if (key.equals(FIELD_DEPUTY)) {
			return getData(person).m_Deputy;
		}
		if (key.equals(FIELD_ASSISTANT)) {
			return getData(person).m_Assistant;
		}
		if (key.equals(FIELD_WEBPAGE)) {
			return getData(person).m_WebPage;
		}

		if (key.equals(FIELD_THEME_URL)) {
			return getData(person).m_ThemeImageURL;
		}
		if (key.equals(FIELD_PROFILE_URL)) {
			return getData(person).m_ProfilePhotoURL;
		}
		if (key.equals(FIELD_AVATAR_URL)) {
			return getData(person).m_AvatarPhotoURL;
		}
		if (key.equals(FIELD_PERSON_URL)) {
			return getData(person).m_ProfileURL;
		}

		return null;

	}

	@Override
	public void readValues(PersonImpl[] persons) {
		for (int i = 0; i < persons.length; i++) {
			getData(persons[i]);
		}

	}

	private MyWebGatePeopleData getData(PersonImpl person) {
		String id = person.getId();
		MyWebGatePeopleData data = (MyWebGatePeopleData) getProperties(id, MyWebGatePeopleData.class);
		if (data == null) {
			synchronized (getSyncObject()) {
				data = (MyWebGatePeopleData) getProperties(id, MyWebGatePeopleData.class);
				if (null == data) {
					data = new MyWebGatePeopleData();
					try {
						ApplicationSettings appSettings = MyWebgateSessionFacade.get().getAppSettings();
						if (appSettings.isAvailable()) {
							Database ndbMWG = ExtLibUtil.getCurrentSession().getDatabase(appSettings.getServer(), appSettings.getPath());
							View viwLUP = ndbMWG.getView("($Users-All)");
							Document docPerson = viwLUP.getDocumentByKey(person.getId());

							if (docPerson != null) {
								process(docPerson, data);
							} else {
								System.out.println("Person not found.");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					addProperties(id, data);
				}
			}
		}
		return data;
	}

	private void process(Document docPerson, MyWebGatePeopleData mwgPerson) {
		try {
			String strMWGUrl = MyWebgateSessionFacade.get().getMyWebGateURL();
			mwgPerson.m_FirstName = docPerson.getItemValueString("FirstName");
			mwgPerson.m_LastName = docPerson.getItemValueString("LastName");
			mwgPerson.m_FullName = docPerson.getItemValueString("FullName");
			mwgPerson.m_DisplayName = docPerson.getItemValueString("DisplayNameT");
			mwgPerson.m_SocialID = docPerson.getItemValueString("IDT");
			mwgPerson.m_Gender = docPerson.getItemValueString("genderT");

			mwgPerson.m_PersonalTitle = docPerson.getItemValueString("Title");
			mwgPerson.m_JopbTitle = docPerson.getItemValueString("JobTitle");
			mwgPerson.m_OfficePhoneNumber = docPerson.getItemValueString("OfficePhoneNumber");

			mwgPerson.m_OfficeFaxPhoneNumber = docPerson.getItemValueString("OfficeFaxPhoneNumber");
			mwgPerson.m_OfficeStreetAddress = docPerson.getItemValueString("OfficeStreetAddress");
			mwgPerson.m_OfficeCity = docPerson.getItemValueString("OfficeCity");
			mwgPerson.m_OfficeZip = docPerson.getItemValueString("OfficeZip");
			mwgPerson.m_OfficeState = docPerson.getItemValueString("OfficeState");
			mwgPerson.m_OfficeCountry = docPerson.getItemValueString("OfficeCountry");
			mwgPerson.m_OfficeLocation = docPerson.getItemValueString("Location");
			mwgPerson.m_OfficeNumber = docPerson.getItemValueString("OfficeNumber");

			mwgPerson.m_CompanyName = docPerson.getItemValueString("CompanyName");
			mwgPerson.m_Department = docPerson.getItemValueString("Department");
			mwgPerson.m_CellPhoneNumber = docPerson.getItemValueString("CellPhoneNumber");
			mwgPerson.m_EMailAddress = docPerson.getItemValueString("InternetAddress");

			mwgPerson.m_MailFile = docPerson.getItemValueString("MailFile");
			mwgPerson.m_MailServer = docPerson.getItemValueString("MailServer");
			mwgPerson.m_DominoUNID = docPerson.getItemValueString("dominoUNID");

			mwgPerson.m_EmployeeID = docPerson.getItemValueString("EmployeeID");
			mwgPerson.m_Manager = makeCanonical(docPerson.getItemValueString("Manager"));
			mwgPerson.m_Deputy = makeCanonical(docPerson.getItemValueString("Deputy"));
			mwgPerson.m_Assistant = makeCanonical(docPerson.getItemValueString("Assistant"));
			mwgPerson.m_WebPage = docPerson.getItemValueString("WebpageT");

			mwgPerson.m_ThemeImageURL = buildURL(docPerson.getItemValueString("ThemeImageURLT"), strMWGUrl, "defaultProfileTheme.jpg");
			mwgPerson.m_ProfilePhotoURL = buildURL(docPerson.getItemValueString("ProfilePhotoT"), strMWGUrl, "nophoto.png");
			mwgPerson.m_AvatarPhotoURL = buildURL(docPerson.getItemValueString("avatarPhotoT"), strMWGUrl, "nophoto.png");
			mwgPerson.m_ProfileURL = strMWGUrl + "/directory.xsp?person=" + mwgPerson.m_SocialID;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String buildURL(String strPic, String strURL, String strDefault) {
		if (strPic == null || strPic.equals("")) {
			if (strDefault == null || strDefault.equals("")) {
				return "";
			} else {
				return strURL + "/" + strDefault;
			}
		}
		return strURL + "/" + strPic;
	}

	private String makeCanonical(String strUserName) {
		String strRC = strUserName;
		try {
			if (strRC != null && !"".equals(strRC)) {
				Name nonCurrent = ExtLibUtil.getCurrentSession().createName(strUserName);
				strRC = nonCurrent.getCanonical();
				nonCurrent.recycle();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRC;
	}

}
