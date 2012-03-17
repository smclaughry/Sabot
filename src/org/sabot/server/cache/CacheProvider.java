package org.sabot.server.cache;

import java.util.Collections;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.inject.Inject;

import org.apache.commons.logging.Log;

import com.google.inject.Provider;

public class CacheProvider implements Provider<Cache>{

	private final Log logger;

	@Inject
	public CacheProvider(Log logger){
		this.logger = logger;
		
	}
	
	@Override
	public Cache get() {
		try {
			return CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}
	
}
