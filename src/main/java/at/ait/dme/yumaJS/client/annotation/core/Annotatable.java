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
	
	private InitParams initParams;
	
	private JavaScriptObject onAnnotationCreated = null;
	
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
	
	@Export
	public void addAnnotationCreatedListener(JavaScriptObject callback) {
		this.onAnnotationCreated = callback;
	};
	
	protected void fireOnAnnotationCreated(Annotation a) {
		if (onAnnotationCreated != null)
			_fireOnAnnotationCreated(a, onAnnotationCreated);
	}
	
	private native void _fireOnAnnotationCreated(Annotation a, JavaScriptObject callback) /*-{
		callback(a);
	}-*/;
	
}
