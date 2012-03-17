package org.sabot.client.service.oauth;

import org.sabot.client.facebook.GoogleUser;
import org.sabot.client.service.EnvironmentConfiguration;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JavaScriptObject;

public class GoogleAuthorizer implements OauthAuthorizer {
	private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/auth";

	// The auth scope being requested. This scope will allow the application to
	// read Buzz activities, comments, etc., as if it was the user.
	//	private static final String GOOGLE_PLUS_ME = "https://www.googleapis.com/auth/plus.me";
	private static final String USERINFO_EMAIL = "https://www.googleapis.com/auth/userinfo.email";
	private static final String USERINFO_PROFILE = "https://www.googleapis.com/auth/userinfo.profile";
		
	@Override
	public AuthRequest getAuthRequest(Auth auth, EnvironmentConfiguration environmentConfiguration) {
		return new AuthRequest(AUTH_URL, environmentConfiguration.getGoogleAppId()).withScopes(USERINFO_EMAIL,USERINFO_PROFILE);
	}

	@Override
	public void fetchUserInfo(String token, final Callback<RemoteUser, String> callback) {
		String url = "https://www.googleapis.com/oauth2/v1/userinfo?access_token="+token;
		JsonUtility.getJson(url, new JSOCallback(){
			@Override
			public void onSuccess(JavaScriptObject jso) {
				try {
					GoogleUser user = jso.cast();
					callback.onSuccess(user.asRemoteUser());
				} catch (Exception e) {
					callback.onFailure("Failed to cast: "+jso.toSource());
				}
			}

			@Override
			public void onFailure(String message) {
				callback.onFailure(message);
			}});
	}


}
