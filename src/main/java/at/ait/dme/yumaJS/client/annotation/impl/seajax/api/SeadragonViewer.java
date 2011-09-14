package at.ait.dme.yumaJS.client.annotation.impl.seajax.api;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

/**
 * Wraps some relevant methods of Seadragon.Viewer API class. 
 * (For convenience, some of the methods of the Viewer.viewport
 * class are directly included in this wrapper.)
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class SeadragonViewer {

	private JavaScriptObject viewer;
	
	public SeadragonViewer(JavaScriptObject viewer) {
		this.viewer = viewer;
	}
	
	public SeadragonPoint pointFromPixel(SeadragonPoint p) {
		return _pointFromPixel(viewer, p);
	}
	
	private native SeadragonPoint _pointFromPixel(JavaScriptObject viewer, SeadragonPoint p) /*-{
		return viewer.viewport.pointFromPixel(p, true);
	}-*/;
	
	public void addOverlay(Element el, SeadragonRect rect) {
		_addOverlay(viewer, el, rect);
	}
	
	private native void _addOverlay(JavaScriptObject viewer, Element el, SeadragonRect rect) /*-{
		viewer.drawer.addOverlay(el, rect);
	}-*/;
	
	public void addAnimationtListener(SeadragonAnimationHandler listener) {
		_addAnimationListener(viewer, listener);
	}
	
	private native void _addAnimationListener(JavaScriptObject viewer, SeadragonAnimationHandler handler) /*-{
		viewer.addEventListener('animation', function() {
			handler.@at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonAnimationHandler::onAnimation()();
		});
	}-*/;
	
}
