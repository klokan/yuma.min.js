package at.ait.dme.yumaJS.client.annotation.editors;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RootPanel;

import at.ait.dme.yumaJS.client.annotation.core.Annotatable;
import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.annotation.editors.selection.RangeSelection;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.InadequateBrowserException;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.ProgressBar;
import at.ait.dme.yumaJS.client.annotation.widgets.EditForm;
import at.ait.dme.yumaJS.client.init.Labels;

public class AudioRangeEditor extends Editor {
	
	private RangeSelection selection;
	
	private EditForm editForm;

	public AudioRangeEditor(Annotatable annotatable, ProgressBar progressBar, int start, int end, Labels labels) throws InadequateBrowserException {
		super(annotatable);
		
		selection = new RangeSelection(progressBar, start, end);
		
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
		
		RootPanel.get().add(editForm, start, end);
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
