package at.ait.dme.yumaJS.client.annotation.impl.html5media.audio;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.core.Annotatable;
import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.AnnotationTrack;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.InadequateBrowserException;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.ProgressBar;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.event.TimeUpdateHandler;
import at.ait.dme.yumaJS.client.annotation.selection.RangeSelection;
import at.ait.dme.yumaJS.client.annotation.widgets.DetailsPopup;
import at.ait.dme.yumaJS.client.init.InitParams;
import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
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
	
	private AudioElement audioElement;
	
	private AnnotationTrack annotationTrack; 
	
	public AudioPlayer(String audioURL, String id) {
		this(audioURL, id, null);
	}
	
	public AudioPlayer(String audioURL, String id, final InitParams initParams) {
		super(initParams);
		
		final Element el = DOM.getElementById(id);
		if (el == null) {
			Window.alert("Error: no element with id '" + id + "' found on this page");
			throw new RuntimeException();
		}
		
		
		try {
			final ExtendedAudio audio = new ExtendedAudio();		
		
			FlowPanel playerPanel = new FlowPanel();
			playerPanel.setStyleName("audio-player");
			
			// TODO set via initParams!
			playerPanel.setPixelSize(460, 20);
			
			audioElement = audio.getAudioElement();
			audioElement.setSrc(audioURL);
			audioElement.setPreload("PRELOAD_AUTO");
			audioElement.load();
			audioElement.setAutoplay(false);
			playerPanel.add(audio);
			
			// TODO pass stylesheet file via init param
			final ToggleButton btnPlayPause = new ToggleButton(
					new Image("../css/theme-dark/audio-play.png"),
					new Image("../css/theme-dark/audio-pause.png"));
			btnPlayPause.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (audioElement.isPaused()) {
						btnPlayPause.addStyleName("button-state-play");
						play();
					} else {
						btnPlayPause.addStyleName("button-state-pause");
						pause();
					}
				}
			});
			btnPlayPause.setStyleName("button-play-pause");
			btnPlayPause.addStyleName("button-state-pause");
			// TODO set via initParams!
			btnPlayPause.setPixelSize(20, 20);
			playerPanel.add(btnPlayPause);
	
			final PushButton btnAnnotate = new PushButton(new Image("../css/theme-dark/audio-annotate.png"));
			btnAnnotate.setStyleName("button-annotate");
			btnAnnotate.setPixelSize(25, 20);
			playerPanel.add(btnAnnotate);
			
			// TODO set via initParams!
			final ProgressBar progressBar = new ProgressBar(audio, 335, 20);
			progressBar.setStyleName("progressbar");
			
			btnAnnotate.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					// TODO dummy implementation!
					try {
						int x = progressBar.toOffsetX(audio.getAudioElement().getCurrentTime());
						showEditForm(
								x + progressBar.getAbsoluteLeft() - 5, 
								progressBar.getAbsoluteTop() + progressBar.getOffsetHeight() - 2,
								new RangeSelection(progressBar, x, x + 1));
					} catch (InadequateBrowserException e) {
						// Can never happen
						throw new RuntimeException(e);
					}
				}
			});
			
			annotationTrack = new AnnotationTrack(progressBar, initParams);
			annotationTrack.setStyleName("annotation-track");
			playerPanel.add(annotationTrack);
					
			final Label clock = new Label("0:00 | 0:00");
			clock.setStyleName("clock");
			// TODO set via initParams!
			clock.setPixelSize(70, 20);
			playerPanel.add(clock);
			
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
			
			RootPanel.get(id).add(playerPanel);	
			RootPanel.get(id).add(progressBar, annotationTrack.getAbsoluteLeft(), annotationTrack.getAbsoluteTop());
		} catch (InadequateBrowserException e) {
			YUMA.fatalError(e.getMessage());
		}
	}
	
	public void play() {
		audioElement.play();
	}
	
	public void pause() {
		audioElement.pause();
	}
	
	public boolean isPlaying() {
		return !audioElement.isPaused();
	}

	@Override
	protected void addAnnotation(Annotation a, Labels labels) {
		annotationTrack.addAnnotation(a, labels);
	}
	
}
