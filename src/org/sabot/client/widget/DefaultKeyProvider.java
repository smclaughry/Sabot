package org.sabot.client.widget;

import com.google.gwt.view.client.ProvidesKey;

public class DefaultKeyProvider<T> implements ProvidesKey<T>{ 

	private T defaultValue = null;

	public void setDefaultValue(T defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	@Override
	public Object getKey(T item) {
		if(item == null) {
			return defaultValue;
		}
		return item;
	}
	
}
