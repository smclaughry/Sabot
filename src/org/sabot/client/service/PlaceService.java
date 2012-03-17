package org.sabot.client.service;

import javax.inject.Inject;

import org.sabot.client.util.GoogleAnalytics;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Singleton;

@Singleton
public class PlaceService implements PlaceChangeEvent.Handler {

	private Place defaultPlace = Place.NOWHERE;
	
	private final GoogleAnalytics googleAnalytics;

	private final PlaceHistoryMapper mapper;

	@Inject
	public PlaceService(EventBus eventBus,
						GoogleAnalytics googleAnalytics,
						PlaceHistoryMapper mapper) {
		this.googleAnalytics = googleAnalytics;
		this.mapper = mapper;
		eventBus.addHandler(PlaceChangeEvent.TYPE, this);
	}
	
	@Override
	public void onPlaceChange(PlaceChangeEvent event) {
		googleAnalytics.trackAreaRequest(tokenForPlace(event.getNewPlace()));		
	}
	
	private String tokenForPlace(Place newPlace) {
	    if (defaultPlace.equals(newPlace)) {
	      return "";
	    }
	    
	    String token = mapper.getToken(newPlace);
	    if (token != null) {
	      return token;
	    }

	    return "";
	  }
}
