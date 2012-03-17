package org.sabot.server.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.sabot.shared.command.Action;

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
public class AuthorizationRegistryImpl implements AuthorizationRegistry {

	
	
	private final Map<Class<? extends Action<?>>, List<Authorizer>> authorizerListMap = new HashMap<Class<? extends Action<?>>, List<Authorizer>>();

	public List<Authorizer> getAuthorizersForAction(Class<? extends Action<?>> actionClass) {
		return authorizerListMap.get(actionClass);
	}

	public void registerAuthorizer(Class<? extends Action<?>> actionClass, Authorizer authorizer) {
		List<Authorizer> authorizers = authorizerListMap.get(actionClass);
		if(authorizers == null){
			authorizers = new ArrayList<Authorizer>();
			authorizerListMap.put(actionClass, authorizers);
		}
		authorizers.add(authorizer);
	}	
}