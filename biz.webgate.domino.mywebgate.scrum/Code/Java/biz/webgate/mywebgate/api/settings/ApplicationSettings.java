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
package biz.webgate.mywebgate.api.settings;

import java.io.Serializable;

public class ApplicationSettings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean available = false;
	private String m_FQDN;
	private String m_Server;
	private String m_Path;
	private String m_ExternalCertifier;
	private String m_InternalCertifier;
	private String m_GlobalID;
	private String m_SystemPrefix;
	private String m_SFN;
	public String getFQDN() {
		return m_FQDN;
	}
	public void setFQDN(String fqdn) {
		m_FQDN = fqdn;
	}
	public String getServer() {
		return m_Server;
	}
	public void setServer(String server) {
		m_Server = server;
	}
	public String getPath() {
		return m_Path;
	}
	public void setPath(String path) {
		m_Path = path;
	}
	public void setExternalCertifier(String itemValueString) {
		m_ExternalCertifier = itemValueString;
		
	}
	public void setInternalCertifier(String itemValueString) {
		m_InternalCertifier = itemValueString;
		
	}
	public void setGlobalID(String itemValueString) {
		m_GlobalID = itemValueString;
		
	}
	public void setSystemPrefix(String itemValueString) {
		m_SystemPrefix = itemValueString;
		
	}
	public void setSFN(String itemValueString) {
		m_SFN = itemValueString;
		
	}
	public String getExternalCertifier() {
		return m_ExternalCertifier;
	}
	public String getInternalCertifier() {
		return m_InternalCertifier;
	}
	public String getGlobalID() {
		return m_GlobalID;
	}
	public String getSystemPrefix() {
		return m_SystemPrefix;
	}
	public String getSFN() {
		return m_SFN;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
}
