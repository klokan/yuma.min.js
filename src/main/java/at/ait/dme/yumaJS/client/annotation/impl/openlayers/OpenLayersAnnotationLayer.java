package at.ait.dme.yumaJS.client.annotation.impl.openlayers;

import org.timepedia.exporter.client.Exportable;

import com.google.gwt.core.client.JavaScriptObject;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.core.Annotatable;
import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.BoxesLayer;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.Map;
import at.ait.dme.yumaJS.client.init.InitParams;

public class OpenLayersAnnotationLayer extends Annotatable implements Exportable {

	private static final String MEDIATYPE = "MAP";

	private BoxesLayer annotationLayer;
	
	public OpenLayersAnnotationLayer(JavaScriptObject map) {
		this(map, null);
	}
	
	public OpenLayersAnnotationLayer(JavaScriptObject openLayersMap, InitParams params) {
		super(params);

		if (openLayersMap == null) 
			YUMA.fatalError("Error: OpenLayers map undefined (not initialized yet?)");
		
		Map map = new Map(openLayersMap);
		
		// TODO make annotation layer name configurable via init params
		annotationLayer = BoxesLayer.create("Annotations");
		map.addBoxesLayer(annotationLayer);
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
