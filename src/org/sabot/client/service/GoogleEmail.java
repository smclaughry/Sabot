package org.sabot.client.service;

import com.google.gwt.core.client.JavaScriptObject;

public class GoogleEmail extends JavaScriptObject {
	protected GoogleEmail() {}
	
	public final boolean isPrimary() {
		return Boolean.parseBoolean(this.primary());
	}
	
	private final native String primary() /*-{
		return this.primary?this.primary:"false";
	}-*/;
	
	public final native String getAddress() /*-{
		return this.address; 
	}-*/;
}
