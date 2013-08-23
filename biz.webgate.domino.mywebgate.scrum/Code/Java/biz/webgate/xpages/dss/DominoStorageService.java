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
package biz.webgate.xpages.dss;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.View;
import biz.webgate.xpages.dss.annotations.DominoEntity;
import biz.webgate.xpages.dss.annotations.DominoStore;
import biz.webgate.xpages.dss.binding.DateBinder;
import biz.webgate.xpages.dss.binding.Domino2JavaBinder;
import biz.webgate.xpages.dss.binding.DoubleArrayBinder;
import biz.webgate.xpages.dss.binding.DoubleBinder;
import biz.webgate.xpages.dss.binding.IBinder;
import biz.webgate.xpages.dss.binding.IntBinder;
import biz.webgate.xpages.dss.binding.Java2DominoBinder;
import biz.webgate.xpages.dss.binding.LongBinder;
import biz.webgate.xpages.dss.binding.StringArrayBinder;
import biz.webgate.xpages.dss.binding.StringBinder;
import biz.webgate.xpages.util.ServiceSupport;

public class DominoStorageService {

	private static final String UNID_IDENTIFIER = "@UNID";
	private static DominoStorageService m_Service;
	private Hashtable<String, Domino2JavaBinder> m_Loader = new Hashtable<String, Domino2JavaBinder>();
	private Hashtable<String, Java2DominoBinder> m_Saver = new Hashtable<String, Java2DominoBinder>();
	private Hashtable<String, DominoStore> m_StoreDefinitions = new Hashtable<String, DominoStore>();

	private DominoStorageService() {

	}

	public static DominoStorageService getInstance() {
		if (m_Service == null) {
			m_Service = new DominoStorageService();
		}
		return m_Service;
	}

	public boolean saveObject(Object objCurrent, Database ndbTarget) throws DSSException {
		if (!objCurrent.getClass().isAnnotationPresent(DominoStore.class)) {
			throw new DSSException("No @DominoStore defined for " + objCurrent.getClass().getCanonicalName());
		}
		if (!m_StoreDefinitions.containsKey(objCurrent.getClass().getCanonicalName())) {
			prepareBinders(objCurrent);
		}
		DominoStore dsDefinition = m_StoreDefinitions.get(objCurrent.getClass().getCanonicalName());
		Java2DominoBinder j2d = m_Saver.get(objCurrent.getClass().getCanonicalName());
		return saveObject2Document(dsDefinition, j2d, objCurrent, ndbTarget);
	}

	public boolean getObject(Object objCurrent, Object primaryKey, Database ndbTarget) throws DSSException {
		if (!objCurrent.getClass().isAnnotationPresent(DominoStore.class)) {
			throw new DSSException("No @DominoStore defined for " + objCurrent.getClass().getCanonicalName());
		}
		if (!m_StoreDefinitions.containsKey(objCurrent.getClass().getCanonicalName())) {
			prepareBinders(objCurrent);

		}
		DominoStore dsDefinition = m_StoreDefinitions.get(objCurrent.getClass().getCanonicalName());
		Domino2JavaBinder d2j = m_Loader.get(objCurrent.getClass().getCanonicalName());
		return getObjectFromDocument(dsDefinition, d2j, objCurrent, primaryKey, ndbTarget);

	}

	private void prepareBinders(Object objCurrent) {
		DominoStore dsCurrent = objCurrent.getClass().getAnnotation(DominoStore.class);
		Domino2JavaBinder d2j = buildLoadBinder(dsCurrent, objCurrent.getClass());
		Java2DominoBinder j2d = buildSaveBinder(dsCurrent, objCurrent.getClass());
		m_Loader.put(objCurrent.getClass().getCanonicalName(), d2j);
		m_Saver.put(objCurrent.getClass().getCanonicalName(), j2d);
		m_StoreDefinitions.put(objCurrent.getClass().getCanonicalName(), dsCurrent);
	}

	public boolean getObjectWithDocument(Object objCurrent, Document docCurrent) {
		try {
			if (!objCurrent.getClass().isAnnotationPresent(DominoStore.class)) {
				throw new DSSException();
			}
			if (!m_StoreDefinitions.containsKey(objCurrent.getClass().getCanonicalName())) {
				prepareBinders(objCurrent);

			}
			DominoStore dsDefinition = m_StoreDefinitions.get(objCurrent.getClass().getCanonicalName());
			Domino2JavaBinder d2j = m_Loader.get(objCurrent.getClass().getCanonicalName());
			d2j.processDocument(docCurrent, objCurrent);
			if (UNID_IDENTIFIER.equals(dsDefinition.View())) {
				setUNID2PK(dsDefinition, objCurrent, docCurrent);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean getObjectFromDocument(DominoStore dsDefinition, Domino2JavaBinder d2j, Object objCurrent, Object pk, Database ndbTarget) {
		try {
			Document docCurrent = getDocument(dsDefinition, objCurrent, ndbTarget, false);
			if (docCurrent == null) {
				return false;
			}
			d2j.processDocument(docCurrent, objCurrent);
			if (UNID_IDENTIFIER.equals(dsDefinition.View())) {
				setUNID2PK(dsDefinition, objCurrent, docCurrent);
			}
			docCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean saveObject2Document(DominoStore ds, Java2DominoBinder j2d, Object obj, Database ndbTarget) {
		try {
			Document docCurrent = getDocument(ds, obj, ndbTarget, true);
			j2d.processDocument(docCurrent, obj);
			docCurrent.save(true, false, true);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private Document getDocument(DominoStore ds, Object obj, Database ndbTarget, boolean createOnFail) throws NotesException {
		Document docCurrent = null;
		Object objID = getObjectID(ds, obj);
		if (objID != null && !"".equals(objID)) {
			if (UNID_IDENTIFIER.equals(ds.View())) {
				docCurrent = ndbTarget.getDocumentByUNID("" + objID);
			} else {
				View viwCurrent = ndbTarget.getView(ds.View());
				docCurrent = viwCurrent.getDocumentByKey(objID, true);
				viwCurrent.recycle();
			}
		}
		if (docCurrent == null && createOnFail) {
			docCurrent = ndbTarget.createDocument();
			docCurrent.replaceItemValue("Form", ds.Form());
			if (UNID_IDENTIFIER.equals(ds.View())) {
				setUNID2PK(ds, obj, docCurrent);
			}
		}
		return docCurrent;
	}

	private void setUNID2PK(DominoStore ds, Object obj, Document docCurrent) {
		try {
			Method mt = obj.getClass().getMethod("set" + ds.PrimaryKeyField(), String.class);
			mt.invoke(obj, docCurrent.getUniversalID());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object getObjectID(DominoStore ds, Object obj) {
		try {
			if (ds.PrimaryFieldClass().equals(String.class)) {
				return StringBinder.getInstance().getValue(obj, ds.PrimaryKeyField());
			}
			if (ds.PrimaryFieldClass().equals(Integer.class)) {
				return IntBinder.getInstance().getValue(obj, ds.PrimaryKeyField());
			}
			if (ds.PrimaryFieldClass().equals(Double.class)) {
				return DoubleBinder.getInstance().getValue(obj, ds.PrimaryKeyField());
			}
			if (ds.PrimaryFieldClass().equals(Double[].class)) {
				return DoubleArrayBinder.getInstance().getValue(obj, ds.PrimaryKeyField());
			}
			if (ds.PrimaryFieldClass().equals(Double.class)) {
				return LongBinder.getInstance().getValue(obj, ds.PrimaryKeyField());
			}
			if (ds.PrimaryFieldClass().equals(Date.class)) {
				return DateBinder.getInstance().getValue(obj, ds.PrimaryKeyField());
			}
			if (ds.PrimaryFieldClass().equals(String[].class)) {
				return StringArrayBinder.getInstance().getValue(obj, ds.PrimaryKeyField());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Java2DominoBinder buildSaveBinder(DominoStore dsStore, Class<?> currentClass) {
		Java2DominoBinder j2dRC = new Java2DominoBinder();
		for (Field fldCurrent : currentClass.getDeclaredFields()) {
			if (fldCurrent.isAnnotationPresent(DominoEntity.class)) {
				DominoEntity de = fldCurrent.getAnnotation(DominoEntity.class);
				if (!de.readOnly()) {
					IBinder<?> binder = DefinitionFactory.getBinder(fldCurrent.getType(), fldCurrent.getGenericType());
					if (binder != null) {
						HashMap<String, Object> addValues = new HashMap<String, Object>();
						if (de.isNames())
							addValues.put("isNames", true);
						if (de.isAuthor())
							addValues.put("isAuthor", true);
						if (de.isReader())
							addValues.put("isReader", true);
						if (de.dateOnly())
							addValues.put("dateOnly", true);
						if (de.showNameAs() != null)
							addValues.put("showNameAs", de.showNameAs().toString());
						j2dRC.addDefinition(de.FieldName(), ServiceSupport.buildCleanFieldName(dsStore, fldCurrent.getName()), binder,
								addValues);
					}
				}
			}
		}
		return j2dRC;
	}

	private Domino2JavaBinder buildLoadBinder(DominoStore dsStore, Class<?> currentClass) {
		Domino2JavaBinder djdRC = new Domino2JavaBinder();
		for (Field fldCurrent : currentClass.getDeclaredFields()) {
			if (fldCurrent.isAnnotationPresent(DominoEntity.class)) {
				DominoEntity de = fldCurrent.getAnnotation(DominoEntity.class);
				if (!de.writeOnly()) {
					if (de.isFormula()) {
						IBinder<?> binder = DefinitionFactory.getFormulaBinder(fldCurrent.getType());
						if (binder != null) {
							djdRC.addDefinition(de.FieldName(), ServiceSupport.buildCleanFieldName(dsStore, fldCurrent.getName()), binder,
									null);
						}
					} else {
						IBinder<?> binder = DefinitionFactory.getBinder(fldCurrent.getType(), fldCurrent.getGenericType());
						HashMap<String, Object> addValues = new HashMap<String, Object>();
						if (de.isNames())
							addValues.put("isNames", true);
						if (de.isAuthor())
							addValues.put("isAuthor", true);
						if (de.isReader())
							addValues.put("isReader", true);
						if (de.dateOnly())
							addValues.put("dateOnly", true);
						if (de.showNameAs() != null)
							addValues.put("showNameAs", de.showNameAs().toString());

						if (binder != null) {
							djdRC.addDefinition(de.FieldName(), ServiceSupport.buildCleanFieldName(dsStore, fldCurrent.getName()), binder,
									addValues);
						}
					}
				}
			}
		}
		return djdRC;
	}

}
