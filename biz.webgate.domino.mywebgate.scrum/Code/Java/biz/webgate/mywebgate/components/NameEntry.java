/*
 * � Copyright WebGate Consulting AG, 2013
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


import java.io.Serializable;
import java.util.HashMap;

public class NameEntry implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HashMap<String, String> m_Values;
	private String m_ReturnValue;
	
	public void setValues(HashMap<String, String> values) {
		m_Values = values;
	}
	public HashMap<String, String> getValues() {
		return m_Values;
	}
	public void setReturnValue(String returnValue) {
		m_ReturnValue = returnValue;
	}
	public String getReturnValue() {
		return m_ReturnValue;
	}
	
}
