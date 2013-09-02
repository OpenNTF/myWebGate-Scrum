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
package biz.webgate.scrum.scrumdocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import biz.webgate.scrum.bug.BugSessionFacade;
import biz.webgate.scrum.iteration.IterationSessionFacade;
import biz.webgate.scrum.project.ProjectSessionFacade;
import biz.webgate.scrum.task.TaskSessionFacade;
import biz.webgate.scrum.userstory.UserstorySessionFacade;

public class ScrumDocumentSessionFacade {
	public static final String BEAN_NAME = "documentBean"; //$NON-NLS-1$

	public static ScrumDocumentSessionFacade get(FacesContext context) {
		ScrumDocumentSessionFacade bean = (ScrumDocumentSessionFacade) context
				.getApplication().getVariableResolver().resolveVariable(
						context, BEAN_NAME);
		return bean;
	}

	public static ScrumDocumentSessionFacade get() {
		return get(FacesContext.getCurrentInstance());
	}

	public static final int SORT_BY_FORM = 1;
	public static final int SORT_BY_CREATEDAT = 2;
	public static final int SORT_BY_MODIFIEDAT = 3;
	public static final int SORT_BY_AUTHOR = 4;
	public static final int SORT_BY_CUSTOMER = 5;
	public static final int SORT_BY_PROJECT = 6;
	public static final int SORT_BY_SUBJECT = 7;
	public static final int SORT_BY_STATUS = 8;
	public static final int SORT_BY_RESPONSIBLE = 9;
	public static final int SORT_BY_DUEDATE = 10;
	public static final int SORT_BY_ID = 11;

	public IScrumDocument getDocumentById(String strId) {
		if (ProjectSessionFacade.get().getProjectById(strId) != null) {
			return ProjectSessionFacade.get().getProjectById(strId);
		} else if (IterationSessionFacade.get().getIterationById(strId) != null) {
			return IterationSessionFacade.get().getIterationById(strId);
		} else if (UserstorySessionFacade.get().getUserstoryById(strId) != null) {
			return UserstorySessionFacade.get().getUserstoryById(strId);
		} else if (TaskSessionFacade.get().getTaskById(strId) != null) {
			return TaskSessionFacade.get().getTaskById(strId);
		} else if (BugSessionFacade.get().getBugById(strId) != null) {
			return BugSessionFacade.get().getBugById(strId);
		} else {
			System.out.println("Could not get Document with id " + strId
					+ ". No form associable.");
		}
		return null;
	}

	public List<IScrumDocument> getAllDocuments(int sortOrder, boolean reverse) {
		List<IScrumDocument> lstAll = new ArrayList<IScrumDocument>();
		lstAll.addAll(ProjectSessionFacade.get().getProjectsAsScrumDocuments());
		lstAll.addAll(UserstorySessionFacade.get().getUserstoriesAsScrumDocuments(reverse));
		lstAll.addAll(TaskSessionFacade.get().getTasksAsScrumDocuments(false));
		lstAll.addAll(BugSessionFacade.get().getBugsAsScrumDocuments(reverse));
		ScrumDocumentSortFactory.sortDocuments(lstAll, sortOrder, reverse);
		return lstAll;
	}

	public List<IScrumDocument> getTBDocuments(int sortOrder, boolean reverse,
			boolean complete, String filterType, String filterStatus,
			String filterAssignee) {
		try {
			List<IScrumDocument> lstAll = new ArrayList<IScrumDocument>();

			if ((filterType.equals("") || filterType.equals("Tasks"))) {
				for (IScrumDocument task : TaskSessionFacade.get().getTasksAsScrumDocuments(false)) {
					if ((complete == false && !task.getStatus().equals("4") || complete == true)
							&& (filterStatus.equals("") || filterStatus.equals(task.getStatus()))
							&& (filterAssignee.equals("") || filterAssignee.equals(task.getResponsible()))) {
						lstAll.add(task);
					}
				}
			}
			if ((filterType.equals("") || filterType.equals("Bugs"))) {
				for (IScrumDocument bug : BugSessionFacade.get().getBugsAsScrumDocuments(false)) {
					if ((complete == false && !bug.getStatus().equals("4") || complete == true)
							&& (filterStatus.equals("") || filterStatus.equals(bug.getStatus()))
							&& (filterAssignee.equals("") || filterAssignee.equals(bug.getResponsible()))) {
						lstAll.add(bug);
					}
				}
			}
			ScrumDocumentSortFactory.sortDocuments(lstAll, sortOrder, reverse);
			return lstAll;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<String> getTBAssignees() throws IOException {
		List<String> myList = new ArrayList<String>();

		for (IScrumDocument task : TaskSessionFacade.get().getTasksAsScrumDocuments(false)) {
			if (!task.getResponsible().equals("") && !myList.contains(task.getResponsible())) {
				myList.add(task.getResponsible());
			}
		}
		for (IScrumDocument bug : BugSessionFacade.get().getBugsAsScrumDocuments(false)) {
			if (!bug.getResponsible().equals("") && !myList.contains(bug.getResponsible())) {
				myList.add(bug.getResponsible());
			}
		}

		ScrumDocumentSortFactory.sortList(myList);
		return myList;
	}

	public List<IScrumDocument> getDocumentsByTag(int sortOrder, boolean reverse, String tag) {
		List<IScrumDocument> lstByTag = new ArrayList<IScrumDocument>();
		for (IScrumDocument doc : getAllDocuments(sortOrder, reverse)) {
			if (doc.getTags().contains(tag)) {
				lstByTag.add(doc);
			}
		}
		return lstByTag;
	}

	public List<IScrumDocument> getDocumentsSinceDate(int sortOrder, boolean reverse, Date limit) {
		if (limit == null) {
			return getAllDocuments(sortOrder, reverse);
		}
		List<IScrumDocument> lstWithLimit = new ArrayList<IScrumDocument>();
		Calendar cal1 = Calendar.getInstance(), cal2 = Calendar.getInstance();
		long t1, t2;
		for (IScrumDocument doc : getAllDocuments(SORT_BY_CREATEDAT, false)) {
			if (doc.getCreatedAt() == null) {
				lstWithLimit.add(doc);
			} else {
				cal1.setTime(doc.getCreatedAt());
				cal1.set(Calendar.HOUR_OF_DAY, 0);
				cal1.set(Calendar.MINUTE, 0);
				cal1.set(Calendar.SECOND, 0);
				cal1.set(Calendar.MILLISECOND, 0);
				t1 = cal1.getTimeInMillis();
				cal2.setTime(limit);
				cal2.set(Calendar.HOUR_OF_DAY, 0);
				cal2.set(Calendar.MINUTE, 0);
				cal2.set(Calendar.SECOND, 0);
				cal2.set(Calendar.MILLISECOND, 0);
				t2 = cal2.getTimeInMillis();
				if (t1 >= t2) {
					lstWithLimit.add(doc);
				} else {
					break;
				}
			}
		}
		ScrumDocumentSortFactory.sortDocuments(lstWithLimit, sortOrder, reverse);
		return lstWithLimit;
	}
}
