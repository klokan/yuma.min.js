package at.ait.dme.yumaJS.client.annotation.impl.seajax;

import java.util.HashMap;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.core.Annotatable;
import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonViewer;
import at.ait.dme.yumaJS.client.annotation.selection.ResizableBoxSelection;
import at.ait.dme.yumaJS.client.annotation.widgets.event.DeleteHandler;
import at.ait.dme.yumaJS.client.init.InitParams;
import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
	
	private AbsolutePanel annotationLayer;
	
	private SeadragonViewer viewer;
	
	private HashMap<Annotation, ZoomableAnnotationOverlay> annotations = 
		new HashMap<Annotation, ZoomableAnnotationOverlay>();

	public SeajaxAnnotationLayer(String id, JavaScriptObject deepZoomViewer) {
		this(id, deepZoomViewer, null);
	}
	
	public SeajaxAnnotationLayer(String id, JavaScriptObject deepZoomViewer, InitParams params) {
		super(params);
		
		Element div = DOM.getElementById(id);
		if (div == null)
			YUMA.fatalError("Error: no element with id '" + id + "' found on this page");
		
		if (!div.getTagName().toLowerCase().equals("div"))
			YUMA.fatalError("Error: you can only create a DeepZoomCanvas on a <div> element");
		
		if (deepZoomViewer == null) 
			YUMA.fatalError("Error: Seadragon viewer not found (not initialized yet?)");
			
		annotationLayer = new AbsolutePanel();
		annotationLayer.setVisible(false);
		annotationLayer.setStyleName("deepzoom-canvas");
		annotationLayer.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		annotationLayer.setPixelSize(div.getOffsetWidth(), div.getOffsetHeight());
		RootPanel.get().add(annotationLayer, div.getAbsoluteLeft(), div.getAbsoluteTop());
		
		viewer = new SeadragonViewer(deepZoomViewer);
	}
	
	public void newAnnotation() {
		annotationLayer.setVisible(true);
		final ResizableBoxSelection selection = new ResizableBoxSelection(annotationLayer, null);
		selection.addSaveClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addAnnotation(selection.getAnnotation(), null);
				selection.remove();
				annotationLayer.setVisible(false);
			}
		});
	}
	
	@Override
	protected void addAnnotation(final Annotation a, final Labels labels) {
		ZoomableAnnotationOverlay overlay = 
			new ZoomableAnnotationOverlay(a, viewer, labels);

		overlay.getDetailsPopup().addDeleteHandler(new DeleteHandler() {
			public void onDelete(Annotation annotation) {
				removeAnnotation(a, labels);
			}
		});
		
		annotations.put(a, overlay);
	}

	@Override
	protected void removeAnnotation(Annotation a, Labels labels) {
		ZoomableAnnotationOverlay overlay = annotations.get(a);
		if (overlay != null) {
			overlay.destroy();
			annotations.remove(a);
		}
	}

}
