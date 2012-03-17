package org.sabot.client.util;

import org.sabot.client.widget.IsAndAcceptsOneWidget;
import org.sabot.client.widget.WidgetContainer;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public class WidgetWiringUtility {
	public static void wireItUp(AcceptsOneWidget panel, IsWidget widget, WidgetContainer... widgetContainers){
		AcceptsOneWidget parent = panel;
		for(WidgetContainer widgetContainer : widgetContainers){
			IsAndAcceptsOneWidget child = widgetContainer.getIsAndAcceptsOneWidget();
			parent.setWidget(child);
			parent = child;
		}
		parent.setWidget(widget);
	}
}
