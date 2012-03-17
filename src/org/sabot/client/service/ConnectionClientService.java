package org.sabot.client.service;

import org.sabot.client.service.oauth.RemoteUser;

import com.google.gwt.core.client.Callback;

public interface ConnectionClientService {
	boolean isLoggedIn();
	boolean isConnected();
	void login(Callback<RemoteUser, Throwable> handler, boolean okToPopup);
	void fetchUserInfo(Callback<RemoteUser, String> callback);
	void ensureScopePermissions(Callback<String, Throwable> callback, Scope... scopes);
}
