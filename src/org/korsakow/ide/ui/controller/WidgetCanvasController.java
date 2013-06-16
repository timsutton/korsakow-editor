package org.korsakow.ide.ui.controller;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.xml.parsers.ParserConfigurationException;

import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.interfacebuilder.DomChangeListener;
import org.korsakow.ide.ui.interfacebuilder.DomChangeNotifier;
import org.korsakow.ide.ui.interfacebuilder.WidgetCanvas;
import org.korsakow.ide.ui.interfacebuilder.WidgetCanvasModel;
import org.korsakow.ide.ui.interfacebuilder.WidgetCanvasModelAdapter;
import org.korsakow.ide.ui.interfacebuilder.WidgetCanvasModelListener;
import org.korsakow.ide.ui.interfacebuilder.WidgetResizer;
import org.korsakow.ide.ui.interfacebuilder.widget.SnuAutoLink;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.ide.util.UIUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.jroller.santosh.border.DefaultResizableBorder;

public class WidgetCanvasController implements WidgetCanvasModelListener
{
	private final WidgetCanvas widgetCanvas;
	private final WidgetDomSynchronizer domSynchronizer;
	private Document doc;
	private final UndoDomListener undoDomListener;
	private final DomChangeNotifier domNotifier;
	private final WidgetDomParser widgetParser;
	private final WidgetSelectionController selectionController;
	private final MouseInputListener resizeController;
	private boolean isUpdate = false;
	public WidgetCanvasController(WidgetCanvas widgetCanvas)
	{
		this.widgetCanvas = widgetCanvas;
		widgetCanvas.getModel().addListener(this);
		
		resizeController = new WidgetResizer(widgetCanvas);
		selectionController = new WidgetSelectionController(widgetCanvas, resizeController);
		
		widgetCanvas.addKeyListener(new WidgetKeyboardMover(widgetCanvas));

		widgetCanvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, UIUtil.getPlatformCommandKeyMask()), "undo");
		widgetCanvas.getActionMap().put("undo", new UndoAction());
		widgetCanvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "deleteSelected");
		widgetCanvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "deleteSelected");
		widgetCanvas.getActionMap().put("deleteSelected", new DeleteSelectedAction());
		widgetCanvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, UIUtil.getPlatformCommandKeyMask()), "selectAll");
		widgetCanvas.getActionMap().put("selectAll", new SelectAllAction());
		
		try {
			doc = DomUtil.createDocument();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		}
		Element root = doc.createElement("Interface");
		doc.appendChild(root);
		Element widgetsRoot = doc.createElement("widgets");
		root.appendChild(widgetsRoot);
		
		domNotifier = new DomChangeNotifier();
		undoDomListener = new UndoDomListener(doc, domNotifier);
		widgetParser = new WidgetDomParser();

		domSynchronizer = new WidgetDomSynchronizer(domNotifier, doc);
	}
	public void initialState()
	{
		undoDomListener.setInitialState();
	}
	public void gridSizeChanged(int oldWidth, int oldHeight, int newWidth, int newHeight)
	{
		widgetCanvas.setGridSize(newWidth, newHeight);
	}
	public void movieSizeChanged(int oldWidth, int oldHeight, int newWidth, int newHeight)
	{
	}

	public void widgetAdded(WidgetModel widget) {
		domSynchronizer.widgetAdded(widget);
		widget.getComponent().removeMouseListener(selectionController);
		widget.getComponent().addMouseListener(selectionController);
		updateSnuAutoLinks();
	}

	public void widgetRemoved(WidgetModel widget) {
		domSynchronizer.widgetRemoved(widget);
		updateSnuAutoLinks();
	}
	
	public void widgetsDepthChanged() {
		
	}
	
	public void selectionChanged(Collection<WidgetModel> oldSelection, Collection<WidgetModel> newSelection)
	{
		for (WidgetComponent comp : widgetCanvas.getWidgetComponents()) {
			if (widgetCanvas.getModel().isSelected(comp.getWidget())) {
				comp.removeMouseListener(resizeController);
				comp.removeMouseMotionListener(resizeController);
				comp.addMouseListener(resizeController);
				comp.addMouseMotionListener(resizeController);
				comp.setBorder(new DefaultResizableBorder(6, comp.isResizable() && newSelection.size()==1));
				comp.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			} else {
				comp.setBorder(widgetCanvas.getDefaultBorder());
				comp.removeMouseListener(resizeController);
				comp.removeMouseMotionListener(resizeController);
			}
		}
		widgetCanvas.repaint(); // this ensures the apparent z-order doesn't change when setBorder causes a repaint
	}

	private void updateSnuAutoLinks()
	{
		int snuAudoLinkIndex = 0;
		Collection<WidgetModel> widgets = widgetCanvas.getModel().getWidgets();
		for (WidgetModel widget : widgets) {
			switch (widget.getWidgetType())
			{
			case SnuAutoLink:
				SnuAutoLink snuAutoLink = (SnuAutoLink)widget;
				snuAutoLink.setIndex(snuAudoLinkIndex);
				++snuAudoLinkIndex;
				break;
			}
		}
	}
	
	private class UndoAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e) {
			undoDomListener.back();
		}
	}
	private class SelectAllAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e) {
			widgetCanvas.getSelectionModel().selectAll();
		}
	}
	private class DeleteSelectedAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e) {
			widgetCanvas.getModel().removeWidgets(widgetCanvas.getSelectionModel().getSelectedWidgets());
		}
	}
	private class UndoDomListener implements DomChangeListener
	{
		private class HistoryEntry
		{
			public Node node;
			public String changeName;
			public boolean coalescable;
			public HistoryEntry(Node node, String changeName, boolean coalescable)
			{
				this.node = node;
				this.changeName = changeName;
				this.coalescable = coalescable;
			}
		}
		private final List<HistoryEntry> history = new ArrayList<HistoryEntry>();
		private final Document doc;
		private final DomChangeNotifier notifier;
		public UndoDomListener(Document doc, DomChangeNotifier notifier)
		{
			this.doc = doc;
			this.notifier = notifier;
			notifier.add(this);
			setInitialState();
		}
		public void setInitialState()
		{
			history.clear();
			history.add(new HistoryEntry(doc.getDocumentElement().cloneNode(true), "initial state", false));
		}
		public void domDocumentChange(Document doc, String changeName, boolean coalescable)
		{
			if (isUpdate)
				return;
			if (changeName == null)
				changeName = "[batch]"; // just for debugging clarity
			Node copy = doc.getDocumentElement().cloneNode(true);
			history.add(new HistoryEntry(copy, changeName, coalescable));
//			Logger.getLogger(WidgetCanvasController.class).debug("UndoHistory: " + changeName + "; " + history.size());
//			dump();
		}
		public void back()
		{
			if (history.isEmpty())
				throw new IllegalStateException();
			isUpdate = true;
			widgetCanvas.getModel().clearWidgets();

			doc.removeChild(doc.getDocumentElement());
			HistoryEntry entry;
			if (history.size() > 1) {
				// latest entry always contains current state
				history.remove(history.size()-1);
			}

			entry = history.get(history.size()-1);
			doc.appendChild(entry.node.cloneNode(true));

//			Logger.getLogger(WidgetCanvasController.class).debug("Undo to " + entry.changeName + "; " + (history.size()));
			widgetParser.parseDom(doc);
			for (WidgetModel widget : widgetParser.getWidgets()) {
				widget.removePropertyChangeListener(domSynchronizer); // in case its already added
				widget.addPropertyChangeListener(domSynchronizer);
				widgetCanvas.getModel().addWidget(widget);
			}
			widgetCanvas.repaint();
			isUpdate = false;
		}
	}
	private class WidgetDomSynchronizer extends WidgetCanvasModelAdapter implements PropertyChangeListener
	{
		private final DomChangeNotifier notifier;
		private final Document doc;
		public WidgetDomSynchronizer(DomChangeNotifier notifier, Document dom)
		{
			this.notifier = notifier;
			doc = dom;
		}
		public Element getRoot()
		{
			return DomUtil.findChildByTagName(doc.getDocumentElement(), "widgets");
		}
		@Override
		public void widgetAdded(WidgetModel widget)
		{
			if (isUpdate)
				return;
			Element elm = DomUtil.getElementById(doc, ""+widget.getId());
			if (elm != null)
				throw new IllegalStateException("widget not found: " + widget.getId());
			elm = doc.createElement("WidgetModel");
			getRoot().appendChild(elm);
			elm.setAttribute("id", ""+widget.getId());
			elm.setIdAttribute("id", true);
			assert DomUtil.getElementById(doc, ""+widget.getId()) != null;
			widget.addPropertyChangeListener(this);
			updateWidget(elm, widget); // dont update with an incomplete widget
			notifier.notifyDomChanged(doc, "widget added", false);
		}
		@Override
		public void widgetRemoved(WidgetModel widget)
		{
			if (isUpdate)
				return;
			Element elm = DomUtil.getElementById(doc, ""+widget.getId());
			if (elm == null) {
				new IllegalStateException().printStackTrace();
				throw new IllegalStateException();
			}
			elm.getParentNode().removeChild(elm);
			widget.removePropertyChangeListener(this);
			notifier.notifyDomChanged(doc, "widget removed", false);
		}
		@Override
		public void widgetsDepthChanged()
		{
			if (isUpdate)
				return;
			notifier.notifyDomChanged(doc, "widget removed", false);
		}
		public void propertyChange(PropertyChangeEvent event)
		{
			if (isUpdate)
				return;
			WidgetModel widget = (WidgetModel)event.getSource();
			Element elm = DomUtil.getElementById(doc, ""+widget.getId());
			if (elm == null)
				throw new IllegalStateException("no element for #" + widget.getId() + " on propertychange for " + event.getPropertyName());
			updateWidget(elm, widget);
			notifier.notifyDomChanged(doc, event.getPropertyName(), true);
		}
		public void updateWidget(Element elm, WidgetModel widget)
		{
			setProperty(elm, "type", widget.getWidgetType().getId());
			setProperty(elm, "x", widget.getX());
			setProperty(elm, "y", widget.getY());
			setProperty(elm, "width", widget.getWidth());
			setProperty(elm, "height", widget.getHeight());
			for (String propertyId : widget.getDynamicPropertyIds()) {
				setProperty(elm, propertyId, widget.getDynamicProperty(propertyId));
			}
		}
		private void setProperty(Element elm, String propertyName, Object propertyValue)
		{
			// null is represented as not being there. how else can this be done? attribute?
			if (propertyValue == null)
				return;
			Element propertyElement = DomUtil.findChildByTagName(elm, propertyName);
			if (propertyElement == null) {
				propertyElement = doc.createElement(propertyName);
				elm.appendChild(propertyElement);
			}
			while (propertyElement.hasChildNodes())
				propertyElement.removeChild(propertyElement.getFirstChild());
			propertyElement.appendChild(doc.createCDATASection(propertyValue!=null?propertyValue.toString():""));
		}
	}
	private static class WidgetSelectionController extends MouseInputAdapter
	{
		private boolean didDrag = false;
		private final WidgetCanvas canvas;
		private final MouseInputListener resizeListener;
		
		public WidgetSelectionController(WidgetCanvas canvas, MouseInputListener resizeListener)
		{
			this.canvas = canvas;
			this.resizeListener = resizeListener;
		}
	    @Override
		public void mouseDragged(MouseEvent me)
	    {
	    	if (!didDrag) {
		    	didDrag = true;
				canvas.requestFocus();
				WidgetComponent widget = (WidgetComponent)me.getComponent();
				boolean wasSelected = canvas.getSelectionModel().isSelected(widget.getWidget());
				boolean replace = !UIUtil.isPlatformMultipleSectionKeyDown(me);
				if (!wasSelected) {
					if (replace) {
						canvas.getSelectionModel().clearSelection();
						canvas.getSelectionModel().addSelected(widget.getWidget());
					} else {
						canvas.getSelectionModel().addSelected(widget.getWidget());
					}
				}
				if (!wasSelected) {
					// TODO: insert comment as to why we do this
					resizeListener.mousePressed(me);
				}
	    	}
	    }
		@Override
		public void mouseReleased(MouseEvent me)
		{
			me.getComponent().removeMouseMotionListener(this);
			if (!didDrag) {
				canvas.requestFocus();
				WidgetComponent widget = (WidgetComponent)me.getComponent();
				toggleSelected(widget, !UIUtil.isPlatformMultipleSectionKeyDown(me));
			}
		}
		@Override
		public void mousePressed(MouseEvent me)
	 	{
			WidgetComponent widget = (WidgetComponent)me.getComponent();
//			if (!getSelectionModel().isSelected(widget.getWidget()))
				me.getComponent().addMouseMotionListener(this);
			didDrag = false;
		}
		private void toggleSelected(WidgetComponent widget, boolean replace)
		{
			if (canvas.getSelectionModel().isSelected(widget.getWidget())) {
				if (replace) {
					canvas.getSelectionModel().clearSelection();
				} else
					canvas.getSelectionModel().removeSelected(widget.getWidget());
			} else {
				if (replace) {
					canvas.getSelectionModel().clearSelection();
					canvas.getSelectionModel().addSelected(widget.getWidget());
				} else {
					canvas.getSelectionModel().addSelected(widget.getWidget());
				}
			}
		}
	}
	private class WidgetKeyboardMover extends KeyAdapter
	{
		private final WidgetCanvas canvas;
		private final WidgetCanvasModel model;
		
		public WidgetKeyboardMover(WidgetCanvas canvas) {
			this.canvas = canvas;
			model = canvas.getModel();
		}
	    @Override
		public void keyPressed(KeyEvent e)
	    {
	    	switch (e.getKeyCode())
	    	{
	    	case KeyEvent.VK_LEFT:
	    	case KeyEvent.VK_RIGHT:
	    	case KeyEvent.VK_UP:
	    	case KeyEvent.VK_DOWN:
	    		break;
    		default:
    			return;
	    	}
	    	
	    	Rectangle startBounds = new Rectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
	    	Collection<WidgetComponent> selected = canvas.getSelectedWidgetComponents();
	    	for (WidgetComponent widget : selected) {
	    		startBounds.x = Math.min(startBounds.x, widget.getX());
	    		startBounds.y = Math.min(startBounds.y, widget.getY());
	    		startBounds.width = Math.max(startBounds.width, widget.getWidth());
	    		startBounds.height = Math.max(startBounds.height, widget.getHeight());
	    	}
	    	Point startPoint = new Point(startBounds.x, startBounds.y);

			boolean maintainAspect = true;
			
	    	int incrementX = e.isShiftDown()?1:10;
			int incrementY = e.isShiftDown()?1:10;
			boolean snapToGrid = !e.isAltDown();
			if (snapToGrid) {
				incrementX = model.getGridWidth();
				incrementY = model.getGridHeight();
			}
	    	int moveX = 0;
	    	int moveY = 0;
	    	switch (e.getKeyCode())
	    	{
	    	case KeyEvent.VK_LEFT:
	    		moveX = -incrementX;
	    		break;
	    	case KeyEvent.VK_RIGHT:
	    		moveX = incrementX;
	    		break;
	    	case KeyEvent.VK_UP:
	    		moveY = -incrementY;
	    		break;
	    	case KeyEvent.VK_DOWN:
	    		moveY = incrementY;
	    		break;
	    	}

	    	if (moveX == 0 && moveY ==0)
	    		return;
	    	
			Point movePoint = new Point(startBounds.x + moveX, startBounds.y + moveY);
			
	    	WidgetResizer.Bounds newBounds = WidgetResizer.doResizeOrMove(model, Cursor.MOVE_CURSOR, startPoint, startBounds, movePoint, model.getGridWidth(), model.getGridHeight(), snapToGrid, maintainAspect);
	    	
			int dx = newBounds.x1 - startBounds.x;
			int dy = newBounds.y1 - startBounds.y;
			
	    	Point p = new Point();
	    	for (WidgetComponent widget : selected) {
    			p.x = widget.getX();
    			p.y = widget.getY();
    			p.x += dx;
    			p.y += dy;

				widget.setLocation(widget.getX()+dx, widget.getY()+dy);
	    	}
	    }
	}
}
