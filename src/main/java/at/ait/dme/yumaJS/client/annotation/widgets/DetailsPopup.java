package at.ait.dme.yumaJS.client.annotation.widgets;

import at.ait.dme.yumaJS.client.annotation.core.Annotation;
import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PushButton;

/**
 * The overlay element used to show the details of an {@link Annotation}.
 * The overlay is composed of a container DIV (CSS class 'annotation-popup')
 * and the following nested elements:
 * <p>
 * <li>
 * <ul>A DIV containing the text content</ul>
 * <ul>A 'REPLY' button</ul> 
 * <ul>An 'EDIT' button</ul>
 * <ul>A 'DELETE' button</ul>
 * </li>
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class DetailsPopup extends Composite implements HasMouseOutHandlers {
	
	private FlowPanel container;
	
	public DetailsPopup(Annotation a, Labels labels) {
		FlowPanel content = new FlowPanel();
		content.setStyleName("annotation-popup-content");
		content.add(new InlineHTML(a.getText()));
		
		PushButton btnReply, btnEdit, btnDelete;
		if (labels == null) {
			btnReply = new PushButton("REPLY");
			btnEdit = new PushButton("EDIT");
			btnDelete = new PushButton("DELETE");
		} else {
			btnReply = new PushButton(labels.reply());
			btnEdit = new PushButton(labels.edit());
			btnDelete = new PushButton(labels.delete());			
		}

		btnReply.setStyleName("annotation-popup-button");
		btnEdit.setStyleName("annotation-popup-button");
		btnDelete.setStyleName("annotation-popup-button");
		
		container = new FlowPanel();
		container.setStyleName("annotation-popup");		
		container.add(content);
		container.add(btnReply);
		container.add(btnEdit);
		container.add(btnDelete);
		container.setVisible(false);
		
		initWidget(container);
		
		addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				container.setVisible(false);
			}
		});
	}
	
	public boolean contains(int x, int y) {
		int left = container.getAbsoluteLeft();
		int top = container.getAbsoluteTop();
		int w = container.getOffsetWidth();
		int h = container.getOffsetHeight();
		
		if (x < left)
			return false;
		
		if (x > left + w)
			return false;
		
		if (y < top)
			return false;
		
		if (y > top + h)
			return false;
		
		return true;
	}
	
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

}
