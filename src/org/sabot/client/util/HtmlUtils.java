package org.sabot.client.util;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class HtmlUtils {

	public static SafeHtml fromTrustedString(String summary) {
		if (null == summary) {
			return SafeHtmlUtils.fromTrustedString("");
		} else {
			return SafeHtmlUtils.fromTrustedString(summary);
		}
	}
	
	public static SafeHtml pIfy(String messageBody) {
		StringBuilder retval = new StringBuilder();
		if (null != messageBody) {
			for(String line : messageBody.split("\n")){
				if (line.trim().isEmpty()) {
					retval.append("<p>&nbsp;</p>");
				} else {
					retval.append("<p>").append(SafeHtmlUtils.htmlEscape(line)).append("</p>");
				}
			}
		}
		return SafeHtmlUtils.fromTrustedString(retval.toString());
	}

	public static SafeHtml brIfy(String text) {
		StringBuilder retval = new StringBuilder();
		for(String line : text.split("\n")){
			if (line.trim().isEmpty()) {
				retval.append("<br>");
			} else {
				retval.append(SafeHtmlUtils.htmlEscape(line)).append("<br>");
			}
		}
		return SafeHtmlUtils.fromTrustedString(retval.toString());
	}
}
