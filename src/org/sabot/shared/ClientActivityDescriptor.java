package org.sabot.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

public class ClientActivityDescriptor implements Serializable {
	private static final long serialVersionUID = 2627800186498574133L;
	private String performanceReport;
	private String usageReport;
	private ArrayList<String> comments;
	private TreeMap<Date, String> exceptions;
	
	ClientActivityDescriptor() {
		//GWTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT!
	}

	public ClientActivityDescriptor(String performanceReport, String usageReport, ArrayList<String> comments, HashMap<Date, Throwable> exceptions) {
		this.performanceReport = performanceReport;
		this.usageReport = usageReport;
		this.comments = new ArrayList<String>(comments);
		this.exceptions = translate(exceptions);
	}

	private TreeMap<Date, String> translate(Map<Date, Throwable> exceptions) {
		TreeMap<Date, String> retval = new TreeMap<Date,String>();
		
		for (Map.Entry<Date, Throwable> entry : exceptions.entrySet()) {
			retval.put(entry.getKey(), getStackTrace(entry.getValue()));
		}

		return retval;
	}

	private String getStackTrace(Throwable e) {
		if (null == e) {
			return "Null Throwable";	        
		} else {
			StringBuilder retval = new StringBuilder(e.toString());
	        for (Object line : e.getStackTrace()) {
	            retval.append(line + "\n");
	        }
	        return retval.toString();
		}
	}

	public boolean isEmpty() {
		return !((null != this.performanceReport && 0 != this.performanceReport.length())
			     || (null != this.usageReport && 0 != this.usageReport.length())
			     || (null != this.comments && 0 != this.comments.size())
			     || (null != this.exceptions && 0 != this.exceptions.size()));
	}

	public String getPerformanceReport() {
		return performanceReport;
	}

	public String getUsageReport() {
		return usageReport;
	}

	public ArrayList<String> getComments() {
		return comments;
	}

	public TreeMap<Date, String> getExceptions() {
		return exceptions;
	}
}
