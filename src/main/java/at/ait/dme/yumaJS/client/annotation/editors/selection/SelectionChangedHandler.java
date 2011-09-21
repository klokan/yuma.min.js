package at.ait.dme.yumaJS.client.annotation.editors.selection;

import at.ait.dme.yumaJS.client.annotation.core.Fragment;

/**
 * A handler interface for reacting to selection change events.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public interface SelectionChangedHandler {

	public void onSelectionChanged(Fragment fragment);
	
}
