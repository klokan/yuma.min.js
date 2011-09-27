package at.ait.dme.yumaJS.client.annotation.editors;

import com.google.gwt.user.client.ui.AbsolutePanel;

import at.ait.dme.yumaJS.client.annotation.core.Annotatable;
import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.core.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.core.Fragment;
import at.ait.dme.yumaJS.client.annotation.editors.selection.ResizableBoxSelection;
import at.ait.dme.yumaJS.client.annotation.editors.selection.Selection;
import at.ait.dme.yumaJS.client.annotation.editors.selection.SelectionChangedHandler;
import at.ait.dme.yumaJS.client.annotation.widgets.EditForm;
import at.ait.dme.yumaJS.client.init.Labels;

/**
 * An {@link Editor} implementation for images and zoomable images,
 * using {@link ResizableBoxSelection}.
 *  
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class ResizableBoxEditor extends Editor {
	
	private AbsolutePanel panel;

	public ResizableBoxEditor(Annotatable annotatable, AbsolutePanel panel, Labels labels) {
		this(annotatable, panel, labels, null);
	}
	
	public ResizableBoxEditor(Annotatable annotatable, AbsolutePanel panel, Labels labels, Annotation initialValue) {
		super(annotatable);
		this.panel = panel;
		
		Selection selection = new ResizableBoxSelection(panel, labels, initialValue.getFragment().getBoundingBox());
		selection.setSelectionChangedHandler(new SelectionChangedHandler() {
			public void onSelectionChanged(Fragment fragment) {
				updateEditForm();
			}
		});
		setSelection(selection);
		
		EditForm editForm = new EditForm(selection, labels, initialValue);
		setEditForm(editForm);
		panel.add(editForm, 0, 0);
		updateEditForm();
	}
	
	private void updateEditForm() {
		BoundingBox bbox = selection.getSelectedFragment().getBoundingBox();
		panel.setWidgetPosition(editForm, bbox.getX(), bbox.getY() + bbox.getHeight());
	}
	
}
