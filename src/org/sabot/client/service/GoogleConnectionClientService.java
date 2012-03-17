package org.sabot.client.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.sabot.client.facebook.GoogleUser;
import org.sabot.client.service.oauth.JSOCallback;
import org.sabot.client.service.oauth.RemoteUser;

import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.api.gwt.oauth2.client.SabotAuth;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class GoogleConnectionClientService extends AbstractConnectionClientService {
	//FIXME looked into this, but it doesn't seem useful.  Might have to revisit: http://gwt-gdata.appspot.com/samples/v2.2.1/HelloGData.html/
	private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	private static final String IDENTITY_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
//	private static final String CONTACTS_URL = "https://www.google.com/m8/feeds/contacts/default/full?alt=json-in-script&start-index=7&max-results=1";
	private static final String CONTACTS_URL = "https://www.google.com/m8/feeds/contacts/default/full?alt=json-in-script&max-results=300";
	// fields=link,entry(@gd:etag,id,updated,link[@rel='edit']))
	// OR
	//https://www-opensocial.googleusercontent.com/api/people/@me/@self
	//"https://www-opensocial.googleusercontent.com/api/people/@me/@all";
	
	public enum GoogleScopes implements Scope{
		EMAIL("https://www.googleapis.com/auth/userinfo.email"),
		PROFILE("https://www.googleapis.com/auth/userinfo.profile"),
		CONTACTS("https://www.google.com/m8/feeds"),
		GPLUS_ME("https://www.googleapis.com/auth/plus.me");
		
		private final String key;

		private GoogleScopes(String key) {
			this.key = key;
		}
		
		@Override
		public String getKey() {
			return key;
		}
	}

	private EnvironmentConfiguration environmentConfiguration;

	@Inject
	public GoogleConnectionClientService(SabotAuth auth,
										   EnvironmentConfiguration environmentConfiguration) {
		super(auth);
		this.environmentConfiguration = environmentConfiguration;
	}

	@Override
	protected AuthRequest getAuthRequest(Scope... scopes) {
		String[] scopeKeys = new String[scopes.length];
		for(int x = 0; x < scopes.length; x++) {
			scopeKeys[x] = scopes[x].getKey();
		}
		return new AuthRequest(AUTH_URL, environmentConfiguration.getGoogleAppId()).withScopes(scopeKeys);
	}

	@Override
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void login(Callback<RemoteUser, Throwable> handler, boolean okToPopup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchUserInfo(final Callback<RemoteUser, String> callback) {
		JSOCallback jsoCallback = new JSOCallback(){
			@Override
			public void onSuccess(JavaScriptObject jso) {
				if (null == jso) {
					callback.onFailure("Null response");
				} else {
					try {
						GoogleUser user = jso.cast();
//						if (null == user.getError()) { // FIXME Need to figure out how Google reports errors
							callback.onSuccess(user.asRemoteUser());
//						} else {
//							callback.onFailure(user.getError());
//						}
					} catch (Exception e) {
						callback.onFailure("Failed to cast: "+jso.toSource());
					}
				}
			}

			@Override
			public void onFailure(String message) {
				callback.onFailure(message);
			}
		};
		processCall(IDENTITY_URL, jsoCallback, GoogleScopes.PROFILE); // FIXME possibly EMAIL?
	}

	public void fetchContacts(final Callback<List<GoogleContact>, String> callback) {
		JSOCallback jsoCallback = new JSOCallback() {
			@Override
			public void onSuccess(JavaScriptObject jso) {
				if (null == jso) {
					callback.onFailure("Null response");
				} else {
					try {
						GoogleContactContainer container = jso.cast();
						JsArray<GoogleContact> datums = container.getFeedEntryArray();
						List<GoogleContact> retval = new ArrayList<GoogleContact>();
						for (int x=0;x<datums.length();x++) {
							GoogleContact datum = datums.get(x);
							retval.add(datum);
						}
						if (!retval.isEmpty()) {
							callback.onSuccess(retval);
						} else {
							callback.onFailure("No contacts retrieved");
						}
					} catch (Exception e) {
						callback.onFailure("Failed to cast: "+ e.getClass().getName() +" : "+e.getMessage());
					}
				}
			}
			
			@Override
			public void onFailure(String message) {
				callback.onFailure(message);
			}
		};
		processCall(CONTACTS_URL, jsoCallback, GoogleScopes.CONTACTS);
	}
}
