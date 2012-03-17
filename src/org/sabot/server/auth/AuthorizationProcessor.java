package org.sabot.server.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sabot.server.ActionHandler;
import org.sabot.server.auth.AuthorizationPolicy.Policy;
import org.sabot.shared.command.Action;

import com.google.inject.Binding;
import javax.inject.Inject;

import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

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
public final class AuthorizationProcessor {

	private static Map<Provider<Authorizer>, Class<? extends Action<?>>> authRegMap = new HashMap<Provider<Authorizer>, Class<? extends Action<?>>>();
	
	private AuthorizationProcessor(){
		//Hide me
	}
	
	public static void queueAuthorizationRegistration(Provider<Authorizer> provider, Class<? extends Action<?>> actionClass){
		authRegMap.put(provider, actionClass);
	}
	
	private static void wireRegistry(AuthorizationRegistry registry){
			for(Map.Entry<Provider<Authorizer>, Class<? extends Action<?>>> entry : authRegMap.entrySet()){
				registry.registerAuthorizer(entry.getValue(), entry.getKey().get());
			}
			authRegMap = new HashMap<Provider<Authorizer>, Class<? extends Action<?>>>();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Inject
	public static void processAuthorizations(Injector injector, AuthorizationRegistry registry){
		wireRegistry(registry);
		
		List<Binding<ActionHandler>> handlers = injector.findBindingsByType(TypeLiteral.get(ActionHandler.class));
		
		for(Binding<ActionHandler> binding : handlers){
			ActionHandler handler = binding.getProvider().get();
			AuthorizationPolicy authorizationPolicy = handler.getClass().getAnnotation(AuthorizationPolicy.class);
			if(authorizationPolicy == null){
				 throw new IllegalArgumentException("Handler : " + handler.getClass().getSimpleName() + " must bear an AuthorizationPolicy biatch.");
			}
						
			if(authorizationPolicy.value() == Policy.AUTHORIZED){
				List<Authorizer> authorizersForAction = registry.getAuthorizersForAction(handler.getActionType());
				if(authorizersForAction == null || authorizersForAction.isEmpty()){
					throw new IllegalArgumentException("Handler : " + handler.getClass().getSimpleName() + " is an authorized handler and has no registered authorizers biatch.");
				}else{
					for(Authorizer authorizer : authorizersForAction){
						for(Class<?> argClass : authorizer.argumentClasses()){
							if(!argClass.isAssignableFrom(handler.getActionType())){
								throw new IllegalArgumentException("Action : " + handler.getActionType().getSimpleName() + " does not implement the interface:(" + argClass.getSimpleName() + ") required by the associated handler.");
							}
						}
					}
				}				
			}
		}
	}
}
