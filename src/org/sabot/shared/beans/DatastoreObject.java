package org.sabot.shared.beans;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.PrePersist;

import com.google.appengine.api.datastore.KeyFactory;
import com.googlecode.objectify.Key;

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
public abstract class DatastoreObject<T> implements Serializable, HasId<T>{
	private static final long serialVersionUID = 4796069210589784446L;

	@Id private Long id;
	private Integer version = 0;

	/**
	 * Auto-increment version # whenever persisted
	 */
	@PrePersist
	void onPersist(){
		this.version++;
	}
	
	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Integer getVersion(){
		return version;
	}
	
	public void setVersion(Integer version){
		this.version = version;
	}

	@Override
	public Key<T> getKey() {
		return keyFor(getClassForKey(), id);
	}
	
	public  static final <T> Key<T> keyFor(Class<T> clazz, Long id){
		String kind = clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);
		return id == null ? null : new Key<T>(KeyFactory.createKey(null, kind, id));
	}
	
	public static <T> Key<T> keyFor(Class<T> clazz, String key) {
		try{
			return keyFor(clazz, Long.parseLong(key));
		}catch(NumberFormatException nfe){
			return null;
		}
	}
	
	protected abstract Class<T> getClassForKey();
}
