package at.ait.dme.yumaJS.client.annotation.editors;

import com.google.gwt.user.client.ui.AbsolutePanel;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.editors.selection.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.editors.selection.Range;
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
		super(annotatable, initialValue);
		this.panel = panel;
		
		BoundingBox bbox = null;
		if (initialValue != null)
			bbox = annotatable.toBounds(initialValue.getFragment()); 
				
		Selection selection = new ResizableBoxSelection(panel, labels, bbox);
		selection.setSelectionChangedHandler(new SelectionChangedHandler() {
			public void onBoundsChanged(BoundingBox bbox) {
				updateEditForm();
			}

			public void onRangeChanged(Range range) {
				// This editor does not have ranges
			}
		});
		setSelection(selection);
		
		EditForm editForm = new EditForm(selection, labels, initialValue);
		setEditForm(editForm);
		panel.add(editForm, 0, 0);
		updateEditForm();
	}
	
	private void updateEditForm() {
		BoundingBox bbox = selection.getSelectedBounds();
		panel.setWidgetPosition(editForm, bbox.getX(), bbox.getY() + bbox.getHeight());
	}
	
}
