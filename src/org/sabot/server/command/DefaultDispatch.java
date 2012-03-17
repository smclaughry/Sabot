package org.sabot.server.command;

import java.util.List;

import org.apache.commons.logging.Log;
import org.sabot.server.ActionHandler;
import org.sabot.server.ExecutionContext;
import org.sabot.server.SessionData;
import org.sabot.server.auth.AuthenticationPolicy;
import org.sabot.server.auth.AuthorizationPolicy;
import org.sabot.server.auth.AuthorizationRegistry;
import org.sabot.server.auth.Authorizer;
import org.sabot.server.dao.SabotUserDao;
import org.sabot.server.inject.module.ActionHandlerRegistry;
import org.sabot.shared.beans.SabotUser;
import org.sabot.shared.command.Action;
import org.sabot.shared.command.Result;
import org.sabot.shared.exception.ActionException;
import org.sabot.shared.exception.AuthenticationException;
import org.sabot.shared.exception.AuthorizationFailureException;
import org.sabot.shared.exception.UnsupportedActionException;

import javax.inject.Inject;

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
public class DefaultDispatch implements Dispatch {

	private static final long serialVersionUID = -1333211933272879829L;

	private transient final ActionHandlerRegistry actionHandlerRegistry;
	private final SessionData sessionData;
	private Log logger;

	private final SabotUserDao<? extends SabotUser> userDao;
	private final AuthorizationRegistry authorizationRegistry;

    @Inject
    public DefaultDispatch(ActionHandlerRegistry actionHandlerRegistry,
    					   SessionData sessionData,
    					   SabotUserDao<? extends SabotUser> userDao,
    					   AuthorizationRegistry authorizationRegistry,
    					   Log logger) {
        this.actionHandlerRegistry = actionHandlerRegistry;
		this.sessionData = sessionData;
		this.userDao = userDao;
		this.authorizationRegistry = authorizationRegistry;
		this.logger = logger;
    }

    @Override
	public <A extends Action<R>, R extends Result> R execute( A action ) throws ActionException {
        DefaultExecutionContext ctx = new DefaultExecutionContext( this );
        try {
            return doExecute( action, ctx );
        } catch ( ActionException e ) {
            ctx.rollback();
            throw e;
        }
    }

    private <A extends Action<R>, R extends Result> R doExecute(A action, ExecutionContext ctx) throws ActionException {
        ActionHandler<A, R> handler = findHandler( action );

        doAuthentication(handler);
        doAuthorization(handler, action);
        
        R result = handler.execute( action, ctx );
        return result;
    }

    private <A extends Action<R>, R extends Result> void doAuthentication(ActionHandler<A, R> handler) throws ActionException{
    	AuthenticationPolicy authenticationPolicy = handler.getClass().getAnnotation(AuthenticationPolicy.class);
    	if(authenticationPolicy == null){
    		throw new ActionException("No available authentication policy for the class: " + handler.getClass().getSimpleName());
    	}
    	if(authenticationPolicy.value() == AuthenticationPolicy.Policy.AUTHENTICATED && !sessionData.isLoggedIn()){
    		throw new AuthenticationException();
    	}
    }
    
    @SuppressWarnings("unchecked")
	private <A extends Action<R>, R extends Result> void doAuthorization(ActionHandler<A, R> handler, A action) throws ActionException{
        AuthorizationPolicy authPolicy = handler.getClass().getAnnotation(AuthorizationPolicy.class);
    	if(authPolicy == null){
        	throw new ActionException("No available authorization policy for the class: " + handler.getClass().getSimpleName());
        }
    	if(authPolicy.value() == AuthorizationPolicy.Policy.AUTHORIZED){
    		SabotUser user = userDao.getCurrentUser();
			Class<? extends Action<? extends Result>> class1 = (Class<? extends Action<? extends Result>>) action.getClass();
			List<Authorizer> authorizersForAction = authorizationRegistry.getAuthorizersForAction(class1);
			if(authorizersForAction == null || authorizersForAction.isEmpty()){
				String message = "No authorizers registered for handler: " + handler.getClass().getSimpleName();
				logger.error(message);
				throw new AuthorizationFailureException(message);
			}
			for(Authorizer authorizer : authorizersForAction){
				boolean authorized;
				try{
					authorized = authorizer.isAuthorized(user, action);
				}catch(Exception e){
					logger.error(e);
					throw new AuthorizationFailureException("Exception in authorization", e);
				}
				if(!authorized){
					throw new AuthorizationFailureException("Authorization failed");						
				}
			}
    	}
    }
    
    private <A extends Action<R>, R extends Result> ActionHandler<A, R> findHandler( A action ) throws UnsupportedActionException {
        ActionHandler<A, R> handler = actionHandlerRegistry.findHandler( action );
        if ( handler == null ){
            throw new UnsupportedActionException( action );
        }
        return handler;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private <A extends Action<R>, R extends Result> void doRollback( A action, R result, ExecutionContext ctx )
            throws ActionException {
        ActionHandler<A, R> handler = findHandler( action );
        if (handler instanceof ChangeActionHandler<?,?>) {
        	((ChangeActionHandler)handler).rollback( action, result, ctx );
        }
    }
    
    private static class DefaultExecutionContext implements ExecutionContext {
        private final DefaultDispatch dispatch;

        private final List<ActionResult<?, ?>> actionResults;

        public DefaultExecutionContext( DefaultDispatch dispatch ) {
            this.dispatch = dispatch;
            this.actionResults = new java.util.ArrayList<ActionResult<?, ?>>();
        }

        @Override
        public <A extends Action<R>, R extends Result> R execute( A action ) throws ActionException {
            return execute( action, true );
        }

        @Override
        public <A extends Action<R>, R extends Result> R execute( A action, boolean allowRollback )
                throws ActionException {
            R result = dispatch.doExecute( action, this );
            if ( allowRollback ){
                actionResults.add( new ActionResult<A, R>( action, result ) );
            }
            return result;
        }
        
        /**
         * Rolls back all logged action/results.
         * 
         * @throws ActionException
         *             If there is an action exception while rolling back.
         * @throws ServiceException
         *             If there is a low level problem while rolling back.
         */
        @SuppressWarnings("unchecked")
		private void rollback() throws ActionException {
            for ( int i = actionResults.size() - 1; i >= 0; i-- ) {
                @SuppressWarnings("rawtypes")
				ActionResult actionResult = actionResults.get( i );
                this.rollback( actionResult.getAction(), actionResult.getResult() );
            }
        }

        @Override
        public <A extends Action<R>, R extends Result> void rollback( A action, R result )
                throws ActionException {
            dispatch.doRollback( action, result, this );
        }

    }
}
