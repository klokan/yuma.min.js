package at.ait.dme.yumaJS.client;

import at.ait.dme.yumaJS.client.annotation.impl.html5media.audio.AudioPlayer;

import com.google.gwt.core.client.EntryPoint;

public class TestAudioHosted implements EntryPoint {
	
	public void onModuleLoad() {
		new AudioPlayer("../samples/KevinMacLeod-AcidTrumpet.ogg", "annotateMe");
	}

}
