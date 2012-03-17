package org.sabot.client.widget;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class PopupMenu{
	private final PopupPanel popupPanel;
	private final PopupMenu.PopupMenuBar menuBar;
	private FlowPanel mainPanel;
	
	PopupMenu(PopupPanel popupPanel, PopupMenuBar menuBar){
		this.popupPanel = popupPanel;
		this.menuBar = menuBar;
		this.popupPanel.add(menuBar);
	}
	
	public PopupMenu(){
		this.popupPanel = new PopupPanel(true);
		mainPanel = new FlowPanel();
		this.menuBar = new PopupMenuBar(this.popupPanel, true);
		getMainPanel().add(this.menuBar);
		this.popupPanel.add(getMainPanel());		
	}

	public PopupMenu(Widget widget){
		this.popupPanel = new PopupPanel(true);
		mainPanel = new FlowPanel();
		mainPanel.add(widget);
		this.menuBar = new PopupMenuBar(this.popupPanel, true);
		getMainPanel().add(this.menuBar);
		this.popupPanel.add(getMainPanel());		
	}
	
	public PopupPanel getPopupPanel() {
		return popupPanel;
	}

	public MenuBar getMenuBar() {
		return menuBar;
	}

	public MenuBar createChildMenuBar(){
		return new PopupMenuBar(this.popupPanel, true);
	}
	
	public boolean hasMenuItems(){
		return menuBar.getItemCount() > 0;
	}
	
	public FlowPanel getMainPanel() {
		return mainPanel;
	}

	static class PopupMenuBar extends MenuBar {
		private final PopupPanel popupPanel;

		public PopupMenuBar(PopupPanel popupPanel, boolean autoHide){
			super(autoHide);
			this.popupPanel = popupPanel;
		}
		
		public int getItemCount(){
			return super.getItems().size();
		}

		@Override
		public MenuItem addItem(String text, boolean asHTML, Command cmd) {
			return super.addItem(text, asHTML, new ProxyCommand(popupPanel, cmd));
		}

		@Override
		public MenuItem addItem(String text, Command cmd) {
			return super.addItem(text, new ProxyCommand(popupPanel, cmd));
		}	
		
		private static class ProxyCommand implements Command{

			private final Command command;
			private final PopupPanel panel;

			public ProxyCommand(PopupPanel popupPanel, Command command){
				this.panel = popupPanel;
				this.command = command;	
			}
			
			@Override
			public void execute() {
				panel.hide();
				command.execute();
			}	
		}
	}
}