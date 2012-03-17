package org.sabot.client.place.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Command;

public class MenuModel {
	private final boolean hasChildren;
	private final String text;
	private final boolean isHTML;
	private List<MenuModel> children;
	private final Command command;
	
	public MenuModel(String text, Command command){
		this(text, false, command);
	}
	
	public MenuModel(String text, boolean isHTML, Command command){
		this.text = text;
		this.isHTML = isHTML;
		this.command = command;
		hasChildren = false;			
	}
	
	public MenuModel(String text, boolean isHTML){
		this.text = text;
		this.isHTML = isHTML;
		hasChildren = true;
		command = null;
		children = new ArrayList<MenuModel>();
	}
	
	public boolean hasChildren(){
		return hasChildren;
	}
	
	public List<MenuModel> getChildren(){
		return children;
	}

	public String getText() {
		return text;
	}

	public boolean isHTML() {
		return isHTML;
	}

	public Command getCommand() {
		return command;
	}
}