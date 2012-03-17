package org.sabot.client.service;

import org.sabot.shared.command.Action;
import org.sabot.shared.command.Result;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
public class RetrySet<R extends Result> {
	private final Place place;
	private final Action<R> action;
	private final AsyncCallback<R> callback;

	public RetrySet(Action<R> action, AsyncCallback<R> callback, Place place){
		this.action = action;
		this.callback = callback;
		this.place = place;
		
	}

	public Action<R> getAction() {
		return action;
	}

	public AsyncCallback<R> getCallback() {
		return callback;
	}

	public Place getPlace() {
		return place;
	}
}