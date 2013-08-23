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
package biz.webgate.scrum.iteration;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IterationSortFactory {
	private static Comparator<Iteration> m_CreatedAtComp = new IterationComparator(
			IterationSessionFacade.SORT_BY_CREATEDAT);
	private static Comparator<Iteration> m_ModifiedAtComp = new IterationComparator(
			IterationSessionFacade.SORT_BY_MODIFIEDAT);
	private static Comparator<Iteration> m_IndexComp = new IterationComparator(
			IterationSessionFacade.SORT_BY_INDEX);
	private static Comparator<Iteration> m_SubjectComp = new IterationComparator(
			IterationSessionFacade.SORT_BY_SUBJECT);
	private static Comparator<Iteration> m_DueComp = new IterationComparator(
			IterationSessionFacade.SORT_BY_DUE);
	private static Comparator<Iteration> m_StatusComp = new IterationComparator(
			IterationSessionFacade.SORT_BY_STATUS);

	public static void sortIterations(List<Iteration> lstIterations,
			int sortOrder, boolean reverse) {
		switch (sortOrder) {
		case IterationSessionFacade.SORT_BY_CREATEDAT:
			Collections.sort(lstIterations, m_CreatedAtComp);
			break;
		case IterationSessionFacade.SORT_BY_MODIFIEDAT:
			Collections.sort(lstIterations, m_ModifiedAtComp);
			break;
		case IterationSessionFacade.SORT_BY_INDEX:
			Collections.sort(lstIterations, m_IndexComp);
			break;
		case IterationSessionFacade.SORT_BY_SUBJECT:
			Collections.sort(lstIterations, m_SubjectComp);
			break;
		case IterationSessionFacade.SORT_BY_DUE:
			Collections.sort(lstIterations, m_DueComp);
			break;
		case IterationSessionFacade.SORT_BY_STATUS:
			Collections.sort(lstIterations, m_StatusComp);
			break;
		}
		if (reverse) {
			Collections.reverse(lstIterations);
		}
	}
}
