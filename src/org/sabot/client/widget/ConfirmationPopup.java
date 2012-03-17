package org.sabot.client.widget;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ConfirmationPopup {
	public interface ConfirmationPopupBinder extends UiBinder<Widget, ConfirmationPopup> {}
	private static ConfirmationPopupBinder uiBinder = GWT.create(ConfirmationPopupBinder.class);

	private final DialogBox dialog;

	private Command command;

	@UiField Label actionText;
	@UiField ImageButton confirmButton;
	@UiField ImageButton cancelButton;

	@Inject
	public ConfirmationPopup(DialogBox dialog) {
		this.dialog = dialog;
		dialog.add(uiBinder.createAndBindUi(this));
		dialog.setGlassEnabled(true);
	}

	@UiHandler("confirmButton")
	void onConfirmClicked(ClickEvent event){
		command.execute();
		dialog.hide();
	}
	
	@UiHandler("cancelButton")
	void onCancelClicked(ClickEvent event){
		dialog.hide();		
	}
	
	public void present(String confirmText, String confirmButtonText, String cancelButtonText, Command command) {
		this.command = command;
		//this.actionText.setText(confirmText);
		dialog.setText(confirmText);
		dialog.setTitle(confirmText);
		this.confirmButton.setText(confirmButtonText);
		this.cancelButton.setText(cancelButtonText);
		dialog.center();
	}
}
