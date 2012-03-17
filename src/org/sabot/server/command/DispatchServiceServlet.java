package org.sabot.server.command;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sabot.client.service.DispatchService;
import org.sabot.shared.command.Action;
import org.sabot.shared.command.Result;
import org.sabot.shared.exception.ActionException;
import org.sabot.shared.exception.DataServiceTimeoutException;
import org.sabot.shared.exception.RequestProcessingTimeoutException;

import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.apphosting.api.DeadlineExceededException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
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
public class DispatchServiceServlet extends RemoteServiceServlet implements DispatchService {
	private static final long serialVersionUID = -5888606600381295197L;

	private final Dispatch dispatch;
	private transient final Log logger = LogFactory.getLog(DispatchServiceServlet.class);

    @Inject
    public DispatchServiceServlet( Dispatch dispatch ) {
        this.dispatch = dispatch;
    }

    public <A extends Action<R>, R extends Result> R execute( A action ) throws Exception {
        try {
            return dispatch.execute( action );
        }catch(DatastoreTimeoutException e){
        	if(logger != null){
    			logger.warn( "Datastore Timeout Exception thrown while executing " + action.getClass().getName(), e );
    		}
        	throw new DataServiceTimeoutException(e);
    	}catch(DeadlineExceededException e){
    		if(logger != null){
    			logger.warn( "DeadlineExceededException thrown while executing " + action.getClass().getName(), e );
    		}
    		throw new RequestProcessingTimeoutException(e);
    	}catch ( RuntimeException e ) {
    		if(logger != null){
    			logger.warn( "RuntimeException while executing " + action.getClass().getName() + ": " + e.getMessage(), e );
    		}
    		e.printStackTrace();
    		throw new ActionException(e);
        }catch (Exception e) {
        	if (logger != null) {
        		logger.info("Checked exception while executing " + action.getClass().getName() + ": " + e.getMessage(), e );
        	}
        	throw e;
        }
    }
}
