package org.sabot.client.service.oauth;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JavaScriptObject;

public abstract class JSOCallback implements Callback<JavaScriptObject,String> {
	public abstract void onSuccess(JavaScriptObject jso);
	public abstract void onFailure(String message);
}