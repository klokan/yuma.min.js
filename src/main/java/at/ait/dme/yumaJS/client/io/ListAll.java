package at.ait.dme.yumaJS.client.io;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ListAll {
	
	private static final String JSONP_PATH = "api/annotation/jsonp/list?objectURI=";
	
	public static void executeJSONP(String serverURL, String objectURI, AsyncCallback<JavaScriptObject> callback) {
		JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
		jsonp.requestObject(serverURL + JSONP_PATH + objectURI, callback);
	}

}
