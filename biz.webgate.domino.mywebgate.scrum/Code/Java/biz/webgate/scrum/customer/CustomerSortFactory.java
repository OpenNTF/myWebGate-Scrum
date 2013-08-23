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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomerSortFactory {
	private static Comparator<Customer> m_NameComp = new CustomerComparator(CustomerSessionFacade.SORT_BY_NAME);
	
	public static void sortCustomers(List<Customer> lstCustomers,int sortOrder, boolean reverse) {
		switch (sortOrder) {
		case CustomerSessionFacade.SORT_BY_NAME:
			Collections.sort(lstCustomers, m_NameComp);
			break;
		}
		if (reverse) {
			Collections.reverse(lstCustomers);
		}
	}
}
