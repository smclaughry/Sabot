package org.sabot.shared.utility;

import java.util.ArrayList;
import java.util.List;

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

public final class EmailUtils {
	
	private static final String emailPattern = "^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$";
	
	private EmailUtils(){
		// stay out
	}
	
	public static boolean isValidEmailAddress(String value) {
		if(value == null) return false;
 
		return value.matches(emailPattern);
	}
	
	public static List<String> splitEmails(String emailBlob){
		ArrayList<String> retval = new ArrayList<String>();
		for(String address : emailBlob.replaceAll("(\\r|\\n)", "").split(",")){
			retval.add(address);
		}
		return retval;
	}
}
