package at.ait.dme.yumaJS.client.annotation.impl.openlayers.api;

import com.google.gwt.core.client.JavaScriptObject;

public class Pixel extends JavaScriptObject {
	
	protected Pixel() { }
	
	public static native Pixel create(int x, int y) /*-{
		return new $wnd.OpenLayers.Pixel(x, y);
	}-*/;
	
	public final native int getX() /*-{
		return this.x;
	}-*/;
	
	public final native int getY() /*-{
		return this.y;
	}-*/;

}
