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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Session;
import lotus.domino.View;
import biz.webgate.scrum.bug.Bug;
import biz.webgate.scrum.bug.BugSessionFacade;
import biz.webgate.scrum.project.Project;
import biz.webgate.scrum.project.ProjectSessionFacade;
import biz.webgate.scrum.task.Task;
import biz.webgate.scrum.task.TaskSessionFacade;
import biz.webgate.xpages.dss.DominoStorageService;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class UserstoryStorageService {

	private static UserstoryStorageService m_UserstoriesS;

	private Date m_LastModified;

	private UserstoryStorageService() {
		m_LastModified = new Date();
	}

	public static UserstoryStorageService getInstance() {
		if (m_UserstoriesS == null) {
			m_UserstoriesS = new UserstoryStorageService();
		}
		return m_UserstoriesS;
	}

	public Userstory createNewUserstory(Session sesCurrent) {
		try {
			Userstory newUserstory = new Userstory();
			newUserstory.setId(UUID.randomUUID().toString());

			// temporary save to allow attachments
			newUserstory.setTempSave("1");
			DominoStorageService.getInstance().saveObject(newUserstory,
					sesCurrent.getCurrentDatabase());
			return newUserstory;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean saveUserstory(Userstory curUserstory, Session sesCurrent) {
		try {
			m_LastModified = new Date();
			List<String> lstReader = new ArrayList<String>();
			// get readers from project
			Project project = ProjectSessionFacade.get().getProjectById(curUserstory.getProjectId());
			lstReader.addAll(project.getReader());
			curUserstory.setReader(lstReader);
			curUserstory.setAuthors(lstReader);
			
			//clean-up
			int status = Integer.parseInt(curUserstory.getStatus());
			if (status < 2 || status == 9) {
				//no effective start/end date if "idea", "planned" or "postponed"
				curUserstory.setStart(null);
				curUserstory.setEnd(null);
			} 
			if (status != 9) {
				//no postponing reason if not postponed
				curUserstory.setPostponingReason("");
			}

			curUserstory.setTempSave(null);
			return DominoStorageService.getInstance().saveObject(curUserstory, sesCurrent.getCurrentDatabase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteUserstory(Userstory curUserstory,
			Session currentSession) {
		m_LastModified = new Date();
		boolean noException = true;
		for (Task task : TaskSessionFacade.get().getTasksOfProject(
				TaskSessionFacade.SORT_BY_CREATEDAT, true,
				curUserstory.getProjectId(), "", curUserstory.getId(), "",
				false)) {
			noException = TaskSessionFacade.get().deleteTask(task);
			if (!noException)
				break;
		}
		if (noException) {
			for (Bug bug : BugSessionFacade.get().getBugsOfProject(
					BugSessionFacade.SORT_BY_CREATEDAT, true,
					curUserstory.getProjectId(), "", curUserstory.getId(), "",
					false)) {
				noException = BugSessionFacade.get().deleteBug(bug);
				if (!noException)
					break;
			}
		}
		if (noException) {
			try {
				Document docProcess;
				docProcess = ExtLibUtil.getCurrentDatabase().getView(
						"lupUserstoriesByID").getDocumentByKey(
						curUserstory.getId(), true);
				if (docProcess != null) {
					docProcess.removePermanently(true);
				}
				return true;
			} catch (Exception e) {
				System.out.println("Deletion of Userstory "
						+ curUserstory.getSubject() + " failed.");
				e.printStackTrace();
			}
		} else {
			System.out.println("Deletion of Userstory "
					+ curUserstory.getSubject() + " failed.");
		}
		return false;
	}

	public Userstory getUserstoryById(String strUserstoryId, Session sesCurrent) {
		Userstory curUserstory = new Userstory();
		curUserstory.setId(strUserstoryId);
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			if (!DominoStorageService.getInstance().getObject(curUserstory,
					strUserstoryId, ndbCurrent))
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curUserstory;
	}

	public List<Userstory> getAllUserstories(Session sesCurrent) {
		List<Userstory> lstUserstory = new ArrayList<Userstory>();
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			View viwUserstory = ndbCurrent.getView("lupUserstoriesById");
			Document docNext = viwUserstory.getFirstDocument();
			while (docNext != null) {
				Document docProcess = docNext;
				docNext = viwUserstory.getNextDocument(docNext);
				Userstory newUserstory = new Userstory();
				if (DominoStorageService.getInstance().getObjectWithDocument(
						newUserstory, docProcess)) {
					if (!newUserstory.getIsDeleted().equals("true")) {
						lstUserstory.add(newUserstory);
					}
				}
				docProcess.recycle();
			}
			viwUserstory.recycle();
			ndbCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lstUserstory;
	}

	public List<Userstory> getUserstoriesOfProject(Session sesCurrent,
			String projectID) {
		List<Userstory> lstUserstory = new ArrayList<Userstory>();
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			View viwUserstory = ndbCurrent.getView("lupUserstoriesById");
			Document docNext = viwUserstory.getFirstDocument();
			while (docNext != null) {
				Document docProcess = docNext;
				docNext = viwUserstory.getNextDocument(docNext);
				Userstory newUserstory = new Userstory();
				if (DominoStorageService.getInstance().getObjectWithDocument(
						newUserstory, docProcess)) {
					if (newUserstory.getProjectId().equals(projectID)
							&& !newUserstory.getIsDeleted().equals("true")) {
						lstUserstory.add(newUserstory);
					}
				}
				docProcess.recycle();
			}
			viwUserstory.recycle();
			ndbCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lstUserstory;
	}

	public List<Userstory> getUserstoriesOfIteration(Session sesCurrent,
			String iterationId) {
		List<Userstory> userstories = getAllUserstories(sesCurrent);
		List<Userstory> iterationUserstories = new ArrayList<Userstory>();
		for (Userstory userstory : userstories) {
			if (userstory.getIterationId() != null) {
				if (userstory.getIterationId().equals(iterationId)) {
					iterationUserstories.add(userstory);
				}
			}
		}
		return iterationUserstories;
	}

	public boolean isDirty(Date datCheck) {
		return m_LastModified.after(datCheck);
	}
}
