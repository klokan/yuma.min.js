package at.ait.dme.yumaJS.client.annotation.impl.seajax.api;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Wraps the Seadragon.Point API class.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class SeadragonPoint extends JavaScriptObject {
	
	protected SeadragonPoint() { }
	
	public static native SeadragonPoint create(float x, float y) /*-{
		return new $wnd.Seadragon.Point(x, y);
	}-*/;
	
	public final native float getX() /*-{
		return this.x;
	}-*/;
	
	public final native float getY() /*-{
		return this.y;
	}-*/;

}
