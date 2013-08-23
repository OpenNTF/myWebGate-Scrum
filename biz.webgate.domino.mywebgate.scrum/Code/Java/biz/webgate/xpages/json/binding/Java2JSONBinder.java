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
package biz.webgate.xpages.json.binding;

import java.util.ArrayList;
import java.util.List;

import biz.webgate.xpages.json.Definition;

import com.ibm.domino.services.util.JsonWriter;

public class Java2JSONBinder {

	private List<Definition> m_Definitions = new ArrayList<Definition>();

	public void addDefinition(Definition def) {
		m_Definitions.add(def);
	}

	public void processJSON(JsonWriter jsWriter, Object obj) {
		for (Definition def : m_Definitions) {
			def.process2JSON(jsWriter, obj);
			
		}
	}
}
