package at.ait.dme.yumaJS.client;

import at.ait.dme.yumaJS.client.annotation.impl.image.ImageAnnotationLayer;
import at.ait.dme.yumaJS.client.init.InitParams;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;

public class TestImageHosted implements EntryPoint {

	public void onModuleLoad() {
		final ImageAnnotationLayer annotationLayer = 
			new ImageAnnotationLayer("annotateMe", createInitParams());
		
		PushButton annotate = new PushButton("Add Note");
		annotate.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				annotationLayer.createNewAnnotation();
			}
		});
		
		RootPanel.get().add(annotate);
	}
	
	private native InitParams createInitParams() /*-{
		return {
			labels:{
				'save': 'SPEICHERN',
				'cancel': 'ABBRECHEN',
				'reply': 'ANTWORTEN',
				'edit': 'BEARBEITEN',
				'delete': 'L\u00d6SCHEN'
			}
		};
	}-*/;

}
