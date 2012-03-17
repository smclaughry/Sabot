package org.sabot.client.util;

import org.sabot.client.widget.HasErrorIndication;
import org.sabot.shared.utility.EmailUtils;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;

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
public final class ValidatorUtility {
	
	private static final String ZIPCODE_PATTERN = "\\d{5}(?(?=-)-\\d{4})";
	
	private ValidatorUtility(){
		//keep out
	}
	
	public static boolean isValidContent(HasText hasText){
		if(hasText == null || hasText.getText() == null || hasText.getText().trim().isEmpty()){
			return false;
		}
		return true;
	}
	
	public static boolean isValidNumericContent(HasText hasText, HasErrorIndication errorIndicator){
		boolean valid = isValidNumericContent(hasText);
		errorIndicator.setInError(!valid);
		return valid;
	}
	
	public static boolean isValidNumericContent(HasText hasText){
		if(hasText == null || hasText.getText() == null || hasText.getText().trim().isEmpty()){
			return false;
		}else {
			try {
				Double.parseDouble(hasText.getText());
			}catch (Exception e) {
				return false;
			}
		}
		return true;
	}
	
	private static boolean isValidContent(HasValue<?> hasValue) {
		if(hasValue == null || hasValue.getValue() == null){
			return false;
		}
		return true;
	}
		
	public static boolean isValidContent(HasText hasText, HasErrorIndication errorIndicator){
		boolean valid = isValidContent(hasText);
		errorIndicator.setInError(!valid);
		return valid;
	}
	
	public static boolean isValidContent(HasValue<?> hasValue, HasErrorIndication errorIndicator){
		boolean valid = isValidContent(hasValue);
		errorIndicator.setInError(!valid);
		return valid;
	}
	

	public static boolean isValidEmailAddress(HasText hasText){
		return isValidContent(hasText)
			&& EmailUtils.isValidEmailAddress(hasText.getText());
	}
	
	public static boolean isValidEmailAddress(HasText hasText, HasErrorIndication errorIndicator){
		boolean valid = isValidEmailAddress(hasText);
		errorIndicator.setInError(!valid);
		return valid;
	}
	
	public static boolean isValidMatchingContent(HasText first, HasText second){
		return isValidContent(first) 
			&& isValidContent(second)
			&& first.getText().equals(second.getText());
	}
	
	public static boolean isValidZipCode(HasText hasText, HasErrorIndication errorIndicator){
		return isValidContent(hasText)
			&& isValidZipCode(hasText.getText());
	}
	
	public static boolean isValidZipCode(String value) {
		if(value == null) return false;
 
		return value.matches(ZIPCODE_PATTERN);
	}
}
