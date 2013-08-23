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
package biz.webgate.mywebgate.components;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.faces.context.FacesContext;

import biz.webgate.xpages.dss.binding.util.FileHelper;
import biz.webgate.xpages.util.MimeTypeService;

import com.ibm.domino.services.HttpServiceConstants;
import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.rest.RestServiceEngine;
import com.ibm.xsp.component.UIFileuploadEx.UploadedFile;
import com.ibm.xsp.extlib.component.rest.CustomService;
import com.ibm.xsp.extlib.component.rest.CustomServiceBean;
import com.ibm.xsp.http.IUploadedFile;

public class ComponentSessionFacade extends CustomServiceBean implements Serializable {

	public final static int CC_TYPE_MV = 1;
	public final static int CC_TYPE_FILE = 2;

	private static final long serialVersionUID = 1L;

	private HashMap<String, String> m_MultiValueFields = new HashMap<String, String>();
	private HashMap<String, FileHelper> m_Files = new HashMap<String, FileHelper>();

	private HashMap<String, HashMap<String, Object>> m_LoadedMvCCs = new HashMap<String, HashMap<String, Object>>();

	public static final String BEAN_NAME = "ccBean"; //$NON-NLS-1$

	public static ComponentSessionFacade get(FacesContext context) {
		ComponentSessionFacade bean = (ComponentSessionFacade) context.getApplication().getVariableResolver().resolveVariable(context, BEAN_NAME);
		return bean;
	}

	public static ComponentSessionFacade get() {
		return get(FacesContext.getCurrentInstance());
	}

	public MimetypesFileTypeMap getMimeTypes() {
		return MimeTypeService.getInstance().getMimeTypes();
	}

	// ------------------------
	// MULTI VALUE
	// ------------------------

	public void addCustomControl(String strId, Object objObject, String strObjectAlias, String strFieldName, int ccType) {

		switch (ccType) {
		case CC_TYPE_MV:
			MultiValueService.getInstance().addCustomControl(strId, objObject, strObjectAlias, strFieldName, m_LoadedMvCCs);
			break;
		default:
			break;
		}
	}

	public void removeCustomControl(String strObjectAlias, String strObjectId, int ccType) {

		switch (ccType) {
		case CC_TYPE_MV:
			MultiValueService.getInstance().removeCustomControl(strObjectAlias, strObjectId, m_LoadedMvCCs);
			break;
		default:
			break;
		}
	}

	public Object getValue(Object obj, String strFieldName) {
		return MultiValueService.getInstance().getValue(obj, strFieldName, m_MultiValueFields);
	}

	public void updateMultiValueData(Object objObject, String strObjectAlias) {
		MultiValueService.getInstance().updateMultiValueData(objObject, strObjectAlias, m_LoadedMvCCs, m_MultiValueFields);
	}

	// ------------------------
	// FILES
	// ------------------------
	public List<FileHelper> getValue(Object obj, String strFieldName, String strCcId) {
		List<FileHelper> rc = FileService.getInstance().getValue(obj, strFieldName, strCcId, m_Files);
		return rc;
	}

	public void uploadFile(UploadedFile file, Object obj, String strFieldName, String strCcId, String strNewId, String strPostUploadProcessMethod) {
		if (file == null)
			return;

		List<FileHelper> objFiles = getValue(obj, strFieldName, strCcId);
		if (objFiles == null)
			return;

		FileService.getInstance().uploadFile(file, obj, strFieldName, strCcId, strNewId, objFiles, m_Files);
		try {

			IUploadedFile FTemp = file.getUploadedFile();
			File SrFile = FTemp.getServerFile();

			File FNew = new File(SrFile.getParentFile().getAbsolutePath() + File.separator + FTemp.getClientFileName());
			SrFile.renameTo(FNew);

			if (strPostUploadProcessMethod != null && !"".equals(strPostUploadProcessMethod)) {
				CustomPostProcesses cpp = CustomPostProcesses.getInstance();
				Method mt = cpp.getClass().getMethod(strPostUploadProcessMethod, File.class, Object.class);
				if (mt != null) {
					mt.invoke(cpp, FNew, obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean addFileToObject(Object obj, String strFieldName, String strCcId, String strNewId) {
		try {

			Method mt = null;
			List<FileHelper> objFiles = getValue(obj, strFieldName, strCcId);
			if (objFiles == null) {
				return false;
			}
			FileHelper fhCurrent = m_Files.get(strCcId + "|" + strNewId);

			if (fhCurrent != null) {
				if (!objFiles.contains(fhCurrent)) {
					objFiles.add(fhCurrent);
					mt = obj.getClass().getMethod("set" + strFieldName, List.class);
					mt.invoke(obj, objFiles);
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void removeFile(Object obj, String strFieldName, String strCcId, String strFileId) {
		List<FileHelper> objFiles = getValue(obj, strFieldName, strCcId);
		FileHelper fhCurrent = m_Files.get(strCcId + "|" + strFileId);
		if (fhCurrent != null) {
			FileService.getInstance().removeFile(fhCurrent);

			try {
				objFiles.remove(fhCurrent);
				Method mt = obj.getClass().getMethod("set" + strFieldName, List.class);
				mt.invoke(obj, objFiles);
				// getValue(obj, strFieldName, strCcId);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * public void unregisterFiles(String strCcId, String strFileId) {
	 * FileService.getInstance().unregisterFiles(strCcId, strFileId, m_Files); }
	 */

	public static final String SERVICE_FILE = "/file/";

	@Override
	public void renderService(CustomService service, RestServiceEngine engine) throws ServiceException {

		String strMethod = engine.getHttpRequest().getMethod();
		String strPathInfo = engine.getHttpRequest().getPathInfo();

		if ("GET".equalsIgnoreCase(strMethod)) {
			if (strPathInfo.startsWith(SERVICE_FILE)) {

				String strFileInfos = strPathInfo.substring(SERVICE_FILE.length());
				String strCcId = strFileInfos.substring(0, strFileInfos.indexOf("/"));
				String strFileId = strFileInfos.substring(strFileInfos.indexOf("/") + 1);

				FileHelper fhCurrent = m_Files.get(strCcId + "|" + strFileId);

				if (fhCurrent == null) {
					System.out.println("fhCurrent " + fhCurrent + " - strFileId (" + strFileId + ") == null");
					return;
				}

				engine.getHttpResponse().setContentType(HttpServiceConstants.CONTENTTYPE_APPLICATION_JSON_UTF8);
				engine.getHttpResponse().setHeader("Content-disposition", "attachment;filename=\"" + fhCurrent.getDisplayName() + "\"");

				FileService.getInstance().processToStream(engine, fhCurrent);

				return;
			}
			// ErrorJSONBuilder.getInstance().processError2JSON(engine, 7004,
			// strPathInfo + " is not supported.", null);

			return;
		}
	}

	// NAMES

	public List<NameEntry> searchNames(String strSearch, String mwgServer, String mwgPath, boolean isLookup, String strLkpView, String strFtSearch, String strInputString,
			ArrayList<String> returnFields, String returnField, boolean isFormula, int nSortOrder, String searchFieldInView) {
		return NameSearchService.getInstance().searchNames(strSearch, mwgServer, mwgPath, isLookup, strLkpView, strFtSearch, strInputString, returnFields, returnField, isFormula, nSortOrder,
				searchFieldInView);
	}

	public List<NameEntry> getAllNames(String mwgServer, String mwgPath, boolean isLookup, String strLkpView, String strFtSearch, String strInputString, ArrayList<String> returnFields,
			String returnField, boolean isFormula, int nSortOrder, int nMaxResult) {
		return NameSearchService.getInstance().getAllNames(mwgServer, mwgPath, isLookup, strLkpView, strFtSearch, strInputString, returnFields, returnField, isFormula, nSortOrder, nMaxResult);
	}

	public String getTypeAhead(String strSearch, String mwgServer, String mwgPath, boolean isLookup, String strLkpView, String strFtSearch, String strInputString, ArrayList<String> returnFields,
			String returnField, boolean isFormula, int nSortOrder, String strAddFunction, String searchFieldInView) {
		String result = "<ul><li><span class='informal'>Suggestions:</span></li>";

		if (returnFields == null) {

			List<String> entries = MultiValueService.getInstance().getTypeAhead(strSearch, mwgServer, mwgPath, isLookup, strLkpView, strFtSearch, strInputString, returnFields, returnField, isFormula,
					nSortOrder);
			for (String entry : entries) {
				int start = entry.toLowerCase().indexOf(strSearch.toLowerCase());
				int stop = start + 3 + strSearch.length();

				StringBuffer sb = new StringBuffer(entry);
				sb.insert(start, "<b>");
				sb.insert(stop, "</b>");

				result += "<li><a onclick=\""+ strAddFunction +"();\"><p>" + sb + "</p></a></li>";
			}
		} else {
			List<NameEntry> entries = searchNames(strSearch, mwgServer, mwgPath, isLookup, strLkpView, strFtSearch, strInputString, returnFields, returnField, isFormula, nSortOrder, searchFieldInView);
			for (NameEntry entry : entries) {

				int start = entry.getReturnValue().toLowerCase().indexOf(strSearch.toLowerCase());
				int stop = start + 3 + strSearch.length();

				StringBuffer sb = new StringBuffer(entry.getReturnValue());
				if (start >= 0) {
					sb.insert(start, "<b>");
					sb.insert(stop, "</b>");
				}
				result += "<li><a onclick=\""+ strAddFunction +"();\"><p>" + sb + "</p></a></li>";

				// result += "<li><span class='informal'>ZUSATZ INFO:</span><b>"
				// +
				// entry.getReturnValue() + "</b></li>";

			}
		}
		result += "</ul>";

		// System.out.println(result);
		return result;
	}

}
