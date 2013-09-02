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

import java.util.Comparator;
import java.util.Date;

public class ScrumDocumentComparator implements Comparator<IScrumDocument> {
	private int m_SortOrder = ScrumDocumentSessionFacade.SORT_BY_FORM;

	public ScrumDocumentComparator(int sortOrder) {
		m_SortOrder = sortOrder;
	}

	public int compare(IScrumDocument sd1, IScrumDocument sd2) {
		String s1 = "";
		String s2 = "";
		switch (m_SortOrder) {
		case ScrumDocumentSessionFacade.SORT_BY_FORM:
			s1 = sd1.getForm().toLowerCase();
			s2 = sd2.getForm().toLowerCase();
			break;
		case ScrumDocumentSessionFacade.SORT_BY_CREATEDAT:
			return compareDates(sd2.getCreatedAt(), sd1.getCreatedAt());
		case ScrumDocumentSessionFacade.SORT_BY_MODIFIEDAT:
			return compareDates(sd2.getModifiedAt(), sd1.getModifiedAt());
		case ScrumDocumentSessionFacade.SORT_BY_AUTHOR:
			s1 = sd1.getAuthor().toLowerCase();
			s2 = sd2.getAuthor().toLowerCase();
			break;
		case ScrumDocumentSessionFacade.SORT_BY_CUSTOMER:
			s1 = sd1.getCustomer().toLowerCase();
			s2 = sd2.getCustomer().toLowerCase();
			break;
		case ScrumDocumentSessionFacade.SORT_BY_PROJECT:
			s1 = sd1.getProject().toLowerCase();
			s2 = sd2.getProject().toLowerCase();
			break;
		case ScrumDocumentSessionFacade.SORT_BY_SUBJECT:
			s1 = sd1.getSubject().toLowerCase();
			s2 = sd2.getSubject().toLowerCase();
			break;
		case ScrumDocumentSessionFacade.SORT_BY_STATUS:
			s1 = sd1.getStatus().toLowerCase();
			s2 = sd2.getStatus().toLowerCase();
			break;
		case ScrumDocumentSessionFacade.SORT_BY_RESPONSIBLE:
			s1 = sd1.getResponsible().toLowerCase();
			s2 = sd2.getResponsible().toLowerCase();
			break;
		case ScrumDocumentSessionFacade.SORT_BY_DUEDATE:
			return compareDates(sd2.getDueDate(), sd1.getDueDate());
		case ScrumDocumentSessionFacade.SORT_BY_ID:
			if (sd1.getReadableId() != null)
				s1 = sd1.getReadableId().toLowerCase();
			if (sd2.getReadableId() != null)
				s2 = sd2.getReadableId().toLowerCase();
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
