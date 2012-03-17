package org.sabot.client.service.oauth;

import com.google.gwt.core.client.JavaScriptObject;


public class JsonUtility {
	private static int requestCount = 0;
	
	private JsonUtility() {
		// No.  Fuck off.
	}
	
	public static void getJson(String url, JSOCallback jsoCallback) {
		String dynamicCallback = "callback" + (requestCount++);
		String delimiterIJustMetHer =  url.contains("?") ? "&" : "?";
		String dynamicUrl = url+delimiterIJustMetHer+"callback="+dynamicCallback;
		getJson(dynamicCallback, dynamicUrl, jsoCallback);
	}
	
	private native static void getJson(String callback, String url, JSOCallback jsoCallback) /*-{
	   	// Create a script element.
	   	var script = document.createElement("script");
	   	script.setAttribute("src", url);
	   	script.setAttribute("type", "text/javascript");
	
	   	// Define the callback function on the window object.
	   	window[callback] = function(jsonObj) {
	   		//alert("got : "+JSON.stringify(jsonObj));
	    	jsoCallback.@org.sabot.client.service.oauth.JSOCallback::onSuccess(Lcom/google/gwt/core/client/JavaScriptObject;)(jsonObj);
	    	window[callback + "done"] = true;
		}
	
	   	// JSON download has 5-second timeout.
	   	setTimeout(function() {
	    	if (!window[callback + "done"]) {
	       		jsoCallback.@org.sabot.client.service.oauth.JSOCallback::onFailure(Ljava/lang/String;)("Timeout");
	     	}
	
			// Cleanup. Remove script and callback elements.
	    	document.body.removeChild(script);
	    	delete window[callback];
	    	delete window[callback + "done"];
	   	}, 5000);
	
	   	// Attach the script element to the document body.
	   	document.body.appendChild(script);
	}-*/;
	
   public static native String stringify(JavaScriptObject obj) /*-{
       return $wnd.JSON.stringify(obj);
   }-*/;

   /*
    * Takes in a trusted JSON String and evals it.
    * @param JSON String that you trust
    * @return JavaScriptObject that you can cast to an Overlay Type
    */
   public static native JavaScriptObject parseJson(String jsonStr) /*-{
   		return eval("(" + jsonStr + ")");
   }-*/;
}
