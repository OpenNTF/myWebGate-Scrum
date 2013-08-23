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
 *
 * Original code by:  
	Author : Ferhat BULUT
	Email  : ferhat@bestcoder.net
	Blog   : http://www.bestcoder.net
	http://openntf.org/XSnippets.nsf/snippet.xsp?id=sample-about-namepicker-data-provider-databean
	
	Modified to use myWebGate as directory and added fallback when target db is not fulltext-indexed.

	Known limitations: 
	 - cannot be used as typeAheadValue source for fields.
*/

package biz.webgate.mywebgate.api.dataSourceNamePicker;

// This is a data source for the ExtLib NamePicker that allows searching for both first- and lastname

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Name;
import lotus.domino.NotesException;
import biz.webgate.mywebgate.api.MyWebgateSessionFacade;
import biz.webgate.mywebgate.api.settings.ApplicationSettings;
import biz.webgate.xpages.util.logging.LoggerFactory;

import com.ibm.commons.util.QuickSort;
import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.complex.ValueBindingObjectImpl;
import com.ibm.xsp.extlib.component.picker.data.INamePickerData;
import com.ibm.xsp.extlib.component.picker.data.IPickerEntry;
import com.ibm.xsp.extlib.component.picker.data.IPickerOptions;
import com.ibm.xsp.extlib.component.picker.data.IPickerResult;
import com.ibm.xsp.extlib.component.picker.data.SimplePickerResult;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class NamePickerPeople extends ValueBindingObjectImpl implements INamePickerData {
	static String m_oldStartKey = "";
	static String[] m_entryList = null;

	public NamePickerPeople() {
	}

	public IPickerResult readEntries(IPickerOptions options) {
		Logger logCurrent = LoggerFactory.getLogger(this.getClass().getCanonicalName());
		List<IPickerEntry> entries = new ArrayList<IPickerEntry>();
		String startKey = options.getStartKey();
		int i = 0;
		if (startKey != null && !startKey.equals(m_oldStartKey)) {

			try {
				ApplicationSettings appSettings = MyWebgateSessionFacade.get().getAppSettings();
				if (!appSettings.isAvailable()) {
					logCurrent.severe("No mywebgate configuration available");
					return null;
				}
				// Open myWebGate Directory (should be fulltext-indexed!)
				Database mwgDatabase = ExtLibUtil.getCurrentSession().getDatabase(appSettings.getServer(), appSettings.getPath());
				if (!mwgDatabase.isOpen()) {
					logCurrent.severe("Unable to open "+appSettings.getServer() +"!!"+appSettings.getPath() );
					return null;
				}
				DocumentCollection docCollection;
				if (mwgDatabase.isFTIndexed()) {
					String ftSearchKey = "[Form] = \"Person\" AND ([FirstName] CONTAINS \"" + startKey + "\" OR [LastName] CONTAINS \"" + startKey + "\")";
					docCollection = mwgDatabase.FTSearch(ftSearchKey, 50, Database.FT_SCORES, Database.FT_DATABASE | Database.FT_FUZZY);
				} else {
					System.out.println("Namepicker warning: myWebGate Database is not fulltext indexed!" );
		        	docCollection = mwgDatabase.getView("($VIMPeople)").getAllDocumentsByKey(startKey, false);
				}
					if (docCollection.getCount() > 0) {
						m_oldStartKey = startKey;
						m_entryList = new String[docCollection.getCount()];
						i = 0;
						Document docNext = docCollection.getFirstDocument();
						while (docNext != null) {
							Document docContact = docNext;
							docNext = docCollection.getNextDocument(docNext);
							String label = docContact.getItemValueString("FirstName") + " " + docContact.getItemValueString("LastName");
							String strValue = docContact.getItemValueString("FullName");
							Name nmCurrent = ExtLibUtil.getCurrentSession().createName(strValue);
							m_entryList[i] = label + "~" + nmCurrent.getAbbreviated();
							nmCurrent.recycle();
							docContact.recycle();
							i++;
						}

						(new QuickSort.StringArray(m_entryList) {
							@Override
							public int compare(String arg0, String arg1) {
								return StringUtil.compareToIgnoreCase(arg0, arg1);
							}
						}).sort();

						for (i = 0; i < m_entryList.length; i++) {
							String[] entryValues = m_entryList[i].split("~");
							entries.add(new SimplePickerResult.Entry(entryValues[1], entryValues[0]));
						}
					}
					docCollection.recycle();


			} catch (NotesException e) {
				//logCurrent.log(Level.Severe,"General Error", e);
				throw new FacesExceptionEx("NotesException - " + e.getMessage());
			}
		} else {
			if (m_entryList != null) {
				entries.clear();
				for (i = 0; i < m_entryList.length; i++) {
					String[] entryValues = m_entryList[i].split("~");
					IPickerEntry pickerEntry = new SimplePickerResult.Entry(entryValues[1], entryValues[0]);
					entries.add(pickerEntry);
				}
			}
		}

		return new SimplePickerResult(entries, -1);
	}

	public String[] getSourceLabels() {
		return null;
	}

	public boolean hasCapability(int capability) {
		return false;
	}

	public List<IPickerEntry> loadEntries(Object[] ids, String[] attributeNames) {
		return null;
	}
}
