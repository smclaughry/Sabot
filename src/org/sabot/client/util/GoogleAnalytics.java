package org.sabot.client.util;

import javax.inject.Inject;

import org.sabot.client.service.EnvironmentConfiguration;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Window;

/*
 * Library Sabot: a library for accelerating GWT and AppEngine development
 * 
 * Copyright (C) 2011  Phil Craven, Stephen McLaughry
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
public class GoogleAnalytics {
	private final String server;
	private final NativeMethods nativeMethods;
	private final EnvironmentConfiguration environmentConfiguration;

	public static class EventTracker{
		private final String category;
		private String action;
		private String description;
		private final long startMillis;
		
		public EventTracker(String category, String action){
			this(category, action, "");
		}
		
		public EventTracker(String category, String action, String description){
			this.category = category;
			this.action = action;
			this.description = description;
			this.startMillis = System.currentTimeMillis();
		}
		
		void trackEvent(GoogleAnalytics googleAnalytics, long endMillis){
			int elapsedTime = (int) (endMillis - startMillis);
			googleAnalytics.trackEvent(category, action, description, elapsedTime);
		}

		public void indicateFailure() {
			action = action + " - FAILURE";
		}
		
		public void setDescription(String description) {
			if (null == description) {
				this.description = "";
			} else {
				this.description = description;
			}
		}
	}
	
	@Inject
	public GoogleAnalytics(NativeMethods nativeMethods,
						   EnvironmentConfiguration environmentConfiguration){
		this.nativeMethods = nativeMethods;
		this.environmentConfiguration = environmentConfiguration;		
		this.server = Window.Location.getHostName();
	}

	public void trackAreaRequest(final String areaUrl) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				String replace = areaUrl.replaceFirst("~", "?").replaceAll("~", "&");
				nativeMethods.trackAreaRequest(server, environmentConfiguration.getGoogleAnalyticsSiteTrackerCode(), replace);
			}
		});
	}
	
	public void trackEvent(final EventTracker eventTracker){
		final long endMillis = System.currentTimeMillis();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				eventTracker.trackEvent(GoogleAnalytics.this, endMillis);
			}
		});
	}
	
	void trackEvent(final String category, final String action, final String description, final int additionalValue){
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				nativeMethods.trackEvent(server, environmentConfiguration.getGoogleAnalyticsSiteTrackerCode(), category, action, description, additionalValue);
			}
		});
	}
	
	public static class NativeMethods {
		native void trackAreaRequest(String server, String trackerCode, String areaUrl) /*-{
			try {
				$wnd.pageTracker = $wnd._gat._getTracker(trackerCode);
				$wnd.pageTracker._setDomainName(server);
				$wnd.pageTracker._trackPageview(areaUrl);
			} catch(err) {$doc.title = err;}
		}-*/;
		
		native void trackEvent(String server, String trackerCode, String category, String action) /*-{
			try {
				if ($wnd.pageTracker == null) {
					$wnd.pageTracker = $wnd._gat._getTracker(trackerCode);
					$wnd.pageTracker._setDomainName(server);
					$wnd.pageTracker._trackPageview();
				}
				$wnd.pageTracker._trackEvent(category, action);
			} catch(err) {$doc.title = err;}
		}-*/;
	
		native void trackEvent(String server, String trackerCode, String category, String action, String description) /*-{
			try {
				if ($wnd.pageTracker == null) {
					$wnd.pageTracker = $wnd._gat._getTracker(trackerCode);
					$wnd.pageTracker._setDomainName(server);
					$wnd.pageTracker._trackPageview();
				}
				$wnd.pageTracker._trackEvent(category, action, description);
			} catch(err) {$doc.title = err;}
		}-*/;
		
		native void trackEvent(String server, String trackerCode, String category, String action, String description, int additionalValue) /*-{
			try {
				if ($wnd.pageTracker == null) {
					$wnd.pageTracker = $wnd._gat._getTracker(trackerCode);
					$wnd.pageTracker._setDomainName(server);
					$wnd.pageTracker._trackPageview();
				}
				$wnd.pageTracker._trackEvent(category, action, description, additionalValue);
			} catch(err) {$doc.title = err;}
		}-*/;
	}
}
