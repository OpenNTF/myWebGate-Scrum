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
package biz.webgate.xpages.util.logging;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class LoggerFactory {

	private static HashMap<String, Logger> m_RegistredLoggers = new HashMap<String, Logger>();

	public static Logger getLogger(String strName) {
		try {
			if (m_RegistredLoggers.containsKey(strName)) {
				return m_RegistredLoggers.get(strName);
			}
			Logger logRC = java.util.logging.Logger.getAnonymousLogger();
			// TODO: Log Configuration aus dem BASE Nehmen
			LogSettings logSet = new LogSettings();
			logRC.setLevel(LogSettings.getLogLevel(logSet.getLogLevel()));
			DominoHandler dh = new DominoHandler(strName, LogSettings
					.getLogLevel(logSet.getLogLevelFile()));
			ConsoleHandler ch = new ConsoleHandler(strName, LogSettings
					.getLogLevel(logSet.getLogLevelConsole()));
			logRC.addHandler(dh);
			logRC.addHandler(ch);
			m_RegistredLoggers.put(strName, logRC);
			return logRC;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void notifyLogLevelChange(LogSettings logSetCurrent) {
		for (Iterator<Logger> itLogger = m_RegistredLoggers.values().iterator(); itLogger
				.hasNext();) {
			Logger logCurrent = itLogger.next();
			logCurrent.setLevel(LogSettings.getLogLevel(logSetCurrent
					.getLogLevel()));
			for (Handler hanCurrent : logCurrent.getHandlers()) {
				if (hanCurrent instanceof DominoHandler) {
					DominoHandler dh = (DominoHandler) hanCurrent;
					dh.setLevel(LogSettings.getLogLevel(logSetCurrent
							.getLogLevelFile()));
				}
				if (hanCurrent instanceof ConsoleHandler) {
					ConsoleHandler ch = (ConsoleHandler) hanCurrent;
					ch.setLevel(LogSettings.getLogLevel(logSetCurrent
							.getLogLevelConsole()));
				}
			}
		}
	}
}
