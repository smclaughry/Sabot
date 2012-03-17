package org.sabot.client.widget;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.ListBox;

public class TextListBox extends ListBox implements HasText{

	@Override
	public String getText() {
		if(this.getSelectedIndex() == -1){
			return "";
		}
		return this.getValue(this.getSelectedIndex());
	}

	@Override
	public void setText(String text) {
		this.setSelectedIndex(-1);
		if(text != null){
			for(int x = 0; x < this.getItemCount(); x++){
				if(this.getValue(x).equals(text)){
					this.setSelectedIndex(x);
					break;
				}
			}
		}
	}
}