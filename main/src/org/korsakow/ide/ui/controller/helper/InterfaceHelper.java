package org.korsakow.ide.ui.controller.helper;

import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Settings;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.UpdateInterfaceCommand;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.mapper.input.ImageInputMapper;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.domain.mapper.input.SettingsInputMapper;
import org.korsakow.domain.mapper.input.SoundInputMapper;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.interfacebuilder.InterfaceBuilderMainPanel;
import org.korsakow.ide.ui.interfacebuilder.WidgetCanvasModel;

public class InterfaceHelper {
	
	public static void initView(InterfaceBuilderMainPanel view, String name) throws MapperException
	{
		ISettings settings = SettingsInputMapper.find();
		IProject project = ProjectInputMapper.find();
		final int gridWidth = 20;
		final int gridHeight = 20;
		
		view.setNameFieldText(name);
		view.getCanvas().getModel().setGridSize(gridWidth, gridHeight);
		view.getGridInfoPanel().setGridWidthValue(gridWidth);
		view.getGridInfoPanel().setGridHeightValue(gridHeight);
		view.getCanvas().getModel().notifyInitialState();
		
		view.setShowBackground(settings.getBoolean(Settings.ShowBackgroundPreview));
		
		view.getCanvas().setBackgroundImage(project.getBackgroundImage());
		view.getCanvas().setBackgroundColor(project.getBackgroundColor());
		
		view.setClickSoundChoices(ViewHelper.sort(SoundInputMapper.findAll(), ISound.class, name));
		view.setBackgroundImageChoices(ViewHelper.sort(ImageInputMapper.findAll(), IImage.class, name));
		
		view.revalidate();
	}
	public static void initView(InterfaceBuilderMainPanel view, IInterface interf) throws MapperException
	{
		ISettings settings = SettingsInputMapper.find();
		IProject project = ProjectInputMapper.find();
		final String similarName = settings.getBoolean(Settings.PutSimilarResourcesAtTop)?interf.getName():null;
		
		view.setResourceId(interf.getId());
		view.setNameFieldText(similarName);
		final WidgetCanvasModel canvasModel = view.getCanvas().getModel();
		canvasModel.setGridSize(interf.getGridWidth(), interf.getGridHeight());
		canvasModel.clearWidgets();
		view.getGridInfoPanel().setGridWidthValue(interf.getGridWidth());
		view.getGridInfoPanel().setGridHeightValue(interf.getGridHeight());
		
		view.setClickSoundChoices(ViewHelper.sort(SoundInputMapper.findAll(), ISound.class, similarName));
		view.setClickSound(interf.getClickSound());
		view.setClickSoundVolume(interf.getClickSoundVolume());
		canvasModel.notifyInitialState();

		view.setBackgroundImageChoices(ViewHelper.sort(ImageInputMapper.findAll(), IImage.class, similarName));
		view.setBackgroundImage(interf.getBackgroundImage());
		view.setBackgroundColorModel(interf.getBackgroundColor());
		
		view.getCanvas().setBackgroundImage(interf.getBackgroundImage()!=null?interf.getBackgroundImage():project.getBackgroundImage());
		view.getCanvas().setBackgroundColor(interf.getBackgroundColor()!=null?interf.getBackgroundColor():project.getBackgroundColor());
		
		view.setShowBackground(settings.getBoolean(Settings.ShowBackgroundPreview));
		
		List<IWidget> widgets = new ArrayList<IWidget>(interf.getWidgets());
		List<WidgetModel> models = createViewModels(
				canvasModel.getMovieOffsetX(),
				canvasModel.getMovieOffsetY(),
				widgets);
		for (WidgetModel model : models)
			canvasModel.addWidget(model);
		
		view.repaint();
		view.revalidate();
	}
	public static Request createRequest(InterfaceBuilderMainPanel view, Long id) throws MapperException
	{
		ISettings settings = SettingsInputMapper.find();
		
		Request request = new Request();
		
		request.set(UpdateInterfaceCommand.ID, id);
		request.set(UpdateInterfaceCommand.NAME, view.getNameFieldText().trim());
		request.set(UpdateInterfaceCommand.KEYWORDS, view.getKeywords());
		
		request.set(UpdateInterfaceCommand.GRID_WIDTH, view.getCanvas().getModel().getGridWidth());
		request.set(UpdateInterfaceCommand.GRID_HEIGHT, view.getCanvas().getModel().getGridHeight());
		request.set(UpdateInterfaceCommand.CLICK_SOUND_ID, view.getClickSoundId());
		request.set(UpdateInterfaceCommand.CLICK_SOUND_VOLUME, view.getClickSoundVolume());
		request.set(UpdateInterfaceCommand.BACKGROUND_IMAGE_ID, view.getBackgroundImageId());
		request.set(UpdateInterfaceCommand.BACKGROUND_COLOR, view.getBackgroundColor());
		
//		settings.setBoolean(Settings.ShowBackgroundColorPreview, view.getShowBackgroundColor());
//		settings.setBoolean(Settings.ShowBackgroundImagePreview, view.getShowBackgroundImage());
		
		ViewHelper.addWidgetsToRequest(
				-view.getCanvas().getModel().getMovieOffsetX(),
				-view.getCanvas().getModel().getMovieOffsetY(),
				request, view.getCanvas().getModel().getWidgets());
		
		return request;
	}
	private static WidgetModel createViewModel(int offsetX, int offsetY, IWidget widget)
	{
		WidgetModel copy = WidgetType.forId(widget.getWidgetId()).newInstance();
		copy.setName(widget.getName());
		copy.setKeywords(widget.getKeywords());
		copy.setPersistCondition(widget.getPersistCondition());
		copy.setPersistAction(widget.getPersistAction());
		copy.setX(widget.getX() + offsetX);
		copy.setY(widget.getY() + offsetY);
		copy.setWidth(widget.getWidth());
		copy.setHeight(widget.getHeight());
		for (String prop : copy.getDynamicPropertyIds())
			copy.setDynamicProperty(prop, widget.getDynamicProperty(prop));
		return copy;
	}
	private static List<WidgetModel> createViewModels(int offsetX, int offsetY, List<IWidget> widgets)
	{
		List<WidgetModel> copy = new ArrayList<WidgetModel>();
		for (IWidget widget : widgets)
			copy.add(createViewModel(offsetX, offsetY, widget));
		return copy;
	}
}
