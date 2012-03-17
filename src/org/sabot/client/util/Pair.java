package org.sabot.client.util;

import java.io.Serializable;

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
public class Pair<S extends Serializable, T extends Serializable> implements Serializable {
	private static final long serialVersionUID = 2898389671031076471L;

	private S first;
	private T second;
	
	Pair() {
		// GWT
	}

	public Pair(S first,T second) {
		this.first = first;
		this.second = second;
		
	}

	public S getFirst() {
		return first;
	}

	public T getSecond() {
		return second;
	}
}
