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
import biz.webgate.scrum.userstory.Userstory;
import biz.webgate.scrum.userstory.UserstorySessionFacade;
import biz.webgate.xpages.dss.DominoStorageService;

public class IterationStorageService {

	private static IterationStorageService m_IterationSS;

	private Date m_LastModified;

	private IterationStorageService() {
		m_LastModified = new Date();
	}

	public static IterationStorageService getInstance() {
		if (m_IterationSS == null) {
			m_IterationSS = new IterationStorageService();
		}
		return m_IterationSS;
	}
	
	public Iteration createNewIteration(Session sesCurrent) {
		try {
			Iteration newIteration = new Iteration();
			newIteration.setId(UUID.randomUUID().toString());
			
			//temporary save to allow attachments
			newIteration.setTempSave("1");
			DominoStorageService.getInstance().saveObject(newIteration, sesCurrent.getCurrentDatabase());
			return newIteration;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean saveIteration(Iteration curIteration, Session sesCurrent) {
		try {
			m_LastModified = new Date();
			List<String> lstReader = new ArrayList<String>();
			//get readers from project
			Project project = ProjectSessionFacade.get().getProjectById(curIteration.getProjectId());
			lstReader.addAll(project.getReader());			
			curIteration.setReader(lstReader);
			curIteration.setAuthors(lstReader);
			
			curIteration.setTempSave(null);
			return DominoStorageService.getInstance().saveObject(curIteration, sesCurrent.getCurrentDatabase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteIteration(Iteration curIteration,
			Session currentSession) {
		m_LastModified = new Date();
		boolean noException = true;
		for (Userstory userstory : UserstorySessionFacade.get()
				.getUserstoriesOfIteration(curIteration.getId(), false)) {
			userstory.setIterationId("");
			noException = UserstorySessionFacade.get().saveUserstory(userstory);
			if (!noException) {
				break;
			}
		}
		if (noException) {
			try {
				Document docProcess;
				docProcess = ExtLibUtil.getCurrentDatabase().getView(
						"lupIterationsByID").getDocumentByKey(
						curIteration.getId(), true);
				if (docProcess != null) {
					docProcess.removePermanently(true);
				}
				return true;
			} catch (Exception e) {
				System.out.println("Deletion of Iteration "
						+ curIteration.getSubject() + " failed.");
				e.printStackTrace();
			}
		} else {
			System.out.println("Deletion of Iteration "
					+ curIteration.getSubject() + " failed.");
		}
		return false;
	}

	public Iteration getIterationById(String strIterationId, Session sesCurrent) {
		Iteration curIteration = new Iteration();
		curIteration.setId(strIterationId);
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			if (!DominoStorageService.getInstance().getObject(curIteration,
					strIterationId, ndbCurrent))
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curIteration;
	}

	public List<Iteration> getAllIterations(Session sesCurrent) {
		List<Iteration> lstIteration = new ArrayList<Iteration>();
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			View viwIteration = ndbCurrent.getView("lupIterationsById");
			Document docNext = viwIteration.getFirstDocument();
			while (docNext != null) {
				Document docProcess = docNext;
				docNext = viwIteration.getNextDocument(docNext);
				Iteration newIteration = new Iteration();
				if (DominoStorageService.getInstance().getObjectWithDocument(
						newIteration, docProcess)) {
					lstIteration.add(newIteration);
				}
				docProcess.recycle();
			}
			viwIteration.recycle();
			ndbCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lstIteration;
	}

	public boolean isDirty(Date datCheck) {
		return m_LastModified.after(datCheck);
	}
}
