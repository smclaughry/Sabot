package org.sabot.client.service;

import javax.inject.Singleton;

import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;

@Singleton
public class ReloadService {
	private boolean reloading = false;
	public void forceReload() {
		if (!reloading) {
			reloading = true;
			UrlBuilder builder = Window.Location.createUrlBuilder();
			builder.setParameter("t", String.valueOf(System.currentTimeMillis()));
			String url = builder.buildString();
			Window.Location.assign(url);
		}
	}
}
