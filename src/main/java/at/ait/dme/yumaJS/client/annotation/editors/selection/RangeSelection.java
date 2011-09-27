package at.ait.dme.yumaJS.client.annotation.editors.selection;

import at.ait.dme.yumaJS.client.annotation.core.Fragment;
import at.ait.dme.yumaJS.client.annotation.core.Range;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.InadequateBrowserException;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.ProgressBar;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.RootPanel;

public class RangeSelection extends Selection {

	private Canvas selectionCanvas;
	
	private CanvasElement canvasElement;
	
	private Context2d context;
	
	private ProgressBar progressBar;
	
	private boolean dragging = false;
	
	private boolean startHandle = false;
	
	private boolean endHandle = false;
		
	private int startX, endX;
	
	public RangeSelection(ProgressBar progressBar, int startOffsetX, int endOffsetX) 
		throws InadequateBrowserException {
		
		this.progressBar = progressBar;
		this.startX = startOffsetX;
		this.endX = endOffsetX;
		
		selectionCanvas = Canvas.createIfSupported();
		if (selectionCanvas == null)
			throw new InadequateBrowserException("HTML5 Canvas not supported");

		selectionCanvas.getElement().getStyle().setProperty("outline", "none");
		selectionCanvas.getElement().getStyle().setZIndex(999999);
		
		canvasElement = selectionCanvas.getCanvasElement();
		canvasElement.setWidth(progressBar.getOffsetWidth());
		canvasElement.setHeight(progressBar.getOffsetHeight());
				
		context = selectionCanvas.getContext2d();
		
		selectionCanvas.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				disableTextSelection();
				dragging = true;
			}
		});
		
		selectionCanvas.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				int x = event.getX();

				if (dragging) {
					if (startHandle) {
						startX = x;
					} else if (endHandle) {
						endX = x;
					}
					refresh();
				} else {
					if (x < startX && x > (startX - 5)) {
						startHandle = true;
						selectionCanvas.getElement().getStyle().setCursor(Cursor.E_RESIZE);
					} else if (x > endX && (x < endX + 5)) {
						endHandle = true;
						selectionCanvas.getElement().getStyle().setCursor(Cursor.E_RESIZE);
					} else {
						startHandle = false;
						endHandle = false;
						selectionCanvas.getElement().getStyle().setCursor(Cursor.AUTO);
					}					
				}				
			}
		});
		
		selectionCanvas.addMouseUpHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				enableTextSelection();
				dragging = false;
			}
		});
		
		RootPanel.get().add(selectionCanvas, progressBar.getAbsoluteLeft(), progressBar.getAbsoluteTop());
		refresh();
	}
		
	private void refresh() {
		context.clearRect(0, 0, canvasElement.getWidth(), canvasElement.getHeight());
		context.setFillStyle("#ffa500");
		
		context.setGlobalAlpha(0.8);
		context.fillRect(startX, 0, 1, canvasElement.getHeight());
		context.fillRect(endX, 0, 1, canvasElement.getHeight());
		
		context.setGlobalAlpha(0.4);
		context.fillRect(startX, 0, endX - startX, canvasElement.getHeight());
	}
	
	public int getStartOffsetX() {
		return startX;
	}

	@Override
	public Fragment getSelectedFragment() {
		double startTime = progressBar.toTime(startX);
		double endTime = progressBar.toTime(endX);
		
		if (startTime < endTime) {
			return Fragment.create(Range.create(startTime, endTime));
		} else {
			return Fragment.create(Range.create(endTime, startTime));			
		}
	}

	@Override
	public void setSelectionChangedHandler(SelectionChangedHandler handler) {
		// We don't currently do anything on range selection changes
	}

	@Override
	public void destroy() {
		selectionCanvas.removeFromParent();
	}
	
}
