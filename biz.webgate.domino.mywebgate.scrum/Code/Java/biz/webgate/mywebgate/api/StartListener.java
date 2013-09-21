/*
 * � Copyright WebGate Consulting AG, 2012
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


import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.application.events.ApplicationListener2;

public class StartListener implements ApplicationListener2 {

	public void applicationRefreshed(ApplicationEx arg0) {
		try {
			//System.out.println("Refresh of " + arg0.getApplicationId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void applicationCreated(ApplicationEx arg0) {
		try {
			//System.out.println("Create of " + arg0.getApplicationId());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void applicationDestroyed(ApplicationEx arg0) {
		try {
			//TODO: Auf Logger �ndern!
			//System.out.println("Destroy of " + arg0.getApplicationId());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
