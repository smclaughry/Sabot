package org.sabot.server.cache;

import javax.cache.Cache;

public class CacheHelper {

	public interface CachedCommand<T>{
		public T execute();
	}
	
	private CacheHelper(){
		//hide me
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T getFromCache(String key, Cache cache){
		if(cache == null){
			return null;
		}
		return (T) cache.get(key);
	}
	
	@SuppressWarnings("unchecked")
	private static void addToCache(String key, Object value, Cache cache){
		if(cache != null){
			cache.put(key, value);
		}
	}
	
	public static <T> T getValue(String key, Cache cache, CachedCommand<T> command){
		@SuppressWarnings("unchecked")
		T value = (T) getFromCache(key, cache);
		if(value == null){
			value = command.execute();
			if(value != null){
				addToCache(key, value, cache);
			}
		}
		return value;
	}
}
