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
package biz.webgate.scrum.admin;

import java.util.List;

import lotus.domino.Document;
import lotus.domino.RichTextItem;
import lotus.domino.Session;

public class MailerFactory {

	public static boolean sendLink(String strURL, List<String> sendTo, String strComment, Session sesCurrent) {
		try {
			Document docMail = sesCurrent.getCurrentDatabase().createDocument();
			docMail.replaceItemValue("SendTo", sendTo);
			docMail.replaceItemValue("Subject", sesCurrent.getCurrentDatabase().getTitle() + ": please check this link");
			
			RichTextItem rtiBody = docMail.createRichTextItem("Body");
			rtiBody.appendText("" + strURL);
			rtiBody.addNewLine(2);
			rtiBody.appendText(strComment);
			
			docMail.send(false);
			
			System.out.println("target url = " + "/show.xsp?id=" + strURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
