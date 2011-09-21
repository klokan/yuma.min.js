package at.ait.dme.yumaJS.client.annotation.selection;

import at.ait.dme.yumaJS.client.annotation.core.Fragment;

/**
 * An abstract base class for fragment selection tools.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public abstract class Selection {
	
	public abstract Fragment getSelectedFragment();
	
	public abstract void clear();

	public static native void disableTextSelection() /*-{
		document.onselectstart = function() {return false;} 
		document.onmousedown = function() {return false;}
		document.body.style.MozUserSelect='none'; 
	}-*/;
	
	public static native void enableTextSelection() /*-{
		document.onselectstart = function() {return true;}
  		document.onmousedown = function() {return true;}
  		document.body.style.MozUserSelect='all';
	}-*/;
	
}
