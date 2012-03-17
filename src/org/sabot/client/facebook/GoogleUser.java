package org.sabot.client.facebook;

import org.sabot.client.service.oauth.OauthProvider;
import org.sabot.client.service.oauth.RemoteUser;

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
public class GoogleUser extends JavaScriptObject {
/*
{
 "id": "101372910321809994811",
 "email": "smclaughry@gmail.com",
 "verified_email": true,
 "name": "Stephen McLaughry",
 "given_name": "Stephen",
 "family_name": "McLaughry",
 "picture": "https://lh5.googleusercontent.com/-3mt8i0qk_7o/AAAAAAAAAAI/AAAAAAAAAVc/5r92fHHY9eo/photo.jpg",
 "gender": "male",
 "locale": "en"
}
 */
	// Overlay types always have protected, zero-arg constructors
	  protected GoogleUser() { }

	  public final native String getId( ) /*-{ return this.id; }-*/;
//	  public final native String getDisplayName() /*-{ return this.displayName; }-*/;
	  public final native String getFirstName() /*-{ return this.given_name; }-*/;
	  public final native String getLastName()  /*-{ return this.family_name; }-*/;
	  public final native String getEmail() /*-{ return this.email; }-*/;
//	  public final native String getUsername() /*-{ return this.username; }-*/;

//	  public final String getFullName() {
//	    return getFirstName() + " " + getLastName();
//	  }
	  
		public final RemoteUser asRemoteUser() {
			return new RemoteUser(getFirstName(), getLastName(), getEmail(), getId(), OauthProvider.GOOGLE);
		}
}
