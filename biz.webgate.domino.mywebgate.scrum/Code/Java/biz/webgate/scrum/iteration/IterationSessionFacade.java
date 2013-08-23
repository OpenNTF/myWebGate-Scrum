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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class IterationSessionFacade {
	public static final String BEAN_NAME = "iterationBean"; //$NON-NLS-1$

	public static IterationSessionFacade get(FacesContext context) {
		IterationSessionFacade bean = (IterationSessionFacade) context
				.getApplication().getVariableResolver().resolveVariable(
						context, BEAN_NAME);
		return bean;
	}

	public static IterationSessionFacade get() {
		return get(FacesContext.getCurrentInstance());
	}

	public static final int SORT_BY_CREATEDAT = 1;
	public static final int SORT_BY_MODIFIEDAT = 2;
	public static final int SORT_BY_INDEX = 3;
	public static final int SORT_BY_SUBJECT = 4;
	public static final int SORT_BY_DUE = 5;
	public static final int SORT_BY_STATUS = 6;

	private HashMap<String, Iteration> m_IterationList;
	private Date m_LastAccessed = new Date();

	public Iteration createNewIteration() {
		return IterationStorageService.getInstance().createNewIteration(ExtLibUtil.getCurrentSession());		
	}

	public boolean saveIteration(Iteration curIteration) {
		m_IterationList = null;
		m_LastAccessed = new Date();
		return IterationStorageService.getInstance().saveIteration(curIteration, ExtLibUtil.getCurrentSession());
	}

	public boolean deleteIteration(Iteration curIteration) {
		m_IterationList = null;
		m_LastAccessed = new Date();
		boolean success = IterationStorageService.getInstance()
				.deleteIteration(curIteration, ExtLibUtil.getCurrentSession());
		return success;
	}

	public Iteration getIterationById(String strIterationId) {
		return loadIteration(strIterationId);
	}

	public List<Iteration> getAllIterations(int sortOrder, boolean reverse) {
		List<Iteration> lstAll = IterationStorageService.getInstance()
				.getAllIterations(ExtLibUtil.getCurrentSession());
		IterationSortFactory.sortIterations(lstAll, sortOrder, reverse);
		return lstAll;
	}

	public List<Iteration> getIterationsOfProject(int sort, boolean reverse, String projectId) {
		List<Iteration> projectiterations = new ArrayList<Iteration>();
		List<Iteration> iterations = getAllIterations(sort, reverse);
		for (Iteration iteration : iterations) {
			if (iteration.getProjectId().equals(projectId)) {
				projectiterations.add(iteration);
			}
		}
		return projectiterations;
	}

	public int getNextUnusedIndex(String projectId) {
		int index = 1;
		for (Iteration iteration : getIterationsOfProject(SORT_BY_INDEX, false,
				projectId)) {
			if (iteration.getIndex() == index) {
				index++;
			}
		}
		return index;
	}

	@Deprecated
	public List<SelectItem> getIterationSelectItemsOfProject(String projectId)
			throws IOException {
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
		for (Iteration iteration : getIterationsOfProject(SORT_BY_INDEX, false,
				projectId)) {
			selectItems.add(new SelectItem(iteration.getId(), iteration
					.getIndex()
					+ " - " + iteration.getSubject()));
		}
		return selectItems;
	}

	public List<String> getIterationValuesOfProject(String projectId)
			throws IOException {
		List<String> selectItems = new ArrayList<String>();
		for (Iteration iteration : getIterationsOfProject(SORT_BY_INDEX, false,
				projectId)) {
			selectItems.add(iteration.getIndex() + " - "
					+ iteration.getSubject() + "|" + iteration.getId());
		}
		return selectItems;
	}

	private Iteration loadIteration(String strID) {
		if (m_IterationList == null
				|| IterationStorageService.getInstance()
						.isDirty(m_LastAccessed)) {
			m_IterationList = new HashMap<String, Iteration>();
		}
		if (m_IterationList.containsKey(strID)) {
			return m_IterationList.get(strID);
		}
		Iteration p = IterationStorageService.getInstance().getIterationById(
				strID, ExtLibUtil.getCurrentSession());
		m_LastAccessed = new Date();
		if (p != null) {
			m_IterationList.put(strID, p);
		}
		return p;
	}
}
