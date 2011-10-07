package at.ait.dme.yumaJS.client.annotation.editors;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
				
				Create.executeJSONP(a, new AsyncCallback<String>() {
					public void onSuccess(String result) {	
						System.out.println(result);
					}
					
					public void onFailure(Throwable caught) {
						System.out.println(caught);
					}
				});
				// TODO only add annotation on success - or at least show
				// a message that storing failed
				
				// TODO should we allow 'demo' mode with no server side 
				// annotation storage as well?
				annotatable.addAnnotation(a);
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
