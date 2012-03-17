package org.sabot.client.place.activity;

import org.sabot.client.place.ActivityProxy;
import org.sabot.client.place.ActivityProxy.ActivityLoadCallback;
import org.sabot.client.place.PlaceActivity;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.place.shared.Place;

public class ActivityProxyProvider<P extends Place, A extends PlaceActivity<P>> {
	
	private final Class<P> placeClass;
	private final AsyncProvider<A> activityProvider;
	private final ActivityLoadCallback loadCallback;

	public ActivityProxyProvider(AsyncProvider<A> activityProvider, Class<P> placeClass, ActivityLoadCallback loadCallback) {
		this.activityProvider = activityProvider;
		this.placeClass = placeClass;
		this.loadCallback = loadCallback;
	}
	
	@SuppressWarnings("unchecked")
	public ActivityProxy<P, A> proxyFor(Place place){
		if(place.getClass().equals(placeClass)){
			return new ActivityProxy<P, A>(activityProvider, (P) place, loadCallback);
		}
		return null;
	}
}
