package org.sabot.server.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Embedded;
import javax.persistence.Transient;

import com.google.inject.Provider;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Query;

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
public class ObjectifyGenericDao<T> {

	static final int BAD_MODIFIERS = Modifier.FINAL | Modifier.STATIC | Modifier.TRANSIENT;

    protected Class<T> clazz;

	private final Provider<Objectify> objectifyProvider;
	 
    /**
     * We've got to get the associated domain class somehow
     *
     * @param clazz
     */
    protected ObjectifyGenericDao(Class<T> clazz, Provider<Objectify> objectifyProvider){
        this.clazz = clazz;
		this.objectifyProvider = objectifyProvider;
    }
	 
    public Key<T> put(T entity){
        return ofy().put(entity);
    }
	 

	public Map<Key<T>, T> putAll(Iterable<T> entities){
        return ofy().put(entities);
    }
	 
    public void delete(T entity){
        ofy().delete(entity);
    }
	 
    public void deleteKey(Key<T> entityKey){
        ofy().delete(entityKey);
    }
	 
    public void deleteAll(Iterable<T> entities){
        ofy().delete(entities);
    }
	 
    public void deleteKeys(Iterable<Key<T>> keys){
        ofy().delete(keys);
    }
	 
    public T get(Long id) {
        return ofy().get(this.clazz, id);
    }
	 
    public T get(Key<T> key){
        return ofy().get(key);
    }
	
    public Map<Key<T>, T> get(List<Key<T>> keys){
        return ofy().get(keys);
    }

    public List<T> getAll(Iterable<Key<T>> keys){
    	return new ArrayList<T>(ofy().get(keys).values());
    }
    
    public T find(Long id) {
        return ofy().find(this.clazz, id);
    }
	 
    public T find(Key<T> key){
        return ofy().find(key);
    }
    
    public List<Key<T>> listAll(){
        Query<T> q = ofy().query(clazz);
        return q.listKeys();
    }
	
    /**
     * Convenience method to get all objects matching a single property
     *
     * @param propName
     * @param propValue
     * @return T matching Object
     */
    public T getByProperty(String propName, Object propValue){
        Query<T> q = ofy().query(clazz);
        q.filter(propName, propValue);
        return q.get();
    }
	 
    public List<T> listByProperty(String propName, Object propValue){
        Query<T> q = ofy().query(clazz);
        q.filter(propName, propValue);
        return q.list();
    }
	 
    public List<Key<T>> listKeysByProperty(String propName, Object propValue){
        Query<T> q = ofy().query(clazz);
        q.filter(propName, propValue);
        return asKeyList(q.fetchKeys());
    }
	 
    public T getByExample(T exampleObj){
        Query<T> queryByExample = buildQueryByExample(exampleObj);
        Iterable<T> iterableResults = queryByExample.list();
        Iterator<T> i = iterableResults.iterator();
        T obj = i.next();
        if (i.hasNext())
            throw new RuntimeException("Too many results");
        return obj;
    }
	 
    public List<T> listByExample(T exampleObj){
        Query<T> queryByExample = buildQueryByExample(exampleObj);
        return queryByExample.list();
    }
	 
    private List<Key<T>> asKeyList(Iterable<Key<T>> iterableKeys){
        ArrayList<Key<T>> keys = new ArrayList<Key<T>>();
        for (Key<T> key : iterableKeys){
            keys.add(key);
        }
        return keys;
    }
	 
    private Query<T> buildQueryByExample(T exampleObj){
        Query<T> q = ofy().query(clazz);
 
        // Add all non-null properties to query filter
        for (Field field : clazz.getDeclaredFields()){
            // Ignore transient, embedded, array, and collection properties
            if (field.isAnnotationPresent(Transient.class)
                || (field.isAnnotationPresent(Embedded.class))
                || (field.getType().isArray())
                || (Collection.class.isAssignableFrom(field.getType()))
                || ((field.getModifiers() & BAD_MODIFIERS) != 0)){
                continue;
            }
 
            field.setAccessible(true);
 
            Object value;
            try{
                value = field.get(exampleObj);
            }catch (IllegalArgumentException e){
                throw new RuntimeException(e);
            }catch (IllegalAccessException e){
                throw new RuntimeException(e);
            }

            if (value != null){
                q.filter(field.getName(), value);
            }
        }
        return q;
    }
    
    protected Objectify ofy() {
    	return objectifyProvider.get();
    }
}
