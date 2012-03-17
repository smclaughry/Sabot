package org.sabot.client.service;

import org.sabot.client.util.GoogleAnalytics.EventTracker;
import org.sabot.shared.command.Action;
import org.sabot.shared.command.Result;
import org.sabot.shared.exception.AuthenticationException;
import org.sabot.shared.exception.ServiceTimeoutException;
import org.sabot.shared.exception.UnathorizedActionException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.StatusCodeException;

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

public abstract class AppCallback<T extends Result> implements AsyncCallback<T> {

	private final Action<T> action;
	private Retry retry;
	private final boolean shouldRetry;
	private final CallbackManager callbackManager;
	private final EventTracker eventTracker;

	public AppCallback(CallbackManager callbackManager, EventTracker eventTracker, Action<T> action, boolean shouldRetry){
		this.callbackManager = callbackManager;
		this.eventTracker = eventTracker;
		this.action = action;
		this.shouldRetry = shouldRetry;
		this.retry = null;
	}
	
	@Override
	public void onFailure(Throwable caught) {
		if(caught instanceof ServiceTimeoutException || caught instanceof StatusCodeException){
			Retry retry = getRetry();
			if(retry == null){
				GWT.log("Naughty place after service timeout or StatusCodeExc", caught);
				callbackManager.goToErrorPlace();
			}else{
				Timer timer = new Timer(){
					@Override
					public void run() {
						callbackManager.retryAction(action, AppCallback.this);
					}
				};
				timer.schedule(retry.getMilliSeconds());
			}
		} else if (caught instanceof IncompatibleRemoteServiceException) {
			callbackManager.forceReload();
		}else if(caught instanceof AuthenticationException){
			callbackManager.doAuthentication();
		}else if(caught instanceof UnathorizedActionException){
			callbackManager.goHome();
		}else{
			onFail(caught);
		}
	}

	private Retry getRetry(){
		if(!shouldRetry){
			return null;
		}
		if(retry == null){
			retry = Retry.FIRST;
		}else{
			retry = retry.getNextRetry();
		}
		return retry;
	}
	
	protected void onFail(Throwable caught){
		GWT.log("Naughty naughty", caught);
		eventTracker.indicateFailure();
		callbackManager.goToErrorPlace();
	}
}