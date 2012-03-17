package org.sabot.client.widget;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class CollapsePanel extends Composite implements HasWidgets, HasText, HasHTML, HasClickHandlers {
	
	private static CollapsePanelUiBinder uiBinder = GWT.create(CollapsePanelUiBinder.class);

	interface CollapsePanelUiBinder extends UiBinder<Widget, CollapsePanel> {}

	@UiField HTML panelTitle;
	@UiField FlowPanel contents;

	@UiField Image expandArrow;
	@UiField Image collapseArrow;
	
	public CollapsePanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void add(Widget w) {
		contents.add(w);
	}

	@Override
	public void clear() {
		contents.clear();
	}

	@Override
	public Iterator<Widget> iterator() {
		return contents.iterator();
	}

	@Override
	public boolean remove(Widget w) {
		return contents.remove(w);
	}
	
	public void setExpanded(boolean isExpanded){
		contents.setVisible(isExpanded);
		expandArrow.setVisible(isExpanded);
		collapseArrow.setVisible(!isExpanded);
	}

	public boolean isExpanded(){
		return contents.isVisible();
	}
	
	@Override
	public String getText() {
		return panelTitle.getText();
	}

	@Override
	public void setText(String text) {
		panelTitle.setText(text);
	}

	@Override
	public String getHTML() {
		return panelTitle.getHTML();
	}

	@Override
	public void setHTML(String html) {
		panelTitle.setHTML(html);
	}
	
	@UiHandler("titlePanel")
	public void onTitleClicked(ClickEvent event){
		this.setExpanded(!this.isExpanded());
		this.fireEvent(event);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return this.addHandler(handler, ClickEvent.getType());
	}
}
