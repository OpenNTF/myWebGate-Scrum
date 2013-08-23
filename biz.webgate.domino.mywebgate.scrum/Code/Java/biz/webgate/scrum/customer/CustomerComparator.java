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

import java.util.Comparator;

public class CustomerComparator implements Comparator<Customer> {
	private int m_SortOrder = CustomerSessionFacade.SORT_BY_NAME;
	
	public CustomerComparator(int iSortOrder) {
		m_SortOrder = iSortOrder;
	}
	
	public int compare(Customer o1, Customer o2) {
		String s1 = "";
		String s2 = "";
		
		switch (m_SortOrder) {
		case CustomerSessionFacade.SORT_BY_NAME:
			s1 = o1.getName().toLowerCase();
			s2 = o2.getName().toLowerCase();
			break;
		}
		return s1.compareTo(s2);
	}
}
