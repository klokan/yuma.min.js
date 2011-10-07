package at.ait.dme.yumaJS.client.annotation.editors.selection;

/**
 * A handler interface for reacting to selection change events.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public interface SelectionChangedHandler {

	public void onBoundsChanged(BoundingBox bbox);
	
	public void onRangeChanged(Range range);
	
}
