package at.ait.dme.yumaJS.client.annotation.selection;

import at.ait.dme.yumaJS.client.annotation.core.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.core.Fragment;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * An implementation of {@link Selection} that provides 'rubberband 
 * selection' behavior to select spatial (rectangular) fragments
 * on annotatable media.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class RubberbandSelection extends Selection {
	
	/**
	 * The Rubberband is singleton!
	 */
	private static RubberbandSelection instance = null;
	
	/**
	 * The parent element - all coordinates are relative to this!
	 */
	private Element parent;
	
	/**
	 * The border DIVs
	 */
	private FlowPanel outerBorder, innerBorder;
	
	/**
	 * The anchor point coordinates
	 */
	private int anchorX, anchorY;
	
	/**
	 * The coordinates of the point opposite the anchor
	 */
	private int oppositeX, oppositeY;
	
	/**
	 * The mousemove DOM handler registration
	 */
	private HandlerRegistration mouseMoveRegistration = null;
	
	/**
	 * The mouseup DOM handler registration
	 */
	private HandlerRegistration mouseUpRegistration = null;
	
	public static RubberbandSelection setStart(Element parentElement, int x, int y) {
		if (instance != null)
			instance.clear();
			
		instance = new RubberbandSelection(parentElement, x, y);
		return instance;
	}
	
	private RubberbandSelection(Element parentEl, int x, int y) {
		this.parent = parentEl;
		anchorX = x;
		anchorY = y;
		oppositeX = x;
		oppositeY = y;
		
		outerBorder = new FlowPanel();
		outerBorder.setStyleName("rubberband-outer");
		innerBorder = new FlowPanel();
		innerBorder.setWidth("100%");
		innerBorder.setHeight("100%");
		innerBorder.setStyleName("rubberband-inner");
		outerBorder.add(innerBorder);
		RootPanel r = RootPanel.get();
		disableTextSelection(r.getElement());
		r.add(outerBorder, x + parentEl.getAbsoluteLeft(), y + parentEl.getAbsoluteTop());
	}
	
	@Override
	public void setSelectionHandler(final SelectionHandler handler) {
		RootPanel r = RootPanel.get();
		mouseMoveRegistration = r.addDomHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				oppositeX = event.getRelativeX(parent);
				oppositeY = event.getRelativeY(parent);				
				setDimensions(oppositeX, oppositeY);
			}
		}, MouseMoveEvent.getType());
		
		mouseUpRegistration = r.addDomHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				handler.onSelect(selectCurrent());
			}
		}, MouseUpEvent.getType());
	}
	
	private Fragment selectCurrent() {
		// De-activate the rubberband
		mouseMoveRegistration.removeHandler();
		mouseUpRegistration.removeHandler();
		enableTextSelection(RootPanel.get().getElement());
		
		// Return the currently selected BBox
		return getSelectedFragment();
	}
	
	@Override
	public Fragment getSelectedFragment() {
		int left = (anchorX > oppositeX) ? oppositeX : anchorX;
		int top = (anchorY > oppositeY) ? oppositeY : anchorY;
		int width = Math.abs(anchorX - oppositeX);
		int height = Math.abs(anchorY - oppositeY);
		return Fragment.create(BoundingBox.create(left, top, width, height));
	}
	
	private void setDimensions(int mouseX, int mouseY) {
		int left, top, width, height;
		
		if (mouseX > anchorX) {
			width = mouseX - anchorX;
			left = anchorX;
		} else {
			width = anchorX - mouseX;
			left = mouseX;
		}
		
		if (mouseY > anchorY) {
			height = mouseY - anchorY;
			top = anchorY;
		} else {		
			height = anchorY - mouseY;
			top = mouseY;
		}
		
		RootPanel.get().setWidgetPosition(outerBorder, 
				left + parent.getAbsoluteLeft(), top + parent.getAbsoluteTop());
		outerBorder.setPixelSize(width, height);	
	}
	
	@Override
	public void clear() {
		outerBorder.removeFromParent();
		instance = null;
	}

}
