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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ibm.xsp.extlib.util.ExtLibUtil;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Session;
import lotus.domino.View;
import biz.webgate.scrum.bug.Bug;
import biz.webgate.scrum.bug.BugSessionFacade;
import biz.webgate.scrum.customer.Customer;
import biz.webgate.scrum.customer.CustomerSessionFacade;
import biz.webgate.scrum.iteration.Iteration;
import biz.webgate.scrum.iteration.IterationSessionFacade;
import biz.webgate.scrum.task.Task;
import biz.webgate.scrum.task.TaskSessionFacade;
import biz.webgate.scrum.userstory.Userstory;
import biz.webgate.scrum.userstory.UserstorySessionFacade;
import biz.webgate.xpages.dss.DominoStorageService;

public class ProjectStorageService {

	private static ProjectStorageService m_ProjectSS;

	private Date m_LastModified;
	
	private ProjectStorageService() {
		m_LastModified = new Date();
	}

	public static ProjectStorageService getInstance() {
		if (m_ProjectSS == null) {
			m_ProjectSS = new ProjectStorageService();
		}
		return m_ProjectSS;
	}

	public boolean saveProject(Project curProject, Session sesCurrent) {
		try {
			m_LastModified = new Date();
			List<String> lstReader = new ArrayList<String>();
			lstReader.add("[Admin]");
			lstReader.add("[Server]");
			lstReader.add("[Intern]");
			
			//+ all members of the customer definition
			Customer customer = CustomerSessionFacade.get().getCustomerById(curProject.getCustomer());
			lstReader.addAll(customer.getMembers());
			curProject.setReader(lstReader);
			curProject.setAuthors(lstReader);
			
			curProject.setTempSave(null);
			return DominoStorageService.getInstance().saveObject(curProject,
					sesCurrent.getCurrentDatabase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteProject(Project curProject, Session currentSession) {
		m_LastModified = new Date();
		boolean noException = true;
		for (Task task : TaskSessionFacade.get().getTasksOfProject(
				TaskSessionFacade.SORT_BY_CREATEDAT, true, curProject.getId(),
				"", "", "")) {
			noException = TaskSessionFacade.get().deleteTask(task);
			if (!noException)
				break;
		}
		if (noException) {
			for (Bug bug : BugSessionFacade.get().getBugsOfProject(
					BugSessionFacade.SORT_BY_CREATEDAT, true, curProject
							.getId(), "", "", "")) {
				noException = BugSessionFacade.get().deleteBug(bug);
				if (!noException)
					break;
			}
		}
		if (noException) {
			for (Userstory userstory : UserstorySessionFacade.get()
					.getUserstoriesOfProject(
							BugSessionFacade.SORT_BY_CREATEDAT, true,
							curProject.getId())) {
				noException = UserstorySessionFacade.get().deleteUserstory(userstory);
				if (!noException)
					break;
			}
		}
		if (noException) {
			for (Iteration iteration : IterationSessionFacade
					.get().getIterationsOfProject(BugSessionFacade.SORT_BY_CREATEDAT,
							true, curProject.getId())) {
				noException = IterationSessionFacade.get().deleteIteration(iteration);
				if (!noException)
					break;
			}
		}
		if (noException) {
			try {
				Document docProcess;
				docProcess = ExtLibUtil.getCurrentDatabase().getView(
						"lupProjectsByID").getDocumentByKey(curProject.getId(),
						true);
				if (docProcess != null) {
					docProcess.removePermanently(true);
				}
				return true;
			} catch (Exception e) {
				System.out.println("Deletion of Project "
						+ curProject.getSubject() + " failed.");
				e.printStackTrace();
			}
		} else {
			System.out.println("Deletion of Project " + curProject.getSubject()
					+ " failed.");
		}
		return false;
	}

	public Project getProjectById(String strProjectId, Session sesCurrent) {
		Project curProject = new Project();
		curProject.setId(strProjectId);
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			if (!DominoStorageService.getInstance().getObject(curProject,
					strProjectId, ndbCurrent))
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curProject;
	}

	public List<Project> getAllProjects(Session sesCurrent) {
		List<Project> lstProject = new ArrayList<Project>();
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			View viwProject = ndbCurrent.getView("lupProjectsById");
			Document docNext = viwProject.getFirstDocument();
			while (docNext != null) {
				Document docProcess = docNext;
				docNext = viwProject.getNextDocument(docNext);
				Project newProject = new Project();
				if (DominoStorageService.getInstance().getObjectWithDocument(
						newProject, docProcess)) {
					if (newProject.getIsDeleted() == null) {
						lstProject.add(newProject);
					} else if (!newProject.getIsDeleted().equals("true")) {
						lstProject.add(newProject);
					}
				}
				docProcess.recycle();
			}
			viwProject.recycle();
			ndbCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lstProject;
	}

	public List<Project> getMyProjects(Session sesCurrent) {
		List<Project> lstProject = new ArrayList<Project>();
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			View viwProject = ndbCurrent.getView("lupProjectsById");
			Document docNext = viwProject.getFirstDocument();
			while (docNext != null) {
				Document docProcess = docNext;
				docNext = viwProject.getNextDocument(docNext);
				Project newProject = new Project();
				if (DominoStorageService.getInstance().getObjectWithDocument(
						newProject, docProcess)) {
					if (newProject.getInvolved().contains(
							sesCurrent.getEffectiveUserName())
							|| newProject.getInvolved().contains(
									sesCurrent.createName(
											sesCurrent.getEffectiveUserName())
											.getAbbreviated())) {
						if (newProject.getIsDeleted() == null) {
							lstProject.add(newProject);
						} else if (!newProject.getIsDeleted().equals("true")) {
							lstProject.add(newProject);
						}
					}
				}
				docProcess.recycle();
			}
			viwProject.recycle();
			ndbCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lstProject;
	}
	
	public boolean isDirty(Date datCheck) {
		return m_LastModified.after(datCheck);
	}
}
