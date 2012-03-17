package org.sabot.client.service;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class GoogleContact extends JavaScriptObject {
	protected GoogleContact() {}

	public final native JsArray<GoogleEmail> getEmailAddresses() /*-{
		return this.gd$email;
	}-*/;

	public final native String getTitle() /*-{
		if (null == this.title) {
			return null;
		} else {
			return this.title.$t;
		}
	}-*/; 
}
