package at.ait.dme.yumaJS.client;

import at.ait.dme.yumaJS.client.annotation.impl.html5media.audio.AudioPlayer;
import at.ait.dme.yumaJS.client.annotation.impl.image.ImageAnnotationLayer;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.OpenLayersAnnotationLayer;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.SeajaxAnnotationLayer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * The main entry point to the yuma.min.js library. Exports the API
 * and calls the window.onYUMAready JavaScript callback function
 * once yuma.min.js and all images on the page have been loaded.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class YUMA implements EntryPoint {

	public void onModuleLoad() {
		GWT.create(ImageAnnotationLayer.class);
		GWT.create(SeajaxAnnotationLayer.class);
		GWT.create(OpenLayersAnnotationLayer.class);
		GWT.create(AudioPlayer.class);
		
		Timer t = new Timer() {
			@Override
			public void run() {
				pollLoadStatus(this);
			}
		};
		pollLoadStatus(t);
	}
	
	private void pollLoadStatus(Timer t) {
		NodeList<Element> images = RootPanel.getBodyElement().getElementsByTagName("img");
		boolean complete = true;
		for (int i=0; i<images.getLength(); i++) {
			Image img = Image.wrap(images.getItem(i));
			if (img.getWidth() == 0) {
				complete = false;
				break;
			}
		}
		
		if (complete) {
			fireOnYUMAready();
		} else {
			t.schedule(100);
		}
	}
	
	private native void fireOnYUMAready() /*-{
		if ($wnd.onYUMAready && typeof $wnd.onYUMAready == 'function') $wnd.onYUMAready();
	}-*/;
	
	public static void fatalError(String msg) {
		// TODO present alert messages in a form that's visually nicer
		Window.alert(msg);
		throw new RuntimeException();
	}
	
}

