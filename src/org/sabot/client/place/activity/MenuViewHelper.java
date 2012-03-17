/**
 * 
 */
package org.sabot.client.place.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class MenuViewHelper{
	private final List<MenuItem> items = new ArrayList<MenuItem>();
	private final String selectedStyleName;
	private final MenuBar menuBar;
	
	public MenuViewHelper(MenuBar menuBar, String selectedStyleName){
		this.menuBar = menuBar;
		this.selectedStyleName = selectedStyleName;
		
	}

	public void clearMenuItems(){
		menuBar.clearItems();
		items.clear();
	}

	public void addMenuItem(MenuModel menuItem, boolean visible) {		
		addMenuChildren(menuBar, menuItem, visible);
	}
	
	public void selectMenuItemWithLabel(String label) {
		MenuItem selectItem = null;
		for(MenuItem item : items){
			if(label != null && item.getText() != null && label.equals(item.getText())){
				selectItem = item;
				selectItem.addStyleName(selectedStyleName);
			}else{
				item.removeStyleName(selectedStyleName);
			}
		}
		if(selectItem != null){
			MenuBar parentMenu = selectItem.getParentMenu();
			if(parentMenu != null){
				parentMenu.selectItem(selectItem);
			}
		}		
	}
			
	private void addMenuChildren(MenuBar parentBar, MenuModel menuItem, boolean visible){
		MenuItem addItem;
		if(menuItem.hasChildren()){
			MenuBar bar = new MenuBar();
			for(MenuModel child : menuItem.getChildren()){
				addMenuChildren(bar, child, true);
			}
			addItem = parentBar.addItem(menuItem.getText(), bar);
		}else{
			addItem = parentBar.addItem(menuItem.getText(), menuItem.getCommand());
		}
		addItem.setVisible(visible);
		items.add(addItem);
	}
	
	public void setMenuItemWithLabelVisible(String label, boolean visible) {
		for(MenuItem item : items){
			if(label != null && item.getText() != null && label.equals(item.getText())){
				item.setVisible(visible);
				break;
			}
		}		
	}
}