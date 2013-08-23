/*
 * © Copyright WebGate Consulting AG, 2012
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
package biz.webgate.mywebgate.api;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import biz.webgate.mywebgate.api.settings.ApplicationSettings;
import biz.webgate.mywebgate.api.settings.MyWebGateTargetDB;
import biz.webgate.mywebgate.api.settings.SettingStorageService;
import biz.webgate.xpages.util.logging.LogSettings;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class MyWebgateSessionFacade implements Serializable {

	public static final String BEAN_NAME = "mywebgateBean"; //$NON-NLS-1$

	public static MyWebgateSessionFacade get(FacesContext context) {
		MyWebgateSessionFacade bean = (MyWebgateSessionFacade) context.getApplication().getVariableResolver().resolveVariable(context, BEAN_NAME);
		return bean;
	}

	public static MyWebgateSessionFacade get() {
		return get(FacesContext.getCurrentInstance());
	}

	private ApplicationSettings m_AppSettings = null;
	private LogSettings m_LogSettings = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = -7119741466505319161L;

	public String getMyWebGateURL() {
		if (getAppSettings().isAvailable()) {
			return m_AppSettings.getFQDN() + "/" + m_AppSettings.getPath().replace("\\", "/");
		}
		return "";
	}

	public ApplicationSettings getAppSettings() {
		if (m_AppSettings == null) {
			m_AppSettings = SettingStorageService.getInstance().getApplicationSettings();
		}
		return m_AppSettings;
	}

	public MyWebGateTargetDB getMWGSettings() {
		return SettingStorageService.getInstance().getMWGDbSettings();
	}

	public void saveMWGSettings(MyWebGateTargetDB settings) {
		SettingStorageService.getInstance().saveMWGDbSettings(settings);
		m_AppSettings = SettingStorageService.getInstance().getApplicationSettings();
	}

	public LogSettings getLogSettings() {
		if (m_LogSettings == null) {
			m_LogSettings = SettingStorageService.getInstance().getLogSettings(ExtLibUtil.getCurrentSessionAsSigner());
		}
		return m_LogSettings;
	}

	public boolean saveLogSettings(LogSettings logCurrent) {
		if (SettingStorageService.getInstance().saveLogSettings(logCurrent, ExtLibUtil.getCurrentSessionAsSigner())) {
			m_LogSettings = logCurrent;
			return true;
		}
		return false;
	}

	public String getLogLevelTXT(int nLogLevel) {
		return LogSettings.getLogLevelTXT(nLogLevel);
	}
}
