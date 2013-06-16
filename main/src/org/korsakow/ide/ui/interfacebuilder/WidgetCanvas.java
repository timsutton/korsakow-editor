/**
 * 
 */
package org.korsakow.ide.ui.interfacebuilder;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;

import org.korsakow.domain.interf.IImage;
import org.korsakow.ide.resources.media.MediaFactory;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.util.UIUtil;

public class WidgetCanvas extends JPanel
{
	private JPanel contentPanel;
	private GridPanel gridPanel;
	private JComponent dragLayer;
	private JPanel backgroundLayer;
	private JPanel stageLayer;
	private JLayeredPane widgetLayer;
	private Border myDefaultBorder;
	private WidgetCanvasModel model;
	
	public WidgetCanvas()
	{
		setModel(new WidgetCanvasModel());
		initUI();
		initListeners();
	}
	public void setModel(final WidgetCanvasModel model)
	{
		this.model = model;
		model.addListener(new WidgetCanvasModelAdapter() {
			@Override
			public void widgetAdded(WidgetModel widget) {
				add(widget.getComponent());
			}

			@Override
			public void widgetRemoved(WidgetModel widget) {
				remove(widget.getComponent());
			}
			@Override
			public void widgetsDepthChanged() {
				widgetLayer.removeAll();
				int i = 0;
				for (WidgetModel widgetModel : model.getWidgets()) {
					final int index = ++i;
					widgetLayer.add(widgetModel.getComponent(), new Integer(index));
				}
				repaint();
			}
			@Override
			public void selectionChanged(Collection<WidgetModel> oldSelection, Collection<WidgetModel> newSelection) {
			}
		});
	}
	public WidgetCanvasModel getModel()
	{
		return model;
	}
	public WidgetCanvasModel getSelectionModel()
	{
		return model;
	}
	/**
	 * Overridden because we have some components placed outside our bounds. 
	 */
	@Override
	public Dimension getPreferredSize() {
		if (isPreferredSizeSet()) {
			return super.getPreferredSize();
		}
		return new Dimension(model.getStageWidth(), model.getStageHeight());
	}
	public void add(WidgetComponent widget)
	{
		widgetLayer.add(widget, new Integer(widgetLayer.highestLayer()+1));
		widget.setBorder(myDefaultBorder);
//		setLocation(widget, widget.getLocation());
		repaint();
		widget.repaint();
		widget.revalidate();
		//widget.addMouseListener(new WidgetDragger(widget, this));
	}
	public void remove(WidgetComponent widget)
	{
		widgetLayer.remove(widget);
		repaint();
	}
	public List<WidgetComponent> getWidgetComponents()
	{
		List<WidgetComponent> widgetComponents = new ArrayList<WidgetComponent>();
		for (WidgetModel widget : getModel().getWidgets())
			widgetComponents.add(widget.getComponent());
		return widgetComponents;
	}
	public JComponent getDragLayer()
	{
		return dragLayer;
	}
	@Override
	public void doLayout()
	{
		super.doLayout();
		contentPanel.setSize(model.getStageWidth(), model.getStageHeight());
		contentPanel.setLocation(0, 0);
		gridPanel.setSize(model.getStageWidth(), model.getStageHeight());
		gridPanel.setOffset(
			model.getGridWidth()!=0?(model.getMovieOffsetX() % model.getGridWidth()):0,
			model.getGridHeight()!=0?(model.getMovieOffsetY() % model.getGridHeight()):0
		);
				//model.getMovieOffsetX(), 
//							model.getMovieOffsetY());
		//model.getGridWidth()!=0?(model.getMovieOffsetX() % model.getGridWidth()):0
		widgetLayer.setSize(model.getStageWidth(), model.getStageHeight());
		widgetLayer.setLocation(
				0,
				0
//				model.getGridWidth()!=0?(model.getMovieOffsetX() % model.getGridWidth()):0,
//				model.getGridHeight()!=0?(model.getMovieOffsetY() % model.getGridHeight()):0
				);
		
		dragLayer.setSize(model.getStageWidth(), model.getStageHeight());
		
		stageLayer.setLocation( model.getMovieOffsetX(),
								model.getMovieOffsetY());
		stageLayer.setSize(model.getMovieWidth(), model.getMovieHeight());
		
		backgroundLayer.setSize(stageLayer.getWidth(), stageLayer.getHeight());
		backgroundLayer.setLocation(model.getMovieOffsetX(),
									model.getMovieOffsetY());
	}
	public Collection<WidgetComponent> getSelectedWidgetComponents()
	{
		List<WidgetComponent> selectedWidgets = new ArrayList<WidgetComponent>();
		for (WidgetModel widget : getSelectionModel().getSelectedWidgets())
			selectedWidgets.add(widget.getComponent());
		return selectedWidgets;
	}
	private void initUI()
	{
		contentPanel = new JPanel(null);
		
//		setFocusable(true);
		contentPanel.setLayout(null);
		contentPanel.setOpaque(true);
		contentPanel.setBackground(Color.LIGHT_GRAY);
		add(contentPanel);
		
		myDefaultBorder = new LineBorder(Color.black, 1);
				
		backgroundLayer = new JPanel();
		backgroundLayer.setOpaque(true);
		backgroundLayer.setBackground(null);
		contentPanel.add(backgroundLayer, 0);
		
		stageLayer = new JPanel();
		stageLayer.setOpaque(false);
		stageLayer.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		contentPanel.add(stageLayer, 0);
		
		gridPanel = new GridPanel();
		gridPanel.setGridSize(20, 20);
		gridPanel.setOpaque(false);
		gridPanel.setBackground(null);
		contentPanel.add(gridPanel, 0);
		
		widgetLayer = new JLayeredPane();
//		widgetLayer.setFocusable(true);
		widgetLayer.setOpaque(false);
		widgetLayer.setBackground(null);
		widgetLayer.setCursor(Cursor.getDefaultCursor()); // resize border doesn't properly release cursor.
		widgetLayer.addMouseListener(new ClearSelectionMouseListener());
		contentPanel.add(widgetLayer, 0);

		dragLayer = new JPanel(null);
		dragLayer.setVisible(false);
		dragLayer.setOpaque(false);
		dragLayer.setBackground(null);
		contentPanel.add(dragLayer, 0);
		
	}
	private void initListeners()
	{
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				requestFocus();
			}
		});
	}
	public void setGridSize(int width, int height)
	{
		gridPanel.setGridSize(width, height);
		gridPanel.revalidate();
		repaint();
	}
	public void setShowBackground(boolean show)
	{
		backgroundLayer.setVisible(show);
	}
	public void setBackgroundColor(Color c)
	{
		backgroundLayer.setBackground(c);
		backgroundLayer.validate(); // the image may not appear without this;
		// we call validate instead of revalidate because of timing issues (there are already plenty) with the subsequent repaint 
		UIUtil.runUITaskLater(new Runnable() { // doesn't work if we don't wait until "later"
			public void run() {
				repaint(); // the image may, strangely, appear in front of other UI elements without this
			}
		});
	}
	public void setBackgroundImage(IImage image)
	{
		backgroundLayer.removeAll();
		if (image != null)
			backgroundLayer.add(MediaFactory.getMediaNoThrow(image).getComponent());
		backgroundLayer.validate(); // the image may not appear without this;
		// we call validate instead of revalidate because of timing issues (there are already plenty) with the subsequent repaint 
		UIUtil.runUITaskLater(new Runnable() { // doesn't work if we don't wait until "later"
			public void run() {
				repaint(); // the image may, strangely, appear in front of other UI elements without this
			}
		});
	}
	private class ClearSelectionMouseListener extends MouseInputAdapter
	{
		@Override
		public void mouseClicked(MouseEvent me)
		{
			getModel().clearSelection();
		}
	}
	public Border getDefaultBorder()
	{
		return myDefaultBorder;
	}
}