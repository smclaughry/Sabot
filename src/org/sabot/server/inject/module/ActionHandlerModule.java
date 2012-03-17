package org.sabot.server.inject.module;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.sabot.server.ActionHandler;
import org.sabot.server.auth.AuthenticationPolicy;
import org.sabot.server.auth.AuthorizationPolicy;
import org.sabot.server.auth.AuthenticationPolicy.Policy;
import org.sabot.shared.command.Action;
import org.sabot.shared.command.Result;
import org.sabot.shared.exception.AuthenticationException;
import org.sabot.shared.exception.AuthorizationFailureException;

import com.google.inject.AbstractModule;
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
public abstract class ActionHandlerModule extends AbstractModule {
	
	private static final String HANDLER = "Handler : ";
	Map<Class<? extends Action<?>>, Provider<? extends ActionHandler<?, ?>>> actionMap = new HashMap<Class<? extends Action<?>>, Provider<? extends ActionHandler<?,?>>>();

    @Override
    protected final void configure() {
        // This will only get installed once due to equals/hashCode override.
        install( new ServerDispatchModule() );
        configureHandlers();
        bind(new TypeLiteral<Map<Class<? extends Action<?>>, Provider<? extends ActionHandler<?, ?>>>>(){}).toInstance(actionMap);
    }

    /**
     * Override this method to configure handlers.
     */
    protected abstract void configureHandlers();

    protected <H extends ActionHandler<A, ?>, A extends Action<?>> void bindHandler(Class<H> handlerClass, Class<A> actionClass){
    	validateAuthenticationAndAuthorization(handlerClass);
    	actionMap.put(actionClass, getProvider(handlerClass));
    }
    
	private void validateAuthenticationAndAuthorization(Class<? extends ActionHandler<?, ?>> handlerClass) {
		AuthenticationPolicy authenticationPolicy = handlerClass.getAnnotation(AuthenticationPolicy.class);
    	AuthorizationPolicy authorizationPolicy = handlerClass.getAnnotation(AuthorizationPolicy.class);
    	if(authenticationPolicy == null){
    		this.binder().addError(HANDLER + handlerClass.getSimpleName() + " must bear an AuthenticationPolicy biatch.");
    		return;
    	}
    	if(authorizationPolicy == null){
    		this.binder().addError(HANDLER + handlerClass.getSimpleName() + " must bear an AuthorizationPolicy biatch.");
    		return;
		}

    	Method executeMethod = getExecuteMethod(handlerClass);    	
    	boolean throwsAuthenticationException = throwsException(executeMethod, AuthenticationException.class); 
    	boolean throwsAuthorizationException = throwsException(executeMethod, AuthorizationFailureException.class);
    	
    	if(authenticationPolicy.value() == Policy.AUTHENTICATED && !throwsAuthenticationException){
    		this.binder().addError(HANDLER + handlerClass.getSimpleName() + " must throw AuthenticationException on the execute method if the handler is authenticated biatch.");
		}
    	
    	if(authorizationPolicy.value() == AuthorizationPolicy.Policy.AUTHORIZED && !throwsAuthorizationException){
    		this.binder().addError(HANDLER + handlerClass.getSimpleName() + " must throw AuthorizationFailureException on the execute method if the handler is authorized biatch.");
    	}
    	
//		if(authorizationPolicy.value() == AuthorizationPolicy.Policy.AUTHORIZED
//		&& authenticationPolicy.value() == AuthenticationPolicy.Policy.NOT_AUTHENTICATED){
//			this.binder().addError(HANDLER + handlerClass.getSimpleName() + " wants to be authorized yet says it does not want to be authenticated.  That is un-kosher, fix it.");
//		}
	}
	
	private Method getExecuteMethod(Class<? extends ActionHandler<?, ?>> handlerClass){
		for(Method method : handlerClass.getMethods()){
			if(method.getName().equals("execute") &&
			   !method.getReturnType().equals(Result.class)){
				return method;
			}
		}
		return null;
	}
	
	private boolean throwsException(Method method, Class<?> exceptionClass){
		for(Class<?> exceptionType : method.getExceptionTypes()){
			if(exceptionClass.isAssignableFrom(exceptionType)){
				return true;
			}
		}
		return false;
	}
}
