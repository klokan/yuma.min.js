package at.ait.dme.yumaJS.client.annotation.core;

import at.ait.dme.yumaJS.client.annotation.editors.selection.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.editors.selection.Range;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * An annotation media fragment.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class Fragment extends JavaScriptObject {
	
	protected Fragment() { }

	public static native Fragment create(BoundingBox bbox) /*-{
		return { bbox: bbox };
	}-*/;

	public static native Fragment create(Range range) /*-{
		return { range: range };
	}-*/;
	
	public static native Fragment create(BoundingBox bbox, Range range) /*-{
		return { bbox: bbox, range: range };
	}-*/;
	
	public final native BoundingBox getBoundingBox() /*-{
		return this.bbox;
	}-*/;

	public final native Range getRange() /*-{
		return this.range;
	}-*/;

}
