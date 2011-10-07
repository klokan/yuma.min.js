package at.ait.dme.yumaJS.client.annotation.widgets.event;

import at.ait.dme.yumaJS.client.annotation.Annotation;

/**
 * Handler interface for listening to 'Delete Annotation' click events
 * in the user interface. 
 *  
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public interface DeleteHandler {

	public void onDelete(Annotation annotation);
	
}
