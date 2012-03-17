package org.sabot.client.twitter;

/*
 Copyright (c) 2010, David Nelson

 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, are permitted provided 
 that the following conditions are met:

 Redistributions of source code must retain the above copyright notice, this list of conditions and the 
 following disclaimer.
 Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the 
 following disclaimer in the documentation and/or other materials provided with the distribution.
 Neither the name of the Infinite Power Publishing nor the names of its contributors may be used to endorse 
 or promote products derived from this software without specific prior written permission.
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED 
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
 PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR 
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 POSSIBILITY OF SUCH DAMAGE.
 */
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;


// Twitter stuff:
// https://dev.twitter.com/docs/anywhere/welcome
// (probably have to do this on client side, but I'm putting the link here just because)
public class TwitterAnywhere {
	private static AsyncCallback<UserInfo> getUserInfoCallback = null;

	public TwitterAnywhere() {
	}

	public void LoginWithTwitter() {
		LoginWithTwitterNative();
	}

	public void Hovercards(Widget widget, boolean linkify, boolean expandedByDefault) {
		HovercardsNative(addUniqueIdToWidgetAndReturnId(widget), linkify, expandedByDefault);
	}
	
	public static void setup(AsyncCallback<UserInfo> callback) {
		getUserInfoCallback = callback;
		registerParseUserInfo();
	}
	
	public void beginGetCurrentUserInfoObject(AsyncCallback<UserInfo> callback) {
		getUserInfoCallback = callback;

		try {
			// kick off the async processing
			getUserInfoNative();
		} catch (Exception ex) {
			callback.onFailure(ex);
		}
	}

	public static native void registerParseUserInfo() /*-{
		$wnd.parseUserInfo = $entry(@org.sabot.client.twitter.TwitterAnywhere::parseUserInfo(Ljava/lang/String;));
	}-*/;

	public static void parseUserInfo(String json) {
		JSONValue jsonValue = JSONParser.parseLenient(json);

		// screen_name, name, profile_image_url, friends_count, followers_count,
		// description, url, id, statuses_count, following
		getUserInfoCallback.onSuccess(new UserInfo(getJsonValue(jsonValue, "screen_name"), 
												   getJsonValue(jsonValue, "name"), 
												   getJsonValue(jsonValue, "profile_image_url"),
												   getJsonValue(jsonValue, "friends_count"), 
												   getJsonValue(jsonValue, "followers_count"), 
												   getJsonValue(jsonValue, "description"),
												   getJsonValue(jsonValue, "url"), 
												   getJsonValue(jsonValue, "id"), 
												   getJsonValue(jsonValue, "statuses_count")));
	}

	private static String getJsonValue(JSONValue jsonValue, String propertyName) {
		return jsonValue.isObject().get(propertyName).isString().stringValue();
	}

	private static native void HovercardsNative(String domElementId, boolean linkify, boolean expandedByDefault) /*-{
		$wnd.twttr.anywhere(function(T) {
			T("#" + domElementId).hovercards({
				linkify : linkify,
				expanded : expandedByDefault
			});
		});
	}-*/;
	
	private static native void LoginWithTwitterNative() /*-{
		$wnd.twttr.anywhere(function (T) {
     		T.bind("authComplete", function (e, user) {       // triggered when auth completed successfully
					alert('auth complete : '+user);
					alert('auth complete : '+user.data('screen_name'));
					
					var json = "{ " +
					"'screen_name': '" + user.data('screen_name') + "', " +
					"'name': '" + user.data('name') + "', " +
					"'profile_image_url': '" + user.data('profile_image_url')
							+ "', " +
							"'friends_count': '" + user.data('friends_count')
							+ "', " +
							"'followers_count': '"
							+ user.data('followers_count') + "', " +
							"'description': '" + user.data('description')
							+ "', " +
							"'url': '" + user.data('url') + "', " +
							"'id': '" + user.data('id') + "', " +
							"'statuses_count': '"
							+ user.data('statuses_count') + "', " +
							"'following': '" + user.data('following') + "' " +
							"}";
					$wnd.parseUserInfo(json);
    		});
     		T.bind("signOut", function (e) {  // triggered when user logs out
     			alert('signout');
    		});
  		});		
		
		$wnd.twttr.anywhere(function(T) {
			T.signIn();
		});
	}-*/;

	private static native String getUserInfoNative() /*-{
		$wnd.twttr.anywhere(function(T) {
			$wnd.parseUserInfo("{ " +
			"'screen_name': '" + T.currentUser.data('screen_name') + "', " +
			"'name': '" + T.currentUser.data('name') + "', " +
			"'profile_image_url': '" + T.currentUser.data('profile_image_url')
					+ "', " +
					"'friends_count': '" + T.currentUser.data('friends_count')
					+ "', " +
					"'followers_count': '"
					+ T.currentUser.data('followers_count') + "', " +
					"'description': '" + T.currentUser.data('description')
					+ "', " +
					"'url': '" + T.currentUser.data('url') + "', " +
					"'id': '" + T.currentUser.data('id') + "', " +
					"'statuses_count': '"
					+ T.currentUser.data('statuses_count') + "', " +
					"'following': '" + T.currentUser.data('following') + "' " +
					"}");
		});
	}-*/;

	private String addUniqueIdToWidgetAndReturnId(Widget widget) {
		widget.getElement().setId(HTMLPanel.createUniqueId());
		return widget.getElement().getId();
	}
}
