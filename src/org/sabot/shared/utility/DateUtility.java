package org.sabot.shared.utility;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

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

public final class DateUtility {
	private static final long MS_PER_SEC = 1000;
    private static final long MS_PER_MIN = MS_PER_SEC * 60;
    private static final long MS_PER_HOUR = MS_PER_MIN * 60;
    private static final long MS_PER_DAY = MS_PER_HOUR * 24;
	
	private DateUtility(){
		//go away
	}
	
	public static Date cloneDate(Date date){
		return date == null ? null : (Date) date.clone();
	}
	
	public static Date truncateToTime(Date date){
        long time = date.getTime();
        return new Date(time - truncateToDay(date).getTime());
    }
    
    @SuppressWarnings("deprecation")
	public static Date truncateToDay(Date date){
        return new Date(date.getYear(), date.getMonth(), date.getDate());
    }
    
    @SuppressWarnings("deprecation")
	public static Date truncateToMonth(Date date){
        return addDays(truncateToDay(date), 1 - date.getDate());
    }
    
    @SuppressWarnings("deprecation")
	public static Date truncateToYear(Date date){
        date = truncateToMonth(date);
        date.setMonth(0);
        return date;
    }
    
    public static Date addSeconds(Date date, int seconds){
        Date newDate = new Date(date.getTime() + (seconds * MS_PER_SEC));
        return newDate;
    }
    
    public static Date addMinutes(Date date, int minutes){
        Date newDate = new Date(date.getTime() + (minutes * MS_PER_MIN));
        return newDate;
    }
    
    public static Date addHours(Date date, int hours){
        Date newDate = new Date(date.getTime() + (hours * MS_PER_HOUR));
        return newDate;
    }
    
    @SuppressWarnings("deprecation")
	public static Date addDays(Date date, int days){
        Date newDate = new Date(date.getTime());
        newDate.setDate(date.getDate() + days);
        return newDate;
    }
    
    public static Date addMonths(Date date, int months){
        for (; months < 0; months++){
            Date roundedPriorMonth = addDays(truncateToMonth(date), -1);
            date = addDays(date, -getDaysInMonth(roundedPriorMonth)); 
        }
        for (; months > 0; months--){
            date = addDays(date, getDaysInMonth(date));
        }
        return date;
    }
    
    @SuppressWarnings("deprecation")
	public static int getDaysInMonth(Date date){
        return getDaysInMonth(date.getYear() + 1900, date.getMonth());
    }
    
    public static int getDaysInMonth(int year, int month){
        switch (month){
            case 1:
                return (((year % 4) == 0 && (year % 100) != 0) || (year % 400) == 0) ? 29 : 28;
    
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
                
            default:
                return 31;
        }
    }

    /**
     * Determines the number of days between two dates, always rounding up so a difference of 1 day 1 second
     * yield a return value of 2.
     */
    public static int dayDiff(Date endDate, Date startDate){
         return (int)Math.ceil(((double)endDate.getTime() - startDate.getTime()) / MS_PER_DAY);
    }
    
    public static Date today(){
	    return zeroTime(new Date());
	}

	/** this is important to get rid of the time portion, including ms */
	public static Date zeroTime(final Date date){
	    return DateTimeFormat.getFormat("yyyyMMdd").parse(DateTimeFormat.getFormat("yyyyMMdd").format(date));
	}
}
