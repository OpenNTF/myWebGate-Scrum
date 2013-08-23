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

public class CustomPostProcesses {

	private static CustomPostProcesses m_Service;

	private CustomPostProcesses() {

	}

	public static CustomPostProcesses getInstance() {
		if (m_Service == null) {
			m_Service = new CustomPostProcesses();
		}
		return m_Service;
	}
	
	
	public void meineMethode(File test, Object o){		
		/*
		System.out.println(n.getName());
		System.out.println("FILE" + test.getName());
		*/
	}
}
