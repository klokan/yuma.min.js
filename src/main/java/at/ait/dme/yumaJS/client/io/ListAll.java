package at.ait.dme.yumaJS.client.io;

import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ListAll {
	
	public static void executeJSONP(String objectURI, AsyncCallback<String> callback) {
		JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
		jsonp.requestString("http://localhost:8080/yuma4j-server/api/annotation/jsonp/list?objectURI=" + objectURI, callback);
	}

}
