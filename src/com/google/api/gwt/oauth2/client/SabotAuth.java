package com.google.api.gwt.oauth2.client;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.sabot.client.util.GoogleAnalytics;
import org.sabot.client.util.GoogleAnalytics.EventTracker;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.storage.client.Storage;

@Singleton
public class SabotAuth extends Auth{
	  static final AuthImpl INSTANCE = new AuthImpl();

	  private Window window;
	  private static final String DEFAULT_WINDOW_OPTIONS = "width=980,height=600"; 
	  private String windowOptions = DEFAULT_WINDOW_OPTIONS;

	  private final GoogleAnalytics googleAnalytics;

	  @Inject
	  public SabotAuth(GoogleAnalytics googleAnalytics) {
	    super(getTokenStore(), new RealClock(), new RealUrlCodex(), Scheduler.get(),
	    // Default to use the bundled oauthWindow.html
	        GWT.getModuleBaseURL() + "oauthWindow.html");
		this.googleAnalytics = googleAnalytics;
	    register();
	  }
	  
	  @Override
	  public void login(AuthRequest req, final Callback<String, Throwable> callback) {
		  EventTracker eventTracker = new EventTracker(this.getClass().getName(), "OauthLogin");
		  googleAnalytics.trackEvent(eventTracker);
		  super.login(req, wrap(callback));
	  }

	  private Callback<String, Throwable> wrap(final Callback<String, Throwable> callback) {
		final EventTracker returnTracker = new EventTracker(this.getClass().getName(), "OauthLoginReturn");
		return new Callback<String, Throwable>() {
			@Override
			public void onFailure(Throwable reason) {
				callback.onFailure(reason);
			}

			@Override
			public void onSuccess(String result) {
				googleAnalytics.trackEvent(returnTracker);
				callback.onSuccess(result);
			}
		};
	}

	/**
	   * Returns the correct {@link TokenStore} implementation to use based on
	   * browser support for localStorage.
	   */
	  // TODO(jasonhall): This will not result in CookieStoreImpl being compiled out
	  // for browsers that support localStorage, and vice versa? If not, this should
	  // be a deferred binding rule.
	  private static TokenStoreImpl getTokenStore() {
	    return Storage.isLocalStorageSupported() ? new TokenStoreImpl() : new CookieStoreImpl();
	  }

	  /**
	   * Register a global function to receive auth responses from the popup window.
	   */
	  private native void register() /*-{
	    var self = this;
	    if (!$wnd.oauth2) {
	      $wnd.oauth2 = {};
	    }
	    $wnd.oauth2.__doLogin = $entry(function(hash) {
	      self.@com.google.api.gwt.oauth2.client.Auth::finish(Ljava/lang/String;)(hash);
	    });
	  }-*/;

	  /**
	   * Get the OAuth 2.0 token for which this application may not have already
	   * been granted access, by displaying a popup to the user.
	   */
	  @Override
	  void doLogin(String authUrl, Callback<String, Throwable> callback) {
	    if (window != null && window.isOpen()) {
	      callback.onFailure(new IllegalStateException("Authentication in progress"));
	    } else {
	      window = openWindow(authUrl, getWindowOptions());
//	      window.opener().focus(); // FIXME trying to figure out a way to keep the window hidden unless it's actually needed
//	      window.blur();
	      if (window == null || !window.isOpen()) {
	        callback.onFailure(new RuntimeException(
	            "The authentication popup window appears to have been blocked"));
	      }
	    }
	  }

	  @Override
	  void finish(String hash) {
	    // Clean up the popup
	    if (window != null && window.isOpen()) {
	      window.close();
	    }
	    super.finish(hash);
	  }

	  // Because GWT's Window.open() method does not return a reference to the
	  // newly-opened window, we have to manage this all ourselves manually...
	  private static native Window openWindow(String url, String windowOptions) /*-{
	    return $wnd.open(url, 'popupWindow', windowOptions);
	  }-*/;

	  static final class Window extends JavaScriptObject {
	    protected Window() {
	    }

		native boolean isOpen() /*-{
	      return !this.closed;
	    }-*/;

	    native void close() /*-{
	      this.close();
	    }-*/;
	    
	    native void focus() /*-{
    		this.focus();
    	}-*/;

	    native void blur() /*-{
	    	this.blur();
	    }-*/;
	    
	    native Window opener() /*-{
			return this.opener;
		}-*/;
	  }

	  /** Real GWT implementation of Clock. */
	  private static class RealClock implements Clock {
	    @Override
	    public double now() {
	      return Duration.currentTimeMillis();
	    }
	  }

	  /** Real GWT implementation of UrlCodex. */
	  private static class RealUrlCodex implements UrlCodex {
	    @Override
	    public native String encode(String url) /*-{
	      var regexp = /%20/g;
	      return encodeURIComponent(url).replace(regexp, "+");
	    }-*/;

	    @Override
	    public native String decode(String url) /*-{
	      var regexp = /\+/g;
	      return decodeURIComponent(url.replace(regexp, "%20"));
	    }-*/;

	  }
	  	  
	  public boolean hasToken(AuthRequest req) {
		    return expiresIn(req) > Double.NEGATIVE_INFINITY + 10;
	  }
	  
	  public void setWindowOptions(String windowOptions) {
		  this.windowOptions = windowOptions;
	  }
	  
	  private String getWindowOptions() {
		  String retval = this.windowOptions;
		  this.windowOptions = DEFAULT_WINDOW_OPTIONS;
		  return retval;
	  }
	  
	  public String getAccessToken(AuthRequest req) {
		  TokenInfo info = getToken(req);
		  if (info == null || info.expires == null || expiringSoon(info)) {
			  return null;
		  } else {
			  return info.accessToken;
		  }
	  }
}
