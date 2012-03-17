package org.sabot.server.inject.interceptor;

import java.lang.reflect.Method;

import javax.cache.CacheStatistics;
import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sabot.server.cache.CacheService;
import org.sabot.server.utility.StatsLogger;
import org.sabot.server.utility.StatsLogger.LogLevel;

import com.google.appengine.api.quota.QuotaService;
import com.google.appengine.api.quota.QuotaServiceFactory;

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

public class PerformanceMonitorInterceptor implements MethodInterceptor {
	private static final int REPORT_INTERVAL = 30 * 60 * 1000;
	private static final Log LOGGER = LogFactory.getLog(PerformanceMonitorInterceptor.class);
	private static final LogLevel LEVEL = LogLevel.WARN;

	private final StatsLogger statsLogger = new StatsLogger();

	private long lastReportTime;
	private	CacheService cacheService;
	
	@Inject
	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
        long sysStart = System.currentTimeMillis();
        
        int previousHits;
        int previousMisses;
        try {
        	previousHits = (null == cacheService) ? 0 : cacheService.getCacheStatistics().getCacheHits();
        	previousMisses = (null == cacheService)? 0 : cacheService.getCacheStatistics().getCacheMisses();
        } catch (Exception e) {
        	previousHits = 0;
        	previousMisses = 0;
        }

		try {
			return invocation.proceed();
		} finally {
			Class<?> clazz = invocation.getThis().getClass();
			Method method = invocation.getMethod();
			
	        logTimes(sysStart, clazz, method);
			logCacheUsage(clazz, method, previousHits, previousMisses);
	        
	        if (lastReportTime + REPORT_INTERVAL < System.currentTimeMillis()) {
	        	statsLogger.report(LOGGER, LEVEL);
	        	lastReportTime = System.currentTimeMillis();
	        }
		}
	}

	private void logTimes(long sysStart, Class<?> clazz, Method method) {
		QuotaService qs = QuotaServiceFactory.getQuotaService();
		long sysEnd = System.currentTimeMillis();
		double cpuSeconds = qs.convertMegacyclesToCpuSeconds(qs.getCpuTimeInMegaCycles());
		double apiSeconds = qs.convertMegacyclesToCpuSeconds(qs.getApiTimeInMegaCycles());
		long sysMillis = sysEnd - sysStart;
		if (0 < cpuSeconds) {
			statsLogger.log(clazz.getName()+"."+method.getName()+" CPU", cpuSeconds);
		} else {
			statsLogger.count(clazz.getName()+"."+method.getName()+" CPU 0");
		}
		if (0 < apiSeconds) {
			statsLogger.log(clazz.getName()+"."+method.getName()+" API", apiSeconds);
		} else {
			statsLogger.count(clazz.getName()+"."+method.getName()+" API 0");
		}
		if (0 < sysMillis) {
			statsLogger.log(clazz.getName()+"."+method.getName()+" SYS", sysMillis);
		} else {
			statsLogger.count(clazz.getName()+"."+method.getName()+" SYS 0");
		}
	}
	
	private void logCacheUsage(Class<?> clazz, Method method, int previousHits, int previousMisses) {
		if (null != cacheService) {
			try {
				CacheStatistics stats = cacheService.getCacheStatistics();
				statsLogger.log(clazz.getName()+"."+method.getName()+" cache hits", stats.getCacheHits() - previousHits);        
				statsLogger.log(clazz.getName()+"."+method.getName()+" cache misses", stats.getCacheMisses() - previousMisses);
			} catch (Exception ignore) {
				// It's not great that we're ignoring this, but AppEngine's memcache stats code seems to blow up sometimes.
			}
		}
	}
}
