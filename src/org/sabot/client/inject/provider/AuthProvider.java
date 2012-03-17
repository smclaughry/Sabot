package org.sabot.client.inject.provider;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.inject.Provider;

public class AuthProvider implements Provider<Auth>{

	private final Auth auth;

	public AuthProvider(){
		auth = Auth.get();
	}
	
	@Override
	public Auth get() {
		return auth;
	}
}
