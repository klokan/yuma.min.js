package at.ait.dme.yumaJS.client.annotation.impl.html5media;

/**
 * Thrown in case the browser lacks support necessary for the audio player, i.e.
 * HTML5 Canvas and/or HTML Audio.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class InadequateBrowserException extends Exception {

	private static final long serialVersionUID = -8386408059774066157L;

	public InadequateBrowserException() {
		super();
	}
	
	public InadequateBrowserException(String message) {
		super(message);
	}
	
}
