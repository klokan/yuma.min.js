package at.ait.dme.yumaJS.client.io;

import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

import at.ait.dme.yumaJS.client.annotation.Annotation;

public class Create {
	
	private static final String JSONP_PATH = "api/annotation/jsonp/create?json=";
	
	public static void executeJSONP(String serverURL, Annotation a, AsyncCallback<String> callback) {
		JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
		String json = URL.encodeQueryString(new JSONObject(a).toString());
		jsonp.requestString(serverURL + JSONP_PATH + json, callback);
	}

}
