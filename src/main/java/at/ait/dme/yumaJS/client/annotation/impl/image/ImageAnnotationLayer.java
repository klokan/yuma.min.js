package at.ait.dme.yumaJS.client.annotation.impl.image;

import java.util.HashMap;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.core.Annotatable;
import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.selection.ResizableBoxSelection;
import at.ait.dme.yumaJS.client.annotation.widgets.event.DeleteHandler;
import at.ait.dme.yumaJS.client.init.InitParams;
import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
		
		RootPanel.get().add(annotationLayer, e.getAbsoluteLeft(), e.getAbsoluteTop());
	}

	@Override
	protected void addAnnotation(final Annotation a, final Labels labels) {
		ImageAnnotationOverlay overlay = 
			new ImageAnnotationOverlay(a, annotationLayer.getElement(), labels);
		
		overlay.getDetailsPopup().addDeleteHandler(new DeleteHandler() {
			public void onDelete(Annotation annotation) {
				removeAnnotation(a, labels);
			}
		});
		
		annotations.put(a, overlay);
	}
	
	public void newAnnotation() {
		final ResizableBoxSelection selection = new ResizableBoxSelection(annotationLayer, null);
		selection.addSaveClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addAnnotation(selection.getAnnotation(), null);
				selection.remove();
			}
		});
	}

	@Override
	protected void removeAnnotation(Annotation a, Labels labels) {
		ImageAnnotationOverlay overlay = annotations.get(a);
		if (overlay != null) {
			overlay.destroy();
			annotations.remove(a);
		}
	}

}
