package org.sabot.server.inject.module;

import org.sabot.server.command.DefaultDispatch;
import org.sabot.server.command.Dispatch;

import com.google.inject.AbstractModule;

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
public class ServerDispatchModule extends AbstractModule {

    private Class<? extends Dispatch> dispatchClass;

    private Class<? extends ActionHandlerRegistry> actionHandlerRegistryClass;

    public ServerDispatchModule() {
        this( DefaultDispatch.class);
    }

    public ServerDispatchModule( Class<? extends Dispatch> dispatchClass) {
        this.dispatchClass = dispatchClass;
    }

    @Override
    protected final void configure() {
        bind( Dispatch.class ).to( getDispatchClass() );
    }

    protected Class<? extends Dispatch> getDispatchClass() {
        return dispatchClass;
    }

    protected Class<? extends ActionHandlerRegistry> getActionHandlerRegistryClass() {
        return actionHandlerRegistryClass;
    }

    @Override
    public boolean equals( Object obj ) {
        return obj instanceof ServerDispatchModule;
    }

    @Override
    public int hashCode() {
        return ServerDispatchModule.class.hashCode();
    }
}
