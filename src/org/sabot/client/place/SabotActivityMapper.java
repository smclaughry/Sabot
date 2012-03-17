package org.sabot.client.place;

import java.util.HashMap;
import java.util.Map;

import org.sabot.client.place.ActivityProxy.ActivityLoadCallback;
import org.sabot.client.place.activity.ActivityProxyProvider;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.place.shared.Place;

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
public class SabotActivityMapper implements ActivityMapper {
	private final Map<Class<? extends Place>, ActivityProxyProvider<? extends Place, ? extends PlaceActivity<? extends Place>>> activityProviders = new HashMap<Class<? extends Place>, ActivityProxyProvider<? extends Place,? extends PlaceActivity<? extends Place>>>();
	private final ActivityLoadCallback loadCallback;
	
	protected SabotActivityMapper(ActivityLoadCallback loadCallback){
		this.loadCallback = loadCallback;
	}
	
	protected final <P extends Place, A extends PlaceActivity<P>> void mapActivity(Class<P> placeClass, AsyncProvider<A> asyncActivityProxyProvider, ActivityLoadCallback loadCallback){
		activityProviders.put(placeClass, new ActivityProxyProvider<P, A>(asyncActivityProxyProvider, placeClass, loadCallback));
	}
	
	protected final <P extends Place, A extends PlaceActivity<P>> void mapActivity(Class<P> placeClass, AsyncProvider<A> asyncActivityProxyProvider){
		activityProviders.put(placeClass, new ActivityProxyProvider<P, A>(asyncActivityProxyProvider, placeClass, loadCallback));
	}
	
	@Override
	public final Activity getActivity(Place place) {
		ActivityProxyProvider<? extends Place, ? extends Activity> activityProvider = activityProviders.get(place.getClass());
		if(activityProvider != null){
			return activityProvider.proxyFor(place);
		}else{
			GWT.log("Got a place that wasn't mapped to an activity : "+place.getClass());
			return null;
		}		
	}
}
