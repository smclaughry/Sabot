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
public class FBPhoto extends JavaScriptObject {
	  // Overlay types always have protected, zero-arg constructors
	protected FBPhoto() { }

	public final native String getId() /*-{ return this.id; }-*/;
	public final native String getPicture() /*-{ return this.picture; }-*/;
	public final native String getSource()  /*-{ return this.source; }-*/;
	public final native String getHeight() /*-{ return this.height; }-*/;
	public final native String getWidth() /*-{ return this.width; }-*/;
	public final native String getError() /*-{ 
		if (null != this.error) {
			return this.error['message']; 
		} else {
			return null;
		}
	}-*/;
}
