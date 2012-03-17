package org.sabot.server.utility;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public final class PlaceUtility {
	private PlaceUtility(){
		//keep out
	}
	
	public static <P extends Place> String placeToUrlFragment(P place, PlaceTokenizer<P> tokenizer){
		String placeName;
		Prefix prefix = tokenizer.getClass().getAnnotation(Prefix.class);
		if(prefix == null){
			placeName = place.getClass().getSimpleName();
		}else{
			placeName = prefix.value();
		}
		return placeName + ":" + tokenizer.getToken(place);
	}
}
