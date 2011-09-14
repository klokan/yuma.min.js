package at.ait.dme.yumaJS.client.annotation.core;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The annotation.
 *  
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class Annotation extends JavaScriptObject {
	
	protected Annotation() { }
	
	public static native Annotation create(Fragment fragment, String text) /*-{
		return { fragment: fragment, text: text };
	}-*/;
	
	public final native Fragment getFragment() /*-{
		return this.fragment;
	}-*/;
	
	public final native String getText() /*-{
		return this.text;
	}-*/;

}
