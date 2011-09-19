package at.ait.dme.yumaJS.client.annotation.impl.html5media;

import at.ait.dme.yumaJS.client.annotation.impl.html5media.audio.AudioPlayer;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.audio.ExtendedAudio;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CanvasGradient;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * A progress bar GUI element for the VideoPlayer and {@link AudioPlayer} components.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class ProgressBar extends Composite {
	
	private static final int BAR_HEIGHT = 6;

	private AudioElement audio;
	
	private Canvas progressBar;
	
	private CanvasElement canvasElement;
	
	private Context2d context;
	
	private int offsetY;

	private boolean dragging = false;
	
	private CanvasGradient backgroundGradient;
	
	private CanvasGradient foregroundGradient;
		
	public ProgressBar(ExtendedAudio extendedAudio, int width, int height) throws InadequateBrowserException {
		progressBar = Canvas.createIfSupported();
		if (progressBar == null)
			throw new InadequateBrowserException("HTML5 Canvas not supported");

		this.audio = extendedAudio.getAudioElement();
		offsetY = (height - BAR_HEIGHT) / 2;
		
		canvasElement = progressBar.getCanvasElement();
		canvasElement.setWidth(width - 2);
		canvasElement.setHeight(height);	
		
		context = progressBar.getContext2d();
		
		backgroundGradient = context.createLinearGradient(0, offsetY, 0, offsetY + BAR_HEIGHT);
		backgroundGradient.addColorStop(0, "rgba(85, 85, 85, 0.7)");
		backgroundGradient.addColorStop(1, "rgba(150, 150, 150, 0.7)");		
		
		foregroundGradient = context.createLinearGradient(0, offsetY, 0, offsetY + BAR_HEIGHT);
		foregroundGradient.addColorStop(0, "rgba(240, 240, 240, 1)");
		foregroundGradient.addColorStop(1, "rgba(120, 120, 120, 1)");		
		
		context.setFillStyle(backgroundGradient);
		context.fillRect(0, offsetY, width - 2, BAR_HEIGHT);

		progressBar.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				dragging = true;
				audio.setCurrentTime(toTime(event.getX()));
			}
		});
		
		progressBar.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				if (dragging)
					audio.setCurrentTime(toTime(event.getX()));
			}
		});
		
		RootPanel.get().addDomHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				dragging = false;
			}
		}, MouseUpEvent.getType());

		initWidget(progressBar);
	}
	
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return progressBar.addMouseMoveHandler(handler);
	}
	
	public double toTime(double offsetX) {
		double percent = offsetX / (double) canvasElement.getOffsetWidth();
		return audio.getDuration() * percent;
	}
	
	public int toOffsetX(double time) {
		double percent = time / audio.getDuration();
		return (int) (canvasElement.getOffsetWidth() * percent);
	}
	
	public void update(double percent) {
		context.clearRect(0, 0, canvasElement.getWidth(), canvasElement.getHeight());
		
		double total = canvasElement.getOffsetWidth();
		int width = (int) (total * percent);
		
		context.setFillStyle(backgroundGradient);
		context.fillRect(0, offsetY, total, BAR_HEIGHT);
		context.setFillStyle(foregroundGradient);		
		context.fillRect(0, offsetY, width, BAR_HEIGHT);
	}

}
