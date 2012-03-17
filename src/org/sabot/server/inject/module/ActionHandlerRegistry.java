package org.sabot.server.inject.module;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sabot.server.ActionHandler;
import org.sabot.shared.command.Action;
import org.sabot.shared.command.Result;

import com.google.inject.Provider;
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
public class ActionHandlerRegistry {

	private transient final Log logger = LogFactory.getLog(ActionHandlerRegistry.class);

    private final Map<Class<? extends Action<?>>, Provider<? extends ActionHandler<?, ?>>> handlerMap;

    @Inject
    public ActionHandlerRegistry(Map<Class<? extends Action<?>>, Provider<? extends ActionHandler<?, ?>>> handlerMap){
    	this.handlerMap = handlerMap;
    }

    @SuppressWarnings("unchecked")
	public <A extends Action<R>, R extends Result> ActionHandler<A, R> findHandler( A action ) {
    	Provider<? extends ActionHandler<?, ?>> provider = handlerMap.get( action.getClass());
    	if (null == provider) {
    		logger.fatal("No Handler registered for class "+action.getClass().getName());
    		throw new RuntimeException("No Handler registered for class "+action.getClass().getName());
    	} else {
    		return (ActionHandler<A, R>)provider.get();
    	}
    }
}
