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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class CustomerSessionFacade {

	public static final String BEAN_NAME = "customerBean"; //$NON-NLS-1$

	public static CustomerSessionFacade get(FacesContext context) {
		CustomerSessionFacade bean = (CustomerSessionFacade) context.getApplication().getVariableResolver().resolveVariable(context, BEAN_NAME);
		return bean;
	}

	public static CustomerSessionFacade get() {
		return get(FacesContext.getCurrentInstance());
	}
	
	public static final int SORT_BY_NAME = 1;
	
	private HashMap<String, Customer> m_CustomerList;
	private Date m_LastAccessed = new Date();
	
	public Customer createNewCustomer() {
		return CustomerStorageService.getInstance().createNewCustomer(ExtLibUtil.getCurrentSession());		
	}

	public boolean saveCustomer(Customer curCustomer) {
		m_CustomerList = null;
		m_LastAccessed = new Date();
		return CustomerStorageService.getInstance().saveCustomer(curCustomer, ExtLibUtil.getCurrentSession());
	}

	public boolean deleteCustomer(Customer curCustomer) {
		m_CustomerList = null;
		m_LastAccessed = new Date();
		return CustomerStorageService.getInstance().deleteCustomer(curCustomer, ExtLibUtil.getCurrentSession());
	}
	
	public Customer getCustomerById(String strCustomerId) {
		return loadCustomer(strCustomerId);
	}
	
	public List<Customer> getAllCustomers(int sortOrder, boolean reverse) {
		List<Customer> lstAll = CustomerStorageService.getInstance().getAllCustomers(ExtLibUtil.getCurrentSession());
		CustomerSortFactory.sortCustomers(lstAll, sortOrder, reverse);
		return lstAll;
	}
	
	public List<String> getCustomersList() throws IOException {
		List<String> selectItems = new ArrayList<String>();
		for (Customer customer : getAllCustomers(0, false)) {
			selectItems.add(customer.getName() + "|" + customer.getId());
		}
		Collections.sort(selectItems);
		return selectItems;
	}
	
	
	private Customer loadCustomer(String strID) {
		if (m_CustomerList == null || CustomerStorageService.getInstance().isDirty(m_LastAccessed)) {
			m_CustomerList = new HashMap<String, Customer>();
		}
		if (m_CustomerList.containsKey(strID)) {
			return m_CustomerList.get(strID);
		}
		Customer p = CustomerStorageService.getInstance().getCustomerById(strID, ExtLibUtil.getCurrentSession());
		m_LastAccessed = new Date();
		if (p != null) {
			m_CustomerList.put(strID, p);
		}
		return p;
	}
	
}
