package at.ait.dme.yumaJS.client.annotation.impl.html5media;

import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.core.Range;
import at.ait.dme.yumaJS.client.init.InitParams;

public class AnnotationTrack {
	
	public AnnotationTrack(ProgressBar progressBar, InitParams params) {
		
	}
	
	public void addAnnotation(Annotation a) {
		Range r = a.getFragment().getRange();
		System.out.println("Adding range " + r.getFrom() + " to " + r.getTo());
	}

}
