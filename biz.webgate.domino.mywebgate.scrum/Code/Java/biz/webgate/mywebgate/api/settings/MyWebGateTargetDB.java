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

public class MyWebGateTargetDB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String m_Server = "";
	private String m_Path = "";
	private boolean available = false;
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
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
}
