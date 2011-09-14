package at.ait.dme.yumaJS.client.annotation.selection;

import at.ait.dme.yumaJS.client.annotation.core.Fragment;

import com.google.gwt.dom.client.Element;

/**
 * An abstract base class for fragment selection tools.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public abstract class Selection {
	
	public abstract void setSelectionHandler(final SelectionHandler handler);
	
	public abstract Fragment getSelectedFragment();
	
	public abstract void clear();

	public static native void disableTextSelection(Element el) /*-{
		el.onselectstart=function(){return false};
		document.body.style.MozUserSelect='none';
	}-*/;
	
	public static native void enableTextSelection(Element el) /*-{
		el.onselectstart=function(){return true};
		document.body.style.MozUserSelect='all';
	}-*/;
	
}
