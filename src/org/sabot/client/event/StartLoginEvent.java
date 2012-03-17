package org.sabot.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.place.shared.Place;

public class StartLoginEvent extends GwtEvent<StartLoginEvent.Handler> {

	public static interface Handler extends EventHandler {
		public void onStartLogin(Place returnToPlace);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();
	private final Place returnToPlace;
			
	public StartLoginEvent(Place currentPlace) {
		this.returnToPlace = currentPlace;
	}

	@Override
	public final Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onStartLogin(returnToPlace);
	}
}