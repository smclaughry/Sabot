package org.sabot.client.service;

import org.sabot.shared.command.Action;
import org.sabot.shared.command.Result;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

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

public class DefaultDispatchAsync implements DispatchAsync {

    private static final DispatchServiceAsync realService = GWT.create( DispatchService.class );
    private static final String baseUrl = ((ServiceDefTarget)realService).getServiceEntryPoint() + "/";
    
    public DefaultDispatchAsync() {
    	//I be empty
    }

    public <A extends Action<R>, R extends Result> void execute( final A action, final AsyncCallback<R> callback ) {
    	String className = action.getClass().getName();
    	int namePos = className.lastIndexOf('.') + 1;
    	className = className.substring(namePos);
    	((ServiceDefTarget)realService).setServiceEntryPoint(baseUrl + className);
    	 

        realService.execute( action, new AsyncCallback<Result>() {
            public void onFailure( Throwable caught ) {
                callback.onFailure( caught );
            }

            @SuppressWarnings("unchecked")
			public void onSuccess( Result result ) {
                callback.onSuccess( ( R ) result );
            }
        } );
    }
}
