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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import biz.webgate.scrum.customer.Customer;
import biz.webgate.scrum.customer.CustomerSessionFacade;
import biz.webgate.scrum.scrumdocument.IScrumDocument;
import biz.webgate.xpages.dss.annotations.DominoEntity;
import biz.webgate.xpages.dss.annotations.DominoStore;
import biz.webgate.xpages.dss.binding.util.FileHelper;

@DominoStore(Form = "frmProject", PrimaryKeyField = "Id", PrimaryFieldClass = String.class, View = "lupProjectsById")
public class Project implements Serializable, IScrumDocument {

	public static final String FORM = "Project";
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
	@DominoEntity(FieldName = "SubjectT")
	private String m_Subject;
	@DominoEntity(FieldName = "CustomerT")
	private String m_Customer;
	@DominoEntity(FieldName = "LeaderNM", showNameAs = "ABBREVIATE")
	private String m_Leader;
	@DominoEntity(FieldName = "InternalNM", showNameAs = "ABBREVIATE")
	private List<String> m_Internal;
	@DominoEntity(FieldName = "ExternalNM", showNameAs = "ABBREVIATE")
	private List<String> m_External;
	@DominoEntity(FieldName = "StartDT", dateOnly = true)
	private Date m_Start;
	@DominoEntity(FieldName = "EndDT", dateOnly = true)
	private Date m_End;
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

	public void setAuthor(String author) {
		m_Author = author;
	}

	public String getAuthor() {
		return m_Author;
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

	public String getSubject() {
		return m_Subject;
	}

	public void setSubject(String subject) {
		m_Subject = subject;
	}

	public void setCustomer(String customer) {
		m_Customer = customer;
	}

	public String getCustomer() {
		return m_Customer;
	}

	public void setLeader(String leader) {
		m_Leader = leader;
	}

	public String getLeader() {
		return m_Leader;
	}

	public void setInternal(List<String> internal) {
		m_Internal = internal;
	}

	public List<String> getInternal() {
		return m_Internal;
	}

	public void setExternal(List<String> external) {
		m_External = external;
	}

	public List<String> getExternal() {
		return m_External;
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

	public void setTags(List<String> tags) {
		m_Tags = tags;
	}

	public List<String> getTags() {
		return m_Tags;
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
		involved.add(getLeader());
		if (getExternal() != null) {
			involved.addAll(getExternal());
		}
		if (getInternal() != null) {
			involved.addAll(getInternal());
		}
		return involved;
	}

	public String getForm() {
		return FORM;
	}

	public String getProject() {
		return m_Subject;
	}

	public String getStatus() {
		return "";
	}

	public String getResponsible() {
		return m_Leader;
	}

	public Date getDueDate() {
		return m_End;
	}
	
	public String getCustomerName(String strId) {
		Customer customer = CustomerSessionFacade.get().getCustomerById(strId);
		if (customer != null) return customer.getName();
		return "";
	}

	public boolean getIsOverdue() {
		return false;
	}

	public String getReadableId() {
		return null;
	}

}
