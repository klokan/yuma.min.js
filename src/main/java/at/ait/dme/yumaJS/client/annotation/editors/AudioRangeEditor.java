package at.ait.dme.yumaJS.client.annotation.editors;

import com.google.gwt.user.client.ui.RootPanel;

import at.ait.dme.yumaJS.client.annotation.core.Annotatable;
import at.ait.dme.yumaJS.client.annotation.editors.selection.RangeSelection;
import at.ait.dme.yumaJS.client.annotation.editors.selection.Selection;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.InadequateBrowserException;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.ProgressBar;
import at.ait.dme.yumaJS.client.annotation.widgets.EditForm;
import at.ait.dme.yumaJS.client.init.Labels;

public class AudioRangeEditor extends Editor {
	
	public AudioRangeEditor(Annotatable annotatable, ProgressBar progressBar, int start, int end, Labels labels) throws InadequateBrowserException {
		super(annotatable);
		
		Selection selection = new RangeSelection(progressBar, start, end); 
		setSelection(selection);
		
		EditForm editForm = new EditForm(selection, labels);
		setEditForm(editForm);
		RootPanel.get().add(editForm, start, end);
	}

}
