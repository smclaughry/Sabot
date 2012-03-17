package org.sabot.client.service.zerization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.sabot.client.service.PlaceProvider;
import org.sabot.shared.beans.SabotUser;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;

@Singleton
public class ZerizationService implements PlaceChangeEvent.Handler {
	
	private final PlaceProvider placeProvider;
	private final Provider<SabotUser> userProvider;
	private final ZerizationMapper zerizationMapper;
	private final PlaceController placeController;

	@Inject
	public ZerizationService(Provider<SabotUser> userProvider,
							 EventBus eventBus,
							 PlaceController placeController,
							 PlaceProvider placeProvider,
							 ZerizationMapper zerizationMapper) {
		this.userProvider = userProvider;
		this.placeController = placeController;
		this.placeProvider = placeProvider;
		this.zerizationMapper = zerizationMapper;
		eventBus.addHandler(PlaceChangeEvent.TYPE, this);
	}
	
	public boolean isCurrentUserZerized(Zerization... zerizations) {
		return userProvider.get() != null && userProvider.get().getZerizations().containsAll(Arrays.asList(zerizations));
	}

	@Override
	public void onPlaceChange(PlaceChangeEvent event) {
		final List<Zerization> requiredZerizations = zerizationMapper.zerizationsForPlace(event.getNewPlace().getClass());
		if(requiredZerizations != null && !requiredZerizations.isEmpty()) {
			if(userProvider.get() == null || !userProvider.get().getZerizations().containsAll(requiredZerizations)) {
				Scheduler.get().scheduleFinally(new Scheduler.ScheduledCommand() {
					@Override
					public void execute() {
						List<Zerization> missingZerizations = new ArrayList<Zerization>(requiredZerizations);
						if(userProvider.get() != null) {
							missingZerizations.removeAll(userProvider.get().getZerizations());
						}
						placeController.goTo(placeProvider.getZerationsRequiredPlace(requiredZerizations));
					}
				});
			}
		}
	}
}
