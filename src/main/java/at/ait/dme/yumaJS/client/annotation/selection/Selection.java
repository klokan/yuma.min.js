package at.ait.dme.yumaJS.client.annotation.selection;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;

import at.ait.dme.yumaJS.client.annotation.core.Fragment;

/**
 * An abstract base class for fragment selection tools.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public abstract class Selection {
	
	public abstract Fragment getSelectedFragment();
	
	public abstract void clear();

	public static void disableTextSelection() {
		Style body = Document.get().getBody().getStyle();
		body.setProperty("MozUserSelect", "none");
		body.setProperty("WebkitUserSelect", "none");
		body.setProperty("UserSelect", "none");
		_disableTextSelection();
	}
	
	private static native void _disableTextSelection() /*-{
		document.onselectstart = function(){return false;}
		document.onmousedown = function() {return false;}
		document.body.style.MozUserSelect='none'; 
	}-*/;
	
	public static void enableTextSelection() {
		Style body = Document.get().getBody().getStyle();
		body.setProperty("MozUserSelect", "auto");
		body.setProperty("WebkitUserSelect", "auto");
		body.setProperty("UserSelect", "auto");	
		_enableTextSelection();
	}
	
	private static native void _enableTextSelection() /*-{
		document.onselectstart = function(){return true;}
  		document.onmousedown = function(){return true;}
  		document.body.style.MozUserSelect='all';
	}-*/;
	
}
