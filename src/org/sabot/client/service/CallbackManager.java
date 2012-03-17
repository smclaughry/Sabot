package org.sabot.client.service;

import javax.inject.Inject;

import org.sabot.client.event.StartLoginEvent;
import org.sabot.client.util.GoogleAnalytics;
import org.sabot.client.util.GoogleAnalytics.EventTracker;
import org.sabot.shared.command.Action;
import org.sabot.shared.command.DescriptiveAction;
import org.sabot.shared.command.Result;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Singleton;

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

@Singleton
public class CallbackManager {

	private final DispatchAsync dispatchService;
	private final PlaceController placeController;
	private final GoogleAnalytics googleAnalytics;
	private final EventBus eventBus;
	private final UsernameProvider usernameProvider;
	private final PlaceProvider placeProvider;
	private final ReloadService reloadService;

	@Inject
	public CallbackManager(DispatchAsync dispatchService,
						   EventBus eventBus,
						   GoogleAnalytics googleAnalytics,
						   PlaceController placeController,
						   UsernameProvider usernameProvider,
						   PlaceProvider placeProvider,
						   ReloadService reloadService){
		this.dispatchService = dispatchService;
		this.eventBus = eventBus;
		this.googleAnalytics = googleAnalytics;
		this.placeController = placeController;
		this.usernameProvider = usernameProvider;
		this.placeProvider = placeProvider;
		this.reloadService = reloadService;		
	}
		
	public <T extends Result> void retryAction(Action<T> action, AsyncCallback<T> callback) {
		dispatchService.execute(action, callback);
	}

	public void goToPlace(Place place) {
		placeController.goTo(place);
	}
	
	public interface ResultProcessor<R extends Result>{
		 void processResult(R result);
	}

	public void doAuthentication() {
		eventBus.fireEvent(new StartLoginEvent(placeController.getWhere()));
	}
	
	public <A extends Action<R>, R extends Result> void executeEventResultAction(String eventCategory, A action, boolean shouldRetry){
		executeEventResultActionWithProcessor(eventCategory, action, shouldRetry, null);
	}
	
	public <A extends Action<R>, R extends Result> void executeEventResultActionWithProcessor(String eventCategory, A action, boolean shouldRetry, final ResultProcessor<R> resultProcessor){
		final EventTracker tracker = new EventTracker(simpleClassName(eventCategory) + " " + simpleClassName(action.getClass().getName()), trackingActionName(action), usernameProvider.getUserNameForTracking());
		dispatchService.execute(action, new AppCallback<R>(this, tracker, action, shouldRetry){
			@Override
			public void onSuccess(R result) {
				if(result instanceof GwtEvent){
					eventBus.fireEvent((GwtEvent<?>) result);					
					googleAnalytics.trackEvent(tracker);
					if(resultProcessor != null){
						resultProcessor.processResult(result);
					}
				}else{
					placeController.goTo(placeProvider.getErrorPlace());
				}
			}			
		});
	}
	
	private <A extends Action<R>, R extends Result> String trackingActionName(A action) {
		if(action instanceof DescriptiveAction) {
			return ((DescriptiveAction<?>) action).getDescription();
		}
		return simpleClassName(action.getClass().getName());
	}
	
	private String simpleClassName(String notSimpleClassname) {
		if(notSimpleClassname.lastIndexOf('.') != -1) {
			return notSimpleClassname.substring(notSimpleClassname.lastIndexOf('.') + 1);
		}else {
			return notSimpleClassname;
		}
	}

	public void forceReload() {
		reloadService.forceReload();
	}

	public void goHome() {
		placeController.goTo(placeProvider.getHomePlace());
	}

	public void goToErrorPlace() {
		placeController.goTo(placeProvider.getErrorPlace());
	}
}
