package org.sabot.client.service;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class GoogleContactContainer extends JavaScriptObject {
	protected GoogleContactContainer() {}

	public final native JsArray<GoogleContact> getFeedEntryArray()  /*-{
		if (null == this.feed) {
			return new Array();
		} else {
			return this.feed.entry;
		}
	}-*/;
}
