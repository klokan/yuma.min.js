package at.ait.dme.yumaJS.client.annotation.impl.openlayers.api;

import com.google.gwt.core.client.JavaScriptObject;

public class LonLat extends JavaScriptObject {
	
	protected LonLat() { }
	
	public final native double getLon() /*-{
		return this.lon;
	}-*/;

	public final native double getLat() /*-{
		return this.lat;
	}-*/;
	
}
