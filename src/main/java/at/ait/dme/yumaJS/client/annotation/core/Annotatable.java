package at.ait.dme.yumaJS.client.annotation.core;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;

import at.ait.dme.yumaJS.client.init.InitParams;
import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * An abstract base class with cross-cutting functionality for
 * all media annotation implementations.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public abstract class Annotatable implements Exportable {
	
	/**
	 * Initialization parameters handed to the Annotatable on instantiation
	 */
	private InitParams initParams;
	
	/**
	 * The onAnnotationCreated callback function 
	 */
	private JavaScriptObject onAnnotationCreatedCallback = null;
	
	public Annotatable(InitParams params) {
		this.initParams = params;
	}
	
	/**
	 * Returns the init parameters for this {@link Annotatable} or
	 * <code>null</code> if none were provided.
	 * @return the init params or <code>null</code>
	 */
	protected InitParams getInitParams() {
		return initParams;
	}
	
	/**
	 * Returns the labels provided as part of the init parameters for
	 * this {@link Annotatable} or <code>null</code> if no init params
	 * were provided, or the init params did not contain labels.
	 * @return the labels or <code>null</code>
	 */
	protected Labels getLabels() {
		if (initParams == null)
			return null;
		
		return initParams.labels();
	}
	
	/**
	 * Adds a callback function for catching 'annotationCreated' events
	 * @param callback the callback function
	 */
	@Export
	public void addAnnotationCreatedListener(JavaScriptObject callback) {
		this.onAnnotationCreatedCallback = callback;
	};
	
	protected void fireOnAnnotationCreated(Annotation a) {
		if (onAnnotationCreatedCallback != null)
			_fireOnAnnotationCreated(a, onAnnotationCreatedCallback);
	}
	
	private native void _fireOnAnnotationCreated(Annotation a, JavaScriptObject callback) /*-{
		callback(a);
	}-*/;
	
}
