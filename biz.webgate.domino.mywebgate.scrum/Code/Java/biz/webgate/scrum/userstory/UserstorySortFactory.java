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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserstorySortFactory {
	private static Comparator<Userstory> m_ModifiedAtComp = new UserstoryComparator(
			UserstorySessionFacade.SORT_BY_MODIFIEDAT);
	private static Comparator<Userstory> m_SubjectComp = new UserstoryComparator(
			UserstorySessionFacade.SORT_BY_SUBJECT);
	private static Comparator<Userstory> m_StatusComp = new UserstoryComparator(
			UserstorySessionFacade.SORT_BY_STATUS);
	private static Comparator<Userstory> m_IterationComp = new UserstoryComparator(
			UserstorySessionFacade.SORT_BY_ITERATION);
	private static Comparator<Userstory> m_DueComp = new UserstoryComparator(
			UserstorySessionFacade.SORT_BY_DUE);

	public static void sortUserstories(List<Userstory> lstUserstories, int sortOrder, boolean reverse) {
		switch (sortOrder) {
		case UserstorySessionFacade.SORT_BY_MODIFIEDAT:
			Collections.sort(lstUserstories, m_ModifiedAtComp);
			break;
		case UserstorySessionFacade.SORT_BY_SUBJECT:
			Collections.sort(lstUserstories, m_SubjectComp);
			break;
		case UserstorySessionFacade.SORT_BY_STATUS:
			Collections.sort(lstUserstories, m_StatusComp);
			break;
		case UserstorySessionFacade.SORT_BY_ITERATION:
			Collections.sort(lstUserstories, m_IterationComp);
			break;
		case UserstorySessionFacade.SORT_BY_DUE:
			Collections.sort(lstUserstories, m_DueComp);
			break;
		}
		if (reverse) {Collections.reverse(lstUserstories);}
	}
}
