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
package biz.webgate.scrum.userstory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;

import biz.webgate.scrum.iteration.Iteration;
import biz.webgate.scrum.iteration.IterationSessionFacade;
import biz.webgate.scrum.scrumdocument.IScrumDocument;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class UserstorySessionFacade {
	public static final String BEAN_NAME = "userstoryBean"; //$NON-NLS-1$

	public static UserstorySessionFacade get(FacesContext context) {
		UserstorySessionFacade bean = (UserstorySessionFacade) context
				.getApplication().getVariableResolver().resolveVariable(
						context, BEAN_NAME);
		return bean;
	}

	public static UserstorySessionFacade get() {
		return get(FacesContext.getCurrentInstance());
	}

	public static final int SORT_BY_MODIFIEDAT = 1;
	public static final int SORT_BY_SUBJECT = 2;
	public static final int SORT_BY_STATUS = 3;
	public static final int SORT_BY_ITERATION = 4;

	private HashMap<String, Userstory> m_UserstoryList;
	private Date m_LastAccessed = new Date();

	public Userstory createNewUserstory() {
		return UserstoryStorageService.getInstance().createNewUserstory(
				ExtLibUtil.getCurrentSession());
	}

	public boolean saveUserstory(Userstory curUserstory) {
		m_UserstoryList = null;
		m_LastAccessed = new Date();
		return UserstoryStorageService.getInstance().saveUserstory(
				curUserstory, ExtLibUtil.getCurrentSession());
	}

	public boolean deleteUserstory(Userstory curUserstory) {
		m_UserstoryList = null;
		m_LastAccessed = new Date();
		return UserstoryStorageService.getInstance().deleteUserstory(
				curUserstory, ExtLibUtil.getCurrentSession());
	}

	public Userstory getUserstoryById(String strUserstoryId) {
		return loadUserstory(strUserstoryId);
	}

	public List<Userstory> getAllUserstories(int sortOrder, boolean reverse) {
		List<Userstory> lstAll = UserstoryStorageService.getInstance()
				.getAllUserstories(ExtLibUtil.getCurrentSession());
		UserstorySortFactory.sortUserstories(lstAll, sortOrder, reverse);
		return lstAll;
	}

	public List<Userstory> getUserstoriesOfProject(int sortOrder,
			boolean reverse, String projectID) {
		List<Userstory> lstUserstoriesOfProject = UserstoryStorageService
				.getInstance().getUserstoriesOfProject(
						ExtLibUtil.getCurrentSession(), projectID);
		UserstorySortFactory.sortUserstories(lstUserstoriesOfProject,
				sortOrder, reverse);
		return lstUserstoriesOfProject;
	}

	public List<Userstory> getUserstoriesOfIteration(String iterationID,
			boolean reverse) {
		List<Userstory> lstUserstoriesOfProject = UserstoryStorageService
				.getInstance().getUserstoriesOfIteration(
						ExtLibUtil.getCurrentSession(), iterationID);
		UserstorySortFactory.sortUserstories(lstUserstoriesOfProject,
				SORT_BY_SUBJECT, reverse);
		return lstUserstoriesOfProject;
	}

	public int getUserstoriesOfIterationCount(String iterationID) {
		return getUserstoriesOfIteration(iterationID, false).size();
	}

	public List<String> getUserstoryValues() throws IOException {
		List<String> selectItems = new ArrayList<String>();
		for (Userstory userstory : getAllUserstories(SORT_BY_SUBJECT, false)) {
			selectItems.add(userstory.getSubject() + "|" + userstory.getId());
		}
		return selectItems;
	}

	public List<String> getUserstoryValuesOfProject(String projectId) {
		List<String> selectItems = new ArrayList<String>();
		for (Userstory userstory : getUserstoriesOfProject(SORT_BY_SUBJECT,
				false, projectId)) {
			selectItems.add(userstory.getSubject() + "|" + userstory.getId());
		}
		return selectItems;
	}

	public List<String> getAssignedUserstoryValuesOfIteration(String iterationId) {
		List<String> selectItems = new ArrayList<String>();
		for (Userstory userstory : getUserstoriesOfIteration(iterationId, false)) {
			selectItems.add(userstory.getSubject() + "|" + userstory.getId());
		}
		return selectItems;
	}

	public List<String> getAvailableUserstoryValuesOfIteration(
			String iterationId, String projectID) {
		List<String> selectItems = new ArrayList<String>();
		for (Userstory userstory : getUserstoriesOfProject(SORT_BY_SUBJECT,
				false, projectID)) {
			if (userstory.getIterationId() == null
					|| userstory.getIterationId() == "") {
				selectItems.add(userstory.getSubject() + "|"
						+ userstory.getId());
			} else if (!userstory.getIterationId().equals(iterationId)) {
				if (!(IterationSessionFacade.get().getIterationById(
						userstory.getIterationId()) == null)) {
					Iteration iteration = IterationSessionFacade.get()
							.getIterationById(userstory.getIterationId());
					selectItems.add(userstory.getSubject() + " ("
							+ iteration.getIndex() + " - "
							+ iteration.getSubject() + ")" + "|"
							+ userstory.getId());
				} else {
					selectItems.add(userstory.getSubject() + "|"
							+ userstory.getId());
				}
			}
		}
		return selectItems;
	}

	public List<IScrumDocument> getUserstoriesAsScrumDocuments(boolean reverse) {
		List<IScrumDocument> lstSD = new ArrayList<IScrumDocument>();
		for (Userstory userstory : getAllUserstories(SORT_BY_SUBJECT, reverse)) {
			lstSD.add(userstory);
		}
		return lstSD;
	}

	private Userstory loadUserstory(String strID) {
		if (m_UserstoryList == null
				|| UserstoryStorageService.getInstance()
						.isDirty(m_LastAccessed)) {
			m_UserstoryList = new HashMap<String, Userstory>();
		}
		if (m_UserstoryList.containsKey(strID)) {
			return m_UserstoryList.get(strID);
		}
		Userstory p = UserstoryStorageService.getInstance().getUserstoryById(
				strID, ExtLibUtil.getCurrentSession());
		m_LastAccessed = new Date();
		if (p != null) {
			m_UserstoryList.put(strID, p);
		}
		return p;
	}
}
