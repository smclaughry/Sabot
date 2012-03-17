package org.sabot.client.service.zerization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sabot.shared.beans.SabotUser;

import com.google.gwt.place.shared.Place;

public abstract class AbstractZerizationMapper implements ZerizationMapper {

	private HashMap<Class<? extends Place>, List<Zerization>> placeZerizationMap = new HashMap<Class<? extends Place>, List<Zerization>>();
		
	protected final void bindZerization(Class<? extends Place> key, Zerization...zerizations) {
		List<Zerization> list = placeZerizationMap.get(key);
		if(list == null) {
			list = new ArrayList<Zerization>();
			placeZerizationMap.put(key, list);
		}
		
		for(Zerization zer : zerizations) {
			list.add(zer);
		}
	}
	
	@Override
	public boolean isZerizedForPlace(Class<? extends Place> key, SabotUser user) {
		List<Zerization> requiredZerizations = placeZerizationMap.get(key);
		return requiredZerizations == null ? true : user.getZerizations().containsAll(requiredZerizations);
	}

	@Override
	public List<Zerization> zerizationsForPlace(Class<? extends Place> key) {
		return placeZerizationMap.get(key);
	}
}
