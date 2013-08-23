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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjectSortFactory {
	private static Comparator<Project> m_ModifiedAtComp = new ProjectComparator(
			ProjectSessionFacade.SORT_BY_SUBJECT);
	private static Comparator<Project> m_SubjectComp = new ProjectComparator(
			ProjectSessionFacade.SORT_BY_SUBJECT);
	private static Comparator<Project> m_CustomerComp = new ProjectComparator(
			ProjectSessionFacade.SORT_BY_CUSTOMER);
	private static Comparator<Project> m_LeaderComp = new ProjectComparator(
			ProjectSessionFacade.SORT_BY_LEADER);
	private static Comparator<Project> m_StartComp = new ProjectComparator(
			ProjectSessionFacade.SORT_BY_START);
	private static Comparator<Project> m_EndComp = new ProjectComparator(
			ProjectSessionFacade.SORT_BY_END);

	public static void sortProjects(List<Project> lstProjects, int sortOrder, boolean reverse) {
		switch (sortOrder) {
		case ProjectSessionFacade.SORT_BY_MODIFIEDAT:
			Collections.sort(lstProjects, m_ModifiedAtComp);
			break;
		case ProjectSessionFacade.SORT_BY_SUBJECT:
			Collections.sort(lstProjects, m_SubjectComp);
			break;
		case ProjectSessionFacade.SORT_BY_CUSTOMER:
			Collections.sort(lstProjects, m_CustomerComp);
			break;
		case ProjectSessionFacade.SORT_BY_LEADER:
			Collections.sort(lstProjects, m_LeaderComp);
			break;
		case ProjectSessionFacade.SORT_BY_START:
			Collections.sort(lstProjects, m_StartComp);
			break;
		case ProjectSessionFacade.SORT_BY_END:
			Collections.sort(lstProjects, m_EndComp);
			break;
		}
		if (reverse) {Collections.reverse(lstProjects);}
	}
}
