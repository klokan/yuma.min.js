package at.ait.dme.yumaJS.client.annotation.editors.selection;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

import at.ait.dme.yumaJS.client.annotation.core.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.core.Fragment;
import at.ait.dme.yumaJS.client.annotation.editors.selection.Selection;
import at.ait.dme.yumaJS.client.annotation.impl.image.ImageAnnotationLayer;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.SeajaxAnnotationLayer;
import at.ait.dme.yumaJS.client.init.Labels;

/**
 * An sub-class of {@link Selection} that implements a resizable-box selection
 * tool. Used for {@link ImageAnnotationLayer} and {@link SeajaxAnnotationLayer}.
 *  
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class ResizableBoxSelection extends Selection {
	
	private static final double DRAG_START_BUFFER_FACTOR = 0.5;
	
	// Resize handle directions
	private enum Direction { NORTH, EAST, SOUTH, WEST } 
	
	// Constants
	private static final int INITIAL_BOX_SIZE = 60;
	private static final int HANDLE_WIDTH = 11;
	private static final int INITIAL_START_OFFSET = 30;
	
	// The parent panel
	private AbsolutePanel parent;
	
	// The inner box DIV
	private AbsolutePanel inner;
	
	// The handles
	private FlowPanel outer, north, east, south, west;

	// Global mouse move handler instance
	private static HandlerRegistration moveHandler;
	
	// Drag start positions
	private int dragStartX, dragStartY;
	
	// The SelectionChangedHandler (if any)
	private SelectionChangedHandler selectionChangedHandler = null;

	public ResizableBoxSelection(AbsolutePanel parent, Labels labels) {
		this.parent = parent;
		
		// Outer box DIV
		outer = new FlowPanel();
		outer.setPixelSize(INITIAL_BOX_SIZE, INITIAL_BOX_SIZE);
		outer.setStyleName("yuma-bbox-selection-outer");
		
		// Inner box DIV
		inner = new AbsolutePanel();
		inner.setWidth("100%");
		inner.setHeight("100%");
		inner.getElement().getStyle().setCursor(Cursor.MOVE);
		inner.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		inner.setStyleName("yuma-bbox-selection-inner");
		outer.add(inner);
		makeDraggable(inner);
		
		// Resize drag handles
		north = new FlowPanel();
		north.setWidth("100%");
		north.setHeight(HANDLE_WIDTH + "px");
		north.getElement().getStyle().setCursor(Cursor.N_RESIZE);
		north.setStyleName("yuma-bbox-selection-handle");
		inner.add(north, 0, - HANDLE_WIDTH / 2);
		makeResizable(north, Direction.NORTH);
		
		east = new FlowPanel();
		east.setWidth(HANDLE_WIDTH + "px");
		east.setHeight("100%");
		east.getElement().getStyle().setCursor(Cursor.E_RESIZE);
		east.setStyleName("yuma-bbox-selection-handle");
		inner.add(east, INITIAL_BOX_SIZE - HANDLE_WIDTH / 2, 0);
		makeResizable(east, Direction.EAST);
		
		south = new FlowPanel();
		south.setWidth("100%");
		south.setHeight(HANDLE_WIDTH + "px");
		south.getElement().getStyle().setCursor(Cursor.S_RESIZE);
		south.setStyleName("yuma-bbox-selection-handle");
		inner.add(south, 0, INITIAL_BOX_SIZE - HANDLE_WIDTH / 2);
		makeResizable(south, Direction.SOUTH);
		
		west = new FlowPanel();
		west.setWidth(HANDLE_WIDTH + "px");
		west.setHeight("100%");
		west.getElement().getStyle().setCursor(Cursor.W_RESIZE);
		west.setStyleName("yuma-bbox-selection-handle");
		inner.add(west, - HANDLE_WIDTH / 2, 0);
		makeResizable(west, Direction.WEST);
		
		parent.add(outer, INITIAL_START_OFFSET, INITIAL_START_OFFSET);
		
		RootPanel.get().addDomHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				removeHandler();
				enableTextSelection();
			}
		}, MouseUpEvent.getType());
	}
	
	private void removeHandler() {
		if (moveHandler != null) {
			moveHandler.removeHandler();
			moveHandler = null;
		}
	}
	
	private void makeDraggable(Panel panel) {
		panel.addDomHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				disableTextSelection();
				dragStartX = event.getRelativeX(parent.getElement());
				dragStartY = event.getRelativeY(parent.getElement());
				removeHandler();
				
				final int dragStartXBuffer = (int) (outer.getOffsetWidth() * DRAG_START_BUFFER_FACTOR);
				final int dragStartYBuffer = (int) (outer.getOffsetHeight() * DRAG_START_BUFFER_FACTOR);
		
				moveHandler = RootPanel.get().addDomHandler(new MouseMoveHandler() {
					public void onMouseMove(MouseMoveEvent event) {
						event.preventDefault();
						int x = event.getRelativeX(parent.getElement());							
						int y = event.getRelativeY(parent.getElement());
							
						int dX = x - dragStartX;
						int dY = y - dragStartY;
						
						dragStartX = x;
						dragStartY = y;
						
						int left = outer.getAbsoluteLeft() - parent.getAbsoluteLeft();
						int top = outer.getAbsoluteTop() - parent.getAbsoluteTop();
						
						if (left + dX < 0) {
							if (dragStartX < dragStartXBuffer)
								dragStartX = dragStartXBuffer;
							
							left = 0;
							dX = 0;
						} else if (left + dX + outer.getOffsetWidth() > parent.getOffsetWidth()) {
							if (x > parent.getOffsetWidth() - dragStartXBuffer)
								dragStartX = parent.getOffsetWidth() - dragStartXBuffer;
							
							left = parent.getOffsetWidth() - outer.getOffsetWidth();
							dX = 0;
						}
						
						if (top + dY < 0) {
							if (y < dragStartYBuffer)
								dragStartY = dragStartYBuffer;
							
							top = 0;
							dY = 0;
						} else if (top + dY + outer.getOffsetHeight() > parent.getOffsetHeight()) {
							if (y > parent.getOffsetHeight() - dragStartYBuffer)
								dragStartY = parent.getOffsetHeight() - dragStartYBuffer;
							
							top = parent.getOffsetHeight() - outer.getOffsetHeight();
							dY = 0;
						}
						
						parent.setWidgetPosition(outer, left + dX, top + dY);
						fireSelectionChanged();
					}
				}, MouseMoveEvent.getType());
			}
		}, MouseDownEvent.getType());
	}
	
	private void makeResizable(Panel handle, final Direction direction) {		
		handle.addDomHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				disableTextSelection();
				event.stopPropagation();
				removeHandler();
				
				final int borderWidth = outer.getOffsetWidth() - inner.getElement().getClientWidth();
				final int borderHeight = outer.getOffsetHeight() - inner.getElement().getClientHeight();
				
				moveHandler = RootPanel.get().addDomHandler(new MouseMoveHandler() {
					public void onMouseMove(MouseMoveEvent event) {
						int x = event.getRelativeX(parent.getElement());
						if (x < 0) {
							x = 0;
						} else if (x > parent.getOffsetWidth() - borderWidth) {
							x = parent.getElement().getClientWidth() - borderWidth;
						}
						
						int y = event.getRelativeY(parent.getElement());
						if (y < 0) {
							y = 0;
						} else if (y > parent.getOffsetHeight() - borderHeight) {
							y = parent.getElement().getClientHeight() - borderHeight;
						}
						
						if (direction == Direction.NORTH) {
							int currentHeight = inner.getElement().getClientHeight();
							int bottom = outer.getAbsoluteTop() - parent.getAbsoluteTop() + currentHeight;
							int newHeight = bottom - y;
			
							outer.getElement().getStyle().setTop(y, Unit.PX);
							outer.getElement().getStyle().setHeight(newHeight, Unit.PX);
							inner.setWidgetPosition(south, 0, newHeight - HANDLE_WIDTH / 2);
						} else if (direction == Direction.EAST) {
							int left = outer.getAbsoluteLeft() - parent.getAbsoluteLeft();
							int newWidth = x - left;
							
							outer.getElement().getStyle().setWidth(newWidth, Unit.PX);
							inner.setWidgetPosition(east, newWidth - HANDLE_WIDTH / 2, 0);
						} else if (direction == Direction.SOUTH) {
							int top = outer.getAbsoluteTop() - parent.getAbsoluteTop();
							int newHeight = y - top;
							
							outer.getElement().getStyle().setHeight(newHeight, Unit.PX);
							inner.setWidgetPosition(south, 0, newHeight - HANDLE_WIDTH / 2);
						} else if (direction == Direction.WEST) {
							int currentWidth = inner.getElement().getClientWidth();
							int right = outer.getAbsoluteLeft() - parent.getAbsoluteLeft() + currentWidth;
							int newWidth = right - x;
							
							outer.getElement().getStyle().setLeft(x, Unit.PX);
							outer.getElement().getStyle().setWidth(newWidth, Unit.PX);
							inner.setWidgetPosition(east, 0, newWidth - HANDLE_WIDTH / 2);
						}
						
						fireSelectionChanged();
					}
				}, MouseMoveEvent.getType());
			}
		}, MouseDownEvent.getType());
	}
	
	private void fireSelectionChanged() {
		if (selectionChangedHandler != null)
			selectionChangedHandler.onSelectionChanged(getSelectedFragment());
	}
	
	@Override
	public Fragment getSelectedFragment() {
		int left = outer.getAbsoluteLeft() - parent.getAbsoluteLeft();
		int top = outer.getAbsoluteTop() - parent.getAbsoluteTop();
		return Fragment.create(BoundingBox.create(left, top, 
				inner.getElement().getClientWidth(), inner.getElement().getClientHeight()));
	}
	
	@Override
	public void setSelectionChangedHandler(SelectionChangedHandler handler) {
		this.selectionChangedHandler = handler;
	}

	@Override
	public void destroy() {
		outer.removeFromParent();
	}

}
