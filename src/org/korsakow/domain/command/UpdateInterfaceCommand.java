package org.korsakow.domain.command;

import java.awt.Color;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.Interface;
import org.korsakow.domain.WidgetFactory;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.mapper.input.InterfaceInputMapper;
import org.korsakow.domain.proxy.ImageProxy;
import org.korsakow.domain.proxy.SoundProxy;
import org.korsakow.ide.resources.widget.WidgetPersistAction;
import org.korsakow.ide.resources.widget.WidgetPersistCondition;

public class UpdateInterfaceCommand extends AbstractCommand {
	
	public static final String WIDGET_COUNT = "widgetCount";
	public static final String WIDGET_PROPERTY_VALUES = "widgetPropertyValues";
	public static final String WIDGET_PROPERTY_IDS = "widgetPropertyIds";
	public static final String WIDGET_X = "widgetX";
	public static final String WIDGET_Y = "widgetY";
	public static final String WIDGET_WIDTH = "widgetWidth";
	public static final String WIDGET_HEIGHT = "widgetHeight";
	public static final String WIDGET_PERSIST_CONDITION = "widgetPersistCondition";
	public static final String WIDGET_PERSIST_ACTION = "widgetPersistAction";
	public static final String WIDGET_KEYWORDS = "widgetKeywords";
	public static final String WIDGET_TYPE = "widgetType";
	public static final String WIDGET_NAME = "widgetName";


	public static final String KEYWORDS = "keywords";
	public static final String INTERFACE = "interface";
	public static final String CLICK_SOUND_VOLUME = "click_sound_volume";
	public static final String CLICK_SOUND_ID = "click_sound_id";
	public static final String BACKGROUND_IMAGE_ID = "background_image_id";
	public static final String BACKGROUND_COLOR = "background_color";
	public static final String GRID_HEIGHT = "grid_height";
	public static final String GRID_WIDTH = "grid_width";
	public static final String NAME = "name";
	public static final String ID = "id";
	public UpdateInterfaceCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			Interface s = null;
			s = InterfaceInputMapper.map(request.getLong(ID));
			s.setName(request.getString(NAME));
			
			s.setGridWidth(request.getInt(GRID_WIDTH));
			s.setGridHeight(request.getInt(GRID_HEIGHT));
			
			if (request.get(CLICK_SOUND_ID) != null)
				s.setClickSound(new SoundProxy(request.getLong(CLICK_SOUND_ID)));
			else
				s.setClickSound(null);
			s.setClickSoundVolume(request.getFloat(CLICK_SOUND_VOLUME));
			
			s.setBackgroundColor((Color)request.get(BACKGROUND_COLOR));
			if (request.get(BACKGROUND_IMAGE_ID) != null)
				s.setBackgroundImage(new ImageProxy(request.getLong(BACKGROUND_IMAGE_ID)));
			else
				s.setBackgroundImage(null);
			
			response.set(INTERFACE, s);

			s.setKeywords((Collection<IKeyword>)request.get(KEYWORDS));
			
			s.setWidgets(getWidgets(request));
			
			response.set(INTERFACE, s);
			UoW.getCurrent().registerDirty(s);
			UoW.getCurrent().commit();
			UoW.newCurrent();
		} catch (MapperException e) {
			throw new CommandException(e);
		} catch (SQLException e) {
			throw new CommandException(e);
		} catch (KeyNotFoundException e) {
			throw new CommandException(e);
		} catch (CreationException e) {
			throw new CommandException(e);
		}
	}

	public static List<IWidget> getWidgets(Helper request)
	{
		return getWidgets(request, "");
	}
	private static List<IWidget> getWidgets(Helper request, String base)
	{
		int widgetCount = request.getInt(WIDGET_COUNT + base);
		List<IWidget> widgets = new ArrayList<IWidget>();
		for (int i = 0; i < widgetCount; ++i)
		{
			String nextBase = base + "_" + i;
			IWidget widget = WidgetFactory.createNew();
			widget.setName(request.getString(WIDGET_NAME+nextBase));
			widget.setWidgetId(request.getString(WIDGET_TYPE+nextBase));
			widget.setKeywords((Collection<IKeyword>)request.get(WIDGET_KEYWORDS+nextBase));
			widget.setX(request.getInt(WIDGET_X+nextBase));
			widget.setY(request.getInt(WIDGET_Y+nextBase));
			widget.setWidth(request.getInt(WIDGET_WIDTH+nextBase));
			widget.setHeight(request.getInt(WIDGET_HEIGHT+nextBase));
			widget.setPersistCondition(WidgetPersistCondition.forId(request.getString(WIDGET_PERSIST_CONDITION+nextBase)));
			widget.setPersistAction(WidgetPersistAction.forId(request.getString(WIDGET_PERSIST_ACTION+nextBase)));
			List<String> propertyIds = (List<String>)request.get(WIDGET_PROPERTY_IDS+nextBase);
			List<Object> propertyValues = (List<Object>)request.get(WIDGET_PROPERTY_VALUES+nextBase);
			for (int j = 0; j < propertyIds.size(); ++j)
				widget.setDynamicProperty(propertyIds.get(j), propertyValues.get(j));
			widgets.add(widget);
			
		}
		return widgets;
	}
}
