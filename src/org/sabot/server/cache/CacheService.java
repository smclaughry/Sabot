package org.sabot.server.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.cache.Cache;
import javax.cache.CacheStatistics;
import javax.inject.Inject;

import org.sabot.shared.binding.PropertyKeys;

import com.google.inject.Provider;


public class CacheService {
	public interface CachedCommand<T>{
		public T execute();
	}
	
	public interface KeyGenerator<T>{
		public String generateKey(T value);
		public String generateKey(Long id);
	}
	
	private final String defaultSegment;
	private final Cache cache;
	
	@Inject
	public CacheService(@PropertyKeys.DefaultCacheSegmentName String defaultSegment, Provider<Cache> cacheProvider){
		this.defaultSegment = defaultSegment;
		this.cache = cacheProvider.get();
	}
	
	private Object getFromCache(String key){
		return cache.get(key);
	}

	@SuppressWarnings("unchecked")
	private void addToCache(String key, Object value){
		cache.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getSegmentMap(String segmentKey){
		Map<String, Object> segmentMap = (Map<String, Object>) getFromCache(segmentKey);
		if(segmentMap == null){
			segmentMap = new HashMap<String, Object>();
			addToCache(segmentKey, segmentMap);
		}
		return segmentMap;
	}

	public void put(String key, Object value){
		Map<String, Object> segmentMap = getSegmentMap(defaultSegment);
		segmentMap.put(key, value);
	}
	
	public void put(String segmentKey, String key, Object value){
		Map<String, Object> segmentMap = getSegmentMap(segmentKey);
		segmentMap.put(key, value);
	}
	
	public <T> List<T> putAll(KeyGenerator<T> keyGenerator, List<T> values){
		return putAll(defaultSegment, keyGenerator, values);
	}
	
	public <T> List<T> putAll(String segmentKey, KeyGenerator<T> keyGenerator, List<T> values){
		for(T value : values){
			put(segmentKey, keyGenerator.generateKey(value), value);
		}
		return values;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(String segmentKey, String key, CachedCommand<T> command)  {
		Map<String, Object> segmentMap = getSegmentMap(segmentKey);
		Object item = segmentMap.get(key);
		if(item == null && command != null){
			item = command.execute();
			segmentMap.put(key, item);
		}
		return (T) item;
	}
	
	public <T> T getValue(String key, CachedCommand<T> command) {
		return getValue(defaultSegment, key, command);
	}
	
	public void clear(String segmentKey, String key){
		getSegmentMap(segmentKey).remove(key);
	}
	
	public void clear(String segmentKey){
		cache.remove(segmentKey);
	}
	
	public void clear(){
		cache.clear();
	}

	public CacheStatistics getCacheStatistics() {
		return cache.getCacheStatistics();
	}
}