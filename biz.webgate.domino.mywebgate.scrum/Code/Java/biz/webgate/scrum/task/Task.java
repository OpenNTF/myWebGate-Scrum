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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import biz.webgate.scrum.customer.Customer;
import biz.webgate.scrum.customer.CustomerSessionFacade;
import biz.webgate.scrum.project.ProjectSessionFacade;
import biz.webgate.scrum.scrumdocument.IScrumDocument;
import biz.webgate.scrum.userstory.UserstorySessionFacade;
import biz.webgate.xpages.dss.annotations.DominoEntity;
import biz.webgate.xpages.dss.annotations.DominoStore;
import biz.webgate.xpages.dss.binding.util.FileHelper;

@DominoStore(Form = "frmTask", PrimaryKeyField = "Id", PrimaryFieldClass = String.class, View = "lupTasksById")
public class Task implements Serializable, IScrumDocument {

	public static final String FORM = "Task";
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
	@DominoEntity(FieldName = "UserstoryIdT")
	private String m_UserstoryId;
	@DominoEntity(FieldName = "SubjectT")
	private String m_Subject;
	@DominoEntity(FieldName = "TaskIdT")
	private String m_TaskId;
	@DominoEntity(FieldName = "DueDT", dateOnly = true)
	private Date m_Due;
	@DominoEntity(FieldName = "ExpectedEffortN")
	private int m_ExpectedEffort;
	@DominoEntity(FieldName = "EffectiveEffortN")
	private int m_EffectiveEffort;
	@DominoEntity(FieldName = "EditorNM", showNameAs = "ABBREVIATE")
	private String m_Editor;
	@DominoEntity(FieldName = "TesterInternalNM", showNameAs = "ABBREVIATE")
	private String m_TesterInternal;
	@DominoEntity(FieldName = "StatusDL")
	private String m_Status;
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
	private boolean m_IsExecutable;

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

	public void setUserstoryId(String userstoryId) {
		m_UserstoryId = userstoryId;
	}

	public String getIterationId() {
		if (getUserstoryId() == null || getUserstoryId() == "") {
			return "";
		}
		try {
			return UserstorySessionFacade.get().getUserstoryById(
					getUserstoryId()).getIterationId();
		} catch (Exception e) {
			return "";
		}
	}

	public String getUserstoryId() {
		return m_UserstoryId;
	}

	public String getSubject() {
		return m_Subject;
	}

	public void setSubject(String subject) {
		m_Subject = subject;
	}

	public String getTaskId() {
		return m_TaskId;
	}

	public void setTaskId(String taskId) {
		m_TaskId = taskId;
	}

	public void setDue(Date due) {
		m_Due = due;
	}

	public Date getDue() {
		return m_Due;
	}

	public boolean getIsOverdue() {
		if (!m_Status.equals("4") && m_Due != null) {
			return m_Due.compareTo(new Date()) == -1;
		}
		return false;
	}

	public int getExpectedEffort() {
		return m_ExpectedEffort;
	}

	public void setExpectedEffort(int expectedEffort) {
		m_ExpectedEffort = expectedEffort;
	}

	public int getEffectiveEffort() {
		return m_EffectiveEffort;
	}

	public void setEffectiveEffort(int effectiveEffort) {
		m_EffectiveEffort = effectiveEffort;
	}

	public String getEditor() {
		return m_Editor;
	}

	public void setEditor(String editor) {
		m_Editor = editor;
	}

	public String getTesterInternal() {
		return m_TesterInternal;
	}

	public void setTesterInternal(String testerInternal) {
		m_TesterInternal = testerInternal;
	}

	public String getStatus() {
		return m_Status;
	}

	public void setStatus(String status) {
		m_Status = status;
	}

	public List<String> getTags() {
		return m_Tags;
	}

	public void setTags(List<String> tags) {
		m_Tags = tags;
	}

	public void setBody(String body) {
		m_Body = body;
	}

	public String getBody() {
		return m_Body;
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

	public List<String> getInvolved() {
		List<String> involved = new ArrayList<String>();
		involved.add(getAuthor());
		involved.add(getEditor());
		involved.add(getTesterInternal());
		return involved;
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

	public String getResponsible() {
		return m_Editor;
	}

	public Date getDueDate() {
		return m_Due;
	}

	public String getCustomerName(String strId) {
		Customer customer = CustomerSessionFacade.get().getCustomerById(strId);
		if (customer != null)
			return customer.getName();
		return "";
	}

	public String getReadableId() {
		return m_TaskId;
	}

	public boolean getIsExecutable() {
		return m_IsExecutable;
	}

	public void setIsExecutable(boolean isExecutable) {
		m_IsExecutable = isExecutable;
	}

	public boolean isExecutable() {
		// task must have assignee and expected effort to be executable
		return (!m_Editor.equals("") && m_ExpectedEffort > 0) ? true : false;
	}
}
