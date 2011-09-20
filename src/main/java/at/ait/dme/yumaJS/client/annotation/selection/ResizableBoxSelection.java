package at.ait.dme.yumaJS.client.annotation.selection;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

import at.ait.dme.yumaJS.client.annotation.core.Fragment;
import at.ait.dme.yumaJS.client.annotation.widgets.EditForm;
import at.ait.dme.yumaJS.client.init.InitParams;
import at.ait.dme.yumaJS.client.init.Labels;

public class ResizableBoxSelection extends Selection {
	
	private enum Direction { NORTH, EAST, SOUTH, WEST } 
	
	private static final int DEFAULT_SIZE = 60;
	
	private static final int DEFAULT_HANDLE_WIDTH = 11;
	
	private AbsolutePanel parent;
	
	private FlowPanel outer, north, east, south, west;
	
	private AbsolutePanel inner;
	
	private int dragStartX, dragStartY;
	
	private static HandlerRegistration moveHandler;
	
	public ResizableBoxSelection(AbsolutePanel parent, InitParams initParams) {
		this.parent = parent;
		
		outer = new FlowPanel();
		outer.setPixelSize(DEFAULT_SIZE, DEFAULT_SIZE);
		outer.setStyleName("rubberband-outer");
		
		inner = new AbsolutePanel();
		inner.setWidth("100%");
		inner.setHeight("100%");
		inner.getElement().getStyle().setCursor(Cursor.MOVE);
		inner.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		inner.setStyleName("rubberband-inner");
		makeDraggable(inner);
		outer.add(inner);
		
		north = new FlowPanel();
		north.setWidth("100%");
		north.setHeight(DEFAULT_HANDLE_WIDTH + "px");
		north.getElement().getStyle().setCursor(Cursor.N_RESIZE);
		inner.add(north, 0, - DEFAULT_HANDLE_WIDTH / 2);
		makeResizable(north, Direction.NORTH);
		
		east = new FlowPanel();
		east.setWidth(DEFAULT_HANDLE_WIDTH + "px");
		east.setHeight("100%");
		east.getElement().getStyle().setCursor(Cursor.E_RESIZE);
		inner.add(east, DEFAULT_SIZE - DEFAULT_HANDLE_WIDTH / 2, 0);
		makeResizable(east, Direction.EAST);
		
		south = new FlowPanel();
		south.setWidth("100%");
		south.setHeight(DEFAULT_HANDLE_WIDTH + "px");
		south.getElement().getStyle().setCursor(Cursor.S_RESIZE);
		inner.add(south, 0, DEFAULT_SIZE - DEFAULT_HANDLE_WIDTH / 2);
		makeResizable(south, Direction.SOUTH);
		
		west = new FlowPanel();
		west.setWidth(DEFAULT_HANDLE_WIDTH + "px");
		west.setHeight("100%");
		west.getElement().getStyle().setCursor(Cursor.W_RESIZE);
		inner.add(west, - DEFAULT_HANDLE_WIDTH / 2, 0);
		makeResizable(west, Direction.WEST);
		
		Labels labels = (initParams == null) ? null : initParams.labels();
		EditForm editForm = new EditForm(this, labels);
		
		parent.add(outer, 0, 0);
		
		RootPanel.get().addDomHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				removeHandler();
			}
		}, MouseUpEvent.getType());
		
		RootPanel.get().addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				removeHandler();
			}
		}, MouseOutEvent.getType());
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
				dragStartX = event.getClientX();
				dragStartY = event.getClientY();
				removeHandler();
				moveHandler = RootPanel.get().addDomHandler(new MouseMoveHandler() {
					public void onMouseMove(MouseMoveEvent event) {
						int x = event.getClientX();
						int y = event.getClientY();
						
						int dX = x - dragStartX;
						int dY = y - dragStartY;
						
						dragStartX = x;
						dragStartY = y;
						
						int left = outer.getAbsoluteLeft() - parent.getAbsoluteLeft();
						int top = outer.getAbsoluteTop() - parent.getAbsoluteTop();
						parent.setWidgetPosition(outer, left + dX, top + dY);
					}
				}, MouseMoveEvent.getType());
			}
		}, MouseDownEvent.getType());
	}
	
	private void makeResizable(Panel handle, final Direction direction) {
		handle.addDomHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				event.stopPropagation();
				dragStartX = event.getClientX();
				dragStartY = event.getClientY();
				removeHandler();
				moveHandler = RootPanel.get().addDomHandler(new MouseMoveHandler() {
					public void onMouseMove(MouseMoveEvent event) {
						int mouseX = event.getRelativeX(parent.getElement());
						int mouseY = event.getRelativeY(parent.getElement());
						
						if (direction == Direction.NORTH) {
							int currentHeight = inner.getElement().getClientHeight();
							int bottom = outer.getAbsoluteTop() - parent.getAbsoluteTop() + currentHeight;
							int newHeight = bottom - mouseY;
			
							outer.getElement().getStyle().setTop(mouseY, Unit.PX);
							outer.getElement().getStyle().setHeight(newHeight, Unit.PX);
							inner.setWidgetPosition(south, 0, newHeight - DEFAULT_HANDLE_WIDTH / 2);
						} else if (direction == Direction.EAST) {
							int left = outer.getAbsoluteLeft() - parent.getAbsoluteLeft();
							int newWidth = mouseX - left;
							
							outer.getElement().getStyle().setWidth(newWidth, Unit.PX);
							inner.setWidgetPosition(east, newWidth - DEFAULT_HANDLE_WIDTH / 2, 0);
						} else if (direction == Direction.SOUTH) {
							int top = outer.getAbsoluteTop() - parent.getAbsoluteLeft();
							int newHeight = mouseY - top;
							
							outer.getElement().getStyle().setHeight(newHeight, Unit.PX);
							inner.setWidgetPosition(south, 0, newHeight - DEFAULT_HANDLE_WIDTH / 2);
						} else if (direction == Direction.WEST) {
							int currentWidth = inner.getElement().getClientWidth();
							int right = outer.getAbsoluteLeft() - parent.getAbsoluteLeft() + currentWidth;
							int newWidth = right - mouseX;
							
							outer.getElement().getStyle().setLeft(mouseX, Unit.PX);
							outer.getElement().getStyle().setWidth(newWidth, Unit.PX);
							inner.setWidgetPosition(east, 0, newWidth - DEFAULT_HANDLE_WIDTH / 2);
						}
					}
				}, MouseMoveEvent.getType());
			}
		}, MouseDownEvent.getType());
	}
	
	@Override
	public void setSelectionHandler(SelectionHandler handler) {
		// We don't use selection events in this mode
	}

	@Override
	public Fragment getSelectedFragment() {
		return null; //;Fragment.create(BoundingBox.create(left, top, width, height));
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
