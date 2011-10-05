package at.ait.dme.yumaJS.client.annotation.impl.openlayers;

import org.timepedia.exporter.client.Exportable;

import com.google.gwt.core.client.JavaScriptObject;

import at.ait.dme.yumaJS.client.annotation.core.Annotatable;
import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.init.InitParams;

public class OpenLayersAnnotationLayer extends Annotatable implements Exportable {

	private static final String MEDIATYPE = "MAP";

	public OpenLayersAnnotationLayer(JavaScriptObject map) {
		this(map, null);
	}
	
	public OpenLayersAnnotationLayer(JavaScriptObject map, InitParams params) {
		super(params);
	}

	@Override
	public String getObjectURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMediaType() {
		return MEDIATYPE;
	}

	@Override
	protected void onWindowResize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAnnotation(Annotation annotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAnnotation(Annotation annotation) {
		// TODO Auto-generated method stub
		
	}

	public void createNewAnnotation() {
		// TODO implement
	}
	
}
