package org.sabot.client.inject.provider;

import javax.inject.Inject;
import javax.inject.Provider;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;

public class PlaceControllerProvider implements Provider<PlaceController> {

	private final PlaceController placeController;

	@Inject
	public PlaceControllerProvider(Provider<EventBus> eventBusProvider){
		this.placeController = new PlaceController(eventBusProvider.get());
	}
	
	@Override
	public PlaceController get() {
		return placeController;
	}

}
