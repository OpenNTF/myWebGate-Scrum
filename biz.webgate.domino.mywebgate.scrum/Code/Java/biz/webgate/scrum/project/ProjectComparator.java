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

import java.util.Comparator;
import java.util.Date;

public class ProjectComparator implements Comparator<Project> {
	private int m_SortOrder = ProjectSessionFacade.SORT_BY_SUBJECT;

	public ProjectComparator(int iSortOrder) {
		m_SortOrder = iSortOrder;
	}

	public int compare(Project o1, Project o2) {
		String s1 = "";
		String s2 = "";
		switch (m_SortOrder) {
		case ProjectSessionFacade.SORT_BY_MODIFIEDAT:
			return compareDates(o1.getModifiedAt(), o2.getModifiedAt());
		case ProjectSessionFacade.SORT_BY_SUBJECT:
			s1 = o1.getSubject().toLowerCase();
			s2 = o2.getSubject().toLowerCase();
			break;
		case ProjectSessionFacade.SORT_BY_CUSTOMER:
			s1 = o1.getCustomer().toLowerCase();
			s2 = o2.getCustomer().toLowerCase();
			break;
		case ProjectSessionFacade.SORT_BY_LEADER:
			s1 = o1.getLeader().toLowerCase();
			s2 = o2.getLeader().toLowerCase();
			break;
		case ProjectSessionFacade.SORT_BY_START:
			return compareDates(o1.getStart(), o2.getStart());
		case ProjectSessionFacade.SORT_BY_END:
			return compareDates(o1.getEnd(), o2.getEnd());
		}

		return s1.compareTo(s2);
	}

	int compareDates(Date d1, Date d2) {
		if (d1 == null) {
			d1 = new Date();
		}
		if (d2 == null) {
			d2 = new Date();
		}
		return d1.compareTo(d2);
	}
}
