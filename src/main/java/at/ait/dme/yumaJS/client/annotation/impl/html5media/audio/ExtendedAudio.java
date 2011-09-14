package at.ait.dme.yumaJS.client.annotation.impl.html5media.audio;

import at.ait.dme.yumaJS.client.annotation.impl.html5media.InadequateBrowserException;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.event.MediaEndedHandler;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.event.TimeUpdateHandler;

import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.ui.Composite;

/**
 * A class that wraps the default GWT HTML5 audio class and 
 * adds unsupported handler registration methods, plus a few 
 * utility functions.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 * @author Paul Weichhart
 */
public class ExtendedAudio extends Composite {
	
	private Audio audio;
	
	public ExtendedAudio() throws InadequateBrowserException {
		audio = Audio.createIfSupported();
		if (audio == null)
			throw new InadequateBrowserException();
		
		initWidget(audio);
	}
	
	public AudioElement getAudioElement() {
		return audio.getAudioElement();
	}
	
	public String getCurrentTimeFormatted() {
		return getTimeFormatted(audio.getAudioElement().getCurrentTime());
	}
	
	public String getTotalTimeFormatted() {
		return getTimeFormatted(audio.getAudioElement().getDuration());
	}
	
	private String getTimeFormatted(double time) {
		int min = (int) (time / 60);
		int sec = (int) (time % 60);
		String separator = (sec < 10) ? ":0" : ":";
		return min + separator + sec;			
	}
	
	public double getCurrentPercentage() {
		AudioElement el = audio.getAudioElement();
		return el.getCurrentTime() / el.getDuration();
	}
	
	public void addEndedHandler(MediaEndedHandler handler) {
		_addEndedEventHandler(handler, audio.getAudioElement());
	}
	
	private native void _addEndedEventHandler(MediaEndedHandler handler, AudioElement audio) /*-{
		audio.addEventListener("ended", function(event) {
			handler.@at.ait.dme.yumaJS.client.annotation.impl.html5media.event.MediaEndedHandler::onEnded()(); 
		});
	}-*/;
	
	public void addTimeUpdateHandler(TimeUpdateHandler handler) {
		_addTimeUpdateHandler(handler, audio.getAudioElement());
	}
	
	private native void _addTimeUpdateHandler(TimeUpdateHandler handler, AudioElement audio) /*-{
		audio.addEventListener("timeupdate", function(event) {
			handler.@at.ait.dme.yumaJS.client.annotation.impl.html5media.event.TimeUpdateHandler::onTimeUpdate()(); 
		});
	}-*/;
		
}
