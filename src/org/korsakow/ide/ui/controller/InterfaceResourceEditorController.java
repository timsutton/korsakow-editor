package org.korsakow.ide.ui.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.dnd.DropTargetDispatcher;
import org.korsakow.ide.resources.widget.DefaultTableWidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.WidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.propertyhandler.ColorPropertyHandler;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.controller.action.MoveBackwardsAction;
import org.korsakow.ide.ui.controller.action.MoveForwardsAction;
import org.korsakow.ide.ui.controller.action.MoveToBackAction;
import org.korsakow.ide.ui.controller.action.MoveToFrontAction;
import org.korsakow.ide.ui.controller.dnd.ImageDropTarget;
import org.korsakow.ide.ui.controller.dnd.SnuDropTarget;
import org.korsakow.ide.ui.controller.dnd.TextDropTarget;
import org.korsakow.ide.ui.controller.dnd.VideoDropTarget;
import org.korsakow.ide.ui.controller.dnd.WidgetTypeDropTarget;
import org.korsakow.ide.ui.dnd.DataFlavors;
import org.korsakow.ide.ui.interfacebuilder.InterfaceBuilderMainPanel;
import org.korsakow.ide.ui.interfacebuilder.WidgetCanvas;
import org.korsakow.ide.ui.interfacebuilder.WidgetCanvasModel;
import org.korsakow.ide.ui.interfacebuilder.WidgetCanvasModelAdapter;
import org.korsakow.ide.util.UIUtil;


public class InterfaceResourceEditorController
{
	private final ResourceEditor editor;
	private final WidgetCanvasController widgetCanvasController;
	private final InterfaceBuilderMainPanel interfaceBuilderPanel;
	private final WidgetInfoPanelUpdater widgetInfoPanelUpdater = new WidgetInfoPanelUpdater();
	public InterfaceResourceEditorController(ResourceEditor editor, Long resourceId)
	{
		IProject project;
		try {
			project = ProjectInputMapper.find();
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(e);
			throw new IllegalArgumentException(e);
		}
		
		interfaceBuilderPanel = (InterfaceBuilderMainPanel)editor.getResourceView();
		this.editor = editor;
		interfaceBuilderPanel.getInfoPanel().setEnabled(false);
		interfaceBuilderPanel.getInfoPanel().addXChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				onInfoXChange();
			}
		});
		interfaceBuilderPanel.getInfoPanel().addYChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				onInfoYChange();
			}
		});
		interfaceBuilderPanel.getInfoPanel().addWidthChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				onInfoWidthChange();
			}
		});
		interfaceBuilderPanel.getInfoPanel().addHeightChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				onInfoHeightChange();
			}
		});
		ChangeListener gridInfoListener = new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				getCanvasModel().setGridSize(interfaceBuilderPanel.getGridInfoPanel().getGridWidthValue(), interfaceBuilderPanel.getGridInfoPanel().getGridHeightValue());
			}
		};
		interfaceBuilderPanel.getGridInfoPanel().addWidthChangeListener(gridInfoListener);
		interfaceBuilderPanel.getGridInfoPanel().addHeightChangeListener(gridInfoListener);

		interfaceBuilderPanel.getArrangePanel().addMoveBackActionListener(new MoveToBackAction(interfaceBuilderPanel));
		interfaceBuilderPanel.getArrangePanel().addMoveBackwardsActionListener(new MoveBackwardsAction(interfaceBuilderPanel));
		interfaceBuilderPanel.getArrangePanel().addMoveForwardsActionListener(new MoveForwardsAction(interfaceBuilderPanel));
		interfaceBuilderPanel.getArrangePanel().addMoveFrontActionListener(new MoveToFrontAction(interfaceBuilderPanel));
		
		interfaceBuilderPanel.addShowBackgroundListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				getCanvas().setShowBackground(interfaceBuilderPanel.getShowBackground());
			}
		});
		
		widgetCanvasController = new WidgetCanvasController(interfaceBuilderPanel.getCanvas());
		interfaceBuilderPanel.getCanvas().getSelectionModel().addListener(new WidgetCanvasModelAdapter() {
			@Override
			public void selectionChanged(Collection<WidgetModel> oldSelection, Collection<WidgetModel> newSelection) {
				onCanvasSelectionChange(oldSelection, newSelection);
			}
		});
		
		
		interfaceBuilderPanel.addBackgroundImageActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				IImage backgroundImage = interfaceBuilderPanel.getBackgroundImage();
				if (backgroundImage == null) {
					IProject project;
					try {
						project = ProjectInputMapper.find();
					} catch (MapperException e) {
						Application.getInstance().showUnhandledErrorDialog(e);
						return;
					}
					backgroundImage = project.getBackgroundImage();
				}
				
				getCanvas().setBackgroundImage(backgroundImage);
			}
		});
		interfaceBuilderPanel.addClearBackgroundColorActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				IProject project;
				try {
					project = ProjectInputMapper.find();
				} catch (MapperException e) {
					Application.getInstance().showUnhandledErrorDialog(e);
					return;
				}
				getCanvas().setBackgroundColor(project.getBackgroundColor());
				interfaceBuilderPanel.setBackgroundColorModel(null);
			}
		});
		interfaceBuilderPanel.addBackgroundColorActionListener(new BackgroundColorListener(interfaceBuilderPanel));

		final DropTargetDispatcher dropDispatcher = new DropTargetDispatcher();
		interfaceBuilderPanel.getCanvas().setDropTarget(dropDispatcher);
		dropDispatcher.addDropTargetListenerNoThrow(new DropTargetListener() {
			public void dragEnter(DropTargetDragEvent dtde) {
				InterfaceResourceEditorController.this.editor.toFront();
			}
			public void dragExit(DropTargetEvent dte) {
				InterfaceResourceEditorController.this.editor.toFront();
			}
			public void dragOver(DropTargetDragEvent dtde) {
				InterfaceResourceEditorController.this.editor.toFront();
			}
			public void drop(DropTargetDropEvent dtde) {
				InterfaceResourceEditorController.this.editor.toFront();
			}
			public void dropActionChanged(DropTargetDragEvent dtde) {
				InterfaceResourceEditorController.this.editor.toFront();
			}
		});
		dropDispatcher.addFlavorHandler(DataFlavors.TextFlavor, new TextDropTarget(interfaceBuilderPanel.getCanvas()));
		dropDispatcher.addFlavorHandler(DataFlavors.VideoFlavor, new VideoDropTarget(interfaceBuilderPanel.getCanvas()));
		dropDispatcher.addFlavorHandler(DataFlavors.ImageFlavor, new ImageDropTarget(interfaceBuilderPanel.getCanvas()));
		dropDispatcher.addFlavorHandler(DataFlavors.WidgetType, new WidgetTypeDropTarget(interfaceBuilderPanel));
		dropDispatcher.addFlavorHandler(DataFlavors.SnuFlavor, new SnuDropTarget(interfaceBuilderPanel.getCanvas()));

		final int movieWidth = project.getMovieWidth();
		final int movieHeight = project.getMovieHeight();
		final int stageWidth = Math.max(800, movieWidth*2);
		final int stageHeight = Math.max(600, movieHeight*2);
		getCanvasModel().setMovieSize(movieWidth, movieHeight);
		getCanvasModel().setStageSize(stageWidth, stageHeight);

		interfaceBuilderPanel.revalidate();
		interfaceBuilderPanel.repaint();
		
		UIUtil.runUITaskLater(new Runnable() {
			public void run() {
				final JViewport viewport = interfaceBuilderPanel.getCanvasScroll().getViewport();
				viewport.setViewPosition(new Point(Math.abs(viewport.getExtentSize().width - viewport.getViewSize().width)/2, 
												   Math.abs(viewport.getExtentSize().height - viewport.getViewSize().height)/2));
			}
		}, 100);

		// ensures the work-area (stage) grows to fit the whole window
		// and that the whole area is always scrollable even if the window gets smaller
		interfaceBuilderPanel.addComponentListener(new ComponentAdapter() {
		    @Override
			public void componentResized(ComponentEvent e) {
		    	int width = 800;
		    	int height = 600;
		    	
				width = Math.max(width, movieWidth*2);
				height = Math.max(height, movieHeight*2);
				
				width = Math.max(width, interfaceBuilderPanel.getCanvasScroll().getViewport().getVisibleRect().width);
				height = Math.max(height, interfaceBuilderPanel.getCanvasScroll().getViewport().getVisibleRect().height);
				getCanvasModel().setStageSize(width, height);
				getCanvas().revalidate();
		    }
		});
	}
	
	public void initialState()
	{
		widgetCanvasController.initialState();
	}
	public void onResourceSave()
	{
	}
	private WidgetCanvasModel getCanvasModel() {
		return interfaceBuilderPanel.getCanvas().getModel();
	}
	private WidgetCanvas getCanvas() {
		return interfaceBuilderPanel.getCanvas();
	}
	public void onClose()
	{
		editor.dispose();
	}
	private void onCanvasSelectionChange(Collection<WidgetModel> oldSelection, Collection<WidgetModel> newSelection)
	{
		for (WidgetModel widget : oldSelection) {
			widget.disposeWidgetEditor();
			widget.removePropertyChangeListener(widgetInfoPanelUpdater);
		}
		if (newSelection.size() == 1) {
			WidgetModel widget = newSelection.iterator().next();
			interfaceBuilderPanel.getInfoPanel().setEnabled(true);
			interfaceBuilderPanel.getInfoPanel().setXValue(widget.getX() - getCanvasModel().getMovieOffsetX());
			interfaceBuilderPanel.getInfoPanel().setYValue(widget.getY() - getCanvasModel().getMovieOffsetY());
			interfaceBuilderPanel.getInfoPanel().setWidthValue(widget.getWidth());
			interfaceBuilderPanel.getInfoPanel().setHeightValue(widget.getHeight());
			widget.addPropertyChangeListener(widgetInfoPanelUpdater);
			
			WidgetPropertiesEditor widgetEditor = widget.getWidgetEditor();
			JComponent editorComponent = widgetEditor.getWidgetPropertiesEditorComponent(getCanvasModel());
			editorComponent.setPreferredSize(new Dimension(Short.MAX_VALUE, 200));
			interfaceBuilderPanel.setPropertiesEditor(editorComponent);
		} else if (newSelection.size() > 1) {
			interfaceBuilderPanel.getInfoPanel().setEnabled(false);
			DefaultTableWidgetPropertiesEditor widgetEditor = new DefaultTableWidgetPropertiesEditor(newSelection);
			JComponent editorComponent = widgetEditor.getWidgetPropertiesEditorComponent(getCanvasModel());
			editorComponent.setPreferredSize(new Dimension(Short.MAX_VALUE, 200));
			interfaceBuilderPanel.setPropertiesEditor(editorComponent);
		} else {
			interfaceBuilderPanel.getInfoPanel().setEnabled(false);
			interfaceBuilderPanel.setPropertiesEditor(null);
		}
	}
	private void onInfoHeightChange()
	{
		int value = interfaceBuilderPanel.getInfoPanel().getHeightValue();
		Collection<WidgetModel> selection = interfaceBuilderPanel.getCanvas().getSelectionModel().getSelectedWidgets();
		for (WidgetModel widget : selection) {
			widget.setHeight(value);
			widget.getComponent().revalidate();
		}
	}
	private void onInfoWidthChange()
	{
		int value = interfaceBuilderPanel.getInfoPanel().getWidthValue();
		Collection<WidgetModel> selection = interfaceBuilderPanel.getCanvas().getSelectionModel().getSelectedWidgets();
		for (WidgetModel widget : selection) {
			widget.setWidth(value);
			widget.getComponent().revalidate();
		}
	}
	private void onInfoYChange()
	{
		int value = interfaceBuilderPanel.getInfoPanel().getYValue();
		Collection<WidgetModel> selection = interfaceBuilderPanel.getCanvas().getSelectionModel().getSelectedWidgets();
		for (WidgetModel widget : selection) {
			widget.setY(value + getCanvasModel().getMovieOffsetY());
			widget.getComponent().revalidate();
		}
	}
	private void onInfoXChange()
	{
		int value = interfaceBuilderPanel.getInfoPanel().getXValue();
		Collection<WidgetModel> selection = interfaceBuilderPanel.getCanvas().getSelectionModel().getSelectedWidgets();
		for (WidgetModel widget : selection) {
			widget.setX(value + getCanvasModel().getMovieOffsetX());
			widget.getComponent().revalidate();
		}
	}
	private class WidgetInfoPanelUpdater implements PropertyChangeListener
	{
		public void propertyChange(PropertyChangeEvent event) {
			final WidgetModel widget = (WidgetModel)event.getSource();
			Collection<WidgetModel> selected = interfaceBuilderPanel.getCanvas().getSelectionModel().getSelectedWidgets();
			if (!(selected.size()==1 && selected.contains(widget)))
				return;
			// sadly, this runnable-later is due to the fact the in awt-component's property-change notify implementation,
			// the actual change is effected AFTER dispatching....
			// hopefully this run-later wont have any side-effects...
			UIUtil.runUITaskLater(new Runnable() {
				public void run() {
					interfaceBuilderPanel.getInfoPanel().setXValue(widget.getX() - getCanvasModel().getMovieOffsetX());
					interfaceBuilderPanel.getInfoPanel().setYValue(widget.getY() - getCanvasModel().getMovieOffsetY());
					interfaceBuilderPanel.getInfoPanel().setWidthValue(widget.getWidth());
					interfaceBuilderPanel.getInfoPanel().setHeightValue(widget.getHeight());
				}
			});
		}
	}
	private static class BackgroundColorListener implements ActionListener
	{
		private final InterfaceBuilderMainPanel interfaceBuilderPanel;
		public BackgroundColorListener(InterfaceBuilderMainPanel interfaceBuilderPanel)
		{
			this.interfaceBuilderPanel = interfaceBuilderPanel;
		}
		public void actionPerformed(ActionEvent event)
		{
			Color initialColor = interfaceBuilderPanel.getBackgroundColor();
			if (initialColor == null)
				initialColor = Color.black;
			final JColorChooser chooser = ColorPropertyHandler.createColorChooser(initialColor);
			
			ActionListener okListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent event2)
				{
					IProject project;
					try {
						project = ProjectInputMapper.find();
					} catch (MapperException e) {
						Application.getInstance().showUnhandledErrorDialog(e);
						return;
					}
					Color backgroundColor = chooser.getColor();
					if (backgroundColor == null) {
						backgroundColor = project.getBackgroundColor();
					}
					interfaceBuilderPanel.getCanvas().setBackgroundColor(backgroundColor);
					interfaceBuilderPanel.setBackgroundColorModel(backgroundColor);
				}
			};
			ActionListener cancelListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
				}
			};
			final JDialog dialog = JColorChooser.createDialog(interfaceBuilderPanel, "Color", true, chooser, okListener, cancelListener);
			dialog.setVisible(true);
		}
	}
}
