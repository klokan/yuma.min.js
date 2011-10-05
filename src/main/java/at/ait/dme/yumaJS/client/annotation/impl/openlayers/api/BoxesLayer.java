package at.ait.dme.yumaJS.client.annotation.impl.openlayers.api;

import com.google.gwt.core.client.JavaScriptObject;

public class BoxesLayer extends JavaScriptObject {
	
	protected BoxesLayer() { }
	
	public static native BoxesLayer create(String name) /*-{
		return new $wnd.OpenLayers.Layer.Boxes(name);
	}-*/;

}
