package at.ait.dme.yumaJS.client;

import at.ait.dme.yumaJS.client.annotation.impl.openlayers.OpenLayersAnnotationLayer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;

public class TestOpenLayersHosted implements EntryPoint {

	public void onModuleLoad() {
		final OpenLayersAnnotationLayer annotationLayer = 
			new OpenLayersAnnotationLayer(getMap());
		
		PushButton annotate = new PushButton("Add Note");
		annotate.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				annotationLayer.createNewAnnotation();
			}
		});
		
		RootPanel.get().add(annotate);
	}
	
	private native JavaScriptObject getMap() /*-{
		return $wnd.map;
	}-*/;
	
}
