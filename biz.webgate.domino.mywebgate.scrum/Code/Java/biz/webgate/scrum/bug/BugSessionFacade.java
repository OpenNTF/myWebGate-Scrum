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
package biz.webgate.scrum.bug;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;

import biz.webgate.scrum.scrumdocument.IScrumDocument;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class BugSessionFacade {

	public static final String BEAN_NAME = "bugBean"; //$NON-NLS-1$

	public static BugSessionFacade get(FacesContext context) {
		BugSessionFacade bean = (BugSessionFacade) context.getApplication()
				.getVariableResolver().resolveVariable(context, BEAN_NAME);
		return bean;
	}

	public static BugSessionFacade get() {
		return get(FacesContext.getCurrentInstance());
	}
	
	private HashMap<String, Bug> m_BugList;
	private Date m_LastAccessed = new Date();

	public static final int SORT_BY_CUSTOMER = 1;
	public static final int SORT_BY_PROJECT = 2;
	public static final int SORT_BY_USERSTORY = 3;
	public static final int SORT_BY_CREATEDAT = 4;
	public static final int SORT_BY_MODIFIEDAT = 5;
	public static final int SORT_BY_AUTHOR = 6;
	public static final int SORT_BY_SUBJECT = 7;
	public static final int SORT_BY_EDITOR = 8;
	public static final int SORT_BY_DUE = 9;
	public static final int SORT_BY_STATUS = 10;
	public static final int SORT_BY_ID = 11;
	
	public Bug createNewBug(String projID) {
		return BugStorageService.getInstance().createNewBug(projID, ExtLibUtil.getCurrentSession());
	}

	public boolean saveBug(Bug curBug) {
		m_BugList = null;
		m_LastAccessed = new Date();
		return BugStorageService.getInstance().saveBug(curBug, ExtLibUtil.getCurrentSession());
	}

	public boolean deleteBug(Bug curBug) {
		m_BugList = null;
		m_LastAccessed = new Date();
		return BugStorageService.getInstance().deleteBug(curBug, ExtLibUtil.getCurrentSession());
	}

	public Bug getBugById(String strBugId) {
		return loadUserstory(strBugId);
	}

	public List<Bug> getAllBugs(int sortOrder, boolean reverse, String filter) {
		List<Bug> lstAll = BugStorageService.getInstance().getAllBugs(ExtLibUtil.getCurrentSession(), filter);
		BugSortFactory.sortBugs(lstAll, sortOrder, reverse);
		return lstAll;
	}

	public List<Bug> getMyBugs(int sortOrder, boolean reverse, int involvedType, String filter, boolean showCompleted) {
		List<Bug> lstMy = BugStorageService.getInstance().getMyBugs(ExtLibUtil.getCurrentSession(), involvedType, filter, showCompleted);
		BugSortFactory.sortBugs(lstMy, sortOrder, reverse);
		return lstMy;
	}

	public List<Bug> getMyBugs(int sortOrder, boolean reverse, String involvedType, String filter, boolean showCompleted) {
		return getMyBugs(sortOrder, reverse, Integer.parseInt(involvedType), filter, showCompleted);
	}

	public List<Bug> getBugsOfProject(int sortOrder, boolean reverse, String projectID, String statusFilter, String usFilter, String itFilter, boolean hideComplete) {
		List<Bug> lstBugsOfProject = BugStorageService.getInstance().getBugsOfProject(ExtLibUtil.getCurrentSession(), projectID, statusFilter, usFilter, itFilter, hideComplete);
		BugSortFactory.sortBugs(lstBugsOfProject, sortOrder, reverse);
		return lstBugsOfProject;
	}

	public List<Bug> getBugsOfUserstory(int sortOrder, boolean reverse, String userstoryID, String filter) {
		List<Bug> lstBugsOfUserstory = BugStorageService.getInstance().getBugsOfUserstory(ExtLibUtil.getCurrentSession(), userstoryID, filter);
		BugSortFactory.sortBugs(lstBugsOfUserstory, sortOrder, reverse);
		return lstBugsOfUserstory;
	}

	public List<Bug> getBugsOfIteration(int sortOrder, boolean reverse, String iterationID, String statusFilter) {
		List<Bug> lstBugsOfIteration = new ArrayList<Bug>();
		for (Bug bug : getAllBugs(sortOrder, reverse, statusFilter)) {
			if (bug.getIterationId().equals(iterationID)) {
				lstBugsOfIteration.add(bug);
			}
		}
		BugSortFactory.sortBugs(lstBugsOfIteration, sortOrder, reverse);
		return lstBugsOfIteration;
	}

	public List<IScrumDocument> getBugsAsScrumDocuments(boolean reverse) {
		List<IScrumDocument> lstSD = new ArrayList<IScrumDocument>();
		for (Bug bug : getAllBugs(SORT_BY_SUBJECT, reverse, "")) {
			lstSD.add(bug);
		}
		return lstSD;
	}

	private Bug loadUserstory(String strID) {
		if (m_BugList == null || BugStorageService.getInstance().isDirty(m_LastAccessed)) {
			m_BugList = new HashMap<String, Bug>();
		}
		if (m_BugList.containsKey(strID)) {
			return m_BugList.get(strID);
		}
		Bug b = BugStorageService.getInstance().getBugById(strID, ExtLibUtil.getCurrentSession());
		m_LastAccessed = new Date();
		if (b != null) {
			m_BugList.put(strID, b);
		}
		return b;
	}
}
