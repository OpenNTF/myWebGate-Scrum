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

import java.util.Comparator;
import java.util.Date;

import biz.webgate.scrum.project.ProjectSessionFacade;
import biz.webgate.scrum.userstory.UserstorySessionFacade;

public class TaskComparator implements Comparator<Task> {
	private int m_SortOrder = TaskSessionFacade.SORT_BY_SUBJECT;

	public TaskComparator(int iSortOrder) {
		m_SortOrder = iSortOrder;
	}

	public int compare(Task o1, Task o2) {
		String s1 = "";
		String s2 = "";
		switch (m_SortOrder) {
		case TaskSessionFacade.SORT_BY_CUSTOMER:
			if (ProjectSessionFacade.get().getProjectById(o1.getProjectId()) != null) {
				s1 = ProjectSessionFacade.get().getProjectById(
						o1.getProjectId()).getCustomer().toLowerCase();
			}
			if (ProjectSessionFacade.get().getProjectById(o2.getProjectId()) != null) {
				s2 = ProjectSessionFacade.get().getProjectById(
						o2.getProjectId()).getCustomer().toLowerCase();
			}
			break;
		case TaskSessionFacade.SORT_BY_PROJECT:
			if (ProjectSessionFacade.get().getProjectById(o1.getProjectId()) != null) {
				s1 = ProjectSessionFacade.get().getProjectById(
						o1.getProjectId()).getSubject().toLowerCase();
			}
			if (ProjectSessionFacade.get().getProjectById(o2.getProjectId()) != null) {
				s2 = ProjectSessionFacade.get().getProjectById(
						o2.getProjectId()).getSubject().toLowerCase();
			}
			break;
		case TaskSessionFacade.SORT_BY_USERSTORY:
			if (UserstorySessionFacade.get().getUserstoryById(
					o1.getUserstoryId()) != null) {
				s1 = UserstorySessionFacade.get().getUserstoryById(
						o1.getUserstoryId()).getSubject().toLowerCase();
			}
			if (UserstorySessionFacade.get().getUserstoryById(
					o2.getUserstoryId()) != null) {
				s2 = UserstorySessionFacade.get().getUserstoryById(
						o2.getUserstoryId()).getSubject().toLowerCase();
			}
			break;
		case TaskSessionFacade.SORT_BY_ITERATION:
			if (UserstorySessionFacade.get().getUserstoryById(
					o1.getUserstoryId()) != null) {
				s1 = UserstorySessionFacade.get().getUserstoryById(
						o1.getUserstoryId()).getIterationNo();
			}
			if (UserstorySessionFacade.get().getUserstoryById(
					o2.getUserstoryId()) != null) {
				s2 = UserstorySessionFacade.get().getUserstoryById(
						o2.getUserstoryId()).getIterationNo();
			}
			break;
		case TaskSessionFacade.SORT_BY_CREATEDAT:
			return compareDates(o1.getCreatedAt(), o2.getCreatedAt());
		case TaskSessionFacade.SORT_BY_MODIFIEDAT:
			return compareDates(o1.getModifiedAt(), o2.getModifiedAt());
		case TaskSessionFacade.SORT_BY_AUTHOR:
			s1 = o1.getAuthor().toLowerCase();
			s2 = o2.getAuthor().toLowerCase();
			break;
		case TaskSessionFacade.SORT_BY_SUBJECT:
			s1 = o1.getSubject().toLowerCase();
			s2 = o2.getSubject().toLowerCase();
			break;
		case TaskSessionFacade.SORT_BY_EDITOR:
			s1 = o1.getEditor().toLowerCase();
			s2 = o2.getEditor().toLowerCase();
			break;
		case TaskSessionFacade.SORT_BY_DUE:
			return compareDates(o1.getDue(), o2.getDue());		
		case TaskSessionFacade.SORT_BY_STATUS:
			s1 = o1.getStatus().toLowerCase();
			s2 = o2.getStatus().toLowerCase();
			break;
		case TaskSessionFacade.SORT_BY_ID:
			if (o1.getTaskId() != null)
				s1 = o1.getTaskId().toLowerCase();
			if (o2.getTaskId() != null)
				s2 = o2.getTaskId().toLowerCase();
			break;
		}

		return s1.compareTo(s2);
	}

	@SuppressWarnings("deprecation")
	int compareDates(Date d1, Date d2) {
		if (d1 == null) {
			d1 = new Date(2099,12,31);
		}
		if (d2 == null) {
			d2 = new Date(2099,12,31);
		}
		return d1.compareTo(d2);
	}
}
