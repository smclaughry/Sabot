package org.sabot.client.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.sabot.client.facebook.DataListJso;
import org.sabot.client.facebook.FBAlbum;
import org.sabot.client.facebook.FBFriend;
import org.sabot.client.facebook.FBPhoto;
import org.sabot.client.facebook.FBUser;
import org.sabot.client.service.oauth.JSOCallback;
import org.sabot.client.service.oauth.RemoteUser;

import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.api.gwt.oauth2.client.SabotAuth;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

@Singleton
public class FacebookConnectionClientService extends AbstractConnectionClientService {
	
	private static final String FACEBOOK_GRAPH = "https://graph.facebook.com";
	private static final String FACEBOOK_AUTH_URL = "https://www.facebook.com/dialog/oauth";
	private static final String FACEBOOK_IDENTITY_URL = FACEBOOK_GRAPH + "/me";
	private static final String FACEBOOK_ALBUM_URL = FACEBOOK_IDENTITY_URL + "/albums";

	public enum FacebookScopes implements Scope{
		EMAIL("email"),
		PUBLISH("publish_stream"),
		PHOTOS("user_photos"); //I hardly know her!!!
		
		private final String key;

		private FacebookScopes(String key) {
			this.key = key;
			
		}
		
		@Override
		public String getKey() {
			return key;
		}
	}
	
	private final EnvironmentConfiguration environmentConfiguration;

	@Inject
	public FacebookConnectionClientService(SabotAuth auth,
										   EnvironmentConfiguration environmentConfiguration) {
		super(auth);
		this.environmentConfiguration = environmentConfiguration;
	}
	
	@Override
	public void login(Callback<RemoteUser, Throwable> handler, boolean okToPopup) {
		login(true, handler, okToPopup);	
	}
	
	private void login(final boolean retry, final Callback<RemoteUser, Throwable> handler, final boolean okToPopup) {
		AuthRequest authRequest = getAuthRequest(FacebookScopes.EMAIL);
		String token = auth.getAccessToken(authRequest);
		if (null != token) {
			fetchUserInfo(retry, handler, okToPopup);
		} else if(okToPopup){
			auth.login(authRequest, new Callback<String, Throwable>() {
				@Override
				public void onSuccess(String token) {
					fetchUserInfo(retry, handler, okToPopup);
				}
	
				@Override
				public void onFailure(Throwable caught) {
					handler.onFailure(caught);
				}
			});
		}else {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
		        @Override
		        public void execute() {
		        	handler.onFailure(new PopupRequiredException());
		        }
		    });
		}
	}
	
	private void fetchUserInfo(final boolean retry, final Callback<RemoteUser, Throwable> handler, final boolean okToPopup) {
		fetchUserInfo(new Callback<RemoteUser,String>() {
			@Override
			public void onFailure(String reason) {
				GWT.log("Failed: "+reason);
				if (retry) { // Looks like we weren't really logged in after all
					auth.clearAllTokens();
					login(false, handler, okToPopup);
				}
			}

			@Override
			public void onSuccess(RemoteUser remoteUser) {
				handler.onSuccess(remoteUser);
			}});
	}
	
	@Override
	public boolean isConnected() {
		return auth.expiresIn(getAuthRequest(FacebookScopes.EMAIL)) > (Double.NEGATIVE_INFINITY + 10);
	}
	
	@Override 
	public boolean isLoggedIn() {
		return auth.expiresIn(getAuthRequest(FacebookScopes.EMAIL)) > 0;
	}
	
	@Override
	protected AuthRequest getAuthRequest(Scope... scopes) {
		String[] scopeKeys = new String[scopes.length];
		for(int x = 0; x < scopes.length; x++) {
			scopeKeys[x] = scopes[x].getKey();
		}
		return new AuthRequest(FACEBOOK_AUTH_URL, environmentConfiguration.getFacebookAppId()).withScopes(scopeKeys).withScopeDelimiter(",");
	}
	
	@Override
	public void fetchUserInfo(final Callback<RemoteUser,String> callback) {
		JSOCallback jsoCallback = new JSOCallback(){
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
			}
		};
		processCall(FACEBOOK_IDENTITY_URL, jsoCallback, FacebookScopes.EMAIL);
	}
	
	public void fetchPhotoAlbums(final Callback<List<FBAlbum>,String> callback) {
		JSOCallback jsoCallback = new JSOCallback(){
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
			}};
		
		processCall(FACEBOOK_ALBUM_URL, jsoCallback, FacebookScopes.PHOTOS);		
	}
	
	public void fetchPhotos(String albumId, final Callback<List<FBPhoto>,String> callback) {
		JSOCallback jsoCallback = new JSOCallback(){
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
			}
		};		
		processCall(FACEBOOK_GRAPH + "/" +albumId+"/photos", jsoCallback, FacebookScopes.PHOTOS);
	}
	
	public void fetchFriends(final Callback<List<FBFriend>,String> callback) {
		JSOCallback jsoCallback = new JSOCallback(){
			@Override
			public void onSuccess(JavaScriptObject jso) {
				if (null == jso) {
					callback.onFailure("Null response");
				} else {
					try {
						DataListJso dataList = jso.cast();
                        JsArray<JavaScriptObject> data = dataList.getData();
                        List<FBFriend> retval = new ArrayList<FBFriend>();
                        for (int x=0; x<data.length(); x++) {
                                FBFriend friendJso = data.get(x).cast();
                                retval.add(friendJso);
//                                friendFacebookUids.add(new Pair<String,String>(friendJso.getId(), friendJso.getName()));
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
			}
		};		
		processCall(FACEBOOK_IDENTITY_URL + "/friends", jsoCallback, FacebookScopes.EMAIL); // FIXME don't really need EMAIL scope, but not sure that it works with no scope
	}
	
	public String getAuthorizedPictureUrl(String facebookId, Scope... scopes) {
		String token = auth.getAccessToken(getAuthRequest(scopes));
		if (null != token) {
			return getPictureUrl(facebookId)+"?access_token="+token;
		} else {
			return getPictureUrl(facebookId);
		}
	}

	public String getUrl(String facebookId, Scope... scopes) {
		String token = auth.getAccessToken(getAuthRequest(scopes));
		if (null != token) {
			return "https://graph.facebook.com/"+facebookId+"?access_token="+token;
		} else {
			return "https://graph.facebook.com/"+facebookId;
		}
	}
	
	public static String getPictureUrl(String facebookId) {
		return "https://graph.facebook.com/"+facebookId+"/picture";
	}

	public static String getProfileUrl(String facebookId) {
		return "https://www.facebook.com/profile.php?id=" + facebookId;
	}
}
