package at.ait.dme.yumaJS.client.annotation.impl.html5media.audio;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.core.Annotatable;
import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.editors.AudioRangeEditor;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.AnnotationTrack;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.InadequateBrowserException;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.ProgressBar;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.event.TimeUpdateHandler;
import at.ait.dme.yumaJS.client.annotation.widgets.DetailsPopup;
import at.ait.dme.yumaJS.client.init.InitParams;

import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;

/**
 * An annotation-enabled HTML5 audio player.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 * @author Paul Weichhart
 */
@Export
@ExportPackage("YUMA")
public class AudioPlayer extends Annotatable implements Exportable {
	
	private static final int PLAYER_HEIGHT = 20;
	private static final int PLAYER_DEFAULT_WIDTH = 400;
	private static final String DEFAULT_ICON_PATH = "css/yuma.min.css";
	private static final int CLOCK_WIDTH = 70;
	
	private AudioElement audioElement;
	
	private AnnotationTrack annotationTrack; 
	
	public AudioPlayer(String audioURL, String id) {
		this(audioURL, id, null);
	}
	
	public AudioPlayer(String audioURL, final String id, final InitParams initParams) {
		super(initParams);
		
		final Element el = DOM.getElementById(id);
		if (el == null) {
			Window.alert("Error: no element with id '" + id + "' found on this page");
			throw new RuntimeException();
		}
		
		try {
			final ExtendedAudio audio = new ExtendedAudio();		
		
			AbsolutePanel playerPanel = new AbsolutePanel();
			playerPanel.setStyleName("yuma-audio-player");

			int width = (initParams != null && initParams.width() > -1) ?
					initParams.width() : PLAYER_DEFAULT_WIDTH;
			playerPanel.setPixelSize(width, PLAYER_HEIGHT);
			
			audioElement = audio.getAudioElement();
			audioElement.setSrc(audioURL);
			audioElement.setPreload("PRELOAD_AUTO");
			audioElement.load();
			audioElement.setAutoplay(false);
			playerPanel.add(audio);
			
			String iconPath = (initParams != null && initParams.iconPath() != null) ?
					initParams.iconPath() : DEFAULT_ICON_PATH;
			
			Image imgPlay = new Image(iconPath + "/audio-play.png");
			Image imgPause = new Image(iconPath + "/audio-pause.png");
			final ToggleButton btnPlayPause = new ToggleButton(imgPlay, imgPause);
			btnPlayPause.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (audioElement.isPaused())
						play();
					else
						pause();
				}
			});
			btnPlayPause.setStyleName("yuma-audio-button");
			playerPanel.add(btnPlayPause, 0, 5);
	
			Image imgAnnotate = new Image(iconPath + "/audio-annotate.png");
			final PushButton btnAnnotate = new PushButton(imgAnnotate);
			btnAnnotate.setStyleName("yuma-audio-button");
			btnAnnotate.setTitle("Add an Annotation at the current time");
			playerPanel.add(btnAnnotate, 25, 5);
			
			final ProgressBar progressBar = new ProgressBar(audio, 
					width - 65 - CLOCK_WIDTH, PLAYER_HEIGHT);
			progressBar.setStyleName("yuma-audio-progressbar");
			
			final AudioPlayer self = this;
			btnAnnotate.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					try {
						new AudioRangeEditor(self, progressBar,
								progressBar.toOffsetX(audio.getAudioElement().getCurrentTime()),
								getLabels());
					} catch (InadequateBrowserException e) {
						// Should never happen
						throw new RuntimeException(e);
					}
				}
			});
			
			annotationTrack = new AnnotationTrack(progressBar, initParams);
			annotationTrack.setStyleName("yuma-audio-annotationtrack");
			playerPanel.add(annotationTrack, 60, 5);
					
			final Label clock = new Label("0:00 | 0:00");
			clock.setStyleName("yuma-audio-clock");
			clock.setPixelSize(CLOCK_WIDTH, PLAYER_HEIGHT);
			playerPanel.add(clock, width - CLOCK_WIDTH, 5);
			
			audio.addTimeUpdateHandler(new TimeUpdateHandler() {
				public void onTimeUpdate() {
					clock.setText(audio.getCurrentTimeFormatted() + " | " + audio.getTotalTimeFormatted());
					progressBar.update(audio.getCurrentPercentage());
					annotationTrack.showAnnotationAt(audio.getAudioElement().getCurrentTime());
				}
			});
			
			progressBar.addDomHandler(new MouseOutHandler() {
				public void onMouseOut(MouseOutEvent event) {
					DetailsPopup popup = annotationTrack.getCurrentPopup();
					if (popup != null) {
						if (!popup.contains(event.getClientX(), event.getClientY())) {
							annotationTrack.clearCurrentPopup();	
						}
					}
				}
			}, MouseOutEvent.getType());
			playerPanel.add(progressBar, 60, 5);
			
			RootPanel.get(id).add(playerPanel);	
		} catch (InadequateBrowserException e) {
			YUMA.fatalError(e.getMessage());
		}
	}
	
	private void play() {
		audioElement.play();
	}
	
	private void pause() {
		audioElement.pause();
	}

	@Override
	protected void onWindowResize(int width, int height) {
		// No action necessary for audio		
	}
	
	@Override
	public void addAnnotation(Annotation a) {
		annotationTrack.addAnnotation(a, getLabels());
	}

	@Override
	public void removeAnnotation(Annotation annotation) {
		annotationTrack.removeAnnotation(annotation);
	}
	
}
