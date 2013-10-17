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

import java.util.Comparator;
import java.util.Date;


public class UserstoryComparator implements Comparator<Userstory> {
	private int m_SortOrder = UserstorySessionFacade.SORT_BY_SUBJECT;

	public UserstoryComparator(int iSortOrder) {
		m_SortOrder = iSortOrder;
	}

	public int compare(Userstory o1, Userstory o2) {
		String s1 = "";
		String s2 = "";
		switch (m_SortOrder) {
		case UserstorySessionFacade.SORT_BY_MODIFIEDAT:
			return compareDates(o1.getModifiedAt(), o2.getModifiedAt());
		case UserstorySessionFacade.SORT_BY_SUBJECT:
			s1 = o1.getSubject().toLowerCase();
			s2 = o2.getSubject().toLowerCase();
			break;
		case UserstorySessionFacade.SORT_BY_STATUS:
			s1 = o1.getStatus().toLowerCase();
			s2 = o2.getStatus().toLowerCase();
			break;
		case UserstorySessionFacade.SORT_BY_ITERATION:
			s1 = o1.getIteration();
			s2 = o2.getIteration();
			break;
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
