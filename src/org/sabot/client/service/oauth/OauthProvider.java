package org.sabot.client.service.oauth;

import org.sabot.client.service.EnvironmentConfiguration;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.core.client.Callback;

public enum OauthProvider {
	FACEBOOK(new FacebookAuthorizer()),
	GOOGLE(new GoogleAuthorizer());
	
	private final OauthAuthorizer authorizer;

	private OauthProvider(OauthAuthorizer authorizer) {
		this.authorizer = authorizer;
	}

	public AuthRequest getAuthRequest(Auth auth, EnvironmentConfiguration environmentConfiguration) {
		return authorizer.getAuthRequest(auth, environmentConfiguration);
	}

	public void fetchUserInfo(String token, Callback<RemoteUser, String> callback) {
		authorizer.fetchUserInfo(token, callback);
	}

	public void getToken(Auth auth, EnvironmentConfiguration environmentConfiguration, Callback<String, Throwable> callback) {
		AuthRequest req = getAuthRequest(auth, environmentConfiguration);
		if (isCurrent(auth, req)) {
			auth.login(req, callback);
		} else {
			callback.onFailure(new Exception("Not logged in"));
		}
	}

	private boolean isCurrent(Auth auth, AuthRequest req) {
//		return auth.expiresIn(req) > Double.NEGATIVE_INFINITY + 10;
		return (auth.expiresIn(req) > 0);
	}

	public boolean isLoggedIn(Auth auth, EnvironmentConfiguration environmentConfiguration) {
		AuthRequest req = getAuthRequest(auth, environmentConfiguration);
		return isCurrent(auth, req);
	}
}
