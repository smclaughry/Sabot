package org.sabot.client.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

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
public class StatsCollector implements Serializable {
	private static final long serialVersionUID = -7188667265876848217L;

    private final HashMap<String,StatStruct> _values = new HashMap<String,StatStruct>();
    private final HashMap<String,Count> _counts = new HashMap<String,Count>();

    public void log(String statistic, long value) {
        try {
            StatStruct s = (StatStruct)_values.get(statistic);
            if (null == s) {
                synchronized(this) {
                    s = (StatStruct)_values.get(statistic);
                    if (null == s) { // Get it again, make sure we're the only ones doing this
                        s = new StatStruct(statistic);
                        _values.put(statistic,s);
                    } // if
                } // synch
            } // if
            s.log(value);
        } catch (Throwable t) {
            // Logging should never crash the system, so we'll just print it and keep going.
            //Help! Can't do this.  logger.info("StatsLogger.log exception, ignoring",t);
        }
    }

	public void log(String statistic, double value) {
        try {
            StatStruct s = (StatStruct)_values.get(statistic);
            if (null == s) {
                synchronized(this) {
                    s = (StatStruct)_values.get(statistic);
                    if (null == s) { // Get it again, make sure we're the only ones doing this
                        s = new StatStruct(statistic);
                        _values.put(statistic,s);
                    } // if
                } // synch
            } // if
            s.log(value);
        } catch (Throwable t) {
            // Logging should never crash the system, so we'll just print it and keep going.
            //Help! Can't do this.  logger.info("StatsLogger.log exception, ignoring",t);
        }
	}

	public void count(String statistic) {
        try {
            Count c = (Count)_counts.get(statistic);
            if (null == c) {
                synchronized(this) {
                    c = (Count)_counts.get(statistic);
                    if (null == c) { // Get it again, make sure we're the only ones doing this
                        c = new Count(statistic);
                        _counts.put(statistic,c);
                    } // if
                } // synch
            } // if
            c.inc();
        } catch (Throwable t) {
            // Logging should never crash the system, so we'll just print it and keep going.
                //Help! Can't do this.  logger.info("StatsLogger.count exception, ignoring",t);
        }
    }

    public String report() {
        try {
            boolean doReport = false;
            StringBuffer allstats = new StringBuffer("Stats report");

            synchronized(this) {
                for (StatStruct s : _values.values()) {
                    if (s.somethingToReport()) {
                        allstats.append("\n  ").append(s.report());
                        doReport = true;
                    }
                }

                for (Count c : _counts.values()) {
                    if (c.somethingToReport()) {
                        allstats.append("\n  ").append(c.report());
                        doReport = true;
                    }
                }
            } // synch

            if(doReport){
            	return allstats.toString();
            } else {
                return "";
            }
        } catch (Throwable t) {
            // Logging should never crash the system, so we'll just print it and keep going.
            //Help! Can't do this.  logger.info("StatsLogger.report exception, ignoring",t);
            return "";
        }
    }

    /**
     * Inner class holds stat name, min, max, average, and count.
     */
    static class StatStruct {
        public StatStruct(String statname_) {
            statname = statname_;
        }

        private void log(double value) {
            if (value < min){
                min = value;
                timeOfMin = System.currentTimeMillis();
            }
            if (value > max){
                max = value;
                timeOfMax = System.currentTimeMillis();
            }
            sum += value;
            squareSum += (value * value);
            count++;
		}

		private /*synchronized*/ void log(long value) {
            if (value < min){
                min = value;
                timeOfMin = System.currentTimeMillis();
            }
            if (value > max){
                max = value;
                timeOfMax = System.currentTimeMillis();
            }
            sum += value;
            squareSum += (value * value);
            count++;
        }

        private /*synchronized*/ String report() {
            String retval;
            if (count > 0){
                double mean = (sum/(double)count);
                double stdDev;
                double unrounded = Math.sqrt((squareSum / (double)count) - (mean * mean));
                if (Double.isNaN(unrounded)){
                	stdDev = 0;
                }else{
                	stdDev = unrounded;
                }
                retval = statname+" (over "+count+" reports) min: "+min+", max:"+max+", avg: "+mean+", standard dev: "+stdDev;
                retval += "\n  Time of min : " + new Date(timeOfMin) + ", Time of max : " + new Date(timeOfMax);
            }else{
                retval = statname+" had 0 reports, no statistics generated";
            }
            min = Long.MAX_VALUE;
            max = Long.MIN_VALUE;
            sum = 0;
            squareSum = 0;
            count = 0;
            timeOfMin = 0;
            timeOfMax = 0;
            return retval;
        }

        private boolean somethingToReport(){
            if(count != 0){
            	return true;
            }
            return false;
        }

        private final String statname;
        private double min = Long.MAX_VALUE;
        private double max = Long.MIN_VALUE;
        private long sum = 0;
        private long squareSum = 0;
        private long count = 0;
        private long timeOfMax = 0;
        private long timeOfMin = 0;
    } // class StatStruct

    /**
     * Inner class to hold count
     */
    static class Count {
    	
    	private final String statname;
    	private int count = 0;
    	
        public Count(String statname_) {
            statname = statname_;
        }

        private void inc() {
            count++;
        }

        private boolean somethingToReport(){
        	if(count != 0){
        		return true;
        	}
        	return false;
        }

        private String report() {
            int c = count;
            count = 0;
            return "Count of "+statname+": "+c;
        }

    } // class Count
}
