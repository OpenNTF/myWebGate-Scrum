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
package biz.webgate.scrum.scrumdocument;

import java.util.Date;
import java.util.List;

public interface IScrumDocument {

	public abstract String getId();

	public abstract String getForm();

	public abstract Date getCreatedAt();
	
	public abstract Date getModifiedAt();

	public abstract String getAuthor();

	public abstract List<String> getReader();

	public abstract String getCustomer();
	
	public abstract String getCustomerName(String strID);
	
	public abstract String getProject();

	public abstract String getSubject();

	public abstract List<String> getTags();

	public abstract String getBody();
	
	public abstract String getStatus();
	
	public abstract String getResponsible();
	
	public abstract Date getDueDate();
	
	public abstract boolean getIsOverdue();

}