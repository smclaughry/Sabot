package org.sabot.shared.beans;

import java.util.Date;

import org.sabot.shared.utility.DateUtility;

import com.googlecode.objectify.annotation.Entity;

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

@Entity
public class DataPatch extends DatastoreObject<DataPatch> {

	private static final long serialVersionUID = 6946064446305649105L;

	private String patchInfo;
	private Date patchDate;

	public DataPatch(){
		//gwt
	}
	
	public DataPatch(String patchInfo, Date patchDate) {
		this();
		this.patchInfo = patchInfo;
		this.patchDate = DateUtility.cloneDate(patchDate);
	}

	public void setPatchInfo(String patchInfo) {
		this.patchInfo = patchInfo;
	}

	public String getPatchInfo() {
		return patchInfo;
	}

	public void setPatchDate(Date patchDate) {
		this.patchDate = DateUtility.cloneDate(patchDate);
	}

	public Date getPatchDate() {
		return DateUtility.cloneDate(patchDate);
	}

	@Override
	protected Class<DataPatch> getClassForKey() {
		return DataPatch.class;
	}
}
