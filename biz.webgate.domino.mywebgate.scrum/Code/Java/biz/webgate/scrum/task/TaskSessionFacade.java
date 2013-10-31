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
package biz.webgate.scrum.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;

import biz.webgate.scrum.scrumdocument.IScrumDocument;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class TaskSessionFacade {
	public static final String BEAN_NAME = "taskBean"; //$NON-NLS-1$

	public static TaskSessionFacade get(FacesContext context) {
		TaskSessionFacade bean = (TaskSessionFacade) context.getApplication().getVariableResolver().resolveVariable(context, BEAN_NAME);
		return bean;
	}

	public static TaskSessionFacade get() {
		return get(FacesContext.getCurrentInstance());
	}

	private HashMap<String, Task> m_TaskList;
	private Date m_LastAccessed = new Date();

	public static final int SORT_BY_CUSTOMER = 1;
	public static final int SORT_BY_PROJECT = 2;
	public static final int SORT_BY_USERSTORY = 3;
	public static final int SORT_BY_ITERATION = 4;
	public static final int SORT_BY_CREATEDAT = 5;
	public static final int SORT_BY_MODIFIEDAT = 6;
	public static final int SORT_BY_AUTHOR = 7;
	public static final int SORT_BY_SUBJECT = 8;
	public static final int SORT_BY_EDITOR = 9;
	public static final int SORT_BY_DUE = 10;
	
	public static final int SORT_BY_STATUS = 12;
	public static final int SORT_BY_ID = 13;

	public Task createNewTask(String projID) {		
		return TaskStorageService.getInstance().createNewTask(projID, ExtLibUtil.getCurrentSession());
	}

	public boolean saveTask(Task curTask) {
		m_TaskList = null;
		m_LastAccessed = new Date();
		return TaskStorageService.getInstance().saveTask(curTask, ExtLibUtil.getCurrentSession());
	}

	public boolean deleteTask(Task curTask) {
		m_TaskList = null;
		m_LastAccessed = new Date();
		return TaskStorageService.getInstance().deleteTask(curTask, ExtLibUtil.getCurrentSession());
	}

	public Task getTaskById(String strTaskId) {
		return loadUserstory(strTaskId);
	}

	public List<Task> getAllTasks(int sortOrder, boolean reverse, String statusFilter) {
		List<Task> lstAll = TaskStorageService.getInstance().getAllTasks(ExtLibUtil.getCurrentSession(), statusFilter);
		TaskSortFactory.sortTasks(lstAll, sortOrder, reverse);
		return lstAll;
	}

	public List<Task> getMyTasks(int sortOrder, boolean reverse, int involvedType, String statusFilter, boolean showCompleted) {
		List<Task> lstMy = TaskStorageService.getInstance().getMyTasks(ExtLibUtil.getCurrentSession(), involvedType, statusFilter, showCompleted);
		TaskSortFactory.sortTasks(lstMy, sortOrder, reverse);
		return lstMy;
	}

	public List<Task> getTasksOfProject(int sortOrder, boolean reverse, String projectID, String statusFilter, String usFilter, String itFilter, boolean hideComplete) {
		List<Task> lstTasksOfProject = TaskStorageService.getInstance().getTasksOfProject(ExtLibUtil.getCurrentSession(), projectID, statusFilter, usFilter, itFilter, hideComplete);
		TaskSortFactory.sortTasks(lstTasksOfProject, sortOrder, reverse);
		//TODO: secondary sort by ID
		return lstTasksOfProject;
	}

	public List<Task> getTasksOfUserstory(int sortOrder, boolean reverse, String userstoryID, String statusFilter, boolean isExecutable) {
		List<Task> lstTasksOfUserstory = TaskStorageService.getInstance().getTasksOfUserstory(ExtLibUtil.getCurrentSession(), userstoryID, statusFilter, isExecutable);
		TaskSortFactory.sortTasks(lstTasksOfUserstory, sortOrder, reverse);
		return lstTasksOfUserstory;
	}

	public List<Task> getTasksOfIteration(int sortOrder, boolean reverse, String iterationID, String statusFilter) {
		List<Task> lstTasksOfIteration = new ArrayList<Task>();
		for (Task task : getAllTasks(sortOrder, reverse, statusFilter)) {
			if (task.getIterationId().equals(iterationID)) {
				lstTasksOfIteration.add(task);
			}
		}
		TaskSortFactory.sortTasks(lstTasksOfIteration, sortOrder, reverse);
		return lstTasksOfIteration;
	}

	public List<IScrumDocument> getTasksAsScrumDocuments(boolean reverse) {
		List<IScrumDocument> lstSD = new ArrayList<IScrumDocument>();
		for (Task task : getAllTasks(SORT_BY_SUBJECT, reverse, "")) {
			lstSD.add(task);
		}
		return lstSD;
	}

	private Task loadUserstory(String strID) {
		if (m_TaskList == null || TaskStorageService.getInstance().isDirty(m_LastAccessed)) {
			m_TaskList = new HashMap<String, Task>();
		}
		if (m_TaskList.containsKey(strID)) {
			return m_TaskList.get(strID);
		}
		Task p = TaskStorageService.getInstance().getTaskById(strID, ExtLibUtil.getCurrentSession());
		m_LastAccessed = new Date();
		if (p != null) {
			m_TaskList.put(strID, p);
		}
		return p;
	}
		
}
