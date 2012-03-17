package org.sabot.server;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.sabot.shared.beans.SabotUser;

import com.google.inject.Provider;
import com.googlecode.objectify.Key;

public class SessionData <T extends SabotUser>{

	static final String SESSION_TICKLER = "session_tickler";
	static final String USER_KEY = "userKey";
	protected final Provider<HttpSession> sessionProvider;

	@Inject
	public SessionData(Provider<HttpSession> sessionProvider){
		this.sessionProvider = sessionProvider;
	}
	
	public void setUserKey(Key<T> userKey) {
		sessionProvider.get().setAttribute(USER_KEY, userKey);
	}

	@SuppressWarnings("unchecked")
	public Key<T> getUserKey() {
		return (Key<T>) sessionProvider.get().getAttribute(USER_KEY);
	}

	public void clear() {
		sessionProvider.get().invalidate();
	}

	public boolean isLoggedIn() {
		return sessionProvider.get().getAttribute(USER_KEY) != null;
	}
}
