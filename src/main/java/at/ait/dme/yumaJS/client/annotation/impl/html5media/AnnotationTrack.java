package at.ait.dme.yumaJS.client.annotation.impl.html5media;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.user.client.ui.Composite;

import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.core.Range;
import at.ait.dme.yumaJS.client.init.InitParams;

public class AnnotationTrack extends Composite {
	
	private CanvasElement canvasElement;
	
	private Context2d context;
	
	private ProgressBar progressBar;
	
	private List<Annotation> annotations = new ArrayList<Annotation>();
	
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
		
		initWidget(annotationTrack);
	}
	
	public void addAnnotation(Annotation a) {
		annotations.add(a);
		refresh();
	}
	
	private void refresh() {
		context.clearRect(0, 0, canvasElement.getWidth(), canvasElement.getHeight());
		context.setFillStyle("#fff000");
		
		for (Annotation a : annotations) {
			Range r = a.getFragment().getRange();
			int start = progressBar.toOffsetX(r.getFrom());
			int end = progressBar.toOffsetX(r.getTo());
			context.fillRect(start, 0, end - start, canvasElement.getHeight());
		}
	}

}
