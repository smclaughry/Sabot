package org.sabot.client.service.oauth;

import org.sabot.client.service.EnvironmentConfiguration;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.core.client.Callback;

public interface OauthAuthorizer {

	AuthRequest getAuthRequest(Auth auth, EnvironmentConfiguration environmentConfiguration);

	void fetchUserInfo(String token, Callback<RemoteUser, String> callback);

}
