package at.ait.dme.yumaJS.client.annotation.impl.seajax;

import java.util.HashMap;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.editors.ResizableBoxEditor;
import at.ait.dme.yumaJS.client.annotation.editors.selection.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.editors.selection.Range;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonMouseHandler;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonViewer;
import at.ait.dme.yumaJS.client.annotation.widgets.DetailsPopup;
import at.ait.dme.yumaJS.client.annotation.widgets.event.DeleteHandler;
import at.ait.dme.yumaJS.client.annotation.widgets.event.EditHandler;
import at.ait.dme.yumaJS.client.init.InitParams;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * An implementation of {@link Annotatable} for an HTML DIV holding a 
 * Seadragon AJAX viewer instance.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
@Export
@ExportPackage("YUMA")
public class SeajaxAnnotationLayer extends Annotatable implements Exportable {
	
	private static final String MEDIATYPE = "IMAGE";
	
	private static String objectURI;
	
	private HTML parentDiv;
	
	private AbsolutePanel annotationLayer;
	
	private SeadragonViewer viewer;
	
	private HashMap<Annotation, ZoomableAnnotationOverlay> annotations = 
		new HashMap<Annotation, ZoomableAnnotationOverlay>();

	public SeajaxAnnotationLayer(String id, JavaScriptObject deepZoomViewer) {
		this(id, deepZoomViewer, null);
	}
	
	public SeajaxAnnotationLayer(String id, JavaScriptObject deepZoomViewer, InitParams params) {
		super(params);
		
		Element el = DOM.getElementById(id);
		if (el == null)
			YUMA.fatalError("Error: no element with id '" + id + "' found on this page");
		
		if (!el.getTagName().toLowerCase().equals("div"))
			YUMA.fatalError("Error: you can only create a DeepZoomCanvas on a <div> element");
		
		if (deepZoomViewer == null) 
			YUMA.fatalError("Error: Seadragon viewer not found (not initialized yet?)");
		
		parentDiv = HTML.wrap(el);
		
		annotationLayer = new AbsolutePanel();
		annotationLayer.setStyleName("deepzoom-canvas");
		annotationLayer.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		annotationLayer.setPixelSize(parentDiv.getOffsetWidth(), parentDiv.getOffsetHeight());		
		RootPanel.get().insert(annotationLayer, parentDiv.getAbsoluteLeft(), parentDiv.getAbsoluteTop(), 0);
		
		viewer = new SeadragonViewer(deepZoomViewer);
		viewer.addMouseHandler(new SeadragonMouseHandler() {
			public void onMouseOver() {
				parentDiv.addStyleName("hover");
				parentDiv.removeStyleName("no-hover");
			}
			
			public void onMouseOut() {
				parentDiv.removeStyleName("hover");
				parentDiv.addStyleName("no-hover");
			}
		});
		
		objectURI = viewer.getObjectURI();
	}
	
	@Override
	public String getObjectURI() {
		return objectURI;
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
		RootPanel.get().setWidgetPosition(annotationLayer, parentDiv.getAbsoluteLeft(), parentDiv.getAbsoluteTop());
	}
	
	@Override
	public void addAnnotation(Annotation a) {
		ZoomableAnnotationOverlay overlay = 
			new ZoomableAnnotationOverlay(a, this, viewer, getLabels());

		final Annotatable thisAnnotatable = this;
		
		DetailsPopup popup = overlay.getDetailsPopup();
		popup.addEditHandler(new EditHandler() {
			public void onEdit(Annotation annotation) {
				removeAnnotation(annotation);
				new ResizableBoxEditor(thisAnnotatable, annotationLayer, getLabels(), annotation);
			}
		});
		
		popup.addDeleteHandler(new DeleteHandler() {
			public void onDelete(Annotation annotation) {
				removeAnnotation(annotation);
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
		new ResizableBoxEditor(this, annotationLayer, getLabels());
	}
	
}
