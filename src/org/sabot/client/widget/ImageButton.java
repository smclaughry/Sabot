package org.sabot.client.widget;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasText;
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
public class ImageButton extends Composite implements HasClickHandlers, HasText, Focusable {
	public interface ImageButtonBinder extends UiBinder<Widget, ImageButton>{};
	
	private static ImageButtonBinder binder = GWT.create(ImageButtonBinder.class);
	
	@UiField Button button;
	
	private String text;
	private String imgUrl;

	@Inject
	public ImageButton() {
		this.initWidget(binder.createAndBindUi(this));
	}

	@Override
	public void setText(String text) {
		this.text = text;
		setupHtml();
	}

	@Override
	public String getText() {
		return (null == text) ? "" : text;
	}
	
	public void setImageResource(ImageResource resource) {
		this.imgUrl = AbstractImagePrototype.create(resource).getHTML();
		setupHtml();
	}

	private void setupHtml() {
		String imgHtml = (null == imgUrl || imgUrl.isEmpty()) ? "" : imgUrl;
		button.setHTML(imgHtml+getText());
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return button.addClickHandler(handler);
	}

	@Override
	public int getTabIndex() {
		return button.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		button.setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		button.setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		button.setTabIndex(index);
	}
	
	public void setEnabled(boolean enabled) {
		button.setEnabled(enabled);
	}
}
