package at.ait.dme.yumaJS.client.annotation.core;

import at.ait.dme.yumaJS.client.annotation.selection.RubberbandSelection;
import at.ait.dme.yumaJS.client.annotation.selection.Selection;
import at.ait.dme.yumaJS.client.annotation.selection.SelectionHandler;
import at.ait.dme.yumaJS.client.init.InitParams;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Panel;

/**
 * An extension of {@link Annotatable}, specifically for annotatable media that 
 * have spatial fragments, i.e. images, zoomable images, or video.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public abstract class RubberbandAnnotatable extends Annotatable {
	
	public RubberbandAnnotatable(InitParams params) {
		super(params);
	}

	protected void addRubberbandSelection(final Panel annotationLayer) {
		annotationLayer.addDomHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				final Element el = annotationLayer.getElement();
				final Selection rubberband = RubberbandSelection.setStart(el, event.getRelativeX(el), event.getRelativeY(el));
				rubberband.setSelectionHandler(new SelectionHandler() {
					public void onSelect(Fragment fragment) {
						BoundingBox selection = fragment.getBoundingBox();
						showEditForm(
								selection.getX() + annotationLayer.getAbsoluteLeft() - 4, 
								selection.getY() + annotationLayer.getAbsoluteTop() + selection.getHeight(),
								rubberband);
					}
				});
			}
		}, MouseDownEvent.getType());
	}

}
