package org.sabot.server.inject.module;

import java.lang.annotation.Annotation;

import org.sabot.server.auth.AuthorizationProcessor;
import org.sabot.server.auth.AuthorizationRegistry;
import org.sabot.server.auth.AuthorizationRegistryImpl;
import org.sabot.server.auth.Authorizer;
import org.sabot.shared.command.Action;


import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.internal.UniqueAnnotations;

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
public abstract class AbstractAuthorizationModule extends AbstractModule {
	 	 
	 @Override
	 protected final void configure(){
		 bind(AuthorizationRegistry.class).to(AuthorizationRegistryImpl.class).in(Scopes.SINGLETON);
		 requestStaticInjection(AuthorizationProcessor.class);
		 this.configureAuthorizationRegistrations();
	 }
	 
	protected void bindAuthorizer(Class<? extends Authorizer> authorizerClass, Class<? extends Action<?>> actionClass){
		Annotation annotation = UniqueAnnotations.create();
		bind(Authorizer.class).annotatedWith(annotation).to(authorizerClass).in(Singleton.class);
		Provider<Authorizer> provider = getProvider(Key.get(Authorizer.class, annotation));
		
		AuthorizationProcessor.queueAuthorizationRegistration(provider, actionClass);
	}
	 
	protected abstract void configureAuthorizationRegistrations();
}