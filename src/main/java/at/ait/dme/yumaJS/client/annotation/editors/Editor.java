package at.ait.dme.yumaJS.client.annotation.editors;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import at.ait.dme.yumaJS.client.annotation.core.Annotatable;
import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.editors.selection.Selection;
import at.ait.dme.yumaJS.client.annotation.widgets.EditForm;

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
				annotatable.addAnnotation(
					Annotation.create(selection.getSelectedFragment(), editForm.getText()));
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
