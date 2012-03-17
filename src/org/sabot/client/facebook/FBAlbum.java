package org.sabot.client.facebook;

import com.google.gwt.core.client.JavaScriptObject;

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
public class FBAlbum extends JavaScriptObject {
	  // Overlay types always have protected, zero-arg constructors
	protected FBAlbum() { }

	public final native String getId() /*-{ return this.id; }-*/;
	public final native String getName() /*-{ return this.name; }-*/;
	public final native String getError() /*-{ 
		if (null != this.error) {
			return this.error['message']; 
		} else {
			return null;
		}
	}-*/;
}
