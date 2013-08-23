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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Session;
import lotus.domino.View;
import biz.webgate.xpages.dss.DominoStorageService;

public class CustomerStorageService {
	
	private static CustomerStorageService m_CustomerSS;

	private Date m_LastModified;

	private CustomerStorageService() {
		m_LastModified = new Date();
	}

	public static CustomerStorageService getInstance() {
		if (m_CustomerSS == null) {
			m_CustomerSS = new CustomerStorageService();
		}
		return m_CustomerSS;
	}
	
	public Customer createNewCustomer(Session sesCurrent) {
		try {
			m_LastModified = new Date();
			
			Customer newCustomer = new Customer();
			newCustomer.setId(UUID.randomUUID().toString());
			newCustomer.setAuthor(sesCurrent.createName(sesCurrent.getEffectiveUserName()).getAbbreviated());
			newCustomer.setCreatedAt(new Date());
			return newCustomer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean saveCustomer(Customer curCustomer, Session sesCurrent) {
		try {
			m_LastModified = new Date();
			List<String> lstReader = new ArrayList<String>();
			lstReader.add("[Admin]");
			lstReader.add("[Server]");
			lstReader.add("[Intern]");
			curCustomer.setAuthors(lstReader);
			
			lstReader.addAll(curCustomer.getMembers());
			curCustomer.setReader(lstReader);
			
			return DominoStorageService.getInstance().saveObject(curCustomer, sesCurrent.getCurrentDatabase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteCustomer(Customer curCustomer, Session sesCurrent) {
		try {
			Document docProcess;
			docProcess = sesCurrent.getCurrentDatabase().getView("lupCustomersByID").getDocumentByKey(curCustomer.getId(), true);
			if (docProcess != null) {
				docProcess.removePermanently(true);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Customer getCustomerById(String strCustomerId, Session sesCurrent) {
		Customer curCustomer = new Customer();
		curCustomer.setId(strCustomerId);
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			if (!DominoStorageService.getInstance().getObject(curCustomer, strCustomerId, ndbCurrent))
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curCustomer;
	}
	
	public List<Customer> getAllCustomers(Session sesCurrent) {
		List<Customer> lstCustomer = new ArrayList<Customer>();
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			View viwCustomer = ndbCurrent.getView("lupCustomersById");
			Document docNext = viwCustomer.getFirstDocument();
			while (docNext != null) {
				Document docProcess = docNext;
				docNext = viwCustomer.getNextDocument(docNext);
				Customer newCustomer = new Customer();
				if (DominoStorageService.getInstance().getObjectWithDocument(newCustomer, docProcess)) {
					lstCustomer.add(newCustomer);
				}
				docProcess.recycle();
			}
			viwCustomer.recycle();
			ndbCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lstCustomer;
	}
	
	
	public boolean isDirty(Date datCheck) {
		return m_LastModified.after(datCheck);
	}

}
