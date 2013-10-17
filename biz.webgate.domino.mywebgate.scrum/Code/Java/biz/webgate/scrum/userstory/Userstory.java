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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import biz.webgate.scrum.customer.Customer;
import biz.webgate.scrum.customer.CustomerSessionFacade;
import biz.webgate.scrum.iteration.IterationSessionFacade;
import biz.webgate.scrum.project.ProjectSessionFacade;
import biz.webgate.scrum.scrumdocument.IScrumDocument;
import biz.webgate.scrum.task.Task;
import biz.webgate.scrum.task.TaskSessionFacade;
import biz.webgate.xpages.dss.annotations.DominoEntity;
import biz.webgate.xpages.dss.annotations.DominoStore;
import biz.webgate.xpages.dss.binding.util.FileHelper;

@DominoStore(Form = "frmUserstory", PrimaryKeyField = "Id", PrimaryFieldClass = String.class, View = "lupUserstoriesById")
public class Userstory implements Serializable, IScrumDocument {

	public static final String FORM = "Userstory";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DominoEntity(FieldName = "IdT")
	private String m_Id;
	@DominoEntity(FieldName = "CreatedAtDT", dateOnly = true)
	private Date m_CreatedAt;
	@DominoEntity(FieldName = "ModifiedAtDT", dateOnly = true)
	private Date m_ModifiedAt;
	@DominoEntity(FieldName = "AuthorNM", showNameAs = "ABBREVIATE")
	private String m_Author;
	@DominoEntity(FieldName = "ReaderR", isReader = true, showNameAs = "ABBREVIATE")
	private List<String> m_Reader;
	@DominoEntity(FieldName = "AuthorsNM", isAuthor = true, showNameAs = "ABBREVIATE")
	private List<String> m_Authors;
	@DominoEntity(FieldName = "ProjectIdT")
	private String m_ProjectId;
	@DominoEntity(FieldName = "SubjectT")
	private String m_Subject;
	@DominoEntity(FieldName = "IterationIdT")
	private String m_IterationId;
	@DominoEntity(FieldName = "StartDT", dateOnly = true)
	private Date m_Start;
	@DominoEntity(FieldName = "EndDT", dateOnly = true)
	private Date m_End;
	@DominoEntity(FieldName = "TimeN")
	private int m_Time;
	@DominoEntity(FieldName = "StatusDL")
	private String m_Status;
	@DominoEntity(FieldName = "PostponingReasonT")
	private String m_PostponingReason;
	@DominoEntity(FieldName = "TagsDL")
	private List<String> m_Tags;
	@DominoEntity(FieldName = "BodyT")
	private String m_Body;
	@DominoEntity(FieldName = "Files")
	private List<FileHelper> m_Files;
	@DominoEntity(FieldName = "DeletedT")
	private String m_IsDeleted;
	@DominoEntity(FieldName = "TempSave")
	private String m_TempSave;

	public String getId() {
		return m_Id;
	}

	public void setId(String id) {
		m_Id = id;
	}

	public Date getCreatedAt() {
		return m_CreatedAt;
	}

	public void setCreatedAt(Date createdAt) {
		m_CreatedAt = createdAt;
	}

	public Date getModifiedAt() {
		return m_ModifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		m_ModifiedAt = modifiedAt;
	}

	public String getAuthor() {
		return m_Author;
	}

	public void setAuthor(String author) {
		m_Author = author;
	}

	public List<String> getReader() {
		return m_Reader;
	}

	public void setReader(List<String> reader) {
		m_Reader = reader;
	}

	public List<String> getAuthors() {
		return m_Authors;
	}

	public void setAuthors(List<String> authors) {
		m_Authors = authors;
	}

	public void setProjectId(String projectId) {
		m_ProjectId = projectId;
	}

	public String getProjectId() {
		return m_ProjectId;
	}

	public String getSubject() {
		return m_Subject;
	}

	public void setSubject(String subject) {
		m_Subject = subject;
	}

	public String getIterationId() {
		return m_IterationId;
	}

	public void setIterationId(String iterationId) {
		m_IterationId = iterationId;
	}

	public String getIterationNo() {
		if (IterationSessionFacade.get().getIterationById(m_IterationId) != null) {
			biz.webgate.scrum.iteration.Iteration it = IterationSessionFacade
					.get().getIterationById(m_IterationId);
			return "" + it.getIndex();
		}
		return "";
	}

	public String getIteration() {
		if (IterationSessionFacade.get().getIterationById(m_IterationId) != null) {
			biz.webgate.scrum.iteration.Iteration it = IterationSessionFacade
					.get().getIterationById(m_IterationId);
			return it.getIndex() + " - " + it.getSubject();
		}
		return "";
	}

	public String getResponsible() {
		return ProjectSessionFacade.get().getProjectById(m_ProjectId).getResponsible();
	}

	public void setStart(Date start) {
		m_Start = start;
	}

	public Date getStart() {
		return m_Start;
	}

	public void setEnd(Date end) {
		m_End = end;
	}

	public Date getEnd() {
		return m_End;
	}

	public int getTime() {
		return m_Time;
	}

	public void setTime(int time) {
		m_Time = time;
	}

	public String getStatus() {
		return m_Status;
	}

	public void setStatus(String status) {
		m_Status = status;
	}

	public String getPostponingReason() {
		return m_PostponingReason;
	}

	public void setPostponingReason(String postponingReason) {
		m_PostponingReason = postponingReason;
	}

	public List<String> getTags() {
		return m_Tags;
	}

	public void setTags(List<String> tags) {
		m_Tags = tags;
	}

	public String getBody() {
		return m_Body;
	}

	public void setBody(String body) {
		m_Body = body;
	}

	public void setFiles(List<FileHelper> files) {
		m_Files = files;
	}

	public List<FileHelper> getFiles() {
		return m_Files;
	}

	public void setIsDeleted(String isDeleted) {
		m_IsDeleted = isDeleted;
	}

	public String getIsDeleted() {
		if (m_TempSave != null && m_TempSave.equals("1"))
			return "true";
		return m_IsDeleted;
	}

	public void setTempSave(String tempSave) {
		m_TempSave = tempSave;
	}

	public String getTempSave() {
		return m_TempSave;
	}

	public String getCustomer() {
		return ProjectSessionFacade.get().getCustomerNameByProjectID(
				m_ProjectId);
	}

	public String getForm() {
		return FORM;
	}

	public String getProject() {
		return ProjectSessionFacade.get()
				.getProjectNameByProjectID(m_ProjectId);
	}

	public Date getDueDate() {
		return getEnd();
	}

	public String getCustomerName(String strId) {
		Customer customer = CustomerSessionFacade.get().getCustomerById(strId);
		if (customer != null)
			return customer.getName();
		return "";
	}

	public boolean getIsOverdue() {
		return false;
	}

	public String getReadableId() {
		return null;
	}

	public boolean isExecutable() {
		return TaskSessionFacade.get().getTasksOfUserstory(TaskSessionFacade.SORT_BY_ID, false, m_Id, "", true).size() > 0;
	}

	public int getExpectedEffort() {
		int expectedEffort = 0;
		for (Task task : TaskSessionFacade.get().getTasksOfUserstory(TaskSessionFacade.SORT_BY_ID, false, m_Id, "", false)) {
			expectedEffort += task.getTime();
		}
		return expectedEffort;
	}

	public int getCompletedEffort() {
		int completedEffort = 0;
		for (Task task : TaskSessionFacade.get().getTasksOfUserstory(TaskSessionFacade.SORT_BY_ID, false, m_Id, "4", false)) {
			completedEffort += task.getTime();
		}
		return completedEffort;
	}

	public int getRemainingEffort() {
		return getExpectedEffort() - getCompletedEffort();
	}

	public int getRemainingEffortPercent() {
		return (getCompletedEffort() == 0) ? 100 : 100 * getCompletedEffort() / getExpectedEffort();
	}
}
