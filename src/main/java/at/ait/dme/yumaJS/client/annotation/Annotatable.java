package at.ait.dme.yumaJS.client.annotation;

import java.util.ArrayList;
import java.util.List;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;

import at.ait.dme.yumaJS.client.annotation.editors.selection.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.editors.selection.Range;
import at.ait.dme.yumaJS.client.init.InitParams;
import at.ait.dme.yumaJS.client.init.Labels;
import at.ait.dme.yumaJS.client.io.ListAll;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * An abstract base class with cross-cutting functionality for
 * all media annotation implementations.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public abstract class Annotatable implements Exportable {
	
	private InitParams initParams;

	private JavaScriptObject onAnnotationCreatedCallback = null;
	
	public Annotatable(InitParams params) {
		this.initParams = params;
		
		Window.addResizeHandler(new ResizeHandler() {
			public void onResize(ResizeEvent event) {
				onWindowResize(event.getWidth(), event.getHeight());
			}
		});
	}
	
	protected InitParams getInitParams() {
		return initParams;
	}
	
	protected Labels getLabels() {
		if (initParams == null)
			return null;
		
		return initParams.labels();
	}
	
	protected void fetchAnnotations() {
		ListAll.executeJSONP(getObjectURI(), new AsyncCallback<String>() {
			public void onSuccess(String result) {
				JSONArray json = JSONParser.parseStrict(result).isArray();
				if (json!= null) {
					List<Annotation> annotations = new ArrayList<Annotation>();
					for (int i=0; i<json.size(); i++) {
						annotations.add((Annotation) json.get(i).isObject().getJavaScriptObject());
					}
					System.out.println(annotations);
				}
			}
			
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public abstract String getObjectURI();
	
	public abstract String getMediaType();
	
	public abstract String toFragment(BoundingBox bbox, Range range);
	
	public abstract Range toRange(String fragment);
	
	public abstract BoundingBox toBounds(String fragment);
	
	protected abstract void onWindowResize(int width, int height);
	
	public abstract void addAnnotation(Annotation annotation);
	
	public abstract void removeAnnotation(Annotation annotation);
				
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
