package org.sabot.client.service;

import org.sabot.client.service.oauth.JSOCallback;
import org.sabot.client.service.oauth.JsonUtility;

import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.api.gwt.oauth2.client.SabotAuth;
import com.google.gwt.core.client.Callback;

public abstract class AbstractConnectionClientService implements ConnectionClientService {
	protected final SabotAuth auth;

	protected AbstractConnectionClientService(SabotAuth auth) {
		this.auth = auth;
	}

	@Override
	public void ensureScopePermissions(final Callback<String, Throwable> callback,  Scope... scopes) {
		String token = auth.getAccessToken(getAuthRequest(scopes));
		if(token == null) {
			AuthRequest authRequest = getAuthRequest(scopes);
			auth.login(authRequest, callback);
		}else {
			callback.onSuccess(token);
		}
	}

	protected void processCall(final String baseUrl, final JSOCallback jsoCallback, Scope... scopes) {
		ensureScopePermissions(new Callback<String, Throwable>(){
			@Override
			public void onFailure(Throwable reason) {
				jsoCallback.onFailure("Permission not granted " + reason.getMessage());
			}

			@Override
			public void onSuccess(String token) {
				String delimiterIJustMetHer = baseUrl.contains("?") ? "&" : "?";
				String url = baseUrl + delimiterIJustMetHer + "access_token="+token;
				JsonUtility.getJson(url, jsoCallback);
			}
			
		}, scopes);
	}
	
	
	abstract protected AuthRequest getAuthRequest(Scope... scopes);
}
