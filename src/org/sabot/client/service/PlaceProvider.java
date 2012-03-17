package org.sabot.client.service;

import java.util.List;

import org.sabot.client.service.zerization.Zerization;

import com.google.gwt.place.shared.Place;

public interface PlaceProvider {

	public Place getErrorPlace();
	public Place getHomePlace();
	public Place getZerationsRequiredPlace(List<Zerization> requiredZerizations);
}
