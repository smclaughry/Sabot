package org.sabot.client.util;

import java.util.Date;

public class DateTimeFormat {
	
	public String format(String format, Date date){
		return com.google.gwt.i18n.client.DateTimeFormat.getFormat(format).format(date);
	}
	
	public Date parse(String format, String date){
		return com.google.gwt.i18n.client.DateTimeFormat.getFormat(format).parse(date);
	}
}
