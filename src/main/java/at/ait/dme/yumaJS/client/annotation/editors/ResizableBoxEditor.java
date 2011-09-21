package at.ait.dme.yumaJS.client.annotation.editors;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;

import at.ait.dme.yumaJS.client.annotation.core.Annotatable;
import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.core.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.core.Fragment;
import at.ait.dme.yumaJS.client.annotation.editors.selection.ResizableBoxSelection;
import at.ait.dme.yumaJS.client.annotation.editors.selection.SelectionChangedHandler;
import at.ait.dme.yumaJS.client.annotation.widgets.EditForm;
import at.ait.dme.yumaJS.client.init.Labels;

public class ResizableBoxEditor extends Editor {

	private ResizableBoxSelection selection;

	private EditForm editForm;
	
	private AbsolutePanel panel;

	public ResizableBoxEditor(Annotatable annotatable, AbsolutePanel panel, Labels labels) {
		super(annotatable);
		this.panel = panel;
		
		selection = new ResizableBoxSelection(panel, labels);
		selection.setSelectionChangedHandler(new SelectionChangedHandler() {
			public void onSelectionChanged(Fragment fragment) {
				updateEditForm();
			}
		});
		
		editForm = new EditForm(selection, labels);
		editForm.addSaveClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				saveAnnotation();
				destroy();
			}
		});
		
		editForm.addCancelClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});
		
		panel.add(editForm, 0, 0);
		updateEditForm();
	}
	
	private void updateEditForm() {
		BoundingBox bbox = selection.getSelectedFragment().getBoundingBox();
		panel.setWidgetPosition(editForm, bbox.getX(), bbox.getY() + bbox.getHeight());
	}
	
	@Override
	public Annotation getAnnotation() {
		return Annotation.create(selection.getSelectedFragment(), editForm.getText());
	}
	
	private void destroy() {
		editForm.removeFromParent();
		selection.destroy();
	}
	
}
