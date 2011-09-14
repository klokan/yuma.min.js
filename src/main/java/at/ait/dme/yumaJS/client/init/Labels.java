package at.ait.dme.yumaJS.client.init;

import at.ait.dme.yumaJS.client.annotation.core.Annotatable;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Wraps a JavaScript object literal that contains
 * custom labels to be used by implementations
 * of {@link Annotatable}.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class Labels extends JavaScriptObject {

	protected Labels() { }
	
	public final native String save() /*-{
		return this.save;
	}-*/;
	
	public final native String cancel() /*-{
		return this.cancel;
	}-*/;
	
	public final native String reply() /*-{
		return this.reply;
	}-*/;
	
	public final native String edit() /*-{
		return this.edit;
	}-*/;
	
	public final native String delete() /*-{
		return this['delete'];
	}-*/;
	
}
