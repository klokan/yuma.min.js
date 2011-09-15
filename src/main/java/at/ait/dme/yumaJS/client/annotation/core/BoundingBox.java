package at.ait.dme.yumaJS.client.annotation.core;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A bounding box which can be part of an annotation media fragment.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class BoundingBox extends JavaScriptObject {
	
	protected BoundingBox() { }
	
	public static native BoundingBox create(int left, int top, int width, int height) /*-{
		return { left: left, top: top, width: width, height: height };
	}-*/;
	
	public final native int getX() /*-{
		return this.left;
	}-*/;
	
	public final native int getY() /*-{
		return this.top;
	}-*/;
	
	public final native int getWidth() /*-{
		return this.width;
	}-*/;
	
	public final native int getHeight() /*-{
		return this.height;
	}-*/;

}
