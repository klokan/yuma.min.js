package at.ait.dme.yumaJS.client.annotation.editors;

import at.ait.dme.yumaJS.client.annotation.core.Annotatable;
import at.ait.dme.yumaJS.client.annotation.core.Annotation;

public abstract class Editor {
	
	private Annotatable annotatable; 
	
	public Editor(Annotatable annotatable) {
		this.annotatable = annotatable;
	}
	
	protected void saveAnnotation() {
		annotatable.addAnnotation(getAnnotation());
	}
	
	public abstract Annotation getAnnotation();
	
}
