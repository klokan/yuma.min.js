package at.ait.dme.yumaJS.client.annotation.impl.seajax.api;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Wraps the Seadragon.Rect API class.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class SeadragonRect extends JavaScriptObject {
	
	protected SeadragonRect() { }
	
	public static native SeadragonRect create(float x, float y, float width, float height) /*-{
		return new $wnd.Seadragon.Rect(x, y, width, height);
	}-*/;
	
	public final native float getX() /*-{
		return this.x;
	}-*/;

	public final native float getY() /*-{
		return this.y;
	}-*/;
	
	public final native float getWidth() /*-{
		return this.width;
	}-*/;
	
	public final native float getHeight() /*-{
		return this.height;
	}-*/;
	
}
