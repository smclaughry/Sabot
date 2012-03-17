package org.sabot.client.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.sabot.client.util.StatsCollector;
import org.sabot.shared.ClientActivityDescriptor;

import com.google.gwt.user.client.Command;

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
public class ClientActivityCollector {
	
	private final StatsCollector statsCollector = new StatsCollector();
	private final ArrayList<String> comments = new ArrayList<String>();
	private final HashMap<Date,Throwable> exceptions = new HashMap<Date, Throwable>();
	private Command reportCommand;
	
	public ClientActivityDescriptor report() {
		ClientActivityDescriptor retval = new ClientActivityDescriptor(statsCollector.report(), "", comments, exceptions);
		comments.clear();
		exceptions.clear();
		return retval;
	}

	public void logStats(String statistic, long value) {
		this.statsCollector.log(statistic, value);
	}
	
	public void logException(Throwable e) {
		this.exceptions.put(new Date(), e);
		this.reportNow();
	}

	public void recordComment(String comment) {
		this.comments.add(comment);
		this.reportNow();
	}
	
	private void reportNow() {
		if (null != reportCommand) {
			reportCommand.execute();
		}
	}

	public void clearComments(){
		this.comments.clear();
	}

	public void setReportCommand(Command reportCommand) {
		this.reportCommand = reportCommand;
	}
	
	public String reportStats() {
		return statsCollector.report();
	}
}
