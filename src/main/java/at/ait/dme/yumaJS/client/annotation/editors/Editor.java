package at.ait.dme.yumaJS.client.annotation.editors;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.editors.selection.Selection;
import at.ait.dme.yumaJS.client.annotation.widgets.EditForm;
import at.ait.dme.yumaJS.client.io.Create;

/**
 * The abstract base class for all Editor components.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public abstract class Editor {
	
	private Annotatable annotatable;
	
	private Annotation initialAnnotation;
		
	protected Selection selection;
	
	protected EditForm editForm;
	
	public Editor(Annotatable annotatable, Annotation initialAnnotation) {
		this.annotatable = annotatable;
		this.initialAnnotation = initialAnnotation;
	}
	
	protected void setSelection(Selection selection) {
		this.selection = selection;
	}
	
	protected void setEditForm(final EditForm editForm) {
		this.editForm = editForm;
		
		this.editForm.addSaveClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final Annotation a = Annotation.create(
						annotatable.getObjectURI(),
						annotatable.getMediaType(),
						annotatable.toFragment(selection.getSelectedBounds(), selection.getSelectedRange()),
						editForm.getText());
				
				if (annotatable.getServerURL() == null) {
					// Local-mode: just add the annotation without storing
					annotatable.addAnnotation(a);
				} else {
					Create.executeJSONP(annotatable.getServerURL(), a, new AsyncCallback<String>() {
						public void onSuccess(String result) {	
							annotatable.addAnnotation(a);
						}
						
						public void onFailure(Throwable t) {
							YUMA.nonFatalError(t.getMessage());
						}
					});
				}
				
				destroy();
			}
		});
		
		this.editForm.addCancelClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (initialAnnotation != null)
					annotatable.addAnnotation(initialAnnotation);
				destroy();
			}
		});
	}
		
	private void destroy() {
		editForm.removeFromParent();
		selection.destroy();
	}
	
}
