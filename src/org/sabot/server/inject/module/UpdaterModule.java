package org.sabot.server.inject.module;

import org.sabot.server.update.DataUpdater;
import org.sabot.server.update.DataUpdaterRegistry;

import com.google.inject.AbstractModule;
import com.google.inject.internal.UniqueAnnotations;

public abstract class UpdaterModule extends AbstractModule {
	abstract protected void bindUpdaters();
	
	//Do not touch the items below unless you know what you are doing
	//p.s. you do not know what you are doing
	@Override
	protected void configure() {
		bindUpdaters();
		bind(DataUpdaterRegistry.class).asEagerSingleton();
		requestStaticInjection(DataUpdaterRegistry.class);
	}
	
	protected <T extends DataUpdater> void bindUpdater(Class<T> type){
		bind(DataUpdater.class).annotatedWith(UniqueAnnotations.create()).to(type);
	}
}
