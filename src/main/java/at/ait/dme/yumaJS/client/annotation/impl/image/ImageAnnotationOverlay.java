package at.ait.dme.yumaJS.client.annotation.impl.image;

import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.core.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.widgets.DetailsPopup;
import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * An overlay that represents an annotation on an 
 * {@link ImageAnnotationLayer} by combining a 
 * {@link BoundingBoxOverlay} with a fix-location
 * {@link DetailsPopup}.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class ImageAnnotationOverlay extends Composite {
		
	private BoundingBoxOverlay bboxOverlay;
	
	private DetailsPopup detailsPopup;
	
	public ImageAnnotationOverlay(Annotation a, final Element canvas, Labels labels) {
		// The bounding box overlay
		final BoundingBox bbox = a.getFragment().getBoundingBox();
		bboxOverlay = new BoundingBoxOverlay(bbox);
		bboxOverlay.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				detailsPopup.setVisible(true);
			}
		});
		bboxOverlay.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				if (!detailsPopup.contains(
						event.getRelativeX(canvas) + canvas.getAbsoluteLeft(), 
						event.getRelativeY(canvas) + canvas.getAbsoluteTop()))
					
					detailsPopup.setVisible(false);
			}
		});
		
		// The details popup
		detailsPopup = new DetailsPopup(a, labels);
		detailsPopup.setVisible(false);
		
		RootPanel.get().add(bboxOverlay, bbox.getX() + canvas.getAbsoluteLeft(), 
				bbox.getY() + canvas.getAbsoluteTop());
		RootPanel.get().add(detailsPopup, bbox.getX() + canvas.getAbsoluteLeft(), 
				bbox.getY() + canvas.getAbsoluteTop() + bbox.getHeight());
	}
	
	public void destroy() {
		bboxOverlay.removeFromParent();
		detailsPopup.removeFromParent();
	}

}
