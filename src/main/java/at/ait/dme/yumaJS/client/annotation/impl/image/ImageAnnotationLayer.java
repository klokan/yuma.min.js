package at.ait.dme.yumaJS.client.annotation.impl.image;

import java.util.HashMap;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.editors.ResizableBoxEditor;
import at.ait.dme.yumaJS.client.annotation.editors.selection.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.editors.selection.Range;
import at.ait.dme.yumaJS.client.annotation.widgets.DetailsPopup;
import at.ait.dme.yumaJS.client.annotation.widgets.event.DeleteHandler;
import at.ait.dme.yumaJS.client.annotation.widgets.event.EditHandler;
import at.ait.dme.yumaJS.client.init.InitParams;
import at.ait.dme.yumaJS.client.io.Delete;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * An implementation of {@link Annotatable} for an HTML IMAGE.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
@Export
@ExportPackage("YUMA")
public class ImageAnnotationLayer extends Annotatable implements Exportable {
	
	private static final String MEDIATYPE = "IMAGE";
	
	private static String objectURI;
	
	private Element image;
	
	private AbsolutePanel annotationLayer;
	
	private HashMap<Annotation, ImageAnnotationOverlay> annotations = 
		new HashMap<Annotation, ImageAnnotationOverlay>();
	
	public ImageAnnotationLayer(String id) {
		this(id, null);
	}

	public ImageAnnotationLayer(String id, InitParams params) {	
		super(params);
		
		image = DOM.getElementById(id);
		if (image == null)
			YUMA.fatalError("Error: no element with id '" + id + "' found on this page");
		
		if (!image.getTagName().toLowerCase().equals("img"))
			YUMA.fatalError("Error: you can only create an ImageCanvas on an <img> element");
		
		objectURI = Image.wrap(image).getUrl();

		annotationLayer = new AbsolutePanel();
		annotationLayer.setStyleName("image-canvas");		
		annotationLayer.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		annotationLayer.setPixelSize(image.getClientWidth(), image.getClientHeight());
		
		annotationLayer.addDomHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				annotationLayer.addStyleName("hover");
				annotationLayer.removeStyleName("no-hover");
			}
		}, MouseOverEvent.getType());
		
		annotationLayer.addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				annotationLayer.removeStyleName("hover");
				annotationLayer.addStyleName("no-hover");
			}
		}, MouseOutEvent.getType());
		
		RootPanel.get().add(annotationLayer, image.getAbsoluteLeft(), image.getAbsoluteTop());
		
		if (getServerURL() != null) {
			fetchAnnotations(getServerURL());
		}
	}
	
	@Override
	public String getObjectURI() {
		return objectURI;
	}

	@Override
	public String getMediaType() {
		return MEDIATYPE;
	}
	
	@Override
	public String toFragment(BoundingBox bbox, Range range) {
		return "xywh=pixel:" 
			+ bbox.getX()+ "," 
			+ bbox.getY() + "," 
			+ bbox.getWidth() + ","
			+ bbox.getHeight();
	}

	@Override
	public Range toRange(String fragment) {
		// Images don't support range fragments
		return null;
	}

	@Override
	public BoundingBox toBounds(String fragment) {
		if (fragment.startsWith("xywh=pixel:")) {
			fragment = fragment.substring(11);
		} else if (fragment.startsWith("xywh=")) {
			fragment = fragment.substring(5);
		} else {
			return null;
		}

		String[] xywh = fragment.split(",");
		if (xywh.length != 4)
			return null;
		
		return BoundingBox.create(
				Integer.parseInt(xywh[0]),
				Integer.parseInt(xywh[1]),
				Integer.parseInt(xywh[2]),
				Integer.parseInt(xywh[3]));
	}

	@Override
	protected void onWindowResize(int width, int height) {
		RootPanel.get().setWidgetPosition(annotationLayer, image.getAbsoluteLeft(), image.getAbsoluteTop());
	}
	
	@Override
	public void addAnnotation(final Annotation a) {
		ImageAnnotationOverlay overlay = 
			new ImageAnnotationOverlay(a, this, annotationLayer, getLabels());
		
		final Annotatable thisAnnotatable = this;
		
		DetailsPopup details = overlay.getDetailsPopup();
		details.addEditHandler(new EditHandler() {
			public void onEdit(Annotation annotation) {
				removeAnnotation(annotation);
				new ResizableBoxEditor(thisAnnotatable, annotationLayer, annotation);
			}
		});
		
		details.addDeleteHandler(new DeleteHandler() {
			public void onDelete(Annotation annotation) {
				System.out.println("Deleting " + a.getID());
				Delete.executeJSONP(getServerURL(), a.getID(), new AsyncCallback<String>() {
					public void onSuccess(String result) {
						removeAnnotation(a);
					}			

					public void onFailure(Throwable t) {
						YUMA.nonFatalError(t.getMessage());
					}
				});
			}
		});
		
		annotations.put(a, overlay);
		fireOnAnnotationCreated(a);
	}
	
	@Override
	public void removeAnnotation(Annotation a) {
		ImageAnnotationOverlay overlay = annotations.get(a);
		if (overlay != null) {
			overlay.destroy();
			annotations.remove(a);
		}
	}
	
	public void createNewAnnotation() {
		new ResizableBoxEditor(this, annotationLayer);
	}

}
