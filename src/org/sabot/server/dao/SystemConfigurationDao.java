package org.sabot.server.dao;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.sabot.server.cache.CacheService;
import org.sabot.shared.beans.SystemConfiguration;
import org.sabot.shared.binding.SignupAddress;

import com.google.inject.Provider;
import com.googlecode.objectify.Objectify;

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
public class SystemConfigurationDao extends ObjectifyGenericDao<SystemConfiguration>{
	
	private final CacheService cacheService;
	private final String bccEmail;
	private final Provider<HttpServletRequest> requestProvider;

	@Inject
	public SystemConfigurationDao(Provider<Objectify> objectifyProvider,
									  Provider<HttpServletRequest> requestProvider,
									  CacheService cacheService,
									  @SignupAddress String bccEmail){
		super(SystemConfiguration.class, objectifyProvider);
		this.requestProvider = requestProvider;
		this.cacheService = cacheService;
		this.bccEmail = bccEmail;
	}
	
	public SystemConfiguration getSystemConfiguration(){
		return cacheService.getValue(SystemConfiguration.class.getSimpleName(), new CacheService.CachedCommand<SystemConfiguration>(){
			@Override
			public SystemConfiguration execute() {
				SystemConfiguration systemConfiguration = ofy().query(SystemConfiguration.class).get();
				return ensureBasicConfiguration(systemConfiguration); 
			}
		});
	}
	
	private SystemConfiguration ensureBasicConfiguration(SystemConfiguration systemConfiguration){
		SystemConfiguration currentSysConfig = systemConfiguration;
		if (currentSysConfig == null) {
			currentSysConfig = new SystemConfiguration();
		}

		if(currentSysConfig.getBccEmails() == null){
			currentSysConfig.setBccEmails(bccEmail);
			ofy().put(currentSysConfig);
		}
		
		return currentSysConfig;
	}
	
	public String getServerUrl(){
		HttpServletRequest request = requestProvider.get();
		
		SystemConfiguration systemConfiguration = getSystemConfiguration();
		return systemConfiguration.preferredHostScheme(request.getScheme()) + "://" + systemConfiguration.preferredHostUrl(request.getServerName() + systemConfiguration.preferedPort(request.getServerPort()));
	}
}
