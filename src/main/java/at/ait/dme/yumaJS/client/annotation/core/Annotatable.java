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
	
	protected InitParams initParams;
	
	private JavaScriptObject onAnnotationCreated = null;
	
	public Annotatable(InitParams params) {
		this.initParams = params;
	}
	
	protected abstract void addAnnotation(Annotation a, Labels labels);
	
	protected abstract void removeAnnotation(Annotation a, Labels labels);
	
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
