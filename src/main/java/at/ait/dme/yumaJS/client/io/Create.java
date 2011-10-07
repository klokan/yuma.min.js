package at.ait.dme.yumaJS.client.io;

import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

import at.ait.dme.yumaJS.client.annotation.Annotation;

public class Create {
	
	public static void executeJSONP(Annotation a, AsyncCallback<String> callback) {
		JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
		String json = URL.encodeQueryString(new JSONObject(a).toString());
		jsonp.requestString("http://localhost:8080/yuma4j-server/api/annotation/jsonp/create?json=" + json, callback);
	}

}
