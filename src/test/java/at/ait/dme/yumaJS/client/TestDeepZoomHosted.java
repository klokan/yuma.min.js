package at.ait.dme.yumaJS.client;

import at.ait.dme.yumaJS.client.annotation.impl.seajax.SeajaxAnnotationLayer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;

public class TestDeepZoomHosted implements EntryPoint {

	private SeajaxAnnotationLayer canvas = null;
	
	public void onModuleLoad() {				
		final PushButton annotate = new PushButton("Add Note");
		annotate.setStyleName("toggle-annotation");
		annotate.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (canvas == null)
					initCanvas();
				
				canvas.createNewAnnotation();
			}
		});
		
		RootPanel.get().add(annotate);
	}
	
	private void initCanvas() {
		canvas = new SeajaxAnnotationLayer("viewer", getViewer());
	}
	
	private native JavaScriptObject getViewer() /*-{
		return $wnd.viewer;
	}-*/;

}
