package org.sabot.client.widget;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class FixedMenuBar extends MenuBar {
	public void onBrowserEvent(Event event) {
	    super.onBrowserEvent(event);
	    
	    MenuItem item = myFindItem(DOM.eventGetTarget(event));
	    
	    if (item == null){ 
	    	return;
	    }
	    
	    if(DOM.eventGetType(event) == Event.ONMOUSEOUT){
	    	item.removeStyleName("gwt-MenuItem-selected");
	    }else if(DOM.eventGetType(event) == Event.ONMOUSEOVER){
	    	item.addStyleName("gwt-MenuItem-selected");
	    }
	}

	private MenuItem myFindItem(Element hItem) {
		for (MenuItem item : getItems()){
			if (DOM.isOrHasChild(item.getElement(), hItem)){
				return item;
			}
		}
		return null;
	}

	public FixedMenuBar() {
		super();
	}

	public FixedMenuBar(boolean autoClose) {
		super(autoClose);
	}
}
