package at.ait.dme.yumaJS.client.annotation.core;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;

import at.ait.dme.yumaJS.client.annotation.selection.Selection;
import at.ait.dme.yumaJS.client.annotation.widgets.EditForm;
import at.ait.dme.yumaJS.client.init.InitParams;
import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * An abstract base class with cross-cutting functionality 
 * that's useful for all media annotation implementations.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public abstract class Annotatable extends Composite implements Exportable {
	
	private InitParams initParams;
	
	private JavaScriptObject onAnnotationCreated = null;
	
	public Annotatable(InitParams params) {
		this.initParams = params;
	}
		
	protected void showEditForm(int x, int y, final Selection selection) {
		final Labels labels = (initParams == null) ? null : initParams.labels();
		final EditForm f = new EditForm(x, y, selection, labels);
		
		f.addSaveClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Annotation a = Annotation.create(selection.getSelectedFragment(), f.getText());
				addAnnotation(a, labels);
				fireOnAnnotationCreated(a);
			}
		});
		
		RootPanel.get().add(f, x, y);
	}
	
	protected abstract void addAnnotation(Annotation a, Labels labels);
	
	@Export
	public void addAnnotationCreatedListener(JavaScriptObject callback) {
		this.onAnnotationCreated = callback;
	};
	
	private void fireOnAnnotationCreated(Annotation a) {
		if (onAnnotationCreated != null)
			_fireOnAnnotationCreated(a, onAnnotationCreated);
	}
	
	private native void _fireOnAnnotationCreated(Annotation a, JavaScriptObject callback) /*-{
		callback(a);
	}-*/;
	
}
