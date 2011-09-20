package at.ait.dme.yumaJS.client.annotation.impl.seajax;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.core.RubberbandAnnotatable;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonViewer;
import at.ait.dme.yumaJS.client.init.InitParams;
import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * An implementation of {@link RubberbandAnnotatable} 
 * for an HTML DIV holding a Seadragon AJAX viewer instance.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
@Export
@ExportPackage("YUMA")
public class RubberbandSeajaxAnnotationLayer extends RubberbandAnnotatable implements Exportable {
	
	private FlowPanel canvas;
	
	private SeadragonViewer viewer;

	public RubberbandSeajaxAnnotationLayer(String id, JavaScriptObject deepZoomViewer) {
		this(id, deepZoomViewer, null);
	}
	
	public RubberbandSeajaxAnnotationLayer(String id, JavaScriptObject deepZoomViewer, InitParams params) {
		super(params);
		
		Element div = DOM.getElementById(id);
		if (div == null)
			YUMA.fatalError("Error: no element with id '" + id + "' found on this page");
		
		if (!div.getTagName().toLowerCase().equals("div"))
			YUMA.fatalError("Error: you can only create a DeepZoomCanvas on a <div> element");
		
		if (deepZoomViewer == null) 
			YUMA.fatalError("Error: Seadragon viewer not found (not initialized yet?)");
			
		canvas = new FlowPanel();
		canvas.setVisible(false);
		canvas.setStyleName("deepzoom-canvas");
		canvas.setPixelSize(div.getOffsetWidth(), div.getOffsetHeight());
		addRubberbandSelection(canvas);
		RootPanel.get().add(canvas, div.getAbsoluteLeft(), div.getAbsoluteTop());
		
		viewer = new SeadragonViewer(deepZoomViewer);
	}
	
	public void activate() {
		canvas.setVisible(true);
	}
	
	public void deactivate() {
		canvas.setVisible(false);
	}
	
	public boolean isActivated() {
		return canvas.isVisible();
	}
	
	@Override
	protected void addAnnotation(Annotation a, Labels labels) {
		new ZoomableAnnotationOverlay(a, viewer, labels);
	}

}
