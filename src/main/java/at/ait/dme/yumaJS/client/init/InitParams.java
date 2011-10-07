package at.ait.dme.yumaJS.client.init;

import at.ait.dme.yumaJS.client.annotation.Annotatable;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Wraps a JavaScript object literal that carries initialization
 * information to be used by implementations of {@link Annotatable}.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class InitParams extends JavaScriptObject {

	protected InitParams() { }
	
	public final native Labels labels() /*-{
		return this.labels;
	}-*/;
	
	public final native int width() /*-{
		if (this.width)
			return this.width;
		return -1;
	}-*/;
	
	public final native String iconPath() /*-{
		return this.icons;
	}-*/;
	
}
