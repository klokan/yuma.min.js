package at.ait.dme.yumaJS.client.annotation.impl.seajax;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.RootPanel;

import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.editors.selection.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.impl.image.BoundingBoxOverlay;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonAnimationHandler;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonPoint;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonRect;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonViewer;
import at.ait.dme.yumaJS.client.annotation.widgets.DetailsPopup;
import at.ait.dme.yumaJS.client.init.Labels;

/**
 * An overlay that represents an annotation on a {@link SeajaxAnnotationLayer} by attaching a 
 * {@link BoundingBoxOverlay} to a Seadragon rectangle overlay and combining it with a movable
 * {@link DetailsPopup}.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class ZoomableAnnotationOverlay {
	
	private SeadragonViewer viewer;
	
	private BoundingBoxOverlay bboxOverlay;
	
	private Element bboxDiv;
	
	private DetailsPopup detailsPopup;
	
	public ZoomableAnnotationOverlay(Annotation a, SeadragonViewer viewer, Labels labels) {
		this.viewer = viewer;
		
		BoundingBox bbox = a.getFragment().getBoundingBox();
		bboxOverlay = new BoundingBoxOverlay(bbox);
		bboxDiv = bboxOverlay.getElement();
		DOM.sinkEvents(bboxDiv, Event.ONMOUSEOVER | Event.ONMOUSEOUT);
		Event.setEventListener(bboxDiv, new EventListener() {
			public void onBrowserEvent(Event event) {
				if (event.getTypeInt() == Event.ONMOUSEOVER) {
					RootPanel.get().setWidgetPosition(detailsPopup, 
							bboxDiv.getAbsoluteLeft(), 
							bboxDiv.getAbsoluteTop() +  bboxDiv.getOffsetHeight());
					detailsPopup.setVisible(true);
				} else if (event.getTypeInt() == Event.ONMOUSEOUT) {
					if (!detailsPopup.contains(event.getClientX(), event.getClientY()))
						detailsPopup.setVisible(false);
				}
			}
		});
		
		detailsPopup = new DetailsPopup(a, labels);
		detailsPopup.setVisible(false);
		
		SeadragonPoint anchor =
			viewer.pointFromPixel(SeadragonPoint.create(bbox.getX(), bbox.getY())); 
		SeadragonPoint bottomRight = 
			viewer.pointFromPixel(SeadragonPoint.create(bbox.getX() + bbox.getWidth(), bbox.getY() + bbox.getHeight()));
		
		viewer.addOverlay(bboxDiv, SeadragonRect.create(
				anchor.getX(), anchor.getY(), 
				bottomRight.getX() - anchor.getX(), 
				bottomRight.getY() - anchor.getY()));
		
		// TODO this means we're attaching a listener for EVERY annotation 
		// whereas we really only need to listen for those with visible 
		// popup-> make this more efficient!
		viewer.addAnimationtListener(new SeadragonAnimationHandler() {
			public void onAnimation() {
				if (detailsPopup.isVisible()) {
					RootPanel.get().setWidgetPosition(detailsPopup, 
							bboxDiv.getAbsoluteLeft(), 
							bboxDiv.getAbsoluteTop() +  bboxDiv.getOffsetHeight());
				}
			}
		});
		
		RootPanel.get().add(detailsPopup);
	}
	
	public DetailsPopup getDetailsPopup() {
		return detailsPopup;
	}
	
	public void destroy() {
		viewer.removeOverlay(bboxDiv);
		bboxOverlay.removeFromParent();
		detailsPopup.removeFromParent();
	}

}
