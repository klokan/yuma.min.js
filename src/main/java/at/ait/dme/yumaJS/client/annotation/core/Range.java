package at.ait.dme.yumaJS.client.annotation.core;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A (temporal) range which can be part of an annotation media fragment.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class Range extends JavaScriptObject {

	protected Range() { }
	
	public static native Range create(double from, double to) /*-{
		return { from: from, to: to };
	}-*/;

	public final native double getFrom() /*-{
		return this.from;
	}-*/;

	public final native double getTo() /*-{
		return this.to;
	}-*/;

}
