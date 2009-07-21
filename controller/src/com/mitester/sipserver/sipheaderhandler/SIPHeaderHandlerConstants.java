/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: SIPHeaderHandlerConstants.java
 * Copyright (C) 2008 - 2009  Mobax Networks Private Limited
 * miTester for SIP – License Information
 * --------------------------------------------------
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>
 * 
 * LICENSE INFORMATION REGARDING RELATED THIRD-PARTY SOFTWARE
 * -----------------------------------------------------------------------------------------
 * The miTester for SIP relies on the following third party software. Below is the location and license information :
 *---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * Package 						License 										Details
 *---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * Jain SIP stack 				NIST-CONDITIONS-OF-USE 						        https://jain-sip.dev.java.net/source/browse/jain-sip/licenses/
 * Log4J 						The Apache Software License, Version 2.0 			http://logging.apache.org/log4j/1.2/license.html
 * JNetStreamStandalone lib     GNU Library or LGPL			     					http://sourceforge.net/projects/jnetstream/
 * 
 */

/*
 * miTester is a test automation framework developed for testing SIP applications. 
 * Developers and testers can simulate any kind of test cases and test applications.
 *  
 */
package com.mitester.sipserver.sipheaderhandler;

public class SIPHeaderHandlerConstants {
	/**
	 * To represent synbol to header value is copied from the previous request
	 */
	public static final String COPY_VALUE = "***";

	/**
	 * To represent the semicolon
	 */
	public static final String SEMICOLON = ":";

	/**
	 * To represent the regular expression for alert info header
	 */
	public static final String ACCEPT_REGX = "(((\\*\\/\\*)|(text|image|audio|video|application|message|multipart|([a-zA-Z0-9[-_.+!%*`'~]]+)|x-([a-zA-Z0-9[-_.+!%*`'~]]+)[\\/][\\*])|((text|image|audio|video|application|message|multipart|([a-zA-Z0-9[-_.+!%*`'~]]+)|x-([a-zA-Z0-9[-_.+!%*`'~]]+))[\\/]([a-zA-Z0-9[-_.+!%*`'~]]+)))([\\;]([a-zA-Z0-9[-_.+!%*`'~]]+)[\\=]((([a-zA-Z0-9[-_.+!%*`'~]]+[\\s])|((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\"))?[\\s]*?[\\<]((((((sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+)*?)|((((([\\+]\\d{2,})(-?\\d{3})-?)?(\\d{3})(-?\\d{1,}))|((((\\d{2,})?(-?\\d{3})-?)?(\\d{3})(-?\\d{1,}))(;phone-context=(([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,})|(((\\+?\\d{2})?(-?\\d{3})-?)?(\\d{3})?(-?\\d{1,}?)))))(;isub=(\\w\\W)*)?(;ext=((\\d{3,4})|(\\(\\d{3,4}\\))|(\\-\\d{3,4})|(\\.\\d{3,4})))?(;npdi)?(;enumdi)?(;tgrp=(([a-zA-Z0-9-\\s]+)*?))?(;trunk-context=((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?))?))([\\:]([a-zA-Z0-9[-_.+!%*`'~]]+))?[\\@])?)(((?:[^@][a-zA-Z0-9-]+\\.)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)|((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?))((;transport=(udp|tcp|sctp|tls|tls-sctp))?(;user=(phone|ip|dialstring))?(;method=(ACK|BYE|CANCEL|INFO|INVITE|MESSAGE|NOTIFY|OPTIONS|PRACK|PUBLISH|REFER|REGISTER|SUBSCRIBE|UPDATE))?(;ttl=(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))?(;maddr=(([a-zA-Z0-9_]+)|((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)|((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\")))?(;lr)?(;comp=sigcomp)?(;target=([a-zA-Z0-9[-_.+!%*`'~][\\s][/x21-x7E][/xC0-xDF][/xE0-xEF][/xF0-xF7][/xF8-xFB][/xFC-xFD][/x80-xBF]]{1}))?(;cause=(100|180|181|182|183|200|202|300|301|302|305|380|400|401|402|403|404|405|406|407|408|410|412|413|414|415|416|417|420|421|422|423|428|429|433|436|437|438|470|480|481|482|483|484|485|486|487|488|489|491|493|494|500|501|502|503|504|505|513|580|600|603|604|606))?(;orig)?)(;gr=([a-zA-Z0-9[-_.+!%*`'~][\\s][/x21-x7E][/xC0-xDF][/xE0-xEF][/xF0-xF7][/xF8-xFB][/xFC-xFD][/x80-xBF]]{1}))?)|(([a-zA-Z]([a-zA-Z0-9[-.+]]+))[\\:]((((([\\/\\/](((((([a-zA-Z0-9[-_.+!%*`'~]]+)*?)|(((((\\+\\d{2,})?(-?\\d{3})-?)?(\\d{3})(-?\\d{1,}))|((((\\d{2,})?(-?\\d{3})-?)?(\\d{3})(-?\\d{1,}))(;phone-context=(([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,})|(((\\+?\\d{2})?(-?\\d{3})-?)?(\\d{3})?(-?\\d{1,}?)))))(;isub=(\\w\\W)*)?(;ext=((\\d{3,4})|(\\(\\d{3,4}\\))|(\\-\\d{3,4})|(\\.\\d{3,4})))?(;npdi)?(;enumdi)?(;tgrp=(([a-zA-Z0-9-\\s]+)*?))?(;trunk-context=((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?))?))([\\:]([a-zA-Z0-9[-_.+!%*`'~]]+))?[\\@])?(((?:[^@][a-zA-Z0-9-]+\\.)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)|((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)))|(([a-zA-Z0-9[-_.+!%*`'~]]+)*?)))([\\/](((([a-zA-Z0-9[-_.+!%*`'~]]+)*?)([\\;][\\s]?(([a-zA-Z0-9[-_.+!%*`'~]]+)*?))*?)([\\/]((([a-zA-Z0-9[-_.+!%*`'~]]+)*?)([\\;][\\s]?(([a-zA-Z0-9[-_.+!%*`'~]]+)*?))*?))*?))?)|(([\\/](((([a-zA-Z0-9[-_.+!%*`'~]]+)*?)([\\;][\\s]?(([a-zA-Z0-9[-_.+!%*`'~]]+)*?))*?)([\\/]((([a-zA-Z0-9[-_.+!%*`'~]]+)*?)([\\;][\\s]?(([a-zA-Z0-9[-_.+!%*`'~]]+)*?))*?))*?))))([\\?]([a-zA-Z0-9[-_.+!%*`'~]]+))?)|([a-zA-Z0-9[-_.+!%*`'~][\\s][/x21-x7E][/xC0-xDF][/xE0-xEF][/xF0-xF7][/xF8-xFB][/xFC-xFD][/x80-xBF]]+?))))[\\>]))?)([\\,](((\\*\\/\\*)|(text|image|audio|video|application|message|multipart|([a-zA-Z0-9[-_.+!%*`'~]]+)|x-([a-zA-Z0-9[-_.+!%*`'~]]+)[\\/][\\*])|((text|image|audio|video|application|message|multipart|([a-zA-Z0-9[-_.+!%*`'~]]+)|x-([a-zA-Z0-9[-_.+!%*`'~]]+))[\\/]([a-zA-Z0-9[-_.+!%*`'~]]+)))([\\;]([a-zA-Z0-9[-_.+!%*`'~]]+)[\\=]((([a-zA-Z0-9[-_.+!%*`'~]]+[\\s])|((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\"))?[\\s]*?[\\<]((((((sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+)*?)|((((([\\+]\\d{2,})(-?\\d{3})-?)?(\\d{3})(-?\\d{1,}))|((((\\d{2,})?(-?\\d{3})-?)?(\\d{3})(-?\\d{1,}))(;phone-context=(([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,})|(((\\+?\\d{2})?(-?\\d{3})-?)?(\\d{3})?(-?\\d{1,}?)))))(;isub=(\\w\\W)*)?(;ext=((\\d{3,4})|(\\(\\d{3,4}\\))|(\\-\\d{3,4})|(\\.\\d{3,4})))?(;npdi)?(;enumdi)?(;tgrp=(([a-zA-Z0-9-\\s]+)*?))?(;trunk-context=((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?))?))([\\:]([a-zA-Z0-9[-_.+!%*`'~]]+))?[\\@])?)(((?:[^@][a-zA-Z0-9-]+\\.)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)|((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?))((;transport=(udp|tcp|sctp|tls|tls-sctp))?(;user=(phone|ip|dialstring))?(;method=(ACK|BYE|CANCEL|INFO|INVITE|MESSAGE|NOTIFY|OPTIONS|PRACK|PUBLISH|REFER|REGISTER|SUBSCRIBE|UPDATE))?(;ttl=(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))?(;maddr=(([a-zA-Z0-9_]+)|((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)|((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\")))?(;lr)?(;comp=sigcomp)?(;target=([a-zA-Z0-9[-_.+!%*`'~][\\s][/x21-x7E][/xC0-xDF][/xE0-xEF][/xF0-xF7][/xF8-xFB][/xFC-xFD][/x80-xBF]]{1}))?(;cause=(100|180|181|182|183|200|202|300|301|302|305|380|400|401|402|403|404|405|406|407|408|410|412|413|414|415|416|417|420|421|422|423|428|429|433|436|437|438|470|480|481|482|483|484|485|486|487|488|489|491|493|494|500|501|502|503|504|505|513|580|600|603|604|606))?(;orig)?)(;gr=([a-zA-Z0-9[-_.+!%*`'~][\\s][/x21-x7E][/xC0-xDF][/xE0-xEF][/xF0-xF7][/xF8-xFB][/xFC-xFD][/x80-xBF]]{1}))?)|(([a-zA-Z]([a-zA-Z0-9[-.+]]+))[\\:]((((([\\/\\/](((((([a-zA-Z0-9[-_.+!%*`'~]]+)*?)|(((((\\+\\d{2,})?(-?\\d{3})-?)?(\\d{3})(-?\\d{1,}))|((((\\d{2,})?(-?\\d{3})-?)?(\\d{3})(-?\\d{1,}))(;phone-context=(([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,})|(((\\+?\\d{2})?(-?\\d{3})-?)?(\\d{3})?(-?\\d{1,}?)))))(;isub=(\\w\\W)*)?(;ext=((\\d{3,4})|(\\(\\d{3,4}\\))|(\\-\\d{3,4})|(\\.\\d{3,4})))?(;npdi)?(;enumdi)?(;tgrp=(([a-zA-Z0-9-\\s]+)*?))?(;trunk-context=((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?))?))([\\:]([a-zA-Z0-9[-_.+!%*`'~]]+))?[\\@])?(((?:[^@][a-zA-Z0-9-]+\\.)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)|((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)))|(([a-zA-Z0-9[-_.+!%*`'~]]+)*?)))([\\/](((([a-zA-Z0-9[-_.+!%*`'~]]+)*?)([\\;][\\s]?(([a-zA-Z0-9[-_.+!%*`'~]]+)*?))*?)([\\/]((([a-zA-Z0-9[-_.+!%*`'~]]+)*?)([\\;][\\s]?(([a-zA-Z0-9[-_.+!%*`'~]]+)*?))*?))*?))?)|(([\\/](((([a-zA-Z0-9[-_.+!%*`'~]]+)*?)([\\;][\\s]?(([a-zA-Z0-9[-_.+!%*`'~]]+)*?))*?)([\\/]((([a-zA-Z0-9[-_.+!%*`'~]]+)*?)([\\;][\\s]?(([a-zA-Z0-9[-_.+!%*`'~]]+)*?))*?))*?))))([\\?]([a-zA-Z0-9[-_.+!%*`'~]]+))?)|([a-zA-Z0-9[-_.+!%*`'~][\\s][/x21-x7E][/xC0-xDF][/xE0-xEF][/xF0-xF7][/xF8-xFB][/xFC-xFD][/x80-xBF]]+?))))[\\>]))?))*?";

	/**
	 * To represent the regular expression for alert info header
	 */
	public static final String ALERT_INFO_REGX = "(http://)((([a-zA-Z0-9[-.][/]*]+?)))";

	/**
	 * to represent the regular expression for call info header
	 */
	public static final String CALL_INFO_REGX = "(http://)((([a-zA-Z0-9[-.+][/]]+?)))[\\s]*?(;purpose=([a-zA-Z0-9[-.+~!@#%$^*]]+?))?";

	/**
	 * to represent the regular expression for contact header
	 */
	public static final String CONTACT_REGX = "(((((sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+)+?)|((((([\\+]\\d{2,})(-?\\d{3})-?)?(\\d{3})(-?\\d{1,}))|((((\\d{2,})?(-?\\d{3})-?)?(\\d{3})(-?\\d{1,}))))))([\\:]([a-zA-Z0-9[-_.+!%*`'~]]+))?[\\@])?)(((?:[^@][a-zA-Z0-9-]+\\.)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)|((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?))((;transport=(udp|tcp|sctp|tls|tls-sctp))?(;ttl=(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))?(;maddr=(([a-zA-Z0-9_]+)|((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)|((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\")))?(;lr)?(;transport=UDP)?(;comp=sigcomp)?))";

	/**
	 * to represent the regular expression for From header
	 */
	public static final String FROM_REGX = "(((((sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+)*?)|((((([\\+]\\d{2,})(-?\\d{3})-?)?(\\d{3})(-?\\d{1,}))|((((\\d{2,})?(-?\\d{3})-?)?(\\d{3})(-?\\d{1,}))(;phone-context=(([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,})|(((\\+?\\d{2})?(-?\\d{3})-?)?(\\d{3})?(-?\\d{1,}?)))))(;isub=(\\w\\W)*)?(;ext=((\\d{3,4})|(\\(\\d{3,4}\\))|(\\-\\d{3,4})|(\\.\\d{3,4})))?(;npdi)?(;enumdi)?(;tgrp=(([a-zA-Z0-9-\\s]+)*?))?(;trunk-context=((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?))?))([\\:]([a-zA-Z0-9[-_.+!%*`'~]]+))?[\\@])?)(((?:[^@][a-zA-Z0-9-]+\\.)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)|((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?))((;transport=(udp|tcp|sctp|tls|tls-sctp))?(;user=(phone|ip|dialstring))?(;method=(ACK|BYE|CANCEL|INFO|INVITE|MESSAGE|NOTIFY|OPTIONS|PRACK|PUBLISH|REFER|REGISTER|SUBSCRIBE|UPDATE))?(;ttl=(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))?(;maddr=(([a-zA-Z0-9_]+)|((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)|((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\")))?(;lr)?(;comp=sigcomp)?(;target=([a-zA-Z0-9[-_.+!%*`'~][\\s][/x21-x7E][/xC0-xDF][/xE0-xEF][/xF0-xF7][/xF8-xFB][/xFC-xFD][/x80-xBF]]{1}))?(;cause=(100|180|181|182|183|200|202|300|301|302|305|380|400|401|402|403|404|405|406|407|408|410|412|413|414|415|416|417|420|421|422|423|428|429|433|436|437|438|470|480|481|482|483|484|485|486|487|488|489|491|493|494|500|501|502|503|504|505|513|580|600|603|604|606))?(;orig)?)(;gr=([a-zA-Z0-9[-_.+!%*`'~][\\s][/x21-x7E][/xC0-xDF][/xE0-xEF][/xF0-xF7][/xF8-xFB][/xFC-xFD][/x80-xBF]]{1}))?)";

	// "(sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+))[\\@]([^@]([a-zA-Z0-9[-_.+!%*`'~]]+))";

	/**
	 * to represent the regular expression for content length header
	 */
	public static final String CONTENT_LENGTH_REGX = "[0-9]++";

	/**
	 * to represent the regular expression for content-type header
	 */
	public static final String CONTENT_TYPE_REGX = "([a-zA-Z0-9_]+)(/)([a-zA-Z0-9_]+)";

	/**
	 * to represent the regular expression for cseq header
	 */
	public static final String CSEQ_REGX = "([0-9])*?([\\s]+?)(REGISTER|INVITE|ACK|REFER|BYE|CANCEL|SUBSCRIBE|NOTIFY)";

	/**
	 * to represent the regular expression for error info header
	 */
	public static final String ERROR_INFO_REGX = "(sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+))[\\@]([^@]([a-zA-Z0-9[-_.+!%*`'~]]+))";

	/**
	 * to represent the regular expression for expires header
	 */
	public static final String EXPIRES_REGX = "([0-9]++)$";

	/**
	 * to represent the regular expression for max-forwards header
	 */
	public static final String MAX_FORWARD_REGX = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

	/**
	 * to represent the regular expression for MIME-Version header
	 */
	public static final String MIME_VERSION_REGX = "([\\d]+?[\\.][\\d]+?)";

	/**
	 * to represent the regular expression formin expires header
	 */
	public static final String MIN_EXPIRES_REGX = "([\\d]+?)";

	/**
	 * to represent the regular expression for min-Se header
	 */
	public static final String MIN_SE_REGX = "([\\d]+?)";

	/**
	 * to represent the regular expression for retry-after header
	 */
	public static final String RETRY_AFTER_REGX = "([\\d]+?)";

	/**
	 * to represent the regular expression for p-associated-identity header
	 */
	public static final String P_ASSOCIATED_IDENTITY_REGX = "(sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+))[\\@]([^@]([a-zA-Z0-9[-_.+!%*`'~]]+))";

	/**
	 * to represent the regular expression for p-associated-uri header
	 */
	public static final String P_ASSOCIATED_URI_REGX = "(sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+))[\\@]([^@]([a-zA-Z0-9[-_.+!%*`'~]]+))";

	/**
	 * to represent the regular expression for p=-called-party-id header
	 */
	public static final String P_CALLED_PARTY_ID_REGX = "(sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+))[\\@]([^@]([a-zA-Z0-9[-_.+!%*`'~]]+))";

	/**
	 * to represent the regular expression for p-prefreffered-identity header
	 */
	public static final String P_PREFERRED_IDENTITY_REGX = "(sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+))[\\@]([^@]([a-zA-Z0-9[-_.+!%*`'~]]+))";

	/**
	 * to represent the regular expression for record-route header
	 */
	public static final String RECORD_ROUTE_REGX = "(sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+))[\\@]?([^@]([a-zA-Z0-9[-_.+!%*`'~]]+))(;lr)?";

	/**
	 * to represent the regular expression for reffered-by header
	 */
	public static final String REFFERED_BY_REGX = "(sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+))[\\@]([^@]([a-zA-Z0-9[-_.+!%*`'~]]+))(;cid=([a-zA-Z0-9[-.+~!@#%$^*](\")]+?))?";

	/**
	 * to represent the regular expression for refer-to header
	 */
	public static final String REFFER_TO_REGX = "(sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+))[\\@]([^@]([a-zA-Z0-9[-_.+!%*`'~]]+))(;method=(REGISTER|INVITE|ACK|REFER|BYE|CANCEL|SUBSCRIBE|NOTIFY))?(;isfocus)?";

	/**
	 * to represent the regular expression for reply-to header
	 */
	public static final String REPLY_TO_REGX = "(sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+))[\\@]([^@]([a-zA-Z0-9[-_.+!%*`'~]]+))";

	/**
	 * to represent the regular expression forroute header
	 */
	public static final String ROUTE_REGX = "(sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+))[\\@]?([^@]([a-zA-Z0-9[-_.+!%*`'~]]+))(;lr)?";

	/**
	 * to represent the regular expression for RSEQ header
	 */
	public static final String RSEQ_REGX = "([0-9]++)$";

	/**
	 * to represent the regular expression for service-route header
	 */
	public static final String SERVICE_ROUTE_REGX = "(sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+))[\\@]?([^@]([a-zA-Z0-9[-_.+!%*`'~]]+))(;lr)?";

	/**
	 * to represent the regular expression for session expires header
	 */
	public static final String SESSION_EXPIRES_REGX = "([\\d]+?)";

	/**
	 * to represent the regular expression for timestamp header
	 */
	public static final String TIMESTAMP_REGX = "([\\d]+?)";

	/**
	 * to represent the regular expression for to header
	 */
	public static final String TO_REGX = "(((((sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+)*?)|((((([\\+]\\d{2,})(-?\\d{3})-?)?(\\d{3})(-?\\d{1,}))|((((\\d{2,})?(-?\\d{3})-?)?(\\d{3})(-?\\d{1,}))(;phone-context=(([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,})|(((\\+?\\d{2})?(-?\\d{3})-?)?(\\d{3})?(-?\\d{1,}?)))))(;isub=(\\w\\W)*)?(;ext=((\\d{3,4})|(\\(\\d{3,4}\\))|(\\-\\d{3,4})|(\\.\\d{3,4})))?(;npdi)?(;enumdi)?(;tgrp=(([a-zA-Z0-9-\\s]+)*?))?(;trunk-context=((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?))?))([\\:]([a-zA-Z0-9[-_.+!%*`'~]]+))?[\\@])?)(((?:[^@][a-zA-Z0-9-]+\\.)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)|((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?))((;transport=(udp|tcp|sctp|tls|tls-sctp))?(;user=(phone|ip|dialstring))?(;method=(ACK|BYE|CANCEL|INFO|INVITE|MESSAGE|NOTIFY|OPTIONS|PRACK|PUBLISH|REFER|REGISTER|SUBSCRIBE|UPDATE))?(;ttl=(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))?(;maddr=(([a-zA-Z0-9_]+)|((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)|((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\")))?(;lr)?(;comp=sigcomp)?(;target=([a-zA-Z0-9[-_.+!%*`'~][\\s][/x21-x7E][/xC0-xDF][/xE0-xEF][/xF0-xF7][/xF8-xFB][/xFC-xFD][/x80-xBF]]{1}))?(;cause=(100|180|181|182|183|200|202|300|301|302|305|380|400|401|402|403|404|405|406|407|408|410|412|413|414|415|416|417|420|421|422|423|428|429|433|436|437|438|470|480|481|482|483|484|485|486|487|488|489|491|493|494|500|501|502|503|504|505|513|580|600|603|604|606))?(;orig)?)(;gr=([a-zA-Z0-9[-_.+!%*`'~][\\s][/x21-x7E][/xC0-xDF][/xE0-xEF][/xF0-xF7][/xF8-xFB][/xFC-xFD][/x80-xBF]]{1}))?)";

	// "(sip:|sips:)(([a-zA-Z0-9[-_.+!%*`'~]]+))[\\@]([^@]([a-zA-Z0-9[-_.+!%*`'~]]+))";

	/**
	 * To represent the regular expression for date header
	 */
	public static final String DATE_REGX = "(Mon|Tue|Wed|Thu|Fri|Sat|Sun)[\\,][\\s]((\\d{2})[\\s](Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[\\s](\\d{4}))[\\s]([0-2][0-3]([\\:][0-5][0-9]){2})[\\s](GMT)";

	/**
	 * to represent the regular expression for warning
	 */
	public static final String WARNING = "(([1-6][\\d]{1}[\\d]{1}[\\s])((((?:[^@][a-zA-Z0-9-]+\\.)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)|((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?))|([a-zA-Z0-9[-_.+!%*`'~]]+))[\\s]((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\"))([\\,][\\s]?(([1-6][\\d]{1}[\\d]{1}[\\s])((((?:[^@][a-zA-Z0-9-]+\\.)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)|((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?))|([a-zA-Z0-9[-_.+!%*`'~]]+))[\\s]((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\")))*?";

	/**
	 * to represent the regular expression for reason
	 */
	public static final String REASON_REGX = "((SIP|Q.850|Preemption|([a-zA-Z0-9[-_.+!%*`'~]]+))([\\;][\\s]?((cause=(\\d{1,}))|(text=((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\"))|(([a-zA-Z0-9[-_.+!%*`'~]]+?)(=(((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\")|(([a-zA-Z0-9_]+)|((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)|((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\"))|((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\")))?)))*?)([\\,][\\s]?((SIP|Q.850|Preemption|([a-zA-Z0-9[-_.+!%*`'~]]+))([\\;][\\s]?((cause=(\\d{1,}))|(text=((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\"))|(([a-zA-Z0-9[-_.+!%*`'~]]+?)(=(((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\")|(([a-zA-Z0-9_]+)|((?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?)|((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\"))|((\\\"[a-zA-Z0-9[-_+!%*`'~][\\.][\\s]]+?)\\\")))?)))*?))*?";

	/**
	 * To represent the regular expression for date header
	 */
	public static final String JOIN_REGX = "(Mon|Tue|Wed|Thu|Fri|Sat|Sun)[\\,][\\s]((\\d{2})[\\s](Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[\\s](\\d{4}))[\\s]([0-2][0-3]([\\:][0-5][0-9]){2})[\\s](GMT)";

	/**
	 * To represent the regular expression for via header
	 */
	public static final String VIA_REGX = "(SIP/2.0/(UDP|TCP)){1}[\\s](?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)+(?:[a-zA-Z]){2,}\\.?)([\\:](\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5]))?(;comp=sigcomp)?+((;branch=z9hG4bK)[a-zA-Z0-9[-_.+!%*`'~]]{1,}+)?";

}
