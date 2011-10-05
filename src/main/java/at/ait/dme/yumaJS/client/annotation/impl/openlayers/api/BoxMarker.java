package at.ait.dme.yumaJS.client.annotation.impl.openlayers.api;

import com.google.gwt.core.client.JavaScriptObject;

public class BoxMarker extends JavaScriptObject {

	protected BoxMarker() { }
	
	public static native BoxMarker create(Bounds bounds) /*-{
		return new $wnd.OpenLayers.Marker.Box(bounds);
	}-*/;
	
}
