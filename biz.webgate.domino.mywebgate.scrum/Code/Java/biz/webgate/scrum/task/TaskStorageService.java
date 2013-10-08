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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ibm.xsp.extlib.util.ExtLibUtil;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Session;
import lotus.domino.View;
import biz.webgate.scrum.project.Project;
import biz.webgate.scrum.project.ProjectSessionFacade;
import biz.webgate.xpages.dss.DominoStorageService;

public class TaskStorageService {

	private static TaskStorageService m_UserstoriesS;

	private Date m_LastModified;

	private TaskStorageService() {
		m_LastModified = new Date();
	}

	public static TaskStorageService getInstance() {
		if (m_UserstoriesS == null) {
			m_UserstoriesS = new TaskStorageService();
		}
		return m_UserstoriesS;
	}
	
	public Task createNewTask(String projID, Session sesCurrent) {
		try {
			m_LastModified = new Date();
			Task newTask = new Task();
			newTask.setId(UUID.randomUUID().toString());
			
			newTask.setCreatedAt(new Date());
			newTask.setAuthor(sesCurrent.createName(sesCurrent.getEffectiveUserName()).getAbbreviated());
			
			newTask.setStatus("1");
			newTask.setProjectId(projID);
			
			//temporary save to allow attachments
			newTask.setTempSave("1");
			DominoStorageService.getInstance().saveObject(newTask, sesCurrent.getCurrentDatabase());
			
			return newTask;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean saveTask(Task curTask, Session sesCurrent) {
		try {
			m_LastModified = new Date();
			List<String> lstReader = new ArrayList<String>();
			//get readers from project
			Project project = ProjectSessionFacade.get().getProjectById(curTask.getProjectId());
			lstReader.addAll(project.getReader());			
			curTask.setReader(lstReader);
			curTask.setAuthors(lstReader);
			
			//assign task id if needed
			if (curTask.getTaskId() == null || curTask.getTaskId().equals("")) {
				Document docTask = sesCurrent.getCurrentDatabase().getView("lupTasksByProject").getDocumentByKey(curTask.getProjectId(), true);
				if (docTask != null) {
					if (!docTask.hasItem("TaskIdT") || docTask.getItemValueString("TaskIdT").equals("")) {
						curTask.setTaskId("T001");	
					} else {
						String temp = "00" + (Integer.parseInt(docTask.getItemValueString("TaskIdT").substring(1))+1);
						curTask.setTaskId("T" + temp.substring(temp.length() - 3));
					}
				}
			}
			
			//check if task is executable (must have assigned person and expected effort)
			if (!curTask.getEditor().equals("") && curTask.getTime() > 0) {
				curTask.setIsExecutable("1");
			} else {
				curTask.setIsExecutable("0");
			}
			
			curTask.setTempSave(null);
			return DominoStorageService.getInstance().saveObject(curTask, sesCurrent.getCurrentDatabase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteTask(Task curTask, Session currentSession) {
		try {
			m_LastModified = new Date();
			Document docProcess;
			docProcess = ExtLibUtil.getCurrentDatabase().getView("lupTasksByID").getDocumentByKey(curTask.getId(), true);
			if (docProcess != null) {
				docProcess.removePermanently(true);
			}
			return true;
		} catch (Exception e) {
			System.out.println("Deletion of Task " + curTask.getSubject() + " failed.");
			e.printStackTrace();
		}
		return false;
	}

	public Task getTaskById(String strTaskId, Session sesCurrent) {
		Task curUserstory = new Task();
		curUserstory.setId(strTaskId);
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			if (!DominoStorageService.getInstance().getObject(curUserstory, strTaskId, ndbCurrent))
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curUserstory;
	}

	public List<Task> getAllTasks(Session sesCurrent, String statusFilter) {
		List<Task> lstTask = new ArrayList<Task>();
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			View viwTask = ndbCurrent.getView("lupTasksById");
			Document docNext = viwTask.getFirstDocument();
			while (docNext != null) {
				Document docProcess = docNext;
				docNext = viwTask.getNextDocument(docNext);
				Task newTask = new Task();
				if (DominoStorageService.getInstance().getObjectWithDocument(newTask, docProcess)) {
					if (!newTask.getIsDeleted().equals("true") && (statusFilter == null || statusFilter.equals("") || statusFilter.equals(newTask.getStatus()))) {
						lstTask.add(newTask);
					}
				}
				docProcess.recycle();
			}
			viwTask.recycle();
			ndbCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lstTask;
	}

	public List<Task> getMyTasks(Session sesCurrent, int involvedType, String statusFilter, boolean showCompleted) {
		List<Task> lstTask = new ArrayList<Task>();
		try {

			String myName = sesCurrent.createName(sesCurrent.getEffectiveUserName()).getAbbreviated();
			if (statusFilter == null)
				statusFilter = "";
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			View viwTask = ndbCurrent.getView("lupTasksById");
			Document docNext = viwTask.getFirstDocument();
			while (docNext != null) {
				Document docProcess = docNext;
				docNext = viwTask.getNextDocument(docNext);
				Task newTask = new Task();
				if (DominoStorageService.getInstance().getObjectWithDocument(newTask, docProcess)) {
					// check for involvedType (0 = Assigned to me, 1 = Created by me, 2 = both)
					if (!newTask.getIsDeleted().equals("true") && (statusFilter.equals("4") || showCompleted == true || (showCompleted == false && !newTask.getStatus().equals("4") ))) {
						if (statusFilter.equals("") || statusFilter.equals(newTask.getStatus())) {
							switch (involvedType) {
							case 0:
								if (newTask.getEditor().equals(myName)) {
									lstTask.add(newTask);
								}
								break;
							case 1:
								if (newTask.getAuthor().equals(myName)) {
									lstTask.add(newTask);
								}
								break;
							case 2:
								if (newTask.getInvolved().contains(myName)) {
									lstTask.add(newTask);
								}
								break;
							}
						}
					}				
				}
				docProcess.recycle();
			}
			viwTask.recycle();
			ndbCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lstTask;
	}

	public List<Task> getTasksOfProject(Session sesCurrent, String projectID, String statusFilter, String usFilter, String itFilter, boolean hideComplete) {
		List<Task> lstTask = new ArrayList<Task>();
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			View viwTask = ndbCurrent.getView("lupTasksById");
			Document docNext = viwTask.getFirstDocument();
			while (docNext != null) {
				Document docProcess = docNext;
				docNext = viwTask.getNextDocument(docNext);
				Task newTask = new Task();
				if (DominoStorageService.getInstance().getObjectWithDocument(newTask, docProcess)) {
					if (newTask.getProjectId().equals(projectID)
							&& !newTask.getIsDeleted().equals("true")
							&& (statusFilter == null || statusFilter.equals("") || statusFilter.equals(newTask.getStatus()))
							&& (usFilter == null || usFilter.equals("") || usFilter.equals(newTask.getUserstoryId()))
							&& (itFilter == null || itFilter.equals("") || itFilter.equals(newTask.getIterationId()))) {
						if (hideComplete == false || (hideComplete == true && !newTask.getStatus().equals("4"))) {
							lstTask.add(newTask);
						}
					}
				}
				docProcess.recycle();
			}
			viwTask.recycle();
			ndbCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lstTask;
	}

	public List<Task> getTasksOfUserstory(Session sesCurrent, String userstoryID, String statusFilter) {
		List<Task> lstTasks = new ArrayList<Task>();
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			View viwTask = ndbCurrent.getView("lupTasksById");
			Document docNext = viwTask.getFirstDocument();
			while (docNext != null) {
				Document docProcess = docNext;
				docNext = viwTask.getNextDocument(docNext);
				Task newTask = new Task();
				if (DominoStorageService.getInstance().getObjectWithDocument(newTask, docProcess)) {
					if (newTask.getUserstoryId().equals(userstoryID)
							&& !newTask.getIsDeleted().equals("true")
							&& (statusFilter == null || statusFilter.equals("") || statusFilter
									.equals(newTask.getStatus()))) {
						lstTasks.add(newTask);
					}
				}
				docProcess.recycle();
			}
			viwTask.recycle();
			ndbCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lstTasks;
	}

	public boolean isDirty(Date datCheck) {
		return m_LastModified.after(datCheck);
	}
}
