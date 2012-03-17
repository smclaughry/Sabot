package org.sabot.client.service;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.SimpleEventBus;

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
public class EventBusProxy extends SimpleEventBus {
	private ClientActivityCollector activityCollector;

	@Inject
	public EventBusProxy(ClientActivityCollector activityCollector) {
		this.activityCollector = activityCollector;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		try {
			super.fireEvent(event);
		} catch (RuntimeException re) {
			GWT.log("Unexpected exception", re);
			getActivityCollector().logException(re);
		}
	}

	@Override
	public <H extends EventHandler> HandlerRegistration addHandler(Type<H> type, H handler) {
		return super.addHandler(type, handler);
	}

	@Override
	public <H extends EventHandler> HandlerRegistration addHandlerToSource(Type<H> type, Object source, H handler) {
		return super.addHandlerToSource(type, source, handler);
	}

	@Override
	public void fireEventFromSource(GwtEvent<?> event, Object source) {
		super.fireEvent(event);
	}

	public ClientActivityCollector getActivityCollector() {
		return activityCollector;
	}

	public void setActivityCollector(ClientActivityCollector activityCollector) {
		this.activityCollector = activityCollector;
	}
}
