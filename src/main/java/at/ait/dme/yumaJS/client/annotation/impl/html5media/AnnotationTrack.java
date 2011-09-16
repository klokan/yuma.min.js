package at.ait.dme.yumaJS.client.annotation.impl.html5media;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.core.Range;
import at.ait.dme.yumaJS.client.annotation.widgets.DetailsPopup;
import at.ait.dme.yumaJS.client.init.InitParams;
import at.ait.dme.yumaJS.client.init.Labels;

public class AnnotationTrack extends Composite {
	
	private CanvasElement canvasElement;
	
	private Context2d context;
	
	private ProgressBar progressBar;
	
	private Map<Annotation, DetailsPopup> annotations = new HashMap<Annotation, DetailsPopup>();
	
	private DetailsPopup currentPopup = null;
	
	public AnnotationTrack(final ProgressBar progressBar, InitParams params) throws InadequateBrowserException {
		this.progressBar = progressBar;
		
		Canvas annotationTrack = Canvas.createIfSupported();
		if (annotationTrack == null)
			throw new InadequateBrowserException("HTML5 Canvas not supported");
		
		canvasElement = annotationTrack.getCanvasElement();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
		    public void execute () {
				canvasElement.setWidth(progressBar.getOffsetWidth());
				canvasElement.setHeight(progressBar.getOffsetHeight());
		    }
		});
		context = annotationTrack.getContext2d();
		
		progressBar.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				Annotation a = getAnnotation(event.getX());
				if (a == null) {
					clearPopup();
				} else {
					showPopup(annotations.get(a), event.getClientX(), event.getClientY());
				}
			}
		});
		
		initWidget(annotationTrack);
	}
	
	public void showAnnotationAt(double time) {
		if (currentPopup == null || !currentPopup.isVisible()) {
			Annotation a = getAnnotation(time);
			if (a != null)
				showPopup(
						annotations.get(a), 
						canvasElement.getAbsoluteLeft() + progressBar.toOffsetX(time), 
						canvasElement.getAbsoluteTop() + canvasElement.getOffsetHeight());
		}
	}
	
	private void clearPopup() {
		if (currentPopup != null) {
			currentPopup.removeFromParent();
			currentPopup = null;
		}
	}
	
	private void showPopup(DetailsPopup popup, int x, int y) {
		if (currentPopup != null && currentPopup != popup) {
			clearPopup();
		}
		
		if (currentPopup == null) {
			currentPopup = popup;			
			RootPanel.get().add(currentPopup, x, y);
		}
		
		currentPopup.setVisible(true);
	}
	
	public void addAnnotation(Annotation a, Labels labels) {
		annotations.put(a, new DetailsPopup(a, labels));
		refresh();
	}
	
	private Annotation getAnnotation(double time) {
		// TODO make this more efficient!
		// TODO make this handle 'smallest first' overlap scenarios
		for (Annotation a : annotations.keySet()) {
			Range r = a.getFragment().getRange();
			if (time >= r.getFrom() && time <= r.getTo()) {
				return a;
			}
		}
		
		return null;
	}
	
	private Annotation getAnnotation(int offsetX) {
		return getAnnotation(progressBar.toTime(offsetX));
	}
	
	private void refresh() {
		context.clearRect(0, 0, canvasElement.getWidth(), canvasElement.getHeight());
		context.setFillStyle("#fff000");
		
		for (Annotation a : annotations.keySet()) {
			Range r = a.getFragment().getRange();
			int start = progressBar.toOffsetX(r.getFrom());
			int end = progressBar.toOffsetX(r.getTo());
			context.fillRect(start, 0, end - start, canvasElement.getHeight());
		}
	}

}
