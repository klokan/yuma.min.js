package at.ait.dme.yumaJS.client.annotation.editors;

import com.google.gwt.user.client.ui.RootPanel;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.editors.selection.Range;
import at.ait.dme.yumaJS.client.annotation.editors.selection.RangeSelection;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.InadequateBrowserException;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.ProgressBar;
import at.ait.dme.yumaJS.client.annotation.widgets.EditForm;

public class AudioRangeEditor extends Editor {
	
	public AudioRangeEditor(Annotatable annotatable, ProgressBar progressBar, int offsetX)
		throws InadequateBrowserException {

		super(annotatable, null);		
		init(annotatable, progressBar, new RangeSelection(progressBar, offsetX, offsetX + 1), null); 
	}
	
	public AudioRangeEditor(Annotatable annotatable, ProgressBar progressBar, Annotation initialValue) 
		throws InadequateBrowserException {

		super(annotatable, initialValue);
		
		Range r = annotatable.toRange(initialValue.getFragment());
		int fromX = progressBar.toOffsetX(r.getFrom());
		int toX = progressBar.toOffsetX(r.getTo());
		init(annotatable, progressBar, new RangeSelection(progressBar, fromX, toX), initialValue);
	}
		
	private void init(Annotatable annotatable, ProgressBar progressBar, 
		RangeSelection selection, Annotation annotation) {
				
		setSelection(selection);
		
		EditForm editForm = new EditForm(selection, annotatable.getLabels(), annotation);
		setEditForm(editForm);
		
		RootPanel.get().add(editForm, selection.getStartOffsetX()+ progressBar.getAbsoluteLeft(), 
				progressBar.getAbsoluteTop() + progressBar.getOffsetHeight());
	}

}
