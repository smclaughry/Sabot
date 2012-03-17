package org.sabot.client.widget;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.TextBox;

public class FilterTextBox extends TextBox implements BlurHandler, FocusHandler {

	private String promptText;
	private String promptStyleName;
	
	public FilterTextBox(){
		this.addBlurHandler(this);
		this.addFocusHandler(this);
	}

	@Override
	public void onFocus(FocusEvent event) {
		if(getText().equals(promptText)){
			setText("");
			if(promptStyleName != null){
				removeStyleName(promptStyleName);
			}
		}
	}

	@Override
	public void onBlur(BlurEvent event) {
		if(getText().trim().isEmpty()){
			setText(promptText == null ? getText() : promptText);
			if(promptStyleName != null){
				addStyleName(promptStyleName);
			}
		}
	}

	public void setPromptText(String promptText) {
		this.promptText = promptText;
		setText(promptText);
	}

	public String getPromptText() {
		return promptText;
	}

	public void setPromptStyleName(String promptStyleName) {
		this.promptStyleName = promptStyleName;
		this.addStyleName(promptStyleName);
	}

	public String getPromptStyleName() {
		return promptStyleName;
	}

	public void reset() {
		setText(promptText == null ? "" : promptText);
	}
	
	public String getFilterText(){
		return getText().equals(getPromptText()) ? "" : getText();
	}
}
