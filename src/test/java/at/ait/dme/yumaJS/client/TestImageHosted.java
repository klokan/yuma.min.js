package at.ait.dme.yumaJS.client;

import at.ait.dme.yumaJS.client.annotation.impl.image.ImageAnnotationLayer;
import at.ait.dme.yumaJS.client.init.InitParams;

import com.google.gwt.core.client.EntryPoint;

public class TestImageHosted implements EntryPoint {

	public void onModuleLoad() {
		new ImageAnnotationLayer("annotateMe", createInitParams());
		// canvas.addAnnotationCreatedListener(createCallback());
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
