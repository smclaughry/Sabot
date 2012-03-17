package org.sabot.server.update;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.inject.Binding;
import com.google.inject.Injector;
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
public class DataUpdaterRegistry {

	private final ArrayList<DataUpdater> updaters = new ArrayList<DataUpdater>();
	
	public List<DataUpdater> getUpdaters(){
		return updaters;
	}
	
	private void addDataUpdater(DataUpdater updater){
		updaters.add(updater);
	}
	
	@Inject
	public static void processUpdaters(Injector injector, DataUpdaterRegistry updaterRegistry){
		List<Binding<DataUpdater>> updaters = injector.findBindingsByType(TypeLiteral.get(DataUpdater.class));
		
		for(Binding<DataUpdater> binding : updaters){
			updaterRegistry.addDataUpdater(binding.getProvider().get());
		}
	}
}
