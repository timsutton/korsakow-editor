package org.korsakow.domain.command;

import java.awt.Color;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.Project;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.domain.proxy.ImageProxy;
import org.korsakow.domain.proxy.SoundProxy;
import org.korsakow.domain.proxy.UnknownMediaProxy;

public class UpdateProjectCommand extends AbstractCommand{


	public static final String PROJECT = "project";
	public static final String KEYWORDS = "keywords";
	public static final String BACKGROUND_IMAGE_ID = "background_image_id";
	public static final String BACKGROUND_COLOR = "background_color";
	public static final String CLICK_SOUND_VOLUME = "click_sound_volume";
	public static final String CLICK_SOUND_ID = "click_sound_id";
	public static final String MAX_LINKS = "max_links";
	public static final String KEEP_LINKS = "keep_links";
	public static final String RANDOM_LINK_MODE = "random_link_mode";
	public static final String SPLASH_SCREEN_MEDIA_ID = "splash_screen_media_id";
	public static final String BACKGROUND_SOUND_VOLUME = "background_sound_volume";
	public static final String BACKGROUND_SOUND_ID = "background_sound_id";
	public static final String MOVIE_HEIGHT = "movie_height";
	public static final String MOVIE_WIDTH = "movie_width";
	public static final String NAME = "name";
	public static final String ID = "id";

	public UpdateProjectCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			Project p = null;
			p = ProjectInputMapper.map(request.getLong(ID));
			p.setName(request.getString(NAME));
			
			p.setMovieWidth(request.getInt(MOVIE_WIDTH));
			p.setMovieHeight(request.getInt(MOVIE_HEIGHT));
			
			ISound backgroundSound = null;
			if (request.get(BACKGROUND_SOUND_ID) != null)
				backgroundSound = new SoundProxy(request.getLong(BACKGROUND_SOUND_ID));
			p.setBackgroundSound(backgroundSound);
			p.setBackgroundSoundVolume(request.getFloat(BACKGROUND_SOUND_VOLUME));
			
			IMedia splashScreenMedia = null;
			if (request.get(SPLASH_SCREEN_MEDIA_ID) != null)
				splashScreenMedia = new UnknownMediaProxy(request.getLong(SPLASH_SCREEN_MEDIA_ID));
			p.setSplashScreenMedia(splashScreenMedia);
			
			p.setRandomLinkMode(request.getBoolean(RANDOM_LINK_MODE));
			p.setKeepLinksOnEmptySearch(request.getBoolean(KEEP_LINKS));

			p.setMaxLinks(request.getLong(MAX_LINKS));
			
			ISound clickSound = null;
			if (request.get(CLICK_SOUND_ID) != null)
				clickSound = new SoundProxy(request.getLong(CLICK_SOUND_ID));
			p.setClickSound(clickSound);
			p.setClickSoundVolume(request.getFloat(CLICK_SOUND_VOLUME));
			
			IImage backgroundImage = null;
			if (request.get(BACKGROUND_IMAGE_ID) != null)
				backgroundImage = new ImageProxy(request.getLong(BACKGROUND_IMAGE_ID));
			p.setBackgroundImage(backgroundImage);
			Color backgroundColor = null;
			if (request.get(BACKGROUND_COLOR) != null)
				backgroundColor = (Color)request.get(BACKGROUND_COLOR);
			p.setBackgroundColor(backgroundColor);
			
			p.setKeywords((Collection<IKeyword>)request.get(KEYWORDS));
			
			List<IRule> rules = UpdateSnuCommand.getRules(request);
			p.setRules(rules);
			
			response.set(PROJECT, p);
			UoW.getCurrent().registerDirty(p);
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

}
