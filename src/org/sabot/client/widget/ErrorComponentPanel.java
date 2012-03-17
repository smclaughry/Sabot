package org.sabot.client.widget;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

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
public class ErrorComponentPanel extends Composite implements HasWidgets, HasErrorIndication{

	public interface ErrorComponentPanelBinder extends UiBinder<Widget, ErrorComponentPanel>{};

	@UiField FlowPanel childHolder;

	public ErrorComponentPanel(){
		ErrorComponentPanelBinder binder = GWT.create(ErrorComponentPanelBinder.class);
		initWidget(binder.createAndBindUi(this));
	}
	
	@Override
	public void add(Widget w) {
		childHolder.add(w);
	}

	@Override
	public void clear() {
		childHolder.clear();
	}

	@Override
	public Iterator<Widget> iterator() {
		return childHolder.iterator();
	}

	@Override
	public boolean remove(Widget w) {
		return childHolder.remove(w);
	}
	
	public void setInError(boolean isInError){
		if(isInError){
			childHolder.addStyleName("errorPanel");
		}else{
			childHolder.removeStyleName("errorPanel");
		}
	}
	
	public boolean isInError(){
		return childHolder.getStyleName().contains("errorPanel");
	}
}
