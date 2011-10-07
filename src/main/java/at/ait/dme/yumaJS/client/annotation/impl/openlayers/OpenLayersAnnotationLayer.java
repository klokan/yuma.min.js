package at.ait.dme.yumaJS.client.annotation.impl.openlayers;

import org.timepedia.exporter.client.Exportable;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.editors.ResizableBoxEditor;
import at.ait.dme.yumaJS.client.annotation.editors.selection.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.editors.selection.Range;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.Bounds;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.BoxMarker;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.BoxesLayer;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.LonLat;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.Map;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.Pixel;
import at.ait.dme.yumaJS.client.init.InitParams;

public class OpenLayersAnnotationLayer extends Annotatable implements Exportable {

	private static final String MEDIATYPE = "MAP";

	private Map map;
	
	private BoxesLayer annotationLayer;
	
	private AbsolutePanel editingLayer;
	
	public OpenLayersAnnotationLayer(JavaScriptObject map) {
		this(map, null);
	}
	
	public OpenLayersAnnotationLayer(JavaScriptObject openLayersMap, InitParams params) {
		super(params);

		if (openLayersMap == null) 
			YUMA.fatalError("Error: OpenLayers map undefined (not initialized yet?)");
		
		map = new Map(openLayersMap);
		
		// TODO make annotation layer name configurable via init params
		annotationLayer = BoxesLayer.create("Annotations");
		map.addBoxesLayer(annotationLayer);
		
		HTML parentDiv = map.getDiv();
		editingLayer = new AbsolutePanel();
		editingLayer.setStyleName("openlayers-editing-layer");
		editingLayer.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		editingLayer.setPixelSize(parentDiv.getOffsetWidth(), parentDiv.getOffsetHeight());		
		RootPanel.get().insert(editingLayer, parentDiv.getAbsoluteLeft(), parentDiv.getAbsoluteTop(), 0);
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
	public String toFragment(BoundingBox bbox, Range range) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Range toRange(String fragment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BoundingBox toBounds(String fragment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onWindowResize(int width, int height) {
		RootPanel.get().setWidgetPosition(editingLayer, map.getDiv().getAbsoluteLeft(), map.getDiv().getAbsoluteTop());
	}

	@Override
	public void addAnnotation(Annotation annotation) {
		// TODO this transformation needs to be done inside the
		// editor - otherwise we'll run into conflicts with
		// addMethod calls from server-side AJAX load
		BoundingBox bbox = toBounds(annotation.getFragment());
		Pixel topLeft = Pixel.create(bbox.getX(), bbox.getY());
		Pixel bottomRight = Pixel.create(topLeft.getX() + bbox.getWidth(),
				topLeft.getY() + bbox.getHeight());
		
		LonLat llTopLeft = map.getLonLatFromPixel(topLeft);
		LonLat llBottomRight = map.getLonLatFromPixel(bottomRight);
		
		Bounds bounds = Bounds.create(
				llTopLeft.getLon(),
				llBottomRight.getLat(),
				llBottomRight.getLon(),
				llTopLeft.getLat());
		
		annotationLayer.addMaker(BoxMarker.create(bounds));
	}

	@Override
	public void removeAnnotation(Annotation annotation) {
		// TODO Auto-generated method stub
		
	}

	public void createNewAnnotation() {
		new ResizableBoxEditor(this, editingLayer);
	}
	
}
