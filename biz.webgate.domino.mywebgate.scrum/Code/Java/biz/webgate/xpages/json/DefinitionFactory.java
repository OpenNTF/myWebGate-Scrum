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
package biz.webgate.xpages.json;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.ibm.xsp.http.MimeMultipart;

import biz.webgate.xpages.json.annotations.JSONEntity;
import biz.webgate.xpages.json.annotations.JSONObject;
import biz.webgate.xpages.json.binding.BooleanBinder;
import biz.webgate.xpages.json.binding.BusinessObjectBinder;
import biz.webgate.xpages.json.binding.DateBinder;
import biz.webgate.xpages.json.binding.DoubleBinder;
import biz.webgate.xpages.json.binding.IJSONBinder;
import biz.webgate.xpages.json.binding.IntBinder;
import biz.webgate.xpages.json.binding.ListBinder;
import biz.webgate.xpages.json.binding.MimeMultipartBinder;
import biz.webgate.xpages.json.binding.StringBinder;
import biz.webgate.xpages.util.ServiceSupport;

public class DefinitionFactory {

	public static Definition getDefinition(Field fldCurrent, JSONEntity je, JSONObject jo) {

		IJSONBinder<?> jsBinder = getJSONBinder(fldCurrent.getType());
		Class<?> clInner = null;
		if (jsBinder != null) {
			clInner = getInnerClass(fldCurrent);
			Definition defCurrent = new Definition(je.jsonproperty(), ServiceSupport.buildCleanFieldName(jo, fldCurrent.getName()), je
					.showEmptyValue(), jsBinder, clInner);
			return defCurrent;
		}
		return null;
	}

	private static Class<?> getInnerClass(Field fldCurrent) {
		Class<?> clInner = null;
		if (fldCurrent.getGenericType() instanceof ParameterizedType) {
			Type[] genericTypes = ((ParameterizedType) fldCurrent.getGenericType()).getActualTypeArguments();
			if (genericTypes.length > 0) {
				clInner = (Class<?>) genericTypes[0];
			}
		}
		return clInner;
	}

	public static IJSONBinder<?> getJSONBinder(Class<?> clCurrent) {
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
		if (clCurrent.equals(Date.class)) {
			return DateBinder.getInstance();
		}
		if (clCurrent.equals(List.class)) {
			return ListBinder.getInstance();
		}
		if (clCurrent.equals(MimeMultipart.class)) {
			return MimeMultipartBinder.getInstance();
		}
		if (JSONService.getInstance().hasBinderDefinition(clCurrent)) {
			return BusinessObjectBinder.getInstance();
		}
		return null;
	}
}
