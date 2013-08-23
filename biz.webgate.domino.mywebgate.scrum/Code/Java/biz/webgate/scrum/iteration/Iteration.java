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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import biz.webgate.scrum.customer.Customer;
import biz.webgate.scrum.customer.CustomerSessionFacade;
import biz.webgate.scrum.scrumdocument.IScrumDocument;
import biz.webgate.xpages.dss.annotations.DominoEntity;
import biz.webgate.xpages.dss.annotations.DominoStore;
import biz.webgate.xpages.dss.binding.util.FileHelper;

@DominoStore(Form = "frmIteration", PrimaryKeyField = "Id", PrimaryFieldClass = String.class, View = "lupIterationsById")
public class Iteration implements Serializable, IScrumDocument {

	public static final String FORM = "Iteration";
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
	@DominoEntity(FieldName = "IndexN")
	private int m_Index;
	@DominoEntity(FieldName = "SubjectT")
	private String m_Subject;
	@DominoEntity(FieldName = "DueDT", dateOnly = true)
	private Date m_Due;
	@DominoEntity(FieldName = "StatusDL")
	private String m_Status;
	@DominoEntity(FieldName = "BodyT")
	private String m_Body;
	@DominoEntity(FieldName = "Files")
	private List<FileHelper> m_Files;
	@DominoEntity(FieldName = "DeletedT")
	private String m_IsDeleted;
	@DominoEntity(FieldName="TempSave")
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

	public void setIndex(int index) {
		m_Index = index;
	}

	public int getIndex() {
		return m_Index;
	}

	public String getSubject() {
		return m_Subject;
	}

	public void setSubject(String subject) {
		m_Subject = subject;
	}

	public void setDue(Date due) {
		m_Due = due;
	}

	public Date getDue() {
		return m_Due;
	}

	public void setStatus(String status) {
		m_Status = status;
	}

	public String getStatus() {
		return m_Status;
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

	public String getCustomer() {
		return null;
	}

	public String getCustomerName(String strId) {
		Customer customer = CustomerSessionFacade.get().getCustomerById(strId);
		if (customer != null) return customer.getName();
		return "";
	}

	public Date getDueDate() {
		return null;
	}

	public String getForm() {
		return FORM;
	}

	public String getProject() {
		return null;
	}

	public String getResponsible() {
		return null;
	}

	public List<String> getTags() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getIsOverdue() {
		return false;
	}
}
