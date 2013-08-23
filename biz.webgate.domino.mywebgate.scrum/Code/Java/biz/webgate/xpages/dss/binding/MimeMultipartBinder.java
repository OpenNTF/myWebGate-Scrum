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
package biz.webgate.xpages.dss.binding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import lotus.domino.Document;
import lotus.domino.MIMEEntity;
import lotus.domino.RichTextItem;
import lotus.domino.Stream;

import com.ibm.xsp.http.MimeMultipart;

public class MimeMultipartBinder implements IBinder<MimeMultipart> {

	private static MimeMultipartBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			MimeMultipart strValue = null;
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField, MimeMultipart.class);

			MIMEEntity entity = docCurrent.getMIMEEntity(strNotesField);
			if (entity != null) {
				strValue = MimeMultipart.fromHTML(entity.getContentAsText());
			} else if (docCurrent.hasItem(strNotesField)) {

				if (docCurrent.getFirstItem(strNotesField) != null) {
					if (docCurrent.getFirstItem(strNotesField).getType() != 1) {
						strValue = MimeMultipart.fromHTML(docCurrent.getItemValueString(strNotesField));
					} else {
						RichTextItem rti = (RichTextItem) docCurrent.getFirstItem(strNotesField);
						if (rti != null) {

							HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
							String curURL = rq.getRequestURL().toString();
							String docid = docCurrent.getUniversalID();

							String notesURL = curURL.substring(0, curURL.indexOf(rq.getContextPath()) + 1) + docCurrent.getParentDatabase().getFilePath() + "/0/" + docid + "/" + strNotesField
									+ "?OpenField";

							URL docURL = new URL(notesURL);
							URLConnection uc = docURL.openConnection();

							uc.setRequestProperty("Cookie", rq.getHeader("Cookie"));
							uc.connect();

							// do the HTTP request
							BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));

							// process the data returned
							StringBuffer strBuf = new StringBuffer();
							String tmpStr = "";
							while ((tmpStr = in.readLine()) != null) {
								strBuf.append(tmpStr);
							}

							strValue = MimeMultipart.fromHTML(strBuf.toString());

						}
					}
				}
			}
			mt.invoke(objCurrent, strValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {

			MimeMultipart body = getValue(objCurrent, strJavaField);

			Stream stream = docCurrent.getParentDatabase().getParent().createStream();
			if (body != null)
				stream.writeText(body.getHTML());
			else
				return;

			MIMEEntity entity = docCurrent.getMIMEEntity(strNotesField);
			if (entity == null) {
				docCurrent.removeItem(strNotesField);
				entity = docCurrent.createMIMEEntity(strNotesField);
			}
			entity.setContentFromText(stream, "text/html;charset=UTF-8", 1725);
			stream.close();
			// docCurrent.replaceItemValue(strNotesField, strValue);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static IBinder<MimeMultipart> getInstance() {
		if (m_Binder == null) {
			m_Binder = new MimeMultipartBinder();
		}
		return m_Binder;
	}

	private MimeMultipartBinder() {

	}

	public MimeMultipart getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (MimeMultipart) mt.invoke(objCurrent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
