package org.sabot.shared.exception;

import org.sabot.shared.command.Action;
import org.sabot.shared.command.Result;

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

public class UnathorizedActionException extends ActionException {

	private static final long serialVersionUID = -5969009323403895904L;

	/**
     * For serialization.
     */
    UnathorizedActionException() {
    	//For serialization
    }
    
    @SuppressWarnings("unchecked")
	public UnathorizedActionException( Action<? extends Result> action ) {
        this( ( Class<? extends Action<? extends Result>> ) action.getClass() );
    }

    public UnathorizedActionException( Class<? extends Action<? extends Result>> actionClass ) {
        super( "No handler is registered for " + actionClass.getName() );
    }

}
