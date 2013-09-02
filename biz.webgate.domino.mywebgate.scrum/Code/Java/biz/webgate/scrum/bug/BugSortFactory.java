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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BugSortFactory {
	private static Comparator<Bug> m_CustomerComp = new BugComparator(
			BugSessionFacade.SORT_BY_CUSTOMER);
	private static Comparator<Bug> m_ProjectComp = new BugComparator(
			BugSessionFacade.SORT_BY_PROJECT);
	private static Comparator<Bug> m_UserstoryComp = new BugComparator(
			BugSessionFacade.SORT_BY_USERSTORY);
	private static Comparator<Bug> m_CreatedAtComp = new BugComparator(
			BugSessionFacade.SORT_BY_CREATEDAT);
	private static Comparator<Bug> m_ModifiedAtComp = new BugComparator(
			BugSessionFacade.SORT_BY_MODIFIEDAT);
	private static Comparator<Bug> m_AuthorComp = new BugComparator(
			BugSessionFacade.SORT_BY_AUTHOR);
	private static Comparator<Bug> m_SubjectComp = new BugComparator(
			BugSessionFacade.SORT_BY_SUBJECT);
	private static Comparator<Bug> m_EditorComp = new BugComparator(
			BugSessionFacade.SORT_BY_EDITOR);
	private static Comparator<Bug> m_DueComp = new BugComparator(
			BugSessionFacade.SORT_BY_DUE);
	private static Comparator<Bug> m_StatusComp = new BugComparator(
			BugSessionFacade.SORT_BY_STATUS);
	private static Comparator<Bug> m_IdComp = new BugComparator(
			BugSessionFacade.SORT_BY_ID);

	public static void sortBugs(List<Bug> lstBugs, int sortOrder, boolean reverse) {
		switch (sortOrder) {
		case BugSessionFacade.SORT_BY_CUSTOMER:
			Collections.sort(lstBugs, m_CustomerComp);
			break;
		case BugSessionFacade.SORT_BY_PROJECT:
			Collections.sort(lstBugs, m_ProjectComp);
			break;
		case BugSessionFacade.SORT_BY_USERSTORY:
			Collections.sort(lstBugs, m_UserstoryComp);
			break;
		case BugSessionFacade.SORT_BY_CREATEDAT:
			Collections.sort(lstBugs, m_CreatedAtComp);
			break;
		case BugSessionFacade.SORT_BY_MODIFIEDAT:
			Collections.sort(lstBugs, m_ModifiedAtComp);
			break;
		case BugSessionFacade.SORT_BY_AUTHOR:
			Collections.sort(lstBugs, m_AuthorComp);
			break;
		case BugSessionFacade.SORT_BY_SUBJECT:
			Collections.sort(lstBugs, m_SubjectComp);
			break;
		case BugSessionFacade.SORT_BY_EDITOR:
			Collections.sort(lstBugs, m_EditorComp);
			break;
		case BugSessionFacade.SORT_BY_DUE:
			Collections.sort(lstBugs, m_DueComp);
			break;
		case BugSessionFacade.SORT_BY_STATUS:
			Collections.sort(lstBugs, m_StatusComp);
			break;
		case BugSessionFacade.SORT_BY_ID:
			Collections.sort(lstBugs, m_IdComp);
			break;
		}
		if (reverse) {Collections.reverse(lstBugs);}
	}
}
