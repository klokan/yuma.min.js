package at.ait.dme.yumaJS.client;

import at.ait.dme.yumaJS.client.annotation.impl.seajax.SeajaxAnnotationLayer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;

public class TestDeepZoomHosted implements EntryPoint {
	
	private static String BTN_LABEL_ACTIVATED = "STOP ANNOTATING";
	private static String BTN_LABEL_DEACTIVATED = "START ANNOTATING";

	private SeajaxAnnotationLayer canvas = null;
	
	public void onModuleLoad() {				
		final PushButton toggleAnnotation = new PushButton(BTN_LABEL_DEACTIVATED);
		toggleAnnotation.setStyleName("toggle-annotation");
		toggleAnnotation.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (canvas == null)
					initCanvas();
				
				if (canvas.isActivated()) {
					canvas.deactivate();
					toggleAnnotation.getUpFace().setText(BTN_LABEL_DEACTIVATED);
				} else {
					canvas.activate();
					toggleAnnotation.getUpFace().setText(BTN_LABEL_ACTIVATED);					
				}
			}
		});
		
		RootPanel.get().add(toggleAnnotation);
	}
	
	private void initCanvas() {
		canvas = new SeajaxAnnotationLayer("viewer", getViewer());
	}
	
	private native JavaScriptObject getViewer() /*-{
		return $wnd.viewer;
	}-*/;

}
