package at.ait.dme.yumaJS.client.annotation.impl.openlayers.api;

import com.google.gwt.core.client.JavaScriptObject;

public class Map extends JavaScriptObject {
	
	private JavaScriptObject map;
	
	public Map(JavaScriptObject map) { 
		this.map = map;
	}

	public void addBoxesLayer(BoxesLayer boxesLayer) {
		_addBoxesLayer(map, boxesLayer);
	}
	
	private native void _addBoxesLayer(JavaScriptObject map, BoxesLayer boxesLayer) /*-{
		map.addLayer(boxesLayer);
	}-*/;
	
}
