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
package biz.webgate.scrum.customer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import biz.webgate.xpages.dss.annotations.DominoEntity;
import biz.webgate.xpages.dss.annotations.DominoStore;

@DominoStore(Form = "frmCustomer", PrimaryKeyField = "Id", PrimaryFieldClass = String.class, View = "lupCustomersById")
public class Customer implements Serializable {
	
	public static final String FORM = "Customer";
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
	@DominoEntity(FieldName = "NameT")
	private String m_Name;
	@DominoEntity(FieldName = "MembersNM", showNameAs = "ABBREVIATE")
	private List<String> m_Members;
	
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
	public String getName() {
		return m_Name;
	}
	public void setName(String name) {
		m_Name = name;
	}
	public List<String> getMembers() {
		return m_Members;
	}
	public void setMembers(List<String> members) {
		m_Members = members;
	}
	
}
