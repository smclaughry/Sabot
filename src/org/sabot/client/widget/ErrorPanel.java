package org.sabot.client.widget;

import org.sabot.client.messages.ErrorPanelMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
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
public class ErrorPanel extends Composite implements ErrorDisplay {

	@UiField ErrorStyle style;
	@UiField HTML errorTitle;
	@UiField FlowPanel errorItemPanel;

	interface ErrorStyle extends CssResource{
		public String errorItem();
	}
	
	private static ErrorPanelMessages messages = GWT.create(ErrorPanelMessages.class);
	private static ErrorDisplayUiBinder uiBinder = GWT.create(ErrorDisplayUiBinder.class);

	interface ErrorDisplayUiBinder extends UiBinder<Widget, ErrorPanel> {}

	public ErrorPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void addError(String error){
		HTML item = new HTML(error);
		item.addStyleName(style.errorItem());
		errorItemPanel.add(item);
	}
	
	@Override
	public void setErrorTitle(String title) {
		errorTitle.setText(title);
	}
	
	@Override
	public void clearErrors(){
		errorTitle.setText(messages.standardErrorTitle());
		errorItemPanel.clear();
		this.setVisible(false);
	}
}
