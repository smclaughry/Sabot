package org.sabot.client.service.zerization;

import java.util.List;

import org.sabot.shared.beans.SabotUser;

import com.google.gwt.place.shared.Place;

public interface ZerizationMapper {

	boolean isZerizedForPlace(Class<? extends Place> key, SabotUser user);

	List<Zerization> zerizationsForPlace(Class<? extends Place> key);

}
