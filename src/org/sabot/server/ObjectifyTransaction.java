package org.sabot.server;

import java.util.ConcurrentModificationException;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.EntityNotFoundException;
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
public class ObjectifyTransaction {
	
	public interface RetryAround {
		void doIt(ObjectifyTransaction tranny);
	}
	
	private final Objectify delegate;

	public ObjectifyTransaction(Objectify delegate) {
		this.delegate = delegate;
	}

	public void retryTransaction(RetryAround theWork) throws ConcurrentModificationException {
		this.retryTransaction(theWork, 3);
	}
	
	public void retryTransaction(RetryAround theWork, int retries) throws ConcurrentModificationException {
		while(true){
			try{
				theWork.doIt(this);
				this.commit();
				break;
			} catch(ConcurrentModificationException exception){
				if(retries == 0){
					throw exception;
				}
				--retries;
			} finally {
				if(this.isActive()){
					this.rollback();
				}
			}
		}
	}

	public void delete(Iterable<?> arg0) {
		delegate.delete(arg0);
	}

	public void delete(Object arg0) {
		delegate.delete(arg0);
	}

	public <T> T find(Class<? extends T> arg0, long arg1) {
		return delegate.find(arg0, arg1);
	}

	public <T> T find(Class<? extends T> arg0, String arg1) {
		return delegate.find(arg0, arg1);
	}

	public <T> T find(Key<? extends T> arg0) {
		return delegate.find(arg0);
	}

	public <T> Map<?, T> get(Class<? extends T> arg0, Iterable<?> arg1) {
		return delegate.get(arg0, arg1);
	}

	public <T> T get(Class<? extends T> arg0, long arg1)
			throws EntityNotFoundException {
		return delegate.get(arg0, arg1);
	}

	public <T> T get(Class<? extends T> arg0, String arg1)
			throws EntityNotFoundException {
		return delegate.get(arg0, arg1);
	}

	public <T> Map<Key<T>, T> get(Iterable<? extends Key<? extends T>> arg0) {
		return delegate.get(arg0);
	}

	public <T> T get(Key<? extends T> arg0) throws EntityNotFoundException {
		return delegate.get(arg0);
	}

	public DatastoreService getDatastore() {
		return delegate.getDatastore();
	}

	public <T> Map<Key<T>, T> put(Iterable<? extends T> arg0) {
		return delegate.put(arg0);
	}

	public <T> Key<T> put(T arg0) {
		return delegate.put(arg0);
	}

	public <T> Query<T> query() {
		return delegate.query();
	}

	public <T> Query<T> query(Class<T> arg0) {
		return delegate.query(arg0);
	}
	
	public boolean isActive() {
		return delegate.getTxn().isActive();
	}
	
	public void commit() {
		delegate.getTxn().commit();
	}
	
	public void rollback() {
		delegate.getTxn().rollback();
	}
}
