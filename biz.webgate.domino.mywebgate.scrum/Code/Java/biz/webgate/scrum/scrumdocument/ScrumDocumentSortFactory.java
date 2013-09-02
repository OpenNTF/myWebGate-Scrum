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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScrumDocumentSortFactory {
	private static Comparator<IScrumDocument> m_FormComp = new ScrumDocumentComparator(
			ScrumDocumentSessionFacade.SORT_BY_FORM);
	private static Comparator<IScrumDocument> m_CustomerComp = new ScrumDocumentComparator(
			ScrumDocumentSessionFacade.SORT_BY_CUSTOMER);
	private static Comparator<IScrumDocument> m_CreatedAtComp = new ScrumDocumentComparator(
			ScrumDocumentSessionFacade.SORT_BY_CREATEDAT);
	private static Comparator<IScrumDocument> m_ModifiedAtComp = new ScrumDocumentComparator(
			ScrumDocumentSessionFacade.SORT_BY_MODIFIEDAT);
	private static Comparator<IScrumDocument> m_AuthorComp = new ScrumDocumentComparator(
			ScrumDocumentSessionFacade.SORT_BY_AUTHOR);
	private static Comparator<IScrumDocument> m_ProjectComp = new ScrumDocumentComparator(
			ScrumDocumentSessionFacade.SORT_BY_PROJECT);
	private static Comparator<IScrumDocument> m_SubjectComp = new ScrumDocumentComparator(
			ScrumDocumentSessionFacade.SORT_BY_SUBJECT);
	private static Comparator<IScrumDocument> m_StatusComp = new ScrumDocumentComparator(
			ScrumDocumentSessionFacade.SORT_BY_STATUS);
	private static Comparator<IScrumDocument> m_ResponsibleComp = new ScrumDocumentComparator(
			ScrumDocumentSessionFacade.SORT_BY_RESPONSIBLE);
	private static Comparator<IScrumDocument> m_DueDateComp = new ScrumDocumentComparator(
			ScrumDocumentSessionFacade.SORT_BY_DUEDATE);
	private static Comparator<IScrumDocument> m_IdComp = new ScrumDocumentComparator(
			ScrumDocumentSessionFacade.SORT_BY_ID);
	
	public static void sortDocuments(List<IScrumDocument> lstDocument,
			int sortOrder, boolean reverse) {
		switch (sortOrder) {
		case ScrumDocumentSessionFacade.SORT_BY_FORM:
			Collections.sort(lstDocument, m_FormComp);
			break;
		case ScrumDocumentSessionFacade.SORT_BY_CUSTOMER:
			Collections.sort(lstDocument, m_CustomerComp);
			break;
		case ScrumDocumentSessionFacade.SORT_BY_CREATEDAT:
			Collections.sort(lstDocument, m_CreatedAtComp);
			break;
		case ScrumDocumentSessionFacade.SORT_BY_MODIFIEDAT:
			Collections.sort(lstDocument, m_ModifiedAtComp);
			break;
		case ScrumDocumentSessionFacade.SORT_BY_PROJECT:
			Collections.sort(lstDocument, m_ProjectComp);
			break;
		case ScrumDocumentSessionFacade.SORT_BY_AUTHOR:
			Collections.sort(lstDocument, m_AuthorComp);
			break;
		case ScrumDocumentSessionFacade.SORT_BY_SUBJECT:
			Collections.sort(lstDocument, m_SubjectComp);
			break;
		case ScrumDocumentSessionFacade.SORT_BY_STATUS:
			Collections.sort(lstDocument, m_StatusComp);
			break;
		case ScrumDocumentSessionFacade.SORT_BY_RESPONSIBLE:
			Collections.sort(lstDocument, m_ResponsibleComp);
			break;
		case ScrumDocumentSessionFacade.SORT_BY_DUEDATE:
			Collections.sort(lstDocument, m_DueDateComp);
			break;
		case ScrumDocumentSessionFacade.SORT_BY_ID:
			Collections.sort(lstDocument, m_IdComp);
			break;
		}
		if (reverse) {
			Collections.reverse(lstDocument);
		}
	}
	
	public static void sortList(List<String> lstValues) {
		Collections.sort(lstValues);
	}
}
