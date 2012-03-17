package org.sabot.server.utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlUtility {
	private UrlUtility() {
		// Damn kids, stay off my lawn!
	}
	
	public static String encode(String text) {
		try {
			if (null == text) {
				return "";
			} else {
				return URLEncoder.encode(text, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			// If UTF-8 is wrong, I don't want to be right.
			return text;
		}
	}
}
