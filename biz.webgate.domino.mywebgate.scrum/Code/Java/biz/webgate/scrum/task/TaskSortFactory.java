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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskSortFactory {
	private static Comparator<Task> m_CustomerComp = new TaskComparator(
			TaskSessionFacade.SORT_BY_CUSTOMER);
	private static Comparator<Task> m_ProjectComp = new TaskComparator(
			TaskSessionFacade.SORT_BY_PROJECT);
	private static Comparator<Task> m_UserstoryComp = new TaskComparator(
			TaskSessionFacade.SORT_BY_USERSTORY);
	private static Comparator<Task> m_IterationComp = new TaskComparator(
			TaskSessionFacade.SORT_BY_ITERATION);
	private static Comparator<Task> m_CreatedAtComp = new TaskComparator(
			TaskSessionFacade.SORT_BY_CREATEDAT);
	private static Comparator<Task> m_ModifiedAtComp = new TaskComparator(
			TaskSessionFacade.SORT_BY_MODIFIEDAT);
	private static Comparator<Task> m_AuthorComp = new TaskComparator(
			TaskSessionFacade.SORT_BY_AUTHOR);
	private static Comparator<Task> m_SubjectComp = new TaskComparator(
			TaskSessionFacade.SORT_BY_SUBJECT);
	private static Comparator<Task> m_EditorComp = new TaskComparator(
			TaskSessionFacade.SORT_BY_EDITOR);
	private static Comparator<Task> m_DueComp = new TaskComparator(
			TaskSessionFacade.SORT_BY_DUE);
	private static Comparator<Task> m_StatusComp = new TaskComparator(
			TaskSessionFacade.SORT_BY_STATUS);
	private static Comparator<Task> m_IdComp = new TaskComparator(
			TaskSessionFacade.SORT_BY_ID);

	public static void sortTasks(List<Task> lstTasks, int sortOrder, boolean reverse) {
		switch (sortOrder) {
		case TaskSessionFacade.SORT_BY_CUSTOMER:
			Collections.sort(lstTasks, m_CustomerComp);
			break;
		case TaskSessionFacade.SORT_BY_PROJECT:
			Collections.sort(lstTasks, m_ProjectComp);
			break;
		case TaskSessionFacade.SORT_BY_USERSTORY:
			Collections.sort(lstTasks, m_UserstoryComp);
			break;
		case TaskSessionFacade.SORT_BY_ITERATION:
			Collections.sort(lstTasks, m_IterationComp);
			break;
		case TaskSessionFacade.SORT_BY_CREATEDAT:
			Collections.sort(lstTasks, m_CreatedAtComp);
			break;
		case TaskSessionFacade.SORT_BY_MODIFIEDAT:
			Collections.sort(lstTasks, m_ModifiedAtComp);
			break;
		case TaskSessionFacade.SORT_BY_AUTHOR:
			Collections.sort(lstTasks, m_AuthorComp);
			break;
		case TaskSessionFacade.SORT_BY_SUBJECT:
			Collections.sort(lstTasks, m_SubjectComp);
			break;
		case TaskSessionFacade.SORT_BY_EDITOR:
			Collections.sort(lstTasks, m_EditorComp);
			break;
		case TaskSessionFacade.SORT_BY_DUE:
			Collections.sort(lstTasks, m_DueComp);
			break;
		case TaskSessionFacade.SORT_BY_STATUS:
			Collections.sort(lstTasks, m_StatusComp);
			break;
		case TaskSessionFacade.SORT_BY_ID:
			Collections.sort(lstTasks, m_IdComp);
			break;
		}
		if (reverse) {Collections.reverse(lstTasks);}
	}
}
