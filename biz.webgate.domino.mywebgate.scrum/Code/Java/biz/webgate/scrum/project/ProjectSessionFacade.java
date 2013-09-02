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
package biz.webgate.scrum.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import biz.webgate.scrum.scrumdocument.IScrumDocument;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class ProjectSessionFacade {
	public static final String BEAN_NAME = "projectBean"; //$NON-NLS-1$

	public static ProjectSessionFacade get(FacesContext context) {
		ProjectSessionFacade bean = (ProjectSessionFacade) context
				.getApplication().getVariableResolver().resolveVariable(
						context, BEAN_NAME);
		return bean;
	}

	public static ProjectSessionFacade get() {
		return get(FacesContext.getCurrentInstance());
	}

	public static final int SORT_BY_MODIFIEDAT = 1;
	public static final int SORT_BY_SUBJECT = 2;
	public static final int SORT_BY_CUSTOMER = 3;
	public static final int SORT_BY_LEADER = 4;
	public static final int SORT_BY_START = 5;
	public static final int SORT_BY_END = 6;

	private HashMap<String, Project> m_ProjectList;
	private Date m_LastAccessed = new Date();

	public Project createNewProject() {		
		return ProjectStorageService.getInstance().createNewProject(ExtLibUtil.getCurrentSession());		
	}

	public boolean saveProject(Project curProject) {
		m_ProjectList = null;
		m_LastAccessed = new Date();
		return ProjectStorageService.getInstance().saveProject(curProject, ExtLibUtil.getCurrentSession());
	}

	public boolean deleteProject(Project curProject) {
		m_ProjectList = null;
		m_LastAccessed = new Date();
		return ProjectStorageService.getInstance().deleteProject(curProject, ExtLibUtil.getCurrentSession());
	}

	public Project getProjectById(String strProjectId) {
		return loadProject(strProjectId);
	}

	public List<Project> getAllProjects(int sortOrder, boolean reverse) {
		List<Project> lstAll = ProjectStorageService.getInstance().getAllProjects(ExtLibUtil.getCurrentSession());
		ProjectSortFactory.sortProjects(lstAll, sortOrder, reverse);
		return lstAll;
	}

	public List<Project> getMyProjects(int sortOrder, boolean reverse) {
		List<Project> lstMy = ProjectStorageService.getInstance().getMyProjects(ExtLibUtil.getCurrentSession());
		ProjectSortFactory.sortProjects(lstMy, sortOrder, reverse);
		return lstMy;
	}

	public List<IScrumDocument> getProjectsAsScrumDocuments() {
		List<IScrumDocument> lstSD = new ArrayList<IScrumDocument>();
		for (Project project : getAllProjects(SORT_BY_SUBJECT, false)) {
			lstSD.add(project);
		}
		return lstSD;
	}

	public String getProjectNameByProjectID(String projectId) {
		Project p = loadProject(projectId);
		if (p != null) {
			return p.getSubject();
		}
		return "";
	}

	public String getCustomerNameByProjectID(String projectId) {
		Project p = loadProject(projectId);
		if (p != null) {
			return p.getCustomer();
		}
		return "";
	}

	private Project loadProject(String strID) {
		if (m_ProjectList == null || ProjectStorageService.getInstance().isDirty(m_LastAccessed)) {
			m_ProjectList = new HashMap<String, Project>();
		}
		if (m_ProjectList.containsKey(strID)) {
			return m_ProjectList.get(strID);
		}
		Project p = ProjectStorageService.getInstance().getProjectById(strID, ExtLibUtil.getCurrentSession());
		m_LastAccessed = new Date();
		if (p != null) {
			m_ProjectList.put(strID, p);
		}
		return p;
	}
}
