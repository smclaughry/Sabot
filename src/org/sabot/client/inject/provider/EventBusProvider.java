package org.sabot.client.inject.provider;

import javax.inject.Inject;

import org.sabot.client.service.ClientActivityCollector;
import org.sabot.client.service.EventBusProxy;

import com.google.inject.Provider;

public class EventBusProvider implements Provider<EventBusProxy>{

	private final EventBusProxy proxy;

	@Inject
	public EventBusProvider(ClientActivityCollector collector){
		proxy = new EventBusProxy(collector);
	}
	
	@Override
	public EventBusProxy get() {
		return proxy;
	}
}
