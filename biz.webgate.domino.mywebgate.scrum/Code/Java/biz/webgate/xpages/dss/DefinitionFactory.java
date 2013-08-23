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
package biz.webgate.xpages.dss;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import biz.webgate.xpages.dss.binding.BooleanBinder;
import biz.webgate.xpages.dss.binding.DateBinder;
import biz.webgate.xpages.dss.binding.DoubleArrayBinder;
import biz.webgate.xpages.dss.binding.DoubleBinder;
import biz.webgate.xpages.dss.binding.FormulaDateBinder;
import biz.webgate.xpages.dss.binding.FormulaDoubleBinder;
import biz.webgate.xpages.dss.binding.FormulaStringBinder;
import biz.webgate.xpages.dss.binding.IBinder;
import biz.webgate.xpages.dss.binding.IntBinder;
import biz.webgate.xpages.dss.binding.ListStringBinder;
import biz.webgate.xpages.dss.binding.LongBinder;
import biz.webgate.xpages.dss.binding.MimeMultipartBinder;
import biz.webgate.xpages.dss.binding.StringArrayBinder;
import biz.webgate.xpages.dss.binding.StringBinder;
import biz.webgate.xpages.dss.binding.files.FileDownloadBinder;
import biz.webgate.xpages.dss.binding.files.FileUploadBinder;
import biz.webgate.xpages.dss.binding.util.FileHelper;

import com.ibm.xsp.component.UIFileuploadEx.UploadedFile;
import com.ibm.xsp.http.MimeMultipart;

public class DefinitionFactory {

	public static IBinder<?> getBinder(Class<?> clCurrent, Type gtCurrent) {
		// System.out.println(clCurrent);
		if (clCurrent.equals(Boolean.class) || clCurrent.equals(Boolean.TYPE)) {
			return BooleanBinder.getInstance();
		}
		if (clCurrent.equals(String.class)) {
			return StringBinder.getInstance();
		}
		if (clCurrent.equals(Integer.class) || clCurrent.equals(Integer.TYPE)) {
			return IntBinder.getInstance();
		}
		if (clCurrent.equals(Double.class) || clCurrent.equals(Double.TYPE)) {
			return DoubleBinder.getInstance();
		}
		if (clCurrent.equals(Double[].class)) {
			return DoubleArrayBinder.getInstance();
		}
		if (clCurrent.equals(Long.class) || clCurrent.equals(Long.TYPE)) {
			return LongBinder.getInstance();
		}
		if (clCurrent.equals(Date.class)) {
			return DateBinder.getInstance();
		}
		if (clCurrent.equals(String[].class)) {
			return StringArrayBinder.getInstance();
		}
		if (clCurrent.equals(List.class)) {

			if (gtCurrent instanceof ParameterizedType) {
				Type[] genericTypes = ((ParameterizedType) gtCurrent).getActualTypeArguments();
				for (Type genericType : genericTypes) {
					if (String.class.equals(genericType)) {
						return ListStringBinder.getInstance();
					} else if (FileHelper.class.equals(genericType)) {
						return FileDownloadBinder.getInstance();
					} else if (UploadedFile.class.equals(genericType)) {
						return FileUploadBinder.getInstance();
					}

				}
			}

		}
		if (clCurrent.equals(MimeMultipart.class)) {
			return MimeMultipartBinder.getInstance();
		}
		if (clCurrent.equals(UploadedFile.class)) {
			return FileUploadBinder.getInstance();
		}
		return null;
	}

	public static IBinder<?> getFormulaBinder(Class<?> clCurrent) {
		if (clCurrent.equals(String.class)) {
			return FormulaStringBinder.getInstance();
		}
		if (clCurrent.equals(Double.class) || clCurrent.equals(Double.TYPE)) {
			return FormulaDoubleBinder.getInstance();
		}
		if (clCurrent.equals(Date.class)) {
			return FormulaDateBinder.getInstance();
		}
		return null;
	}

}
