package org.sabot.client.place;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

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
public class ActivityProxy<P extends Place, A extends Activity> implements Activity{

	public interface ActivityLoadCallback {
		<A extends Activity> void onSuccess(A activity);
		void onFailure(Throwable caught);
	}
	
	private final AsyncProvider<A> activityProvider;
	private A activity;
	private final ActivityLoadCallback loadCallback;
	private final P place;

	public ActivityProxy(AsyncProvider<A> activityProvider){
		this(activityProvider, null, null);
	}
	
	public ActivityProxy(AsyncProvider<A> activityProvider, P place){
		this(activityProvider, place, null);
	}
	
	public ActivityProxy(AsyncProvider<A> activityProvider, P place, ActivityLoadCallback loadCallback){
		this.activityProvider = activityProvider;
		this.place = place;
		this.loadCallback = loadCallback;
	}
	
	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		activityProvider.get(new AsyncCallback<A>() {
			@Override
			public void onFailure(Throwable caught) {
				if(loadCallback != null) {
					loadCallback.onFailure(caught);
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(A activity) {
				ActivityProxy.this.activity = activity;
				if(ActivityProxy.this.place != null && activity instanceof PlaceActivity){
					((PlaceActivity<P>) activity).setPlace(place);
				}
				activity.start(panel, eventBus);
				if(loadCallback != null) {
					loadCallback.onSuccess(activity);
				}
			}
		});
	}

	@Override
	public String mayStop() {
		return activity == null ? null : activity.mayStop();
	}

	@Override
	public void onCancel() {
		if(activity != null){
			activity.onCancel();
		}
	}

	@Override
	public void onStop() {
		if(activity != null){
			activity.onStop();
		}
	}
	
}