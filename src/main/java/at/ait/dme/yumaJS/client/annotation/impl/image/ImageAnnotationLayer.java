package at.ait.dme.yumaJS.client.annotation.impl.image;

import java.util.HashMap;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.core.Annotatable;
import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.editors.ResizableBoxEditor;
import at.ait.dme.yumaJS.client.annotation.widgets.event.DeleteHandler;
import at.ait.dme.yumaJS.client.init.InitParams;
import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * An implementation of {@link RubberbandAnnotatable} 
 * for an HTML IMAGE.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
@Export
@ExportPackage("YUMA")
public class ImageAnnotationLayer extends Annotatable implements Exportable {
	
	private AbsolutePanel annotationLayer;
	
	private HashMap<Annotation, ImageAnnotationOverlay> annotations = 
		new HashMap<Annotation, ImageAnnotationOverlay>();
	
	public ImageAnnotationLayer(String id) {
		this(id, null);
	}

	public ImageAnnotationLayer(String id, InitParams params) {	
		super(params);
		
		Element e = DOM.getElementById(id);
		if (e == null)
			YUMA.fatalError("Error: no element with id '" + id + "' found on this page");
		
		if (!e.getTagName().toLowerCase().equals("img"))
			YUMA.fatalError("Error: you can only create an ImageCanvas on an <img> element");

		annotationLayer = new AbsolutePanel();
		annotationLayer.setStyleName("image-canvas");		
		annotationLayer.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		annotationLayer.setPixelSize(e.getClientWidth(), e.getClientHeight());
		
		annotationLayer.addDomHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				annotationLayer.addStyleName("hover");
				annotationLayer.removeStyleName("no-hover");
			}
		}, MouseOverEvent.getType());
		
		annotationLayer.addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				annotationLayer.removeStyleName("hover");
				annotationLayer.addStyleName("no-hover");
			}
		}, MouseOutEvent.getType());

		
		RootPanel.get().add(annotationLayer, e.getAbsoluteLeft(), e.getAbsoluteTop());
	}

	@Override
	public void addAnnotation(final Annotation a) {
		ImageAnnotationOverlay overlay = 
			new ImageAnnotationOverlay(a, annotationLayer, getLabels());
		
		overlay.getDetailsPopup().addDeleteHandler(new DeleteHandler() {
			public void onDelete(Annotation annotation) {
				removeAnnotation(a, getLabels());
			}
		});
		
		annotations.put(a, overlay);
		fireOnAnnotationCreated(a);
	}
	
	public void newAnnotation() {
		new ResizableBoxEditor(this, annotationLayer, getLabels());
	}

	protected void removeAnnotation(Annotation a, Labels labels) {
		ImageAnnotationOverlay overlay = annotations.get(a);
		if (overlay != null) {
			overlay.destroy();
			annotations.remove(a);
		}
	}

}
