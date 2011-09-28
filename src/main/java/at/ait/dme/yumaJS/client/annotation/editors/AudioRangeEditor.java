package at.ait.dme.yumaJS.client.annotation.editors;

import com.google.gwt.user.client.ui.RootPanel;

import at.ait.dme.yumaJS.client.annotation.core.Annotatable;
import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.core.Range;
import at.ait.dme.yumaJS.client.annotation.editors.selection.RangeSelection;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.InadequateBrowserException;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.ProgressBar;
import at.ait.dme.yumaJS.client.annotation.widgets.EditForm;
import at.ait.dme.yumaJS.client.init.Labels;

public class AudioRangeEditor extends Editor {
	
	public AudioRangeEditor(Annotatable annotatable, ProgressBar progressBar, Labels labels, int offsetX)
		throws InadequateBrowserException {

		super(annotatable, null);		
		init(annotatable, progressBar, labels, new RangeSelection(progressBar, offsetX, offsetX + 1), null); 
	}
	
	public AudioRangeEditor(Annotatable annotatable, ProgressBar progressBar, Labels labels, Annotation initialValue) 
		throws InadequateBrowserException {

		super(annotatable, initialValue);
		
		Range r = initialValue.getFragment().getRange();
		int fromX = progressBar.toOffsetX(r.getFrom());
		int toX = progressBar.toOffsetX(r.getTo());
		init(annotatable, progressBar, labels, new RangeSelection(progressBar, fromX, toX), initialValue);
	}
		
	private void init(Annotatable annotatable, ProgressBar progressBar, Labels labels, 
		RangeSelection selection, Annotation annotation) {
				
		setSelection(selection);
		
		EditForm editForm = new EditForm(selection, labels, annotation);
		setEditForm(editForm);
		
		RootPanel.get().add(editForm, selection.getStartOffsetX()+ progressBar.getAbsoluteLeft(), 
				progressBar.getAbsoluteTop() + progressBar.getOffsetHeight());
	}

}
