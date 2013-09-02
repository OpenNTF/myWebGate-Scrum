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

public class BugStorageService {

	private static BugStorageService m_ProjectSS;

	private Date m_LastModified;
	
	private BugStorageService() {
		m_LastModified = new Date();
	}

	public static BugStorageService getInstance() {
		if (m_ProjectSS == null) {
			m_ProjectSS = new BugStorageService();
		}
		return m_ProjectSS;
	}
	
	public Bug createNewBug(String projID, Session sesCurrent) {
		try {
			m_LastModified = new Date();
			Bug newBug = new Bug();
			newBug.setId(UUID.randomUUID().toString());
			
			newBug.setAuthor(sesCurrent.createName(sesCurrent.getEffectiveUserName()).getAbbreviated());
			List<String> lstAuthors = new ArrayList<String>();
			lstAuthors.add(sesCurrent.createName(sesCurrent.getEffectiveUserName()).getAbbreviated());
			newBug.setAuthors(lstAuthors);
			
			newBug.setCreatedAt(new Date());
			newBug.setStatus("1");
			newBug.setProjectId(projID);
			
			//temporary save to allow attachments
			newBug.setTempSave("1");
			DominoStorageService.getInstance().saveObject(newBug, sesCurrent.getCurrentDatabase());
			return newBug;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean saveBug(Bug curBug, Session sesCurrent) {
		try {
			m_LastModified = new Date();
			List<String> lstReader = new ArrayList<String>();
			//get readers from project
			Project project = ProjectSessionFacade.get().getProjectById(curBug.getProjectId());
			lstReader.addAll(project.getReader());			
			curBug.setReader(lstReader);
			curBug.setAuthors(lstReader);
			
			if (curBug.getBugId() == null || curBug.getBugId().equals("")) {
				Document docBug = sesCurrent.getCurrentDatabase().getView("lupBugsByProject").getDocumentByKey(curBug.getProjectId(), true);
				if (docBug != null) {
					if (!docBug.hasItem("BugIdT") || docBug.getItemValueString("BugIdT").equals("")) {
						curBug.setBugId("B001");	
					} else {
						String temp = "00" + (Integer.parseInt(docBug.getItemValueString("BugIdT").substring(1))+1);
						curBug.setBugId("B" + temp.substring(temp.length() - 3));
					}
				}
			}
			
			curBug.setTempSave(null);
			return DominoStorageService.getInstance().saveObject(curBug, sesCurrent.getCurrentDatabase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteBug(Bug curBug, Session sesCurrent) {
		try {
			Document docProcess;
			docProcess = ExtLibUtil.getCurrentDatabase().getView("lupBugsByID").getDocumentByKey(curBug.getId(), true);
			if (docProcess != null) {
				docProcess.removePermanently(true);
			}
			return true;
		} catch (Exception e) {
			System.out.println("Deletion of Bug " + curBug.getSubject() + " failed.");
			e.printStackTrace();
		}
		return false;
	}

	public Bug getBugById(String strBugId, Session sesCurrent) {
		Bug curBug = new Bug();
		curBug.setId(strBugId);
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			if (!DominoStorageService.getInstance().getObject(curBug, strBugId, ndbCurrent))
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curBug;
	}

	public List<Bug> getAllBugs(Session sesCurrent, String statusFilter) {
		List<Bug> lstBug = new ArrayList<Bug>();
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			View viwBug = ndbCurrent.getView("lupBugsById");
			Document docNext = viwBug.getFirstDocument();
			while (docNext != null) {
				Document docProcess = docNext;
				docNext = viwBug.getNextDocument(docNext);
				Bug newBug = new Bug();
				if (DominoStorageService.getInstance().getObjectWithDocument(newBug, docProcess)) {
					if (!newBug.getIsDeleted().equals("true") && (statusFilter == null || statusFilter.equals("") || statusFilter.equals(newBug.getStatus()))) {
						lstBug.add(newBug);
					}
				}
				docProcess.recycle();
			}
			viwBug.recycle();
			ndbCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lstBug;
	}

	public List<Bug> getMyBugs(Session sesCurrent, int involvedType, String statusFilter, boolean showCompleted) {
		List<Bug> lstBug = new ArrayList<Bug>();
		try {
			String myName = sesCurrent.createName(sesCurrent.getEffectiveUserName()).getAbbreviated();
			if (statusFilter == null)
				statusFilter = "";
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			View viwBug = ndbCurrent.getView("lupBugsById");
			Document docNext = viwBug.getFirstDocument();
			while (docNext != null) {
				Document docProcess = docNext;
				docNext = viwBug.getNextDocument(docNext);
				Bug newBug = new Bug();
				if (DominoStorageService.getInstance().getObjectWithDocument(newBug, docProcess)) {
					if (!newBug.getIsDeleted().equals("true") && ((statusFilter.equals("4") || statusFilter.equals("9")) || showCompleted == true || (showCompleted == false && (!newBug.getStatus().equals("4") && !newBug.getStatus().equals("9")) ))) {
						if (statusFilter.equals("") || statusFilter.equals(newBug.getStatus())) {
							switch (involvedType) {
							case 0:
								if (newBug.getEditor().equals(myName)) {
									lstBug.add(newBug);
								}
								break;
							case 1:
								if (newBug.getAuthor().equals(myName)) {
									lstBug.add(newBug);
								}
								break;
							case 2:
								if (newBug.getInvolved().contains(myName)) {
									lstBug.add(newBug);
								}
								break;
							}
						}
					}
				}
				docProcess.recycle();
			}
			viwBug.recycle();
			ndbCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lstBug;
	}

	public List<Bug> getBugsOfProject(Session sesCurrent, String projectID, String statusFilter, String usFilter, String itFilter, boolean hideComplete) {
		List<Bug> lstBug = new ArrayList<Bug>();
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			View viwBug = ndbCurrent.getView("lupBugsById");
			Document docNext = viwBug.getFirstDocument();
			while (docNext != null) {
				Document docProcess = docNext;
				docNext = viwBug.getNextDocument(docNext);
				Bug newBug = new Bug();
				if (DominoStorageService.getInstance().getObjectWithDocument(newBug, docProcess)) {
					if (newBug.getProjectId().equals(projectID)
							&& !newBug.getIsDeleted().equals("true")
							&& (statusFilter == null || statusFilter.equals("") || statusFilter.equals(newBug.getStatus()))
							&& (usFilter == null || usFilter.equals("") || usFilter.equals(newBug.getUserstoryId()))
							&& (itFilter == null || itFilter == "" || itFilter.equals(newBug.getIterationId()))) {
						if (hideComplete == false || (hideComplete == true && !newBug.getStatus().equals("4") && !newBug.getStatus().equals("9"))) {
							lstBug.add(newBug);
						}
					}
				}
				docProcess.recycle();
			}
			viwBug.recycle();
			ndbCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lstBug;
	}

	public List<Bug> getBugsOfUserstory(Session sesCurrent, String userstoryID,
			String statusFilter) {
		List<Bug> lstBugs = new ArrayList<Bug>();
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			View viwBug = ndbCurrent.getView("lupBugsById");
			Document docNext = viwBug.getFirstDocument();
			while (docNext != null) {
				Document docProcess = docNext;
				docNext = viwBug.getNextDocument(docNext);
				Bug newBug = new Bug();
				if (DominoStorageService.getInstance().getObjectWithDocument(
						newBug, docProcess)) {
					if (newBug.getUserstoryId().equals(userstoryID)
							&& !newBug.getIsDeleted().equals("true")
							&& (statusFilter == null || statusFilter.equals("") || statusFilter
									.equals(newBug.getStatus()))) {
						lstBugs.add(newBug);
					}
				}
				docProcess.recycle();
			}
			viwBug.recycle();
			ndbCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lstBugs;
	}

	public boolean isDirty(Date datCheck) {
		return m_LastModified.after(datCheck);
	}
}
