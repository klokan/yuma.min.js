package at.ait.dme.yumaJS.client.annotation.impl.seajax;

import java.util.HashMap;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.core.Annotatable;
import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.editors.ResizableBoxEditor;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonViewer;
import at.ait.dme.yumaJS.client.annotation.widgets.event.DeleteHandler;
import at.ait.dme.yumaJS.client.init.InitParams;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * An implementation of {@link RubberbandAnnotatable} 
 * for an HTML DIV holding a Seadragon AJAX viewer instance.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
@Export
@ExportPackage("YUMA")
public class SeajaxAnnotationLayer extends Annotatable implements Exportable {
	
	private Element parentDiv;
	
	private AbsolutePanel annotationLayer;
	
	private SeadragonViewer viewer;
	
	private HashMap<Annotation, ZoomableAnnotationOverlay> annotations = 
		new HashMap<Annotation, ZoomableAnnotationOverlay>();

	public SeajaxAnnotationLayer(String id, JavaScriptObject deepZoomViewer) {
		this(id, deepZoomViewer, null);
	}
	
	public SeajaxAnnotationLayer(String id, JavaScriptObject deepZoomViewer, InitParams params) {
		super(params);
		
		parentDiv = DOM.getElementById(id);
		if (parentDiv == null)
			YUMA.fatalError("Error: no element with id '" + id + "' found on this page");
		
		if (!parentDiv.getTagName().toLowerCase().equals("div"))
			YUMA.fatalError("Error: you can only create a DeepZoomCanvas on a <div> element");
		
		if (deepZoomViewer == null) 
			YUMA.fatalError("Error: Seadragon viewer not found (not initialized yet?)");
			
		annotationLayer = new AbsolutePanel();
		annotationLayer.setVisible(false);
		annotationLayer.setStyleName("deepzoom-canvas");
		annotationLayer.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		annotationLayer.setPixelSize(parentDiv.getOffsetWidth(), parentDiv.getOffsetHeight());
		RootPanel.get().insert(annotationLayer, parentDiv.getAbsoluteLeft(), parentDiv.getAbsoluteTop(), 0);
		
		viewer = new SeadragonViewer(deepZoomViewer);
	}
		
	@Override
	protected void onWindowResize(int width, int height) {
		RootPanel.get().setWidgetPosition(annotationLayer, parentDiv.getAbsoluteLeft(), parentDiv.getAbsoluteTop());
	}
	
	@Override
	public void addAnnotation(final Annotation a) {
		annotationLayer.setVisible(false);
		ZoomableAnnotationOverlay overlay = 
			new ZoomableAnnotationOverlay(a, viewer, getLabels());

		overlay.getDetailsPopup().addDeleteHandler(new DeleteHandler() {
			public void onDelete(Annotation annotation) {
				removeAnnotation(a);
			}
		});
		
		annotations.put(a, overlay);
	}

	@Override
	public void removeAnnotation(Annotation a) {
		ZoomableAnnotationOverlay overlay = annotations.get(a);
		if (overlay != null) {
			overlay.destroy();
			annotations.remove(a);
		}
	}
	
	public void createNewAnnotation() {
		annotationLayer.setVisible(true);
		new ResizableBoxEditor(this, annotationLayer, getLabels());
	}
	

}
