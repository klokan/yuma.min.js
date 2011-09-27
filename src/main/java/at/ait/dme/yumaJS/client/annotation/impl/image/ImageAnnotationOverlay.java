package at.ait.dme.yumaJS.client.annotation.impl.image;

import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.core.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.widgets.DetailsPopup;
import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;

/**
 * An overlay that represents an annotation on an {@link ImageAnnotationLayer}
 * by combining a {@link BoundingBoxOverlay} with a fixed-location
 * {@link DetailsPopup}.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class ImageAnnotationOverlay {
		
	private BoundingBoxOverlay bboxOverlay;
	
	private DetailsPopup detailsPopup;
	
	public ImageAnnotationOverlay(Annotation a, final AbsolutePanel annotationLayer, Labels labels) {
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
						event.getRelativeX(annotationLayer.getElement()) + annotationLayer.getAbsoluteLeft(), 
						event.getRelativeY(annotationLayer.getElement()) + annotationLayer.getAbsoluteTop()))
					
					detailsPopup.setVisible(false);
			}
		});
		
		detailsPopup = new DetailsPopup(a, labels);
		detailsPopup.setVisible(false);
		
		annotationLayer.add(bboxOverlay, bbox.getX(), bbox.getY());
		annotationLayer.add(detailsPopup, bbox.getX(), bbox.getY() + bbox.getHeight());
	}
	
	public DetailsPopup getDetailsPopup() {
		return detailsPopup;
	}
	
	public void destroy() {
		bboxOverlay.removeFromParent();
		detailsPopup.removeFromParent();
	}

}
