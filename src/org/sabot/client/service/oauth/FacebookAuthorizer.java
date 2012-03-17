package org.sabot.client.service.oauth;

import java.util.ArrayList;
import java.util.List;

import org.sabot.client.facebook.DataListJso;
import org.sabot.client.facebook.FBAlbum;
import org.sabot.client.facebook.FBPhoto;
import org.sabot.client.facebook.FBUser;
import org.sabot.client.service.EnvironmentConfiguration;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class FacebookAuthorizer implements OauthAuthorizer {
	// All available scopes are listed here:
	// http://developers.facebook.com/docs/authentication/permissions/
	private static final String EMAIL_SCOPE = "email";
	private static final String PUBLISH_SCOPE = "publish_stream";
	private static final String PHOTOS_SCOPE = "user_photos";

	private static final String FACEBOOK_AUTH_URL = "https://www.facebook.com/dialog/oauth";

	@Override
	public AuthRequest getAuthRequest(Auth auth, EnvironmentConfiguration environmentConfiguration) {
		return new AuthRequest(FACEBOOK_AUTH_URL, environmentConfiguration.getFacebookAppId()).withScopes(EMAIL_SCOPE,PUBLISH_SCOPE,PHOTOS_SCOPE).withScopeDelimiter(",");		
	}

	@Override
	public void fetchUserInfo(String token, final Callback<RemoteUser,String> callback) {
		String url = "https://graph.facebook.com/me?access_token="+token;
		JsonUtility.getJson(url, new JSOCallback(){
			@Override
			public void onSuccess(JavaScriptObject jso) {
				if (null == jso) {
					callback.onFailure("Null response");
				} else {
					try {
						FBUser user = jso.cast();
						if (null == user.getError()) {
							callback.onSuccess(user.asRemoteUser());
						} else {
							callback.onFailure(user.getError());
						}
					} catch (Exception e) {
						callback.onFailure("Failed to cast: "+jso.toSource());
					}
				}
			}

			@Override
			public void onFailure(String message) {
				callback.onFailure(message);
			}});
	}

	public void fetchPhotoAlbums(String token, final Callback<List<FBAlbum>,String> callback) {
		String url = "https://graph.facebook.com/me/albums?access_token="+token;
		JsonUtility.getJson(url, new JSOCallback(){
			@Override
			public void onSuccess(JavaScriptObject jso) {
				if (null == jso) {
					callback.onFailure("Null response");
				} else {
					try {
						DataListJso dataList = jso.cast();
						JsArray<JavaScriptObject> datums = dataList.getData();
						List<FBAlbum> retval = new ArrayList<FBAlbum>();
						for (int x=0;x<datums.length();x++) {
							JavaScriptObject datum = datums.get(x);
							FBAlbum album = datum.cast();
							retval.add(album);
						}
						
						callback.onSuccess(retval);
					} catch (Exception e) {
						callback.onFailure("Failed to cast: "+jso.toSource());
					}
				}
			}

			@Override
			public void onFailure(String message) {
				callback.onFailure(message);
			}});
	}

	public void fetchPhotos(String token, String albumId, final Callback<List<FBPhoto>,String> callback) {
		String url = "https://graph.facebook.com/"+albumId+"/photos?access_token="+token;
		JsonUtility.getJson(url, new JSOCallback(){
			@Override
			public void onSuccess(JavaScriptObject jso) {
				if (null == jso) {
					callback.onFailure("Null response");
				} else {
					try {
						DataListJso dataList = jso.cast();
						JsArray<JavaScriptObject> datums = dataList.getData();
						List<FBPhoto> retval = new ArrayList<FBPhoto>();
						for (int x=0;x<datums.length();x++) {
							JavaScriptObject datum = datums.get(x);
							FBPhoto album = datum.cast();
							retval.add(album);
						}
						
						callback.onSuccess(retval);
					} catch (Exception e) {
						callback.onFailure("Failed to cast: "+jso.toSource());
					}
				}
			}

			@Override
			public void onFailure(String message) {
				callback.onFailure(message);
			}});
	}
}
