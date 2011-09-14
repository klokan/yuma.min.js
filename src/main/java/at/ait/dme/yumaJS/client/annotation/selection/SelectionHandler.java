package at.ait.dme.yumaJS.client.annotation.selection;

import at.ait.dme.yumaJS.client.annotation.core.Fragment;

/**
 * A callback interface for working with {@link Selection}s.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public interface SelectionHandler {
	
	public void onSelect(Fragment fragment);

}
