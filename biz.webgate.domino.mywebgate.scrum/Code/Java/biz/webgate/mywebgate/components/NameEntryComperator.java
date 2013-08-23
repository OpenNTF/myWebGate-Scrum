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


import java.util.Comparator;


public class NameEntryComperator implements Comparator<NameEntry> {

	private String key;
	
	public NameEntryComperator(String sortValue){
		key = sortValue;
	}
	
	
	public int compare(NameEntry arg0, NameEntry arg1) {
		return arg0.getValues().get(key).toLowerCase().compareTo(arg1.getValues().get(key).toLowerCase());
	}

}
