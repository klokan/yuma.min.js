package at.ait.dme.yumaJS.client.annotation.impl.image;

import org.timepedia.exporter.client.Exportable;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.core.RubberbandAnnotatable;
import at.ait.dme.yumaJS.client.init.InitParams;
import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * An implementation of {@link RubberbandAnnotatable} 
 * for an HTML IMAGE.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class RubberbandImageAnnotationLayer extends RubberbandAnnotatable implements Exportable {
	
	private FlowPanel annotationLayer;
	
	private InlineHTML hint;
	
	public RubberbandImageAnnotationLayer(String id) {
		this(id, null);
	}

	public RubberbandImageAnnotationLayer(String id, InitParams params) {	
		super(params);
		
		Element e = DOM.getElementById(id);
		if (e == null)
			YUMA.fatalError("Error: no element with id '" + id + "' found on this page");
		
		if (!e.getTagName().toLowerCase().equals("img"))
			YUMA.fatalError("Error: you can only create an ImageCanvas on an <img> element");

		annotationLayer = new FlowPanel();
		annotationLayer.setStyleName("image-canvas");
		annotationLayer.setPixelSize(e.getClientWidth(), e.getClientHeight());
		
		hint = new InlineHTML("Click and Drag to add a note");
		annotationLayer.add(hint);
		
		addRubberbandSelection(annotationLayer);
		RootPanel.get().add(annotationLayer, e.getAbsoluteLeft(), e.getAbsoluteTop());
	}

	@Override
	protected void addAnnotation(Annotation a, Labels labels) {
		new ImageAnnotationOverlay(a, annotationLayer.getElement(), labels);
	}

}
