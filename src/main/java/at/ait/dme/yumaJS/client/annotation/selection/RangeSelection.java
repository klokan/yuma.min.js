package at.ait.dme.yumaJS.client.annotation.selection;

import at.ait.dme.yumaJS.client.annotation.core.Fragment;
import at.ait.dme.yumaJS.client.annotation.core.Range;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.InadequateBrowserException;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.ProgressBar;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;
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
	
	private int startX, endX;
		
	private SelectionHandler selectionHandler = null;
	
	public RangeSelection(ProgressBar progressBar, int start, int end) throws InadequateBrowserException {
		this.progressBar = progressBar;
		this.startX = start;
		this.endX = end;
		
		selectionCanvas = Canvas.createIfSupported();
		if (selectionCanvas == null)
			throw new InadequateBrowserException("HTML5 Canvas not supported");

		canvasElement = selectionCanvas.getCanvasElement();
		canvasElement.setWidth(progressBar.getOffsetWidth());
		canvasElement.setHeight(progressBar.getOffsetHeight());
				
		context = selectionCanvas.getContext2d();
		
		selectionCanvas.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				disableTextSelection(RootPanel.get().getElement());
				dragging = true;
				startX = event.getX();
			}
		});
		
		selectionCanvas.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				endX = event.getX();
				if (dragging) {
					if (startX < endX) {
						draw(startX, endX);
					} else {
						draw(endX, startX);
					}
				}
			}
		});
		
		selectionCanvas.addMouseUpHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				enableTextSelection(RootPanel.get().getElement());
				dragging = false;
				endX = event.getX();
				if (selectionHandler != null)
					selectionHandler.onSelect(getSelectedFragment());
			}
		});
		
		RootPanel.get().add(selectionCanvas, progressBar.getAbsoluteLeft(), progressBar.getAbsoluteTop());
		draw(startX, endX);
	}
		
	private void draw(int fromX, int toX) {
		context.setFillStyle("#ff0000");
		context.clearRect(0, 0, canvasElement.getWidth(), canvasElement.getHeight());
		context.fillRect(fromX, 0, toX - fromX, canvasElement.getHeight());
	}

	@Override
	public void setSelectionHandler(SelectionHandler handler) {
		this.selectionHandler = handler;
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
	public void clear() {
		selectionCanvas.removeFromParent();
	}

}
