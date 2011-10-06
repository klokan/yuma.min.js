package at.ait.dme.yumaJS.client.annotation.impl.openlayers.api;

import com.google.gwt.core.client.JavaScriptObject;

public class Bounds extends JavaScriptObject {
	
	protected Bounds() { }
	
	public static native Bounds create(double left, double bottom, double right, double top) /*-{
		return new $wnd.OpenLayers.Bounds(left, bottom, right, top);
	}-*/;
	
}
