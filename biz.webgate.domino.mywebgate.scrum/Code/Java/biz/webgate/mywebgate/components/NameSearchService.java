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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;

import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class NameSearchService {

	private static NameSearchService m_Service;

	private NameSearchService() {

	}

	public static NameSearchService getInstance() {
		if (m_Service == null) {
			m_Service = new NameSearchService();
		}
		return m_Service;
	}

	public List<NameEntry> searchNames(String strSearch, String mwgServer, String mwgPath, boolean isLookup, String strLkpView, String strFtSearch, String strInputString,
			ArrayList<String> returnFields, String strReturnField, boolean isFormula, int nSortOrder, String searchFieldInView) {
		if (strSearch == null | "".equals(strSearch))
			return null;
		List<NameEntry> lstRC = new ArrayList<NameEntry>();

		try {
			Session sesCurrent = ExtLibUtil.getCurrentSession();

			// Open Directory (must be fulltext-indexed!)
			Database mwgDatabase = sesCurrent.getDatabase(mwgServer, mwgPath);
			if (!mwgDatabase.isOpen()) {
				System.out.println("Unable to open database: " + mwgPath);
				return null;
			}

			DocumentCollection docCollection;

			String ftSearchKey = "";

			if (strFtSearch != null) {
				ftSearchKey = strFtSearch;
				if (strInputString != null && strSearch != null) {
					ftSearchKey = strFtSearch.replace(strInputString, strSearch);
				}
			}
			if (!isLookup && (strLkpView == null || strLkpView.trim().isEmpty()) && mwgDatabase.isFTIndexed()) {
				docCollection = mwgDatabase.FTSearch(ftSearchKey, 50, Database.FT_SCORES, Database.FT_DATABASE | Database.FT_FUZZY);
			} else {
				if ((!isFormula || !searchFieldInView.trim().isEmpty()) && mwgDatabase.isFTIndexed()) {
					View searchView = mwgDatabase.getView(strLkpView);

					String query = "(FIELD " + strReturnField + " CONTAINS *" + strSearch + "*)";

					if (searchFieldInView != null && !searchFieldInView.trim().isEmpty()) {
						query = "(FIELD " + searchFieldInView + " CONTAINS *" + strSearch + "*)";
					}
					// System.out.println("query?" + query);
					try {
						if ("".equals(ftSearchKey)) {
							searchView.FTSearch(query);
						} else {
							searchView.FTSearch(ftSearchKey);
						}
						ViewEntryCollection vecEntries = searchView.getAllEntries();
						ViewEntry entryNext = vecEntries.getFirstEntry();
						docCollection = searchView.getAllDocumentsByKey("EMPTY_COLLECTION");

						while (entryNext != null) {
							ViewEntry entry = entryNext;
							entryNext = vecEntries.getNextEntry(entry);
							docCollection.addDocument(entry.getDocument());
							entry.recycle();
						}
					} catch (Exception e) {
						e.printStackTrace();
						docCollection = mwgDatabase.getView(strLkpView).getAllDocumentsByKey(strSearch, false);
					}
				} else {
					docCollection = mwgDatabase.getView(strLkpView).getAllDocumentsByKey(strSearch, false);
				}
			}
			// System.out.println("COUNT: " + docCollection.getCount());
			/*
			 * if (docCollection.getCount() > 0) { Document docNext =
			 * docCollection.getFirstDocument(); while (docNext != null) {
			 * Document contactDoc = docNext; docNext =
			 * docCollection.getNextDocument(); NameEntry nmNew = new
			 * NameEntry(); getNameEntry(nmNew, contactDoc, returnFields,
			 * strReturnField, isFormula, sesCurrent); lstRC.add(nmNew); //
			 * nmCurrent.recycle(); contactDoc.recycle(); } }
			 * docCollection.recycle();
			 */

			if (returnFields == null) {
				returnFields = new ArrayList<String>();
				returnFields.add(strReturnField);
			}
			proccessNamesDocumentCollection(docCollection, lstRC, returnFields, strReturnField, isFormula, sesCurrent);

			Collections.sort(lstRC, new NameEntryComperator(returnFields.get(nSortOrder)));

		} catch (Exception e) {
			e.printStackTrace();
			throw new FacesExceptionEx("NotesException - " + e.getMessage());
		}

		// System.out.println(lstRC.size());
		return lstRC;
	}

	public List<NameEntry> getAllNames(String mwgServer, String mwgPath, boolean isLookup, String strLkpView, String strFtSearch, String strInputString, ArrayList<String> returnFields,
			String strReturnField, boolean isFormula, int nSortOrder, int nMaxResult) {

		List<NameEntry> lstRC = new ArrayList<NameEntry>();

		try {
			Session sesCurrent = ExtLibUtil.getCurrentSession();

			// Open Directory (must be fulltext-indexed!)
			Database mwgDatabase = sesCurrent.getDatabase(mwgServer, mwgPath);
			if (!mwgDatabase.isOpen()) {
				System.out.println("Unable to open database: " + mwgPath);
				return null;
			}

			if (!isLookup && (strLkpView == null || strLkpView.trim().isEmpty()) && mwgDatabase.isFTIndexed()) {
				DocumentCollection docCollection;
				docCollection = mwgDatabase.FTSearch(strFtSearch, nMaxResult, Database.FT_SCORES, Database.FT_DATABASE | Database.FT_FUZZY);

				proccessNamesDocumentCollection(docCollection, lstRC, returnFields, strReturnField, isFormula, sesCurrent);
			} else {
				ViewEntryCollection docCollection;
				if (mwgDatabase.isFTIndexed() && strFtSearch != null && !strFtSearch.trim().isEmpty()) {
					View searchView = mwgDatabase.getView(strLkpView);

					searchView.FTSearch(strFtSearch);

					docCollection = searchView.getAllEntries();
				} else {
					docCollection = mwgDatabase.getView(strLkpView).getAllEntries();
				}
				proccessNamesEntryCollection(docCollection, lstRC, returnFields, strReturnField, isFormula, sesCurrent);

			}
			// System.out.println("COUNT: " + docCollection.getCount());

			Collections.sort(lstRC, new NameEntryComperator(returnFields.get(nSortOrder)));

		} catch (NotesException e) {
			e.printStackTrace();
			throw new FacesExceptionEx("NotesException - " + e.getMessage());
		}
		// System.out.println(lstRC.size());
		return lstRC;
	}

	public void proccessNamesDocumentCollection(DocumentCollection docCollection, List<NameEntry> lstRC, ArrayList<String> returnFields, String strReturnField, boolean isFormula, Session sesCurrent) {
		try {
			if (docCollection.getCount() > 0) {
				Document docNext = docCollection.getFirstDocument();
				while (docNext != null) {
					Document contactDoc = docNext;
					docNext = docCollection.getNextDocument();
					NameEntry nmNew = new NameEntry();
					getNameEntry(nmNew, contactDoc, returnFields, strReturnField, isFormula, sesCurrent);
					lstRC.add(nmNew);
					// nmCurrent.recycle();
					contactDoc.recycle();
				}
			}
			docCollection.recycle();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public void proccessNamesEntryCollection(ViewEntryCollection docCollection, List<NameEntry> lstRC, ArrayList<String> returnFields, String strReturnField, boolean isFormula, Session sesCurrent) {
		try {
			if (docCollection.getCount() > 0) {
				ViewEntry veNext = docCollection.getFirstEntry();
				while (veNext != null) {
					ViewEntry contactEntry = veNext;
					veNext = docCollection.getNextEntry();
					NameEntry nmNew = new NameEntry();
					getNameEntry(nmNew, contactEntry.getDocument(), returnFields, strReturnField, isFormula, sesCurrent);
					lstRC.add(nmNew);
					// nmCurrent.recycle();
					contactEntry.recycle();
				}
			}

			docCollection.recycle();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public void getNameEntry(NameEntry nmNew, Document contactDoc, ArrayList<String> returnFields, String strReturnField, boolean isFormula, Session sesCurrent) {
		HashMap<String, String> returnValues = new HashMap<String, String>();

		try {
			for (String rf : returnFields) {
				if (contactDoc.hasItem(rf)) {
					if (contactDoc.getItemValue(rf).size() > 1) {
						contactDoc.getItemValueString(rf);
						System.out.println("CHECK MULTIVALUE NAMES");

					} else {
						returnValues.put(rf, contactDoc.getItemValueString(rf));
					}
				} else {
					try {
						returnValues.put(rf, sesCurrent.evaluate(rf, contactDoc).elementAt(0).toString());
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}
				}
			}

			nmNew.setValues(returnValues);
			if (isFormula) {
				nmNew.setReturnValue(sesCurrent.evaluate(strReturnField, contactDoc).elementAt(0).toString());
			} else {
				if (contactDoc.getItemValue(strReturnField).size() > 1) {

					for (Object itemObject : contactDoc.getItemValue(strReturnField)) {
						Item item = (Item) itemObject;
						switch (item.getType()) {
						case Item.TEXT:
							nmNew.setReturnValue(item.getText());
							break;

						default:
							break;
						}

					}
					System.out.println("CHECK MULTIVALUE NAMES");
				} else {
					nmNew.setReturnValue(contactDoc.getItemValueString(strReturnField));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

}
